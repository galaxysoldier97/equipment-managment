package mc.monacotelecom.tecrep.equipments.service;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.entity.PoAncillaryEquipmentSap;
import mc.monacotelecom.tecrep.equipments.repository.PoAncillaryEquipmentSapRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PoAncillaryEquipmentSapService {

    private final PoAncillaryEquipmentSapRepository repository;

    @Transactional(readOnly = true)
    public List<PoAncillaryEquipmentSap> getAll() {
        return repository.findAll();
    }
}
