package mc.monacotelecom.tecrep.equipments.service;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.AllotmentDTO;
import mc.monacotelecom.tecrep.equipments.dto.AllotmentProvisionedDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.AllotmentExportRequestDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.AllotmentRequestDTO;
import mc.monacotelecom.tecrep.equipments.process.AllotmentProcess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;

@Service
@RequiredArgsConstructor
public class AllotmentService {

    private final AllotmentProcess process;

    @Transactional(readOnly = true)
    public Page<AllotmentDTO> getAll(Pageable pageable) {
        return process.getAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<AllotmentDTO> getAllByBatchNumber(Long batchNumber, Pageable pageable) {
        return process.getAllByBatchNumber(batchNumber, pageable);
    }

    @Transactional(readOnly = true)
    public AllotmentDTO getById(Long allotmentId) {
        return process.getById(allotmentId);
    }

    @Transactional
    public AllotmentDTO add(AllotmentRequestDTO allotmentDTO) {
        return process.add(allotmentDTO);
    }

    @Transactional(readOnly = true)
    public ByteArrayInputStream export(AllotmentExportRequestDTO allotmentExportRequestDTO) {
        return process.export(allotmentExportRequestDTO);
    }

    @Transactional(readOnly = true)
    public String getAllotmentFilename(AllotmentExportRequestDTO allotmentExportRequestDTO) {
        return process.getAllotmentFilename(allotmentExportRequestDTO);
    }

    @Transactional(readOnly = true)
    public AllotmentDTO provisioned(AllotmentProvisionedDTO dto) {
        return process.provisioned(dto);
    }
}
