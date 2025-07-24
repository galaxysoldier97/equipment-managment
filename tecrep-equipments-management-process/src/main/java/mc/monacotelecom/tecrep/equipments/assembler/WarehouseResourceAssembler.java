package mc.monacotelecom.tecrep.equipments.assembler;

import mc.monacotelecom.tecrep.equipments.dto.WarehouseDTO;
import mc.monacotelecom.tecrep.equipments.entity.Warehouse;
import mc.monacotelecom.tecrep.equipments.mapper.WarehouseMapper;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

public class WarehouseResourceAssembler extends RepresentationModelAssemblerSupport<Warehouse, WarehouseDTO> {

    private WarehouseResourceAssembler(Class<?> controllerClass) {
        super(controllerClass, WarehouseDTO.class);
    }

    public static WarehouseResourceAssembler of(Class<?> controllerClass) {
        return new WarehouseResourceAssembler(controllerClass);
    }

    @Override
    public WarehouseDTO toModel(Warehouse entity) {
        return WarehouseMapper.INSTANCE.toDto(entity);
    }
}