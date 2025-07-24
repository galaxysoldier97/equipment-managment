package mc.monacotelecom.tecrep.equipments.process.simcard;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.inventory.common.exporter.process.ExcelGenerator;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.inventory.common.repository.CustomRepository;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchSimCardDTO;
import mc.monacotelecom.tecrep.equipments.entity.SimCard;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

import static mc.monacotelecom.inventory.common.GenericsHelper.objectHasNotNullValues;
import static mc.monacotelecom.inventory.common.Lambdas.verifyOrThrow;
import static mc.monacotelecom.tecrep.equipments.process.simcard.searcher.AbstractSimCardSearcher.prepareSpecifications;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.EXPORTER_REQUIRED_SEARCH_PARAM;

@Component
@RequiredArgsConstructor
public class SimCardExporter {

    private final ExcelGenerator excelGenerator;
    private final CustomRepository customRepository;
    private final LocalizedMessageBuilder localizedMessageBuilder;

    public ByteArrayInputStream export(final SearchSimCardDTO searchSimCardDTO) {

        verifyOrThrow.test(Boolean.FALSE.equals(objectHasNotNullValues(searchSimCardDTO)), new EqmValidationException(localizedMessageBuilder, EXPORTER_REQUIRED_SEARCH_PARAM));
        final var simcards = customRepository.streamAll(prepareSpecifications(searchSimCardDTO), SimCard.class);

        return excelGenerator.writeToExcel(simcards, SimCard.class);
    }
}