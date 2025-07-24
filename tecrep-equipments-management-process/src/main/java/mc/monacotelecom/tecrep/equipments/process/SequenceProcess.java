package mc.monacotelecom.tecrep.equipments.process;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.dto.SequenceDTO;
import mc.monacotelecom.tecrep.equipments.dto.SequenceResponseDTO;
import mc.monacotelecom.tecrep.equipments.entity.SequenceBatchNumber;
import mc.monacotelecom.tecrep.equipments.entity.SequenceICCID;
import mc.monacotelecom.tecrep.equipments.entity.SequenceMSIN;
import mc.monacotelecom.tecrep.equipments.enums.SequenceType;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmNotFoundException;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.mapper.SequenceMapper;
import mc.monacotelecom.tecrep.equipments.repository.SequenceBatchNumberRepository;
import mc.monacotelecom.tecrep.equipments.repository.SequenceICCIDRepository;
import mc.monacotelecom.tecrep.equipments.repository.SequenceMSINRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class SequenceProcess {

    private final SequenceICCIDRepository sequenceICCIDRepository;

    private final SequenceMSINRepository sequenceMSINRepository;

    private final SequenceBatchNumberRepository sequenceBatchNumberRepository;

    private final LocalizedMessageBuilder localizedMessageBuilder;

    private final SequenceMapper sequenceMapper;

    public List<SequenceResponseDTO> getSequencesMSIN() {
        return sequenceMSINRepository.findDistinctSequenceMSINNames()
                .stream()
                .map(name -> {
                    SequenceResponseDTO sequenceResponseDTO = new SequenceResponseDTO();
                    sequenceResponseDTO.setName(name);
                    List<SequenceMSIN> sequenceMSINS = sequenceMSINRepository.findByNameOrderByValueAsc(name);
                    if (!sequenceMSINS.isEmpty()) {
                        sequenceResponseDTO.setFirst(sequenceMSINS.get(0).getValue());
                        sequenceResponseDTO.setLast(sequenceMSINS.get(sequenceMSINS.size() - 1).getValue());
                    }
                    return sequenceResponseDTO;
                })
                .collect(Collectors.toList());
    }

    public List<SequenceResponseDTO> getSequencesICCID() {
        return sequenceICCIDRepository.findDistinctSequenceICCIDNames()
                .stream()
                .map(name -> {
                    SequenceResponseDTO sequenceResponseDTO = new SequenceResponseDTO();
                    sequenceResponseDTO.setName(name);
                    List<SequenceICCID> sequenceICCIDS = sequenceICCIDRepository.findByNameOrderByValueAsc(name);
                    if (!sequenceICCIDS.isEmpty()) {
                        sequenceResponseDTO.setFirst(sequenceICCIDS.get(0).getValue());
                        sequenceResponseDTO.setLast(sequenceICCIDS.get(sequenceICCIDS.size() - 1).getValue());
                    }
                    return sequenceResponseDTO;
                })
                .collect(Collectors.toList());
    }

    public List<SequenceResponseDTO> getSequencesBATCH() {
        List<SequenceResponseDTO> sequenceResponseDTOS = new ArrayList<>();
        SequenceResponseDTO sequenceResponseDTO = new SequenceResponseDTO();
        List<SequenceBatchNumber> sequenceBatchNumbers = sequenceBatchNumberRepository.findAllByOrderByValueAsc();
        if (!sequenceBatchNumbers.isEmpty()) {
            sequenceResponseDTO.setFirst(sequenceBatchNumberRepository.findAllByOrderByValueAsc().get(0).getValue());
            sequenceResponseDTO.setLast(sequenceBatchNumberRepository.findAllByOrderByValueAsc().get(sequenceBatchNumbers.size() - 1).getValue());
            sequenceResponseDTOS.add(sequenceResponseDTO);
        }
        return sequenceResponseDTOS;
    }

    public SequenceDTO addSequence(SequenceType sequenceType, SequenceDTO sequence) {
        switch (sequenceType) {
            case MSIN:
                if (sequenceMSINRepository.existsByValue(sequence.getValue())) {
                    throw new EqmValidationException(localizedMessageBuilder, MSIN_SEQUENCE_VALUE_ALREADY_EXISTS, sequence.getValue());
                }
                return sequenceMapper.toDto(sequenceMSINRepository.save(sequenceMapper.toEntityMSIN(sequence)));
            case ICCID:
                if (sequenceICCIDRepository.existsByValue(sequence.getValue())) {
                    throw new EqmValidationException(localizedMessageBuilder, ICCID_SEQUENCE_VALUE_ALREADY_EXISTS, sequence.getValue());
                }
                return sequenceMapper.toDto(sequenceICCIDRepository.save(sequenceMapper.toEntityICCID(sequence)));
            case BATCHNUMBER:
                if (sequenceBatchNumberRepository.existsByValue(sequence.getValue())) {
                    throw new EqmValidationException(localizedMessageBuilder, BATCH_NUMBER_SEQUENCE_VALUE_ALREADY_EXISTS, sequence.getValue());
                }
                return sequenceMapper.toDto(sequenceBatchNumberRepository.save(new SequenceBatchNumber(sequence.getValue())));
            default:
                throw new EqmValidationException(localizedMessageBuilder, "SEQUENCE_UNEXPECTED", sequenceType);
        }
    }

    public void deleteSequence(SequenceType sequenceType, SequenceDTO sequenceDTO) {
        switch (sequenceType) {
            case MSIN:
                if (Objects.nonNull(sequenceDTO.getValue())) {
                    sequenceMSINRepository.findById(sequenceDTO.getValue())
                            .ifPresentOrElse(sequenceMSINRepository::delete,
                                    () -> {
                                        throw new EqmNotFoundException(localizedMessageBuilder, SEQUENCE_VALUE_NOT_FOUND, sequenceDTO.getValue());
                                    });
                } else {
                    sequenceMSINRepository.deleteByName(sequenceDTO.getName());
                }
                break;
            case ICCID:
                if (Objects.nonNull(sequenceDTO.getValue())) {
                    sequenceICCIDRepository.findById(sequenceDTO.getValue())
                            .ifPresentOrElse(sequenceICCIDRepository::delete,
                                    () -> {
                                        throw new EqmNotFoundException(localizedMessageBuilder, SEQUENCE_VALUE_NOT_FOUND, sequenceDTO.getValue());
                                    });
                } else {
                    sequenceICCIDRepository.deleteByName(sequenceDTO.getName());
                }
                break;
            case BATCHNUMBER:
                if (Objects.nonNull(sequenceDTO.getValue())) {
                    sequenceBatchNumberRepository.findById(sequenceDTO.getValue())
                            .ifPresentOrElse(sequenceBatchNumberRepository::delete,
                                    () -> {
                                        throw new EqmNotFoundException(localizedMessageBuilder, SEQUENCE_VALUE_NOT_FOUND, sequenceDTO.getValue());
                                    });
                } else {
                    sequenceBatchNumberRepository.deleteAll();
                }
                break;
            default:
                throw new EqmValidationException(localizedMessageBuilder, "SEQUENCE_UNEXPECTED", sequenceType);
        }
    }
}

