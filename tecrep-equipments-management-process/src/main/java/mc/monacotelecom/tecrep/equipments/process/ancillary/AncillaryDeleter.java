package mc.monacotelecom.tecrep.equipments.process.ancillary;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.inventory.common.Lambdas;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.repository.AncillaryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static mc.monacotelecom.tecrep.equipments.enums.Status.*;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.ANCILLARY_DELETE_INCORRECT_STATUS;

@RequiredArgsConstructor
@Component
public class AncillaryDeleter {
    private final AncillaryRepository ancillaryRepository;
    private final LocalizedMessageBuilder localizedMessageBuilder;

    public void delete(Long id) {
        var ancillaryEquipment = ancillaryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Lambdas.verifyOrThrow.test(!List.of(DEPRECATED, DEACTIVATED, INSTORE).contains(ancillaryEquipment.getStatus()),
                new EqmValidationException(localizedMessageBuilder, ANCILLARY_DELETE_INCORRECT_STATUS, ancillaryEquipment.getSerialNumber(), ancillaryEquipment.getStatus()));

        ancillaryRepository.delete(ancillaryEquipment);
    }
}
