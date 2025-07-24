package mc.monacotelecom.tecrep.equipments.assembler;

import mc.monacotelecom.tecrep.equipments.dto.CPEDTO;
import mc.monacotelecom.tecrep.equipments.entity.CPE;
import mc.monacotelecom.tecrep.equipments.mapper.CPEMapper;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

public class CPEResourceAssembler extends RepresentationModelAssemblerSupport<CPE, CPEDTO> {
    private final CPEMapper cpeMapper;

    private CPEResourceAssembler(Class<?> controllerClass, CPEMapper cpeMapper) {
        super(controllerClass, CPEDTO.class);
        this.cpeMapper = cpeMapper;
    }

    public static CPEResourceAssembler of(Class<?> controllerClass, CPEMapper cpeMapper) {
        return new CPEResourceAssembler(controllerClass, cpeMapper);
    }

    @Override
    public CPEDTO toModel(CPE entity) {
        return cpeMapper.toDto(entity);
    }
}