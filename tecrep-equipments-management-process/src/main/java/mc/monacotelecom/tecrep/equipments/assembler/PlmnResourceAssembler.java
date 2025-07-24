package mc.monacotelecom.tecrep.equipments.assembler;

import mc.monacotelecom.tecrep.equipments.dto.PlmnDTO;
import mc.monacotelecom.tecrep.equipments.entity.Plmn;
import mc.monacotelecom.tecrep.equipments.mapper.PlmnMapper;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

public class PlmnResourceAssembler extends RepresentationModelAssemblerSupport<Plmn, PlmnDTO> {

    private PlmnResourceAssembler(Class<?> controllerClass) {

        super(controllerClass, PlmnDTO.class);
    }

    public static PlmnResourceAssembler of(Class<?> controllerClass) {

        return new PlmnResourceAssembler(controllerClass);
    }

    @Override
    public PlmnDTO toModel(Plmn entity) {

        return PlmnMapper.INSTANCE.toDto(entity);
    }
}