package mc.monacotelecom.tecrep.equipments.importer.implementations.simcard;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.entity.SimCard;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.repository.SimCardRepository;
import mc.monacotelecom.tecrep.equipments.utils.LuhnValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class SimCardImportValidator {

    private final LocalizedMessageBuilder localizedMessageBuilder;
    private final SimCardRepository simCardRepository;

    public void prevalidate(SimCard simCard) {
        if (simCard.getSerialNumber().isBlank()) {
            throw new EqmValidationException(localizedMessageBuilder, IMPORT_EMPTY_LINE);
        }

        if (simCard.getPlmn() != null && StringUtils.startsWith(simCard.getImsiNumber(), simCard.getPlmn().getCode()) && simCard.getProvider() != null) {
            throw new EqmValidationException(localizedMessageBuilder, INVALID_PLMN_IMSI_PROVIDER, simCard.getPlmn().getCode(), simCard.getImsiNumber(), simCard.getProvider().getName());
        }

        final var existingSimCardWithImsi = simCardRepository.findProjectionByImsiNumber(simCard.getImsiNumber());
        if (existingSimCardWithImsi.isPresent() && !existingSimCardWithImsi.get().getSerialNumber().equals(simCard.getSerialNumber())) {
            throw new EqmValidationException(localizedMessageBuilder, SIMCARD_IMSI_ALREADY_IN_USE, simCard.getImsiNumber());
        }

        if (simCard.getImsiNumber().length() != 15) {
            throw new EqmValidationException(localizedMessageBuilder, IMSI_EXCEEDED_SIZE, simCard.getImsiNumber(), 15);
        }
    }

    public void prevalidateGemalto(SimCard simCard) {
        this.prevalidate(simCard);

        if (Objects.nonNull(simCard.getOtaSalt()) && simCard.getOtaSalt().length() != 32) {
            throw new EqmValidationException(localizedMessageBuilder, SALT_EXCEEDED_SIZE, 32);
        }
    }

    public void validateIdemiaSerialNumber(String serialNumber){
        this.validateSerialNumber(serialNumber);

        var validator = new LuhnValidator(18);
        if (!validator.isCheckDigitValid(serialNumber)) {
            throw new EqmValidationException(localizedMessageBuilder, SIMCARD_SN_INVALID_CHECK_DIGIT, serialNumber);
        }
    }

    public void validateSerialNumber(String serialNumber) {
        if (StringUtils.isBlank(serialNumber)) {
            throw new EqmValidationException(localizedMessageBuilder, SIMCARD_SN_EMPTY);
        }

        if (serialNumber.length() != 20) {
            throw new EqmValidationException(localizedMessageBuilder, SIMCARD_SN_LENGTH, serialNumber);
        }

        if (!serialNumber.endsWith("F")) {
            throw new EqmValidationException(localizedMessageBuilder, SIMCARD_SN_NO_CHECK_DIGIT, serialNumber);
        }
    }
}
