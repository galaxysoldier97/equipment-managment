package mc.monacotelecom.tecrep.equipments.importer.validators;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.inventory.common.Lambdas;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v2.AddCPE;
import mc.monacotelecom.tecrep.equipments.dto.request.update.v2.UpdateCPE;
import mc.monacotelecom.tecrep.equipments.entity.CPE;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.repository.CPERepository;
import org.springframework.stereotype.Component;

import java.util.List;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class CPEValidators {
    private final CPERepository cpeRepository;
    private final LocalizedMessageBuilder localizedMessageBuilder;

    public void validateCpeAttributeExistenceBeforeAdd(AddCPE dto) {
        Lambdas.verifyOrThrow.test(!cpeRepository.findAllByMacAddressCpe(dto.getMacAddressCpe()).isEmpty(),
                new EqmValidationException(localizedMessageBuilder, CPE_MAC_ADDRESS_ALREADY_IN_USE, dto.getMacAddressCpe()));
        Lambdas.verifyOrThrow.test(!cpeRepository.findAllByMacAddressRouter(dto.getMacAddressRouter()).isEmpty(),
                new EqmValidationException(localizedMessageBuilder, CPE_ROUTER_ADDRESS_ALREADY_IN_USE, dto.getMacAddressRouter()));
        Lambdas.verifyOrThrow.test(!cpeRepository.findAllByMacAddressVoip(dto.getMacAddressVoip()).isEmpty(),
                new EqmValidationException(localizedMessageBuilder, CPE_VOIP_ADDRESS_ALREADY_IN_USE, dto.getMacAddressVoip()));
        Lambdas.verifyOrThrow.test(!cpeRepository.findAllByMacAddressLan(dto.getMacAddressLan()).isEmpty(),
                new EqmValidationException(localizedMessageBuilder, CPE_LAN_ADDRESS_ALREADY_IN_USE, dto.getMacAddressLan()));
        Lambdas.verifyOrThrow.test(!cpeRepository.findAllByMacAddress5G(dto.getMacAddress5G()).isEmpty(),
                new EqmValidationException(localizedMessageBuilder, CPE_5G_ADDRESS_ALREADY_IN_USE, dto.getMacAddress5G()));
        Lambdas.verifyOrThrow.test(!cpeRepository.findAllByMacAddress4G(dto.getMacAddress4G()).isEmpty(),
                new EqmValidationException(localizedMessageBuilder, CPE_4G_ADDRESS_ALREADY_IN_USE, dto.getMacAddress4G()));
        Lambdas.verifyOrThrow.test(!cpeRepository.findAllBySerialNumber(dto.getSerialNumber()).isEmpty(),
                new EqmValidationException(localizedMessageBuilder, CPE_SERIAL_NUMBER_ALREADY_IN_USE, dto.getSerialNumber()));
    }

    public void validateCpeAttributeExistenceBeforeUpdate(UpdateCPE dto, CPE cpe) {
        checkUniqueMacAddressCpe(dto, cpe);
        checkUniqueMacAddressRouter(dto, cpe);
        checkUniqueMacAddressVoip(dto, cpe);
        if (StringUtils.isNotBlank(dto.getMacAddressLan()))
            checkUniqueMacAddressLan(dto, cpe);
        if (StringUtils.isNotBlank(dto.getMacAddress5G()))
            checkUniqueMacAddress5G(dto, cpe);
        if (StringUtils.isNotBlank(dto.getMacAddress4G()))
            checkUniqueMacAddress4G(dto, cpe);
        if (StringUtils.isNotBlank(dto.getSerialNumber()))
            checkUniqueSerialNumber(dto, cpe);
    }

    public void validateCpeAttributeExistenceBeforePartialUpdate(UpdateCPE dto, CPE cpe) {
        if (StringUtils.isNotBlank(dto.getMacAddressCpe())) {
            checkUniqueMacAddressCpe(dto, cpe);
        }

        if (StringUtils.isNotBlank(dto.getMacAddressRouter())) {
            checkUniqueMacAddressRouter(dto, cpe);
        }

        if (StringUtils.isNotBlank(dto.getMacAddressVoip())) {
            checkUniqueMacAddressVoip(dto, cpe);
        }

        if (StringUtils.isNotBlank(dto.getMacAddressLan())) {
            checkUniqueMacAddressLan(dto, cpe);
        }

        if (StringUtils.isNotBlank(dto.getMacAddress5G())) {
            checkUniqueMacAddress5G(dto, cpe);
        }

        if (StringUtils.isNotBlank(dto.getMacAddress4G())) {
            checkUniqueMacAddress4G(dto, cpe);
        }

        if (StringUtils.isNotBlank(dto.getSerialNumber())) {
            checkUniqueSerialNumber(dto, cpe);
        }
    }

    private void checkUniqueSerialNumber(UpdateCPE dto, CPE cpe) {
        List<CPE> matchingCPE = cpeRepository.findAllBySerialNumber(dto.getSerialNumber());
        if (matchingCPE.size() > 1 || (!matchingCPE.isEmpty() && !matchingCPE.get(0).getEquipmentId().equals(cpe.getEquipmentId()))) {
            throw new EqmValidationException(localizedMessageBuilder, mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.CPE_SERIAL_NUMBER_ALREADY_IN_USE, dto.getSerialNumber());
        }
    }

    private void checkUniqueMacAddress4G(UpdateCPE dto, CPE cpe) {
        List<CPE> matchingCPE = cpeRepository.findAllByMacAddress4G(dto.getMacAddress4G());
        if (matchingCPE.size() > 1 || (!matchingCPE.isEmpty() && !matchingCPE.get(0).getEquipmentId().equals(cpe.getEquipmentId()))) {
            throw new EqmValidationException(localizedMessageBuilder, mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.CPE_4G_ADDRESS_ALREADY_IN_USE, dto.getMacAddress4G());
        }
    }

    private void checkUniqueMacAddress5G(UpdateCPE dto, CPE cpe) {
        List<CPE> matchingCPE = cpeRepository.findAllByMacAddress5G(dto.getMacAddress5G());
        if (matchingCPE.size() > 1 || (!matchingCPE.isEmpty() && !matchingCPE.get(0).getEquipmentId().equals(cpe.getEquipmentId()))) {
            throw new EqmValidationException(localizedMessageBuilder, mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.CPE_5G_ADDRESS_ALREADY_IN_USE, dto.getMacAddress5G());
        }
    }

    private void checkUniqueMacAddressLan(UpdateCPE dto, CPE cpe) {
        List<CPE> matchingCPE = cpeRepository.findAllByMacAddressLan(dto.getMacAddressLan());
        if (matchingCPE.size() > 1 || (!matchingCPE.isEmpty() && !matchingCPE.get(0).getEquipmentId().equals(cpe.getEquipmentId()))) {
            throw new EqmValidationException(localizedMessageBuilder, mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.CPE_LAN_ADDRESS_ALREADY_IN_USE, dto.getMacAddressLan());
        }
    }

    private void checkUniqueMacAddressVoip(UpdateCPE dto, CPE cpe) {
        List<CPE> matchingCPE = cpeRepository.findAllByMacAddressVoip(dto.getMacAddressVoip());
        if (matchingCPE.size() > 1 || (!matchingCPE.isEmpty() && !matchingCPE.get(0).getEquipmentId().equals(cpe.getEquipmentId()))) {
            throw new EqmValidationException(localizedMessageBuilder, mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.CPE_VOIP_ADDRESS_ALREADY_IN_USE, dto.getMacAddressVoip());
        }
    }

    private void checkUniqueMacAddressRouter(UpdateCPE dto, CPE cpe) {
        List<CPE> matchingCPE = cpeRepository.findAllByMacAddressRouter(dto.getMacAddressRouter());
        if (matchingCPE.size() > 1 || (!matchingCPE.isEmpty() && !matchingCPE.get(0).getEquipmentId().equals(cpe.getEquipmentId()))) {
            throw new EqmValidationException(localizedMessageBuilder, mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.CPE_ROUTER_ADDRESS_ALREADY_IN_USE, dto.getMacAddressRouter());
        }
    }

    private void checkUniqueMacAddressCpe(UpdateCPE dto, CPE cpe) {
        List<CPE> matchingCPE = cpeRepository.findAllByMacAddressCpe(dto.getMacAddressCpe());
        if (matchingCPE.size() > 1 || (!matchingCPE.isEmpty() && !matchingCPE.get(0).getEquipmentId().equals(cpe.getEquipmentId()))) {
            throw new EqmValidationException(localizedMessageBuilder, CPE_MAC_ADDRESS_ALREADY_IN_USE, dto.getMacAddressCpe());
        }
    }
}
