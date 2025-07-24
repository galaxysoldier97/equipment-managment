package mc.monacotelecom.tecrep.equipments.messaging;


import mc.monacotelecom.tecrep.equipments.entity.EsimNotification;
import mc.monacotelecom.tecrep.equipments.integration.BaseIntegrationTest;
import mc.monacotelecom.tecrep.equipments.mapper.EsimNotificationMapperImpl;
import mc.monacotelecom.tecrep.equipments.messagging.handler.EsimNotificationHandler;
import mc.monacotelecom.tecrep.equipments.messagging.listener.EsimNotificationListener;
import mc.monacotelecom.tecrep.equipments.repository.EsimNotificationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static mc.monacotelecom.tecrep.equipments.messaging.HelperTest.eSimNotificationBuild;
import static mc.monacotelecom.tecrep.equipments.messaging.HelperTest.sendMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Sql({"/sql/clean.sql","/sql/simcard_data.sql"})
public class EsimNotificationListenerTest extends BaseIntegrationTest {

    @SpyBean
    public EsimNotificationListener esimNotificationListener;
    @SpyBean
    public EsimNotificationRepository esimNotificationRepository;
    @SpyBean
    public EsimNotificationHandler esimNotificationHandler;
    @SpyBean
    public EsimNotificationMapperImpl esimNotificationMapper;

    @Test
    void notification_persist_success() {
        esimNotificationListener.onEsimNotificationRequest(sendMessage(eSimNotificationBuild()));
        Optional<EsimNotification> esimNotificationOptional = esimNotificationRepository.findById(3L);
        assertTrue(esimNotificationOptional.isPresent());
        var esimNotification = esimNotificationOptional.get();

        assertEquals("893771033000000007", esimNotification.getIccid());
        assertEquals(1L, esimNotification.getEquipment().getEquipmentId());
        assertEquals("TypeA", esimNotification.getProfileType());
        assertEquals("102", esimNotification.getReasonCode());
        assertEquals("installation", esimNotification.getCheckPoint());
        assertEquals("2020-05-31T20:35:24", esimNotification.getDate().toString());
    }
}
