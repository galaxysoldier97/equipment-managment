package mc.monacotelecom.tecrep.equipments.service;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.entity.HomologacionMaterialSap;
import mc.monacotelecom.tecrep.equipments.repository.HomologacionMaterialSapRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class HomologacionMaterialSapService {

    private final HomologacionMaterialSapRepository repository;

    @Transactional(readOnly = true)
    public Page<HomologacionMaterialSap> getAll(Pageable pageable) {
        return repository.findAll(pageable);
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