package mc.monacotelecom.tecrep.equipments.service;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.v2.StandardLoadDTOV2;
import mc.monacotelecom.tecrep.equipments.process.StandardLoadProcess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StandardLoadService {

    private final StandardLoadProcess standardLoadProcess;

    @Transactional(readOnly = true)
    public StandardLoadDTOV2 getById(Long id) {
        return standardLoadProcess.getById(id);
    }

    @Transactional(readOnly = true)
    public Page<StandardLoadDTOV2> getAll(Pageable pageable) {
        return standardLoadProcess.getAll(pageable);
    }

    @Transactional
    public StandardLoadDTOV2 add(StandardLoadDTOV2 dto) {
        return standardLoadProcess.add(dto);
    }

    @Transactional
    public StandardLoadDTOV2 update(Long id, StandardLoadDTOV2 dto) {
        return standardLoadProcess.update(id, dto);
    }
}

