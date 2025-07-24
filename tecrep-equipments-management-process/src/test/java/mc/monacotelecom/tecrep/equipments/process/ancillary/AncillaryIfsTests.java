package mc.monacotelecom.tecrep.equipments.process.ancillary;

import mc.monacotelecom.inventory.common.connectors.exceptions.ExternalConnectorException;
import mc.monacotelecom.inventory.common.nls.EnableCommonNls;
import mc.monacotelecom.tecrep.equipments.connectors.IfsConnector;
import mc.monacotelecom.tecrep.equipments.connectors.dto.IFSBoxResponse;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchAncillaryEquipmentDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.AncillaryEquipmentDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.AncillaryEquipment;
import mc.monacotelecom.tecrep.equipments.entity.EquipmentModel;
import mc.monacotelecom.tecrep.equipments.entity.Provider;
import mc.monacotelecom.tecrep.equipments.entity.Warehouse;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentModelCategory;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentName;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmNotFoundException;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.mapper.AncillaryMapperImpl;
import mc.monacotelecom.tecrep.equipments.mapper.EquipmentModelMapperImpl;
import mc.monacotelecom.tecrep.equipments.mapper.ProviderMapperImpl;
import mc.monacotelecom.tecrep.equipments.mapper.WarehouseMapperImpl;
import mc.monacotelecom.tecrep.equipments.process.EquipmentModelProcess;
import mc.monacotelecom.tecrep.equipments.process.EquipmentStateMachineWithRecyclingService;
import mc.monacotelecom.tecrep.equipments.process.EquipmentStateMachineWithoutRecyclingService;
import mc.monacotelecom.tecrep.equipments.process.ancillary.searcher.AncillarySearcherWithIfs;
import mc.monacotelecom.tecrep.equipments.repository.*;
import mc.monacotelecom.tecrep.equipments.state.StateMachineService;
import mc.monacotelecom.tecrep.equipments.utils.AuditCleaner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {
        AncillarySearcherWithIfs.class,
        AncillaryStatusChanger.class,
        StateMachineService.class,
        AuditCleaner.class,
        AncillaryMapperImpl.class,
        EquipmentModelMapperImpl.class,
        ProviderMapperImpl.class,
        WarehouseMapperImpl.class
})
@MockBeans({
        @MockBean(EquipmentStateMachineWithoutRecyclingService.class),
        @MockBean(EquipmentStateMachineWithRecyclingService.class),
        @MockBean(WarehouseRepository.class),
        @MockBean(ProviderRepository.class),
        @MockBean(PlmnRepository.class),
        @MockBean(ZoneId.class),
        @MockBean(Clock.class),
        @MockBean(EquipmentModelRepository.class),
        @MockBean(EquipmentRepository.class)
})
@ActiveProfiles({"epic", "test"})
@EnableCommonNls
class AncillaryIfsTests {

    @Autowired
    private AncillarySearcherWithIfs ancillarySearcher;

    @Autowired
    private AncillaryStatusChanger ancillaryStatusChanger;

    @MockBean
    private IfsConnector ifsConnector;

    @MockBean
    private AncillaryRepository ancillaryRepository;

    @MockBean
    private EquipmentModelProcess equipmentModelProcess;

    @MockBean
    private ProviderRepository providerRepository;

    @MockBean
    private WarehouseRepository warehouseRepository;

    @Test
    void externalAncillaryInIfs_callIfs() {
        PageRequest pageable = PageRequest.of(0, 20);
        when(ancillaryRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl(Collections.emptyList(), pageable, 0));

        var ancillaryEquipment = new AncillaryEquipment();
        ancillaryEquipment.setEquipmentId(1L);
        ancillaryEquipment.setRecyclable(Boolean.TRUE);
        when(ancillaryRepository.save(any())).thenReturn(ancillaryEquipment);
        when(ancillaryRepository.findById(1L)).thenReturn(Optional.of(ancillaryEquipment));

        IFSBoxResponse value = new IFSBoxResponse();
        when(ifsConnector.checkBox("123456"))
                .thenReturn(value);

        when(warehouseRepository.findAll()).thenReturn(Collections.singletonList(new Warehouse()));

        SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
        searchAncillaryEquipmentDTO.setSerialNumber("123456");
        Page<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable);

