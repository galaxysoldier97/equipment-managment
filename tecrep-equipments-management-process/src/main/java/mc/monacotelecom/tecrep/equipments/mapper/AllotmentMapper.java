package mc.monacotelecom.tecrep.equipments.mapper;

import mc.monacotelecom.tecrep.equipments.dto.AllotmentDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.AllotmentRequestDTO;
import mc.monacotelecom.tecrep.equipments.entity.AllotmentSummary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface AllotmentMapper {
    AllotmentSummary toEntity(AllotmentRequestDTO dto);

    AllotmentSummary toEntity(AllotmentDTO dto);

    @Mapping(target = "isSentToLogistics", source = "sentToLogisticsDate", qualifiedByName = "isSentToLogistics")
    @Mapping(target = "isProvisioned", source = "provisionedDate", qualifiedByName = "isProvisioned")
    @Mapping(target = "batchNumber", source = "batch.batchNumber")
    AllotmentDTO toDto(AllotmentSummary entity);

    @Named("isSentToLogistics")
    default Boolean isSentToLogistics(LocalDateTime sentToLogisticsDate) {
        return sentToLogisticsDate != null;
    }

    @Named("isProvisioned")
    default Boolean isProvisioned(LocalDateTime provisionedDate) {
        return provisionedDate != null;
    }
}
