package mc.monacotelecom.tecrep.equipments.process;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.dto.v2.StandardLoadDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.StandardLoad;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmNotFoundException;
import mc.monacotelecom.tecrep.equipments.mapper.StandardLoadMapper;
import mc.monacotelecom.tecrep.equipments.repository.StandardLoadRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.STANDARD_LOAD_ID_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class StandardLoadProcess {

    private final StandardLoadRepository standardLoadRepository;
    private final LocalizedMessageBuilder localizedMessageBuilder;
    private final StandardLoadMapper standardLoadMapper;

    public StandardLoadDTOV2 getById(Long id) {
        StandardLoad entity = standardLoadRepository.findById(id)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, STANDARD_LOAD_ID_NOT_FOUND, id));
        return standardLoadMapper.toDto(entity);
    }

    public StandardLoadDTOV2 add(StandardLoadDTOV2 dto) {
        StandardLoad entity = standardLoadMapper.toEntity(dto);
        return standardLoadMapper.toDto(standardLoadRepository.save(entity));
    }

    public StandardLoadDTOV2 update(Long id, StandardLoadDTOV2 dto) {
        StandardLoad entity = standardLoadRepository.findById(id)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, STANDARD_LOAD_ID_NOT_FOUND, id));
        entity.setName(dto.getName());
        entity.setStatus(dto.getStatus());
        return standardLoadMapper.toDto(standardLoadRepository.save(entity));
    }

    public Page<StandardLoadDTOV2> getAll(Pageable pageable) {
        return standardLoadRepository.findAll(pageable)
                .map(standardLoadMapper::toDto);
    }
}

