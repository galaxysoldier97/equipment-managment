package mc.monacotelecom.tecrep.equipments.messagging.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.tecrep.equipments.dto.EsimNotificationIngress;
import mc.monacotelecom.tecrep.equipments.mapper.EsimNotificationMapper;
import mc.monacotelecom.tecrep.equipments.repository.EsimNotificationRepository;
import mc.monacotelecom.tecrep.equipments.repository.SimCardRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class EsimNotificationHandler {

    private final EsimNotificationMapper esimNotificationMapper;
    private final EsimNotificationRepository esimNotificationRepository;
    private final SimCardRepository equipmentRepository;

    @Transactional
    public void handlePersist(EsimNotificationIngress esimNotification) {
        var entity = esimNotificationMapper.toEntity(esimNotification);
        log.info(String.format("ESIM-NOTIFICATION: received entity %s", entity.toString()));
        equipmentRepository.findBySerialNumber(esimNotification.getIccid()).ifPresentOrElse(equipment -> {
            entity.setEquipment(equipment);
            entity.setIccid(equipment.getSerialNumber());

            esimNotificationRepository.save(entity);
            log.info("ESIM-NOTIFICATION: successfully saved entity");
        }, () -> log.info("ESIM-NOTIFICATION: not found related equipment"));
    }
}
