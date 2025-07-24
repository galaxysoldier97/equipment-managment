package mc.monacotelecom.tecrep.equipments.service;


import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.SequenceDTO;
import mc.monacotelecom.tecrep.equipments.dto.SequenceResponseDTO;
import mc.monacotelecom.tecrep.equipments.enums.SequenceType;
import mc.monacotelecom.tecrep.equipments.process.SequenceProcess;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SequenceService {

    private final SequenceProcess sequenceProcess;

    @Transactional
    public SequenceDTO addSequence(SequenceType sequenceType, SequenceDTO sequence) {
        return sequenceProcess.addSequence(sequenceType, sequence);
    }

    @Transactional(readOnly = true)
    public List<SequenceResponseDTO> getSequences(SequenceType sequenceType) {
        switch (sequenceType) {
            case ICCID:
                return sequenceProcess.getSequencesICCID();
            case MSIN:
                return sequenceProcess.getSequencesMSIN();
            default:
                return sequenceProcess.getSequencesBATCH();
        }
    }

    @Transactional
    public void delete(SequenceType sequenceType, SequenceDTO sequenceDTO) {
        sequenceProcess.deleteSequence(sequenceType, sequenceDTO);
    }
}
