package mc.monacotelecom.tecrep.equipments.process.ancillary;

import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.inventory.common.sm.StatusChanger;
import mc.monacotelecom.tecrep.equipments.assembler.AncillaryEquipmentResourceAssembler;
import mc.monacotelecom.tecrep.equipments.dto.AncillaryEquipmentDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.AncillaryEquipmentDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.AncillaryEquipment;
import mc.monacotelecom.tecrep.equipments.entity.Equipment;
import mc.monacotelecom.tecrep.equipments.enums.Event;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmNotFoundException;
import mc.monacotelecom.tecrep.equipments.mapper.AncillaryMapper;
import mc.monacotelecom.tecrep.equipments.repository.AncillaryRepository;
import mc.monacotelecom.tecrep.equipments.repository.EquipmentRepository;
import mc.monacotelecom.tecrep.equipments.state.StateMachineService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.ANCILLARY_EQUIPMENT_NOT_FOUND;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.ANCILLARY_EQUIPMENT_PAIREDEQUIPMENT_NOT_FOUND;

@Component
public class AncillaryRetriever {
    private final AncillaryRepository ancillaryRepository;
    private final LocalizedMessageBuilder localizedMessageBuilder;
    private final AncillaryEquipmentResourceAssembler ancillaryEquipmentResourceAssembler;
    private final StateMachineService stateMachineService;
    private final AncillaryMapper ancillaryMapper;

    public AncillaryRetriever(final AncillaryRepository ancillaryRepository,
                              final EquipmentRepository<Equipment> equipmentRepository,
                              final LocalizedMessageBuilder localizedMessageBuilder,
                              final StateMachineService stateMachineService,
                              final AncillaryMapper ancillaryMapper) {
        this.ancillaryRepository = ancillaryRepository;
        this.localizedMessageBuilder = localizedMessageBuilder;
        this.ancillaryEquipmentResourceAssembler = AncillaryEquipmentResourceAssembler.of(AncillaryRetriever.class, equipmentRepository, ancillaryMapper);
        this.stateMachineService = stateMachineService;
        this.ancillaryMapper = ancillaryMapper;
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public AncillaryEquipmentDTO getByIdV1(final Long ancillaryEquipmentId) {
        return mapWithActionsV1(ancillaryRepository.findById(ancillaryEquipmentId)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, ANCILLARY_EQUIPMENT_NOT_FOUND, ancillaryEquipmentId)));
    }

    public AncillaryEquipmentDTOV2 getById(Long id) {
        return mapWithActions(ancillaryRepository.findById(id)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, ANCILLARY_EQUIPMENT_NOT_FOUND, id)));
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public AncillaryEquipmentDTO getBySerialNumberV1(String serialNumber) {
        return mapWithActionsV1(ancillaryRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, ANCILLARY_EQUIPMENT_NOT_FOUND, serialNumber)));
    }

    public AncillaryEquipmentDTOV2 getBySerialNumber(String serialNumber) {
        return mapWithActions(ancillaryRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, ANCILLARY_EQUIPMENT_NOT_FOUND, serialNumber)));
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public AncillaryEquipmentDTO getByPairedEquipmentSerialV1(String serialNumber) {
        return mapWithActionsV1(ancillaryRepository.findByPairedEquipmentSerialNumber(serialNumber)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, ANCILLARY_EQUIPMENT_PAIREDEQUIPMENT_NOT_FOUND, serialNumber)));
    }

    public AncillaryEquipmentDTOV2 getByPairedEquipmentSerial(String serialNumber) {
        return mapWithActions(ancillaryRepository.findByPairedEquipmentSerialNumber(serialNumber)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, ANCILLARY_EQUIPMENT_PAIREDEQUIPMENT_NOT_FOUND, serialNumber)));
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    private AncillaryEquipmentDTO mapWithActionsV1(AncillaryEquipment ancillaryEquipment) {
        var ancillaryEquipmentDTO = ancillaryEquipmentResourceAssembler.toModel(ancillaryEquipment);
        final StatusChanger<Status, Event> statusEventStatusChanger = stateMachineService.getMachine(ancillaryEquipment.getRecyclable());
        final List<Event> serviceTransitions = new ArrayList<>(statusEventStatusChanger.getAvailableEventsWithId(String.valueOf(ancillaryEquipment.getEquipmentId()), ancillaryEquipmentDTO.getStatus()));
        ancillaryEquipmentDTO.setEvents(serviceTransitions);
        return ancillaryEquipmentDTO;
    }

    private AncillaryEquipmentDTOV2 mapWithActions(AncillaryEquipment ancillaryEquipment) {
        var ancillaryEquipmentDTO = ancillaryMapper.toDtoV2(ancillaryEquipment);
        final StatusChanger<Status, Event> statusEventStatusChanger = stateMachineService.getMachine(ancillaryEquipment.getRecyclable());
        final List<Event> serviceTransitions = new ArrayList<>(statusEventStatusChanger.getAvailableEventsWithId(String.valueOf(ancillaryEquipment.getEquipmentId()), ancillaryEquipmentDTO.getStatus()));
        ancillaryEquipmentDTO.setEvents(serviceTransitions);
        return ancillaryEquipmentDTO;
    }
}
