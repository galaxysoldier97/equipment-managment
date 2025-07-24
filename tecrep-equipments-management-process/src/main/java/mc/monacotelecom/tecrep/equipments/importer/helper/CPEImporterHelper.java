package mc.monacotelecom.tecrep.equipments.importer.helper;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.entity.AncillaryEquipment;
import mc.monacotelecom.tecrep.equipments.entity.CPE;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentCategory;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentName;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.importer.data.GenericEquipementCsvLines;
import mc.monacotelecom.tecrep.equipments.importer.data.GenericEquipementCsvLines.CpeDOCSISCsvLine;
import mc.monacotelecom.tecrep.equipments.importer.implementations.context.CPEImporterContext;
import mc.monacotelecom.tecrep.equipments.importer.implementations.validator.CPEImporterValidator;
import mc.monacotelecom.tecrep.equipments.importer.interfaces.ICPELines;
import mc.monacotelecom.tecrep.equipments.mapper.AncillaryMapper;
import mc.monacotelecom.tecrep.equipments.mapper.CPEMapper;
import mc.monacotelecom.tecrep.equipments.repository.CPERepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static mc.monacotelecom.tecrep.equipments.mapper.CPEMapper.formatMacAddress;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.CPE_CATEGORY_SERIAL_NUMBER_DUPLICATED;

@Component
@RequiredArgsConstructor
public class CPEImporterHelper {

    private final CPERepository cpeRepository;
    private final LocalizedMessageBuilder localizedMessageBuilder;
    private final AncillaryMapper ancillaryMapper;
    private final CPEMapper cpeImportMapper;
    private final CPEImporterValidator cpeImporterValidator;

    public CPE parseLine(final ICPELines parsedLine,
                         final CPEImporterContext cpeImporterContext,
                         final EquipmentName equipmentName) {

        Optional<CPE> existingCpeOpt = cpeRepository.findByCategoryAndSerialNumber(EquipmentCategory.CPE, parsedLine.getSerialNumber());
        if (existingCpeOpt.isPresent()) {
            var existingCpe = existingCpeOpt.get();
            // If it already exists but has no ancillary, then no exception is thrown and we create the ancillary
            if (existingCpe.getAncillaryEquipments().isEmpty() && cpeImporterContext.isShouldCreateAncillaryOnTheFly()) {
                var ancillaryEquipment = createPairedAncillary(parsedLine, cpeImporterContext, existingCpe, equipmentName);
                existingCpe.getAncillaryEquipments().add(ancillaryEquipment);
                return existingCpe;
            } else {
                throw new EqmValidationException(localizedMessageBuilder, CPE_CATEGORY_SERIAL_NUMBER_DUPLICATED, EquipmentCategory.CPE, parsedLine.getSerialNumber());
            }
        } else {
            var cpe = createNewCPE(parsedLine, cpeImporterContext, equipmentName);
            if (parsedLine instanceof GenericEquipementCsvLines.CPEFTTHCsvLine) {
                cpeImporterValidator.checkCpeMacAddressesAreUnique(cpe);
            }
            return cpe;
        }
    }

    private AncillaryEquipment createPairedAncillary(final ICPELines parsedLine,
                                                     final CPEImporterContext cpeImporterContext,
                                                     final CPE cpe,
                                                     final EquipmentName equipmentName) {
        var ancillaryEquipment = ancillaryMapper.toNodeFromCPE(cpeImporterContext, cpe, cpeImporterValidator.validateAncillaryModel(cpeImporterContext), equipmentName);

        if (parsedLine instanceof CpeDOCSISCsvLine) {
            ancillaryEquipment.setSerialNumber(parsedLine.getStbSn());
        } else {
            ancillaryEquipment.setSerialNumber(parsedLine.getOntSn());
            ancillaryEquipment.setMacAddress(formatMacAddress(parsedLine.getGponDef()));
        }

        cpeImporterValidator.validateEmptySerialNumberInAncillaryCsv(ancillaryEquipment.getSerialNumber());
        ancillaryEquipment.setPassword(parsedLine.getOntPw());

        cpeImporterValidator.checkAncillaryMacAddressesAreUnique(ancillaryEquipment);
        return ancillaryEquipment;
    }

    private CPE createNewCPE(final ICPELines parsedLine,
                             final CPEImporterContext cpeImporterContext,
                             final EquipmentName equipmentName) {
        final var cpe = cpeImportMapper.toNode(parsedLine, cpeImporterContext);
        if (cpeImporterContext.isShouldCreateAncillaryOnTheFly()) {
            cpe.getAncillaryEquipments().add(createPairedAncillary(parsedLine, cpeImporterContext, cpe, equipmentName));
        }
        cpeImporterValidator.checkCpeMacAddressesAreUnique(cpe);
        return cpe;
    }
}
