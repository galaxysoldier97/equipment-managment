package mc.monacotelecom.tecrep.equipments.process.ancillary.searcher;

import mc.monacotelecom.tecrep.equipments.dto.AncillaryEquipmentDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchAncillaryEquipmentDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.AncillaryEquipmentDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.AncillaryEquipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;

public interface IAncillarySearcher {
    @Deprecated(since = "2.21.0", forRemoval = true)
    PagedModel<AncillaryEquipmentDTO> getAllV1(Pageable pageable, PagedResourcesAssembler<AncillaryEquipment> assembler);

    @Deprecated(since = "2.21.0", forRemoval = true)
    PagedModel<AncillaryEquipmentDTO> searchV1(SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO, Pageable pageable, PagedResourcesAssembler<AncillaryEquipment> assembler);

    Page<AncillaryEquipmentDTOV2> search(SearchAncillaryEquipmentDTO searchAncillaryEquipmentDTO, Pageable pageable);
}