        assertEquals(1, result.getContent().size());

        verify(ifsConnector).checkBox("123456");
        verify(ancillaryRepository).save(any());
    }

    @Test
    void externalAncillaryInIfs_callIfs_noModel() {
        PageRequest pageable = PageRequest.of(0, 20);
        when(ancillaryRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl(Collections.emptyList(), pageable, 0));

        var ancillaryEquipment = new AncillaryEquipment();
        ancillaryEquipment.setEquipmentId(1L);
        ancillaryEquipment.setRecyclable(Boolean.TRUE);
        when(ancillaryRepository.save(any())).thenReturn(ancillaryEquipment);
        when(ancillaryRepository.findById(1L)).thenReturn(Optional.of(ancillaryEquipment));
        when(warehouseRepository.findAll()).thenReturn(Collections.singletonList(new Warehouse()));

        IFSBoxResponse value = new IFSBoxResponse();
        value.setDescription("myModel");
        when(ifsConnector.checkBox("123456"))
                .thenReturn(value);

        when(equipmentModelProcess.getByNameAndCategory("myModel", EquipmentModelCategory.ANCILLARY))
                .thenThrow(new EqmNotFoundException(null, null));

        when(providerRepository.findAll()).thenReturn(Collections.singletonList(new Provider()));

        SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
        searchAncillaryEquipmentDTO.setSerialNumber("123456");
        Page<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable);

        assertEquals(1, result.getContent().size());

        verify(ifsConnector).checkBox("123456");
        verify(ancillaryRepository).save(any());
    }

    @Test
    void externalAncillaryInIfs_longModelName() {
        PageRequest pageable = PageRequest.of(0, 20);
        when(ancillaryRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl(Collections.emptyList(), pageable, 0));

        var ancillaryEquipment = new AncillaryEquipment();
        ancillaryEquipment.setEquipmentId(1L);
        ancillaryEquipment.setRecyclable(Boolean.TRUE);
        when(ancillaryRepository.save(any())).thenReturn(ancillaryEquipment);
        when(ancillaryRepository.findById(1L)).thenReturn(Optional.of(ancillaryEquipment));

        IFSBoxResponse value = new IFSBoxResponse();
        value.setDescription("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        when(ifsConnector.checkBox("123456"))
                .thenReturn(value);

        when(equipmentModelProcess.getByNameAndCategory("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", EquipmentModelCategory.ANCILLARY))
                .thenReturn(new EquipmentModel());

        when(warehouseRepository.findAll()).thenReturn(Collections.singletonList(new Warehouse()));

        SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
        searchAncillaryEquipmentDTO.setSerialNumber("123456");
        Page<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable);

        assertEquals(1, result.getContent().size());

        verify(equipmentModelProcess).getByNameAndCategory("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", EquipmentModelCategory.ANCILLARY);
        verify(ifsConnector).checkBox("123456");
        verify(ancillaryRepository).save(any());
    }

    @Test
    void externalAncillaryInIfs_callIfs_noDefaultWarehouse() {
        PageRequest pageable = PageRequest.of(0, 20);
        when(ancillaryRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl(Collections.emptyList(), pageable, 0));

        var ancillaryEquipment = new AncillaryEquipment();
        ancillaryEquipment.setEquipmentId(1L);
        ancillaryEquipment.setRecyclable(Boolean.TRUE);
        when(ancillaryRepository.save(any())).thenReturn(ancillaryEquipment);
        when(ancillaryRepository.findById(1L)).thenReturn(Optional.of(ancillaryEquipment));

        IFSBoxResponse value = new IFSBoxResponse();
        when(ifsConnector.checkBox("123456"))
                .thenReturn(value);

        SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
        searchAncillaryEquipmentDTO.setSerialNumber("123456");
        var e = assertThrows(EqmValidationException.class, () -> ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable));
        assertEquals("No default warehouse", e.getMessage());

        verify(ifsConnector).checkBox("123456");
        verify(ancillaryRepository, times(0)).save(any());
    }

    @Test
    void externalAncillaryInIfs_callIfs_noDefaultProvider() {
        PageRequest pageable = PageRequest.of(0, 20);
        when(ancillaryRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl(Collections.emptyList(), pageable, 0));

        var ancillaryEquipment = new AncillaryEquipment();
        ancillaryEquipment.setEquipmentId(1L);
        ancillaryEquipment.setRecyclable(Boolean.TRUE);
        when(ancillaryRepository.save(any())).thenReturn(ancillaryEquipment);
        when(ancillaryRepository.findById(1L)).thenReturn(Optional.of(ancillaryEquipment));
        when(warehouseRepository.findAll()).thenReturn(Collections.singletonList(new Warehouse()));

        IFSBoxResponse value = new IFSBoxResponse();
        value.setDescription("myModel");
        when(ifsConnector.checkBox("123456"))
                .thenReturn(value);

        when(equipmentModelProcess.getByNameAndCategory("myModel", EquipmentModelCategory.ANCILLARY))
                .thenThrow(new EqmNotFoundException(null, null));

        SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
        searchAncillaryEquipmentDTO.setSerialNumber("123456");

        var e = assertThrows(EqmValidationException.class, () -> ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable));
        assertEquals("No default provider", e.getMessage());

        verify(ifsConnector).checkBox("123456");
        verify(ancillaryRepository, times(0)).save(any());
    }

    @Test
    void externalAncillaryInIfs_callIfsExceptionHandling() {
        PageRequest pageable = PageRequest.of(0, 20);
        when(ancillaryRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl(Collections.emptyList(), pageable, 0));

        when(ifsConnector.checkBox("123456"))
                .thenThrow(ExternalConnectorException.class);

        var searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
        searchAncillaryEquipmentDTO.setSerialNumber("123456");
        Page<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable);

        assertEquals(0, result.getContent().size());

        verify(ifsConnector).checkBox("123456");
        verify(ancillaryRepository, times(0)).save(any());
    }

    @Test
    void externalAncillaryInIfs_notCallIfsIfNoSerialNumber() {
        PageRequest pageable = PageRequest.of(0, 20);
        when(ancillaryRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl(Collections.emptyList(), pageable, 0));

        var searchAncillaryEquipmentDTO = new SearchAncillaryEquipmentDTO();
        searchAncillaryEquipmentDTO.setEquipmentName(EquipmentName.BRDBOX);
        Page<AncillaryEquipmentDTOV2> result = ancillarySearcher.search(searchAncillaryEquipmentDTO, pageable);

        assertEquals(0, result.getContent().size());

        verifyNoInteractions(ifsConnector);
    }

    @Nested
    @DisplayName("Test number of cycles fonctionality")
    class NumberOfCycleTest {

        AncillaryEquipment initializeAncillary(Status status, long nRecycle) {
            var ancillary = new AncillaryEquipment();
            ancillary.setStatus(status);
            ancillary.setNumberRecycles(nRecycle);

            return ancillary;
        }

        @Test
        @DisplayName("different status")
        void differentStatus() {
            var ancillaryEquipment = this.initializeAncillary(Status.ACTIVATED, 3L);
            ancillaryStatusChanger.incrementNumberOfRecycles(ancillaryEquipment, Status.ACTIVATED, Status.DEACTIVATED);

            assertEquals(3L, ancillaryEquipment.getNumberRecycles());
        }

        @Test
        @DisplayName("increase number of cycle")
        void increase() {
            var ancillaryEquipment = this.initializeAncillary(Status.DEACTIVATED, 1L);
            ancillaryStatusChanger.incrementNumberOfRecycles(ancillaryEquipment, Status.DEACTIVATED, Status.AVAILABLE);

            assertEquals(2L, ancillaryEquipment.getNumberRecycles());
        }

        @Test
        @DisplayName("increase number of cycle")
        void increase_nCycle_zero() {
            var ancillaryEquipment = this.initializeAncillary(Status.DEACTIVATED, 0L);
            ancillaryStatusChanger.incrementNumberOfRecycles(ancillaryEquipment, Status.DEACTIVATED, Status.AVAILABLE);

            assertEquals(1L, ancillaryEquipment.getNumberRecycles());
        }
    }
}
