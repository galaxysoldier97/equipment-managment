package mc.monacotelecom.tecrep.equipments.process.cpe;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.inventory.common.exporter.process.ExcelGenerator;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.inventory.common.repository.CustomRepository;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchCpeDto;
import mc.monacotelecom.tecrep.equipments.entity.CPE;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.util.Objects;
import java.util.regex.Pattern;

import static mc.monacotelecom.inventory.common.GenericsHelper.objectHasNotNullValues;
import static mc.monacotelecom.inventory.common.Lambdas.verifyOrThrow;
import static mc.monacotelecom.tecrep.equipments.process.cpe.CPESearcher.prepareSpecification;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.CPE_MAC_ADDRESS_NOT_VALID_PATTERN;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.EXPORTER_REQUIRED_SEARCH_PARAM;

@Component
@RequiredArgsConstructor
public class CPEExporter {

    private final ExcelGenerator excelGenerator;
    private final CustomRepository customRepository;
    private final LocalizedMessageBuilder localizedMessageBuilder;

    public ByteArrayInputStream export(final SearchCpeDto searchCpeDto) {

        verifyOrThrow.test((Objects.nonNull(searchCpeDto.getMacAddress()) ),
                new EqmValidationException(localizedMessageBuilder, CPE_MAC_ADDRESS_NOT_VALID_PATTERN, searchCpeDto.getMacAddress()));
        verifyOrThrow.test(Boolean.FALSE.equals(objectHasNotNullValues(searchCpeDto)),
                new EqmValidationException(localizedMessageBuilder, EXPORTER_REQUIRED_SEARCH_PARAM));

        final var cpes = customRepository.streamAll(prepareSpecification(searchCpeDto), CPE.class);

        return excelGenerator.writeToExcel(cpes, CPE.class);
    }
}
