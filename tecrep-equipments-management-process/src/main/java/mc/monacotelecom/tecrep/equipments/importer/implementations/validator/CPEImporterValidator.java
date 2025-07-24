package mc.monacotelecom.tecrep.equipments.importer.implementations.validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.inventory.common.Lambdas;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.entity.AncillaryEquipment;
import mc.monacotelecom.tecrep.equipments.entity.CPE;
import mc.monacotelecom.tecrep.equipments.entity.EquipmentModel;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.importer.data.GenericEquipementCsvLines.CpeDOCSISCsvLine;
import mc.monacotelecom.tecrep.equipments.importer.implementations.context.CPEImporterContext;
import mc.monacotelecom.tecrep.equipments.repository.AncillaryRepository;
import mc.monacotelecom.tecrep.equipments.repository.CPERepository;
import mc.monacotelecom.tecrep.equipments.repository.EquipmentModelRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import static mc.monacotelecom.tecrep.equipments.enums.EquipmentModelCategory.ANCILLARY;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.*;

/**
 * Validate Cpe import process before persist
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CPEImporterValidator {

    private final CPERepository cpeRepository;
    private final EquipmentModelRepository equipmentModelRepository;
    private final AncillaryRepository ancillaryRepository;
    private final LocalizedMessageBuilder localizedMessageBuilder;

    public EquipmentModel validateAncillaryModel(CPEImporterContext importerContext) {
        return importerContext.isShouldCreateAncillaryOnTheFly() ?
                equipmentModelRepository.findByNameAndCategory(importerContext.getEquipmentModel().getName(), ANCILLARY)
                        .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, MODEL_NOT_FOUND_NAME_CATEGORY, importerContext.getEquipmentModel().getName(), ANCILLARY)) :
                null;
    }

    public void validateEmptySerialNumberInAncillaryCsv(String serialNumber) {
        Lambdas.verifyOrThrow.test(StringUtils.isBlank(serialNumber),
                new EqmValidationException(localizedMessageBuilder, CPE_IMPORT_EMPTY_FIELD_SSN_ANCILLARY));
    }

    public void checkAncillaryMacAddressesAreUnique(AncillaryEquipment ancillaryEquipment) {
        Lambdas.verifyOrThrow.test(StringUtils.isNotBlank(ancillaryEquipment.getMacAddress()) && ancillaryRepository.findByMacAddress(ancillaryEquipment.getMacAddress()).isPresent(),
                new EqmValidationException(localizedMessageBuilder, CPE_IMPORT_UNIQUE_FIELD_VALIDATION, "MAC ADDRESS", ancillaryEquipment.getMacAddress()));
    }

    public void checkCpeMacAddressesAreUnique(CPE cpe) {
        Lambdas.verifyOrThrow.test(StringUtils.isNotBlank(cpe.getMacAddressCpe()) && !cpeRepository.findAllByMacAddressCpe(cpe.getMacAddressCpe()).isEmpty(),
                new EqmValidationException(localizedMessageBuilder, CPE_IMPORT_UNIQUE_FIELD_VALIDATION, "MAC ADDRESS CPE", cpe.getMacAddressCpe()));
        Lambdas.verifyOrThrow.test(StringUtils.isNotBlank(cpe.getMacAddressRouter()) && !cpeRepository.findAllByMacAddressRouter(cpe.getMacAddressRouter()).isEmpty(),
                new EqmValidationException(localizedMessageBuilder, CPE_IMPORT_UNIQUE_FIELD_VALIDATION, "MAC ADDRESS ROUTER", cpe.getMacAddressRouter()));
        Lambdas.verifyOrThrow.test(StringUtils.isNotBlank(cpe.getMacAddress4G()) && !cpeRepository.findAllByMacAddress4G(cpe.getMacAddress4G()).isEmpty(),
                new EqmValidationException(localizedMessageBuilder, CPE_IMPORT_UNIQUE_FIELD_VALIDATION, "MAC ADDRESS 4G", cpe.getMacAddress4G()));
        Lambdas.verifyOrThrow.test(StringUtils.isNotBlank(cpe.getMacAddress5G()) && !cpeRepository.findAllByMacAddress5G(cpe.getMacAddress5G()).isEmpty(),
                new EqmValidationException(localizedMessageBuilder, CPE_IMPORT_UNIQUE_FIELD_VALIDATION, "MAC ADDRESS 5G", cpe.getMacAddress5G()));
        Lambdas.verifyOrThrow.test(StringUtils.isNotBlank(cpe.getMacAddressLan()) && !cpeRepository.findAllByMacAddressLan(cpe.getMacAddressLan()).isEmpty(),
                new EqmValidationException(localizedMessageBuilder, CPE_IMPORT_UNIQUE_FIELD_VALIDATION, "MAC ADDRESS LAN", cpe.getMacAddressLan()));
        Lambdas.verifyOrThrow.test(StringUtils.isNotBlank(cpe.getMacAddressVoip()) && !cpeRepository.findAllByMacAddressVoip(cpe.getMacAddressVoip()).isEmpty(),
                new EqmValidationException(localizedMessageBuilder, CPE_IMPORT_UNIQUE_FIELD_VALIDATION, "MAC ADDRESS VOIP", cpe.getMacAddressVoip()));
    }

    public void validateMandatoryFieldsForDOCSISImporter(CpeDOCSISCsvLine parsedLine) {
        Lambdas.verifyOrThrow.test(parsedLine.getModel().isEmpty(),
                new EqmValidationException(localizedMessageBuilder, CPE_IMPORT_EMPTY_FIELD_VALIDATION, "model"));
        Lambdas.verifyOrThrow.test(parsedLine.getSerialNumber().isEmpty(),
                new EqmValidationException(localizedMessageBuilder, CPE_IMPORT_EMPTY_FIELD_VALIDATION, "serial number"));
        Lambdas.verifyOrThrow.test(parsedLine.getMacAddressCpe().isEmpty(),
                new EqmValidationException(localizedMessageBuilder, CPE_IMPORT_EMPTY_FIELD_VALIDATION, "Cpe MAC Address"));
        Lambdas.verifyOrThrow.test(parsedLine.getMacAddressVoip().isEmpty(),
                new EqmValidationException(localizedMessageBuilder, CPE_IMPORT_EMPTY_FIELD_VALIDATION, "VOIP MAC Address"));
        Lambdas.verifyOrThrow.test(parsedLine.getMacAddressRouter().isEmpty(),
                new EqmValidationException(localizedMessageBuilder, CPE_IMPORT_EMPTY_FIELD_VALIDATION, "Router MAC Address"));
    }
}
