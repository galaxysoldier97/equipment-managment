package mc.monacotelecom.tecrep.equipments.process.simcard;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.inventory.common.Lambdas;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.entity.AncillaryEquipment;
import mc.monacotelecom.tecrep.equipments.entity.SimCard;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.repository.SimCardRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static mc.monacotelecom.tecrep.equipments.enums.Status.*;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.DELETE_EXIST_REFERENCE_PAIRED_ID;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.SIMCARD_DELETE_INCORRECT_STATUS;

@Component
@RequiredArgsConstructor
public class SimCardDeleter {
    private final SimCardRepository simCardRepository;
    private final LocalizedMessageBuilder localizedMessageBuilder;

    public void delete(final Long simCardId) {
        SimCard simcard = simCardRepository.findByEquipmentId(simCardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Lambdas.verifyOrThrow.test(!List.of(DEPRECATED, DEACTIVATED, INSTORE).contains(simcard.getStatus()),
                new EqmValidationException(localizedMessageBuilder, SIMCARD_DELETE_INCORRECT_STATUS, simcard.getSerialNumber(), simcard.getStatus()));

        List<AncillaryEquipment> pairedEquipments = simcard.getAncillaryEquipments();
        Lambdas.verifyOrThrow.test(!pairedEquipments.isEmpty(),
                new EqmValidationException(localizedMessageBuilder, DELETE_EXIST_REFERENCE_PAIRED_ID));

        simCardRepository.delete(simcard);
    }
}
