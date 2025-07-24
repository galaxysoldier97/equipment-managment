package mc.monacotelecom.tecrep.equipments.process.ancillary;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.inventory.common.exporter.process.ExcelGenerator;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.inventory.common.repository.CustomRepository;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchAncillaryEquipmentDTO;
import mc.monacotelecom.tecrep.equipments.entity.AncillaryEquipment;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

import static mc.monacotelecom.inventory.common.GenericsHelper.objectHasNotNullValues;
import static mc.monacotelecom.inventory.common.Lambdas.verifyOrThrow;
import static mc.monacotelecom.tecrep.equipments.process.ancillary.searcher.AbstractAncillarySearcher.prepareSpecification;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.EXPORTER_REQUIRED_SEARCH_PARAM;

@Component
@RequiredArgsConstructor
public class AncillaryExporter {

    private final ExcelGenerator excelGenerator;
    private final CustomRepository customRepository;
    private final LocalizedMessageBuilder localizedMessageBuilder;

    public ByteArrayInputStream export(final SearchAncillaryEquipmentDTO searchAncillaryExporterDTO) {

        verifyOrThrow.test(Boolean.FALSE.equals(objectHasNotNullValues(searchAncillaryExporterDTO)), new EqmValidationException(localizedMessageBuilder, EXPORTER_REQUIRED_SEARCH_PARAM));
        final var ancillaryEquipments = customRepository.streamAll(prepareSpecification(searchAncillaryExporterDTO), AncillaryEquipment.class);

        return excelGenerator.writeToExcel(ancillaryEquipments, AncillaryEquipment.class);
    }
}
