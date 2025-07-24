package mc.monacotelecom.tecrep.equipments.service;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.SimCardGenerationConfigurationDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v1.AddSimCardGenerationConfigurationDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v2.AddSimCardGenerationConfigurationDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchSimCardGenerationConfigurationDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.update.UpdateSimCardGenerationConfigurationDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.update.v2.UpdateSimCardGenerationConfigurationV2DTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.SimCardGenerationConfigurationDTOV2;
import mc.monacotelecom.tecrep.equipments.process.SimCardGenerationConfigurationProcess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class SimCardGenerationConfigurationService {

    private final SimCardGenerationConfigurationProcess process;

    @Transactional(readOnly = true)
    @Deprecated(since = "2.21.0", forRemoval = true)
    public Page<SimCardGenerationConfigurationDTO> searchV1(SearchSimCardGenerationConfigurationDTO searchSimCardGenerationConfigurationDTO, Pageable pageable) {
        return process.searchV1(searchSimCardGenerationConfigurationDTO, pageable);
    }

    @Transactional(readOnly = true)
    public Page<SimCardGenerationConfigurationDTOV2> search(SearchSimCardGenerationConfigurationDTO searchSimCardGenerationConfigurationDTO, Pageable pageable) {
        return process.search(searchSimCardGenerationConfigurationDTO, pageable);
    }

    @Transactional(readOnly = true)
    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardGenerationConfigurationDTO getByNameV1(final String name) {
        return process.getByNameV1(name);
    }

    @Transactional(readOnly = true)
    public SimCardGenerationConfigurationDTOV2 getByName(final String name) {
        return process.getByName(name);
    }

    @Transactional
    public void deleteByName(final String name) {
        process.deleteByName(name);
    }

    @Transactional
    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardGenerationConfigurationDTO createV1(final AddSimCardGenerationConfigurationDTO simCardGenerationConfigurationDTO) {
        return process.createV1(simCardGenerationConfigurationDTO);
    }

    @Transactional
    public SimCardGenerationConfigurationDTOV2 create(final AddSimCardGenerationConfigurationDTOV2 simCardGenerationConfigurationDTO) {
        return process.create(simCardGenerationConfigurationDTO);
    }

    @Transactional
    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardGenerationConfigurationDTO updateV1(final String name, final UpdateSimCardGenerationConfigurationDTO simCardGenerationConfigurationDTO) {
        return process.updateV1(name, simCardGenerationConfigurationDTO);
    }

    @Transactional
    public SimCardGenerationConfigurationDTOV2 update(final String name, final UpdateSimCardGenerationConfigurationV2DTO simCardGenerationConfigurationDTO) {
        return process.update(name, simCardGenerationConfigurationDTO);
    }
}
