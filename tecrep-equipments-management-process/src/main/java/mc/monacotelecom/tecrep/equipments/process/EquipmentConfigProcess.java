package mc.monacotelecom.tecrep.equipments.process;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.EquipmentConfigDTO;
import mc.monacotelecom.tecrep.equipments.entity.EquipmentConfig;
import mc.monacotelecom.tecrep.equipments.mapper.EquipmentConfigMapper;
import mc.monacotelecom.tecrep.equipments.repository.EquipmentConfigRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EquipmentConfigProcess {

    private final EquipmentConfigRepository repository;
    private final EquipmentConfigMapper mapper;

    public List<EquipmentConfigDTO> getAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public EquipmentConfigDTO getByNameAndStatus(String name, EquipmentConfig.Status status) {
        return repository.findByNameAndStatus(name, status).map(mapper::toDto).orElse(null);
    }

    public boolean existsByName(String name) {
        return repository.existsByName(name);
    }

    public EquipmentConfigDTO create(EquipmentConfigDTO dto) {
        EquipmentConfig entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    public EquipmentConfigDTO update(Long id, EquipmentConfigDTO dto) {
        return repository.findById(id).map(existing -> {
            if (dto.getName() != null) {
                existing.setName(dto.getName());
            }
            if (dto.getValue() != null) {
                existing.setValue(dto.getValue());
            }
            if (dto.getStatus() != null) {
                existing.setStatus(EquipmentConfig.Status.valueOf(dto.getStatus()));
            }
            return mapper.toDto(repository.save(existing));
        }).orElse(null);
    }

    public boolean delete(Long id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }

}