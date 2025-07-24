package mc.monacotelecom.tecrep.equipments.service;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.FileConfigurationDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchFileConfigurationDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.update.UpdateFileConfigurationDTO;
import mc.monacotelecom.tecrep.equipments.process.FileConfigurationProcess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FileConfigurationService {
    private final FileConfigurationProcess fileConfigurationProcess;

    @Transactional(readOnly = true)
    public Page<FileConfigurationDTO> search(final SearchFileConfigurationDTO searchFileConfigurationDTO, final Pageable pageable) {
        return fileConfigurationProcess.search(searchFileConfigurationDTO, pageable);
    }

    @Transactional(readOnly = true)
    public FileConfigurationDTO getByName(final String name) {
        return fileConfigurationProcess.getByName(name);
    }

    @Transactional
    public void deleteByName(final String name) {
        fileConfigurationProcess.deleteByName(name);
    }

    @Transactional
    public FileConfigurationDTO create(final FileConfigurationDTO fileConfigurationDTO) {
        return fileConfigurationProcess.create(fileConfigurationDTO);
    }

    @Transactional
    public FileConfigurationDTO update(final String name, final UpdateFileConfigurationDTO fileConfigurationDto) {
        return fileConfigurationProcess.update(name, fileConfigurationDto);
    }
}
