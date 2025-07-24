package mc.monacotelecom.tecrep.equipments.process.simcardgenerator;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.inventory.common.Lambdas;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.dto.request.GenerateSimCardsRequestDTO;
import mc.monacotelecom.tecrep.equipments.entity.Batch;
import mc.monacotelecom.tecrep.equipments.entity.SimCardGenerationConfiguration;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.SIMCARD_GENERATION_EMPTY_FIXED_PREFIX;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.SIMCARD_GENERATION_EMPTY_SEQUENCE_PREFIX;


@Component
@RequiredArgsConstructor
@Profile("eir")
public class SimCardIdentifiersGeneratorEir implements SimCardIdentifiersGenerator {

    private final LocalizedMessageBuilder localizedMessageBuilder;

    /**
     * serialNumber:
     * * [Fixed Prefix][Sequence Prefix] [Sequence Number]
     * *   1-7              8-11             12-18
     * *
     */
    @Override
    public String generateSerialNumber(final Batch batch,
                                       final String nextICCID,
                                       final String nextMSIN,
                                       final GenerateSimCardsRequestDTO generateSimCardsRequestDTO,
                                       final SimCardGenerationConfiguration configuration) {

        Lambdas.verifyOrThrow.test(Objects.isNull(configuration.getFixedPrefix()),
                new EqmValidationException(localizedMessageBuilder, SIMCARD_GENERATION_EMPTY_FIXED_PREFIX, configuration.getName()));
        Lambdas.verifyOrThrow.test(Objects.isNull(configuration.getSequencePrefix()),
                new EqmValidationException(localizedMessageBuilder, SIMCARD_GENERATION_EMPTY_SEQUENCE_PREFIX, configuration.getName()));

        return configuration.getFixedPrefix() + configuration.getSequencePrefix() + nextICCID;
    }

    /**
     * imsiNumber:
     * * [PLMNCODE]   [MVNO] [MSIN]
     * *  1-5          6      7-15
     * *
     */
    @Override
    public String generateImsiNumber(final Batch batch,
                                     final String nextICCID,
                                     final String nextMSIN,
                                     final GenerateSimCardsRequestDTO generateSimCardsRequestDTO,
                                     final SimCardGenerationConfiguration configuration) {

        return configuration.getPlmn().getCode() + batch.getInventoryPool().getMvno() + nextMSIN;
    }
}
