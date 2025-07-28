package mc.monacotelecom.tecrep.equipments.service;

import lombok.AllArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.EquipmentModelDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.EquipmentModelCreateDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchEquipmentModelDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.EquipmentModelDTOV2;
import mc.monacotelecom.tecrep.equipments.enums.AccessType;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentModelCategory;
import mc.monacotelecom.tecrep.equipments.process.EquipmentModelProcess;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@AllArgsConstructor
public class EquipmentModelService {

    private final EquipmentModelProcess equipmentModelProcess;

    @Transactional(readOnly = true)
    @Deprecated(since = "2.21.0", forRemoval = true)
    public EquipmentModelDTO getByIdV1(final long id) {
        return equipmentModelProcess.getByIdV1(id);
    }

    @Transactional(readOnly = true)
    public EquipmentModelDTOV2 getById(final long id) {
        return equipmentModelProcess.getById(id);
    }

    @Transactional
    @Deprecated(since = "2.21.0", forRemoval = true)
    public EquipmentModelDTO createV1(final EquipmentModelCreateDTO equipmentModelDTO) {
        return equipmentModelProcess.mapV1(equipmentModelProcess.create(equipmentModelDTO));
    }

    @Transactional
    public EquipmentModelDTOV2 create(final EquipmentModelCreateDTO equipmentModelDTO) {
        return equipmentModelProcess.map(equipmentModelProcess.create(equipmentModelDTO));
    }

    @Transactional
    @Deprecated(since = "2.21.0", forRemoval = true)
    public EquipmentModelDTO updateV1(final long id, final EquipmentModelCreateDTO equipmentModelCreateDTO) {
        return equipmentModelProcess.updateV1(id, equipmentModelCreateDTO);
    }

    @Transactional
    public EquipmentModelDTOV2 update(final long id, final EquipmentModelCreateDTO equipmentModelCreateDTO) {
        return equipmentModelProcess.update(id, equipmentModelCreateDTO);
    }

    @Transactional(readOnly = true)
    @Deprecated(since = "2.21.0", forRemoval = true)
    public Page<EquipmentModelDTO> getAllV1(final Pageable pageable) {
        return equipmentModelProcess.getAllV1(pageable);
    }

    @Transactional(readOnly = true)
    public Page<EquipmentModelDTOV2> search(final SearchEquipmentModelDTO dto, final Pageable pageable) {
        return equipmentModelProcess.search(dto, pageable);
    }

    @Transactional(readOnly = true)
    public List<AccessType> getAccessTypesByCategory(final EquipmentModelCategory category) {
        return equipmentModelProcess.getAccessTypesByCategory(category);
    }

    @Transactional(readOnly = true)
    public List<String> getNamesByCategoryAndAccessType(final EquipmentModelCategory category, final AccessType accessType) {
        return equipmentModelProcess.getNamesByCategoryAndAccessType(category, accessType);
    }

    @Transactional
    public void delete(final long id) {
        equipmentModelProcess.delete(id);
    }
}