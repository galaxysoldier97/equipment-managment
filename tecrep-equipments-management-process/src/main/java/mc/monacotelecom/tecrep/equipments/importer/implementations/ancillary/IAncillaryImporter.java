package mc.monacotelecom.tecrep.equipments.importer.implementations.ancillary;

import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentCategory;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentName;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.repository.AncillaryRepository;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;
import java.util.function.BinaryOperator;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.CPE_CATEGORY_SERIAL_NUMBER_DUPLICATED;

public interface IAncillaryImporter {

    EquipmentName getEquipmentName();
    
    /**
     * Devuelve los nombres de formatos soportados por este importador
     * @return Conjunto de nombres de formatos
     */
    Set<String> getNames();

    default void checkAncillaryExist(final AncillaryRepository ancillaryRepository,
                                     final LocalizedMessageBuilder localizedMessageBuilder,
                                     final String serialNumber) {
        ancillaryRepository.findByCategoryAndSerialNumber(EquipmentCategory.ANCILLARY, serialNumber)
                .ifPresent(id -> {
                    throw new EqmValidationException(localizedMessageBuilder, CPE_CATEGORY_SERIAL_NUMBER_DUPLICATED, EquipmentCategory.ANCILLARY, serialNumber);
                });
    }

    default String cleanupDecimals(String input) {
        return !input.contains(".") ? input : input.split("\\.")[0];
    }

    BinaryOperator<String> setIfBlank = (s1, s2) -> StringUtils.isNotBlank(s1) ? s1 : s2;
}
