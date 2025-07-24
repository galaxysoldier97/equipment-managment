package mc.monacotelecom.tecrep.equipments.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.importer.ImportParameters;
import mc.monacotelecom.inventory.common.event.annotations.SentEvent;
import mc.monacotelecom.inventory.common.importer.dto.ImportHistoryDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.ChangeStatusDto;
import mc.monacotelecom.tecrep.equipments.dto.request.create.v2.AddSimCardDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchSimCardDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.update.v2.UpdateSimCardDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.v2.RevisionDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.v2.SimCardDTOV2;
import mc.monacotelecom.tecrep.equipments.enums.Event;
import mc.monacotelecom.tecrep.equipments.process.simcard.*;
import mc.monacotelecom.tecrep.equipments.process.simcard.searcher.ISimCardSearcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.util.Collection;

@Service
@Slf4j
@RequiredArgsConstructor
public class SimCardService implements ISimCardService {

    private final SimCardRetriever simCardRetriever;
    private final ISimCardSearcher simCardSearcher;
    private final SimCardRevisionRetriever simCardRevisionRetriever;
    private final SimCardCreator simCardCreator;
    private final SimCardImporter simCardImporter;
    private final SimCardUpdater simCardUpdater;
    private final SimCardStatusChanger simCardStatusChanger;
    private final SimCardDeleter simCardDeleter;
    private final SimCardExporter simCardExporter;

    @Override
    @Transactional(readOnly = true)
    public Collection<Event> getAvailableEvents(Long id) {
        return simCardRetriever.getAvailableEvents(id);
    }

    @Override
    @Transactional
    public void delete(Long simCardId) {
        simCardDeleter.delete(simCardId);
    }

    @Override
    @Transactional(readOnly = true)
    public ByteArrayInputStream export(final SearchSimCardDTO searchSimCardDTO) {
        return simCardExporter.export(searchSimCardDTO);
    }

    @Override
    @Transactional
    public ImportHistoryDTO importFile(Long batchNumber, String sessionId, ImportParameters importParameters) {
        return simCardImporter.importPrepayBatch(batchNumber, sessionId, importParameters);
    }

    @Override
    @Transactional
    public ImportHistoryDTO importFile(String fileName, String sessionID, ImportParameters importParameters) {
        return simCardImporter.importPrepayBatch(fileName, sessionID, importParameters);
    }

    @Override
    @Transactional(readOnly = true)
    public SimCardDTOV2 getById(Long id) {
        return simCardRetriever.getById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public SimCardDTOV2 getByICCID(String iccid) {
        return simCardRetriever.getByIccid(iccid);
    }

    @Override
    @Transactional(readOnly = true)
    public SimCardDTOV2 getByIMSI(String imsi) {
        return simCardRetriever.getByIMSI(imsi);
    }

    // Not read-only, for on-the-fly SIM Card creation
    @Transactional
    @Override
    public Page<SimCardDTOV2> search(SearchSimCardDTO dto, Pageable pageable) {
        return simCardSearcher.search(dto, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RevisionDTOV2<SimCardDTOV2>> findRevisions(Long id, Pageable pageable) {
        return simCardRevisionRetriever.findRevisions(id, pageable);
    }

    @Override
    @Transactional
    public SimCardDTOV2 add(AddSimCardDTOV2 dto) {
        return simCardCreator.createAndMap(dto);
    }

    @Override
    @Transactional
    public SimCardDTOV2 update(Long id, UpdateSimCardDTOV2 dto) {
        return simCardUpdater.update(id, dto);
    }

    @Override
    @Transactional
    public SimCardDTOV2 partialUpdateById(Long id, UpdateSimCardDTOV2 dto) {
        return simCardUpdater.partialUpdateById(id, dto);
    }

    @Override
    @Transactional
    public SimCardDTOV2 partialUpdateByICCID(String iccid, UpdateSimCardDTOV2 dto) {
        return simCardUpdater.partialUpdateByIccid(iccid, dto);
    }

    @Override
    @Transactional
    public SimCardDTOV2 partialUpdateByIMSI(String imsi, UpdateSimCardDTOV2 dto) {
        return simCardUpdater.partialUpdateByImsi(imsi, dto);
    }

    @Override
    @Transactional
    public SimCardDTOV2 updatePackById(Long simCardId, UpdateSimCardDTOV2 dto) {
        return simCardUpdater.updatePackById(simCardId, dto);
    }

    @Override
    @Transactional
    public SimCardDTOV2 updatePackByIMSI(UpdateSimCardDTOV2 dto) {
        return simCardUpdater.updatePackByImsi(dto);
    }

    @SentEvent(SimCardDTOV2.class)
    @Override
    @Transactional
    public SimCardDTOV2 changeStateById(Long simCardId, Event event, ChangeStatusDto changeStatusDto) {
        return simCardStatusChanger.changeStateById(simCardId, changeStatusDto, event);
    }

    @SentEvent(SimCardDTOV2.class)
    @Override
    @Transactional
    public SimCardDTOV2 changeStateByICCID(String iccid, Event event, ChangeStatusDto changeStatusDto) {
        return simCardStatusChanger.changeStateByICCID(iccid, changeStatusDto, event);
    }

    @SentEvent(SimCardDTOV2.class)
    @Override
    @Transactional
    public SimCardDTOV2 changeStateByIMSI(String imsi, Event event, ChangeStatusDto changeStatusDto) {
        return simCardStatusChanger.changeStateByIMSI(imsi, changeStatusDto, event);
    }
}
