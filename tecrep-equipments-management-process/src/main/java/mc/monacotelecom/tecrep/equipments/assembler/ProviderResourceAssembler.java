package mc.monacotelecom.tecrep.equipments.assembler;

import mc.monacotelecom.tecrep.equipments.dto.ProviderDTO;
import mc.monacotelecom.tecrep.equipments.entity.Provider;
import mc.monacotelecom.tecrep.equipments.mapper.ProviderMapper;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

public class ProviderResourceAssembler extends RepresentationModelAssemblerSupport<Provider, ProviderDTO> {


    private ProviderResourceAssembler(Class<?> controllerClass) {
        super(controllerClass, ProviderDTO.class);
    }

    public static ProviderResourceAssembler of(Class<?> controllerClass) {
        return new ProviderResourceAssembler(controllerClass);
    }

    @Override
    public ProviderDTO toModel(Provider entity) {
        return ProviderMapper.INSTANCE.toDto(entity);
    }
}