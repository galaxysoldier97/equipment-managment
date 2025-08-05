package mc.monacotelecom.tecrep.equipments.service;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.EquipmentConfigDTO;
import mc.monacotelecom.tecrep.equipments.entity.EquipmentConfig;
import mc.monacotelecom.tecrep.equipments.process.EquipmentConfigProcess;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipmentConfigService {

    private final EquipmentConfigProcess process;

    @Transactional(readOnly = true)
    public List<EquipmentConfigDTO> getAll() {
        return process.getAll();
    }

    @Transactional(readOnly = true)
    public EquipmentConfigDTO getByNameAndStatus(String name, EquipmentConfig.Status status) {
        return process.getByNameAndStatus(name, status);
    }

    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return process.existsByName(name);
    }

    @Transactional
    public EquipmentConfigDTO create(EquipmentConfigDTO dto) {
        return process.create(dto);
    }

    @Transactional
    public EquipmentConfigDTO update(Long id, EquipmentConfigDTO dto) {
        return process.update(id, dto);
    }

    @Transactional
    public boolean delete(Long id) {
        return process.delete(id);
    }
    
}