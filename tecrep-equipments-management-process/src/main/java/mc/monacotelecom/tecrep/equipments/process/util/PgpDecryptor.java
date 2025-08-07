package mc.monacotelecom.tecrep.equipments.process.util;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.operator.jcajce.JcaKeyFingerprintCalculator;
import org.bouncycastle.openpgp.operator.jcajce.JcePBESecretKeyDecryptorBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyDataDecryptorFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.security.Security;
import java.util.Iterator;

public class PgpDecryptor {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private final PGPSecretKeyRingCollection secretKeyRings;
    private final char[] passphrase;

    public PgpDecryptor(InputStream secretKeyIn, char[] passphrase) throws IOException, PGPException {
        this.secretKeyRings = new PGPSecretKeyRingCollection(
                PGPUtil.getDecoderStream(secretKeyIn),
                new JcaKeyFingerprintCalculator()
        );
        this.passphrase = passphrase;
    }

    public byte[] decrypt(InputStream encryptedData) throws IOException, PGPException {
        InputStream decoderStream = PGPUtil.getDecoderStream(encryptedData);
        PGPObjectFactory pgpF = new PGPObjectFactory(decoderStream, new JcaKeyFingerprintCalculator());
        Object o = pgpF.nextObject();
        PGPEncryptedDataList enc;
        if (o instanceof PGPEncryptedDataList) {
            enc = (PGPEncryptedDataList) o;
        } else {
            enc = (PGPEncryptedDataList) pgpF.nextObject();
        }

        Iterator<PGPEncryptedData> it = enc.getEncryptedDataObjects();
        PGPPrivateKey sKey = null;
        PGPPublicKeyEncryptedData pbe = null;

        while (sKey == null && it.hasNext()) {
            pbe = (PGPPublicKeyEncryptedData) it.next();
            PGPSecretKey sk = secretKeyRings.getSecretKey(pbe.getKeyID());
            if (sk != null) {
                sKey = sk.extractPrivateKey(new JcePBESecretKeyDecryptorBuilder().setProvider("BC").build(passphrase));
            }
        }

        if (sKey == null) {
            throw new PGPException("Secret key for message not found.");
        }

        InputStream clear = pbe.getDataStream(new JcePublicKeyDataDecryptorFactoryBuilder().setProvider("BC").build(sKey));
        PGPObjectFactory plainFact = new PGPObjectFactory(clear, new JcaKeyFingerprintCalculator());
        Object message = plainFact.nextObject();
        if (message instanceof PGPCompressedData) {
            PGPCompressedData cData = (PGPCompressedData) message;
            PGPObjectFactory pgpFact = new PGPObjectFactory(cData.getDataStream(), new JcaKeyFingerprintCalculator());
            message = pgpFact.nextObject();
        }

        if (message instanceof PGPLiteralData) {
            PGPLiteralData ld = (PGPLiteralData) message;
            try (InputStream unc = ld.getInputStream()) {
                return IOUtils.toByteArray(unc);
            }
        }
        throw new PGPException("Message is not a simple encrypted file.");
    }
}
