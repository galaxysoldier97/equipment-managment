package mc.monacotelecom.tecrep.equipments.process.simcard;

import mc.monacotelecom.inventory.common.connectors.exceptions.ExternalConnectorException;
import mc.monacotelecom.inventory.common.nls.EnableCommonNls;
import mc.monacotelecom.tecrep.equipments.connectors.TabsConnector;
import mc.monacotelecom.tecrep.equipments.connectors.dto.TABSSimResponse;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchSimCardDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.SimCardDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.Plmn;
import mc.monacotelecom.tecrep.equipments.entity.Provider;
import mc.monacotelecom.tecrep.equipments.entity.SimCard;
import mc.monacotelecom.tecrep.equipments.entity.Warehouse;
import mc.monacotelecom.tecrep.equipments.mapper.*;
import mc.monacotelecom.tecrep.equipments.process.EquipmentStateMachineWithRecyclingService;
import mc.monacotelecom.tecrep.equipments.process.EquipmentStateMachineWithoutRecyclingService;
import mc.monacotelecom.tecrep.equipments.process.simcard.searcher.SimCardSearcherWithTabs;
import mc.monacotelecom.tecrep.equipments.repository.PlmnRepository;
import mc.monacotelecom.tecrep.equipments.repository.ProviderRepository;
import mc.monacotelecom.tecrep.equipments.repository.SimCardRepository;
import mc.monacotelecom.tecrep.equipments.repository.WarehouseRepository;
import mc.monacotelecom.tecrep.equipments.state.StateMachineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import java.time.Clock;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {
        SimCardSearcherWithTabs.class,
        StateMachineService.class,
        SimCardMapperImpl.class,
        PlmnMapperImpl.class,
        ProviderMapperImpl.class,
        WarehouseMapperImpl.class,
        InventoryPoolMapperImpl.class
})
@MockBeans({
        @MockBean(EquipmentStateMachineWithoutRecyclingService.class),
        @MockBean(EquipmentStateMachineWithRecyclingService.class),
        @MockBean(WarehouseRepository.class),
        @MockBean(ProviderRepository.class),
        @MockBean(PlmnRepository.class),
        @MockBean(ZoneId.class),
        @MockBean(Clock.class)
})
@ActiveProfiles({"epic", "test"})
@EnableCommonNls
class SimCardTabsTests {

    @Autowired
    private SimCardSearcherWithTabs simCardSearcher;

    @MockBean
    private TabsConnector tabsConnector;

    @MockBean
    private SimCardRepository simCardRepository;

    @MockBean
    private PlmnRepository plmnRepository;

    @MockBean
    private ProviderRepository providerRepository;

    @MockBean
    private WarehouseRepository warehouseRepository;

    @Test
    void externalSimCardsInTabs_callTabs() {
        PageRequest pageable = PageRequest.of(0, 20);
        when(simCardRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList(), pageable, 0));

        when(plmnRepository.findAll()).thenReturn(Collections.singletonList(new Plmn()));
        when(providerRepository.findAll()).thenReturn(Collections.singletonList(new Provider()));
        when(warehouseRepository.findAll()).thenReturn(Collections.singletonList(new Warehouse()));

        var simCard = new SimCard();
        simCard.setEquipmentId(1L);
        simCard.setRecyclable(Boolean.TRUE);
        when(simCardRepository.save(any())).thenReturn(simCard);
        when(simCardRepository.findByEquipmentId(1L)).thenReturn(Optional.of(simCard));

        TABSSimResponse value = new TABSSimResponse();
        when(tabsConnector.checkSimCard("123456"))
                .thenReturn(value);

        SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
        searchSimCardDTO.setSn("123456");
        Page<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable);

        assertEquals(1, result.getContent().size());

        verify(tabsConnector).checkSimCard("123456");
        verify(simCardRepository).save(any());
    }

    @Test
    void externalSimCardsInTabs_callTabsExceptionHandling() {
        PageRequest pageable = PageRequest.of(0, 20);
        when(simCardRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl(Collections.emptyList(), pageable, 0));

        when(tabsConnector.checkSimCard("123456"))
                .thenThrow(ExternalConnectorException.class);

        SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
        searchSimCardDTO.setSn("123456");
        Page<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable);

        assertEquals(0, result.getContent().size());

        verify(tabsConnector).checkSimCard("123456");
        verify(simCardRepository, times(0)).save(any());
    }

    @Test
    void externalSimCardsInTabs_notCallTabsIfNotIccid() {
        PageRequest pageable = PageRequest.of(0, 20);
        when(simCardRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl(Collections.emptyList(), pageable, 0));


        SearchSimCardDTO searchSimCardDTO = new SearchSimCardDTO();
        searchSimCardDTO.setImsi("123456");
        Page<SimCardDTOV2> result = simCardSearcher.search(searchSimCardDTO, pageable);

        assertEquals(0, result.getContent().size());

        verifyNoInteractions(tabsConnector);
    }
}
