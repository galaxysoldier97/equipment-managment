package mc.monacotelecom.tecrep.equipments.mapper;


import mc.monacotelecom.tecrep.equipments.dto.SequenceDTO;
import mc.monacotelecom.tecrep.equipments.entity.SequenceBatchNumber;
import mc.monacotelecom.tecrep.equipments.entity.SequenceICCID;
import mc.monacotelecom.tecrep.equipments.entity.SequenceMSIN;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface SequenceMapper {


    SequenceDTO toDto(SequenceMSIN entity);

    SequenceDTO toDto(SequenceICCID entity);

    SequenceDTO toDto(SequenceBatchNumber entity);


    SequenceICCID toEntityICCID(SequenceDTO sequenceDTO);

    SequenceMSIN toEntityMSIN(SequenceDTO sequenceDTO);

    SequenceBatchNumber toEntityBatchNumber(SequenceDTO sequenceDTO);


}
