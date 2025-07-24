package mc.monacotelecom.tecrep.equipments.service;

import mc.monacotelecom.importer.ImportParameters;
import mc.monacotelecom.inventory.common.importer.dto.ImportHistoryDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.ChangeStatusDto;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v2.AddSimCardDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchSimCardDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.update.v2.UpdateSimCardDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.v2.RevisionDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.v2.SimCardDTOV2;
import mc.monacotelecom.tecrep.equipments.enums.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayInputStream;
import java.util.Collection;

public interface ISimCardService {
    Collection<Event> getAvailableEvents(Long id);

    void delete(Long simCardId);

    ByteArrayInputStream export(final SearchSimCardDTO searchSimCardDTO);

    ImportHistoryDTO importFile(Long batchNumber, String sessionId, ImportParameters importParameters);

    ImportHistoryDTO importFile(String fileName, String sessionID, ImportParameters importParameters);

    SimCardDTOV2 getById(Long simCardId);

    SimCardDTOV2 getByICCID(String iccid);

    SimCardDTOV2 getByIMSI(String imsi);

    Page<SimCardDTOV2> search(SearchSimCardDTO searchSimCardDTO, Pageable pageable);

    Page<RevisionDTOV2<SimCardDTOV2>> findRevisions(Long equipmentId, Pageable pageable);

    SimCardDTOV2 add(AddSimCardDTOV2 simCardDto);

    SimCardDTOV2 update(Long simCardId, UpdateSimCardDTOV2 dto);

    SimCardDTOV2 partialUpdateById(Long simCardId, UpdateSimCardDTOV2 dto);

    SimCardDTOV2 partialUpdateByICCID(String iccid, UpdateSimCardDTOV2 dto);

    SimCardDTOV2 partialUpdateByIMSI(String imsi, UpdateSimCardDTOV2 dto);

    SimCardDTOV2 updatePackById(Long simCardId, UpdateSimCardDTOV2 dto);

    SimCardDTOV2 updatePackByIMSI(UpdateSimCardDTOV2 dto);

    SimCardDTOV2 changeStateById(Long simCardId, Event event, ChangeStatusDto changeStatusDto);

    SimCardDTOV2 changeStateByICCID(String iccid, Event event, ChangeStatusDto changeStatusDto);

    SimCardDTOV2 changeStateByIMSI(String imsi, Event event, ChangeStatusDto changeStatusDto);
}
