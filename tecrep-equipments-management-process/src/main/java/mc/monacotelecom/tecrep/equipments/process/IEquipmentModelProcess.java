package mc.monacotelecom.tecrep.equipments.process;

import mc.monacotelecom.tecrep.equipments.dto.EquipmentModelDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.EquipmentModelCreateDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchEquipmentModelDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.EquipmentModelDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.EquipmentModel;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentModelCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IEquipmentModelProcess {

    /**
     * Get EquipmentModel entity by its name and category
     *
     * @param name     : target entity name
     * @param category : target entity category
     * @return : {@link EquipmentModel}
     */
    EquipmentModel getByNameAndCategory(final String name, final EquipmentModelCategory category);

    /**
     * Get EquipmentModel entity by its id
     *
     * @param id : target entity id
     * @return : {@link EquipmentModelDTO}
     */
    @Deprecated(since = "2.21.0", forRemoval = true)
    EquipmentModelDTO getByIdV1(final long id);

    EquipmentModelDTOV2 getById(final long id);

    /**
     * Creating new instance of EquipmentModel
     *
     * @param equipmentModelDTO : input parameters
     * @return : {@link EquipmentModel}
     */
    EquipmentModel create(EquipmentModelCreateDTO equipmentModelDTO);

    /**
     * Update instance of EquipmentModel
     *
     * @param id                      : targer id
     * @param equipmentModelCreateDTO : input parameters
     * @return : {@link EquipmentModelDTO}
     */
    @Deprecated(since = "2.21.0", forRemoval = true)
    EquipmentModelDTO updateV1(final long id, EquipmentModelCreateDTO equipmentModelCreateDTO);

    EquipmentModelDTOV2 update(final long id, EquipmentModelCreateDTO equipmentModelCreateDTO);

    /**
     * Delete instance of EquipmentModel
     *
     * @param id : target id
     */
    void delete(final long id);

    /**
     * Map an EquipmentModel entity to EquipmentModelDTO
     *
     * @param equipmentModel Entity
     * @return DTO
     */
    @Deprecated(since = "2.21.0", forRemoval = true)
    EquipmentModelDTO mapV1(final EquipmentModel equipmentModel);

    EquipmentModelDTOV2 map(final EquipmentModel equipmentModel);

    /**
     * Get Equipment Models
     *
     * @param pageable Pagination options
     * @return Page of Equipment Models
     */
    @Deprecated(since = "2.21.0", forRemoval = true)
    Page<EquipmentModelDTO> getAllV1(Pageable pageable);

    Page<EquipmentModelDTOV2> search(SearchEquipmentModelDTO dto, Pageable pageable);
}
