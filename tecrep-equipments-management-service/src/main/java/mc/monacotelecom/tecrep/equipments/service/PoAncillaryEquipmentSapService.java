package mc.monacotelecom.tecrep.equipments.service;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.entity.PoAncillaryEquipmentSap;
import mc.monacotelecom.tecrep.equipments.repository.PoAncillaryEquipmentSapRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mc.monacotelecom.tecrep.equipments.dto.v2.PoAncillaryEquipmentSapDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.HomologacionMaterialSap;
import mc.monacotelecom.tecrep.equipments.repository.HomologacionMaterialSapRepository;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PoAncillaryEquipmentSapService {

    private final PoAncillaryEquipmentSapRepository repository;
    private final HomologacionMaterialSapRepository homologacionRepository;

    @Transactional(readOnly = true)
    public List<PoAncillaryEquipmentSapDTOV2> getAll() {
        return repository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private PoAncillaryEquipmentSapDTOV2 mapToDto(PoAncillaryEquipmentSap entity) {
        PoAncillaryEquipmentSapDTOV2 dto = new PoAncillaryEquipmentSapDTOV2();
        dto.setId(entity.getId());
        dto.setPoNo(entity.getPoNo());
        dto.setModel(entity.getModel());
        dto.setStatus(entity.getStatus());
        if (entity.getModel() != null) {
            homologacionRepository.findByIdMaterialSap(entity.getModel())
                    .map(HomologacionMaterialSap::getEquipmentModelId)
                    .ifPresent(dto::setModelId);
        }
        return dto;
    }

}