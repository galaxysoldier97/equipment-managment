package mc.monacotelecom.tecrep.equipments.process;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.ESimNotificationDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchEsimNotificationDTO;
import mc.monacotelecom.tecrep.equipments.entity.EsimNotification;
import mc.monacotelecom.tecrep.equipments.mapper.EsimNotificationMapper;
import mc.monacotelecom.tecrep.equipments.repository.EsimNotificationRepository;
import mc.monacotelecom.tecrep.equipments.repository.specification.EsimNotificationSpecification;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class EsimNotificationProcess {

    private final EsimNotificationRepository esimNotificationRepository;
    private final EsimNotificationMapper esimNotificationMapper;

    public Page<ESimNotificationDTO> search(SearchEsimNotificationDTO searchEsimNotificationDTO, Pageable pageable) {
        return esimNotificationRepository
                .findAll(this.prepareSpecification(searchEsimNotificationDTO), pageable)
                .map(esimNotificationMapper::toDTO);
    }

    private Specification<EsimNotification> prepareSpecification(SearchEsimNotificationDTO searchEsimNotificationDTO) {
        Specification<EsimNotification> specification;

        specification = Objects.nonNull(searchEsimNotificationDTO.getEquipmentId()) ? Specification.where(EsimNotificationSpecification.hasEquipmentId(searchEsimNotificationDTO.getEquipmentId())) : null;
        specification = StringUtils.isNotBlank(searchEsimNotificationDTO.getIccid()) ? Specification.where(EsimNotificationSpecification.hasIccid(searchEsimNotificationDTO.getIccid())) : specification;
        specification = StringUtils.isNotBlank(searchEsimNotificationDTO.getProfileType()) ? Specification.where(EsimNotificationSpecification.hasProfileType(searchEsimNotificationDTO.getProfileType())) : specification;

        return specification;

    }
}
