package mc.monacotelecom.tecrep.equipments.assembler;

import mc.monacotelecom.tecrep.equipments.dto.SimCardDTO;
import mc.monacotelecom.tecrep.equipments.entity.SimCard;
import mc.monacotelecom.tecrep.equipments.mapper.SimCardMapper;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

public class SimCardResourceAssembler extends RepresentationModelAssemblerSupport<SimCard, SimCardDTO> {

    private final SimCardMapper simCardMapper;

    private SimCardResourceAssembler(Class<?> controllerClass, SimCardMapper simCardMapper) {
        super(controllerClass, SimCardDTO.class);
        this.simCardMapper = simCardMapper;
    }

    public static SimCardResourceAssembler of(Class<?> controllerClass, SimCardMapper simCardMapper) {
        return new SimCardResourceAssembler(controllerClass, simCardMapper);
    }

    @Override
    public SimCardDTO toModel(SimCard entity) {
        SimCardDTO sd = simCardMapper.toDtoV1(entity);
        sd.setEquipmentId(entity.getEquipmentId());
        return sd;
    }
}