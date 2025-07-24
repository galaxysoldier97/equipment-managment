package mc.monacotelecom.tecrep.equipments.process;

import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.inventory.common.CommonFunctions;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.assembler.WarehouseResourceAssembler;
import mc.monacotelecom.tecrep.equipments.dto.WarehouseDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchWarehouseDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.WarehouseDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.Warehouse;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmNotFoundException;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.mapper.WarehouseMapper;
import mc.monacotelecom.tecrep.equipments.repository.WarehouseRepository;
import mc.monacotelecom.tecrep.equipments.repository.specification.WarehouseSpecifications;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.WAREHOUSE_ID_NOT_FOUND;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.WAREHOUSE_RESELLER_CODE_ALREADY_EXIST;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
@Slf4j
public class WarehouseProcess {

    private final WarehouseRepository warehouseRepository;
    private final WarehouseResourceAssembler warehouseResourceAssembler;
    private final LocalizedMessageBuilder localizedMessageBuilder;
    private final WarehouseMapper warehouseMapper;

    public WarehouseProcess(final WarehouseRepository warehouseRepository,
                            final LocalizedMessageBuilder localizedMessageBuilder,
                            final WarehouseMapper warehouseMapper) {
        this.warehouseRepository = warehouseRepository;
        this.localizedMessageBuilder = localizedMessageBuilder;
        this.warehouseMapper = warehouseMapper;
        warehouseResourceAssembler = WarehouseResourceAssembler.of(WarehouseProcess.class);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public PagedModel<WarehouseDTO> searchV1(final SearchWarehouseDTO searchWarehouseDTO, final Pageable pageable, final PagedResourcesAssembler<Warehouse> assembler) {
        final Specification<Warehouse> specification = this.prepareSpecifications(searchWarehouseDTO);

        Page<Warehouse> warehouses = warehouseRepository.findAll(specification, pageable);

        return assembler.toModel(warehouses, warehouseResourceAssembler, linkTo(WarehouseProcess.class).slash("/warehouses").withSelfRel());
    }

    public Page<WarehouseDTOV2> search(final SearchWarehouseDTO searchWarehouseDTO, final Pageable pageable) {
        final Specification<Warehouse> specification = this.prepareSpecifications(searchWarehouseDTO);
        return warehouseRepository.findAll(specification, pageable).map(warehouseMapper::toDtoV2);
    }

    private Specification<Warehouse> prepareSpecifications(final SearchWarehouseDTO searchWarehouseDTO) {
        Specification<Warehouse> specification = null;

        specification = StringUtils.isNotBlank(searchWarehouseDTO.getName()) ? Specification.where(WarehouseSpecifications.nameStartWith(searchWarehouseDTO.getName())) : specification;
        specification = StringUtils.isNotBlank(searchWarehouseDTO.getResellerCode()) ? CommonFunctions.addSpecification(specification, WarehouseSpecifications.resellerCodeStartWith(searchWarehouseDTO.getResellerCode())) : specification;

        return specification;
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public WarehouseDTO getByIdV1(Long warehouseId) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, WAREHOUSE_ID_NOT_FOUND, warehouseId));
        return warehouseMapper.toDto(warehouse);
    }

    public WarehouseDTOV2 getById(Long warehouseId) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, WAREHOUSE_ID_NOT_FOUND, warehouseId));
        return warehouseMapper.toDtoV2(warehouse);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public WarehouseDTO addV1(WarehouseDTO warehouseDTO) {
        Warehouse warehouse = warehouseMapper.toEntity(warehouseDTO);
        Optional<Warehouse> optionalWarehouse = warehouseRepository.findByResellerCode(warehouseDTO.getResellerCode());
        if (optionalWarehouse.isPresent()) {
            throw new EqmValidationException(localizedMessageBuilder, WAREHOUSE_RESELLER_CODE_ALREADY_EXIST, warehouseDTO.getResellerCode());
        }
        return warehouseResourceAssembler.toModel(warehouseRepository.save(warehouse));
    }

    public WarehouseDTOV2 add(WarehouseDTOV2 warehouseDTO) {
        Warehouse warehouse = warehouseMapper.toEntity(warehouseDTO);
        Optional<Warehouse> optionalWarehouse = warehouseRepository.findByResellerCode(warehouseDTO.getResellerCode());
        if (optionalWarehouse.isPresent()) {
            throw new EqmValidationException(localizedMessageBuilder, WAREHOUSE_RESELLER_CODE_ALREADY_EXIST, warehouseDTO.getResellerCode());
        }
        return warehouseMapper.toDtoV2(warehouseRepository.save(warehouse));
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public WarehouseDTO updateV1(Long warehouseId, WarehouseDTO warehouseDTO) {
        Warehouse warehouse = warehouseMapper.toEntity(warehouseDTO);
        warehouse.setWarehouseId(warehouseId);
        return warehouseResourceAssembler.toModel(warehouseRepository.save(warehouse));
    }

    public WarehouseDTOV2 update(Long warehouseId, WarehouseDTOV2 warehouseDTO) {
        Warehouse warehouse = warehouseMapper.toEntity(warehouseDTO);
        warehouse.setWarehouseId(warehouseId);
        return warehouseMapper.toDtoV2(warehouseRepository.save(warehouse));
    }

    public void delete(Long warehouseId) {
        var warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new EqmNotFoundException(localizedMessageBuilder, WAREHOUSE_ID_NOT_FOUND, warehouseId));

        warehouseRepository.delete(warehouse);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public PagedModel<WarehouseDTO> getAll(Pageable pageable, PagedResourcesAssembler<Warehouse> assembler) {
        Page<Warehouse> warehouses = warehouseRepository.findAll(pageable);
        return assembler.toModel(warehouses, warehouseResourceAssembler, linkTo(WarehouseProcess.class).slash("/warehouses").withSelfRel());
    }
}
