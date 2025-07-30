package mc.monacotelecom.tecrep.equipments.service;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.entity.HomologacionMaterialSap;
import mc.monacotelecom.tecrep.equipments.repository.HomologacionMaterialSapRepository;
import mc.monacotelecom.tecrep.equipments.repository.EquipmentModelRepository;
import mc.monacotelecom.tecrep.equipments.dto.v2.HomologacionMaterialSapDTOV2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class HomologacionMaterialSapService {

    private final HomologacionMaterialSapRepository repository;
    private final EquipmentModelRepository equipmentModelRepository;

    @Transactional(readOnly = true)
    public Page<HomologacionMaterialSapDTOV2> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::mapToDto);
    }

    private HomologacionMaterialSapDTOV2 mapToDto(HomologacionMaterialSap entity) {
        HomologacionMaterialSapDTOV2 dto = new HomologacionMaterialSapDTOV2();
        dto.setId(entity.getId());
        dto.setIdMaterialSap(entity.getIdMaterialSap());
        dto.setNameSap(entity.getNameSap());
        dto.setEquipmentModelId(entity.getEquipmentModelId());
        dto.setStatus(entity.getStatus());
        if (entity.getEquipmentModelId() != null) {
            equipmentModelRepository.findById(entity.getEquipmentModelId())
                    .ifPresent(model -> {
                        dto.setEquipmentModelName(model.getName());
                        dto.setAccessType(model.getAccessType());
                    });
        }
        return dto;
    }

    @Transactional
    public HomologacionMaterialSap add(HomologacionMaterialSap homologacionMaterialSap) {
        return repository.save(homologacionMaterialSap);
    }

    @Transactional
    public HomologacionMaterialSap update(HomologacionMaterialSap homologacionMaterialSap) {
        return repository.save(homologacionMaterialSap);
    }
}