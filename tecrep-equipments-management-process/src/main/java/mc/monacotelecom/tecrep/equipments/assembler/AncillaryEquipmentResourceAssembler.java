package mc.monacotelecom.tecrep.equipments.assembler;

import mc.monacotelecom.tecrep.equipments.dto.AncillaryEquipmentDTO;
import mc.monacotelecom.tecrep.equipments.entity.AncillaryEquipment;
import mc.monacotelecom.tecrep.equipments.entity.Equipment;
import mc.monacotelecom.tecrep.equipments.mapper.AncillaryMapper;
import mc.monacotelecom.tecrep.equipments.repository.EquipmentRepository;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

import java.util.Optional;

public class AncillaryEquipmentResourceAssembler extends RepresentationModelAssemblerSupport<AncillaryEquipment, AncillaryEquipmentDTO> {
    private final EquipmentRepository<Equipment> equipmentRepository;
    private final AncillaryMapper ancillaryMapper;

    private AncillaryEquipmentResourceAssembler(Class<?> controllerClass,
                                                final EquipmentRepository<Equipment> equipmentRepository,
                                                final AncillaryMapper ancillaryMapper) {
        super(controllerClass, AncillaryEquipmentDTO.class);
        this.equipmentRepository = equipmentRepository;
        this.ancillaryMapper = ancillaryMapper;
    }

    public static AncillaryEquipmentResourceAssembler of(Class<?> controllerClass, EquipmentRepository<Equipment> equipmentRepository, AncillaryMapper ancillaryMapper) {
        return new AncillaryEquipmentResourceAssembler(controllerClass, equipmentRepository, ancillaryMapper);
    }

    @Override
    public AncillaryEquipmentDTO toModel(AncillaryEquipment ancillaryEquipment) {
        var ancillaryEquipmentDTO = ancillaryMapper.toDtoV1(ancillaryEquipment);

        if (ancillaryEquipment.getPairedEquipment() != null) {
            Optional<Equipment> pairedEquipment = equipmentRepository.findById(ancillaryEquipment.getPairedEquipment().getEquipmentId());
            pairedEquipment.ifPresent(equipment -> ancillaryEquipmentDTO.setPairedEquipmentCategory(equipment.getCategory()));
        }

        return ancillaryEquipmentDTO;
    }
}