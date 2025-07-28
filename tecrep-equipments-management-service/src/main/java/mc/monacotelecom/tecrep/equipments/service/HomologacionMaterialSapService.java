package mc.monacotelecom.tecrep.equipments.service;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.entity.HomologacionMaterialSap;
import mc.monacotelecom.tecrep.equipments.repository.HomologacionMaterialSapRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomologacionMaterialSapService {

    private final HomologacionMaterialSapRepository repository;

    @Transactional(readOnly = true)
    public List<HomologacionMaterialSap> getAll() {
        return repository.findAll();
    }
}
