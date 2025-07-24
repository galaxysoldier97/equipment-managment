package mc.monacotelecom.tecrep.equipments.service;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.ESimNotificationDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchEsimNotificationDTO;
import mc.monacotelecom.tecrep.equipments.process.EsimNotificationProcess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EsimNotificationService {

    private final EsimNotificationProcess esimNotificationProcess;

    @Transactional(readOnly = true)
    public Page<ESimNotificationDTO> search(SearchEsimNotificationDTO searchEsimNotificationDTO, Pageable pageable) {
        return esimNotificationProcess.search(searchEsimNotificationDTO, pageable);
    }
}
