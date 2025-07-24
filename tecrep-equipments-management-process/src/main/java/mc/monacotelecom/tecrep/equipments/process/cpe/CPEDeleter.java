package mc.monacotelecom.tecrep.equipments.process.cpe;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.inventory.common.Lambdas;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.entity.AncillaryEquipment;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmNotFoundException;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.repository.CPERepository;
import org.springframework.stereotype.Component;

import java.util.List;

import static mc.monacotelecom.tecrep.equipments.enums.Status.*;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.*;

@Component
@RequiredArgsConstructor
public class CPEDeleter {
    private final CPERepository cpeRepository;
    private final LocalizedMessageBuilder localizedMessageBuilder;

    public void delete(Long id, boolean forced) {
        var cpe = cpeRepository.findByEquipmentId(id)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, CPE_NOT_FOUND, id));

        Lambdas.verifyOrThrow.test(!List.of(DEPRECATED, DEACTIVATED, INSTORE).contains(cpe.getStatus()),
                new EqmValidationException(localizedMessageBuilder, CPE_DELETE_INCORRECT_STATUS, cpe.getSerialNumber(), cpe.getStatus()));

        if (!forced) {
            List<AncillaryEquipment> pairedEquipments = cpe.getAncillaryEquipments();
            Lambdas.verifyOrThrow.test(!pairedEquipments.isEmpty(),
                    new EqmValidationException(localizedMessageBuilder, CPE_DELETE_ERROR_PAIRED, cpe.getSerialNumber()));
        }

        cpeRepository.deleteById(cpe.getEquipmentId());
    }
}
