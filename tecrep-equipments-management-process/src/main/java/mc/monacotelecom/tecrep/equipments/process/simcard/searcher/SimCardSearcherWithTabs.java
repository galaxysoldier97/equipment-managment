package mc.monacotelecom.tecrep.equipments.process.simcard.searcher;

import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.inventory.common.connectors.exceptions.ExternalConnectorException;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.connectors.TabsConnector;
import mc.monacotelecom.tecrep.equipments.connectors.dto.TABSSimResponse;
import mc.monacotelecom.tecrep.equipments.dto.SimCardDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchSimCardDTO;
import mc.monacotelecom.tecrep.equipments.dto.v2.SimCardDTOV2;
import mc.monacotelecom.tecrep.equipments.entity.SimCard;
import mc.monacotelecom.tecrep.equipments.enums.AccessType;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentCategory;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import mc.monacotelecom.tecrep.equipments.mapper.SimCardMapper;
import mc.monacotelecom.tecrep.equipments.repository.PlmnRepository;
import mc.monacotelecom.tecrep.equipments.repository.ProviderRepository;
import mc.monacotelecom.tecrep.equipments.repository.SimCardRepository;
import mc.monacotelecom.tecrep.equipments.repository.WarehouseRepository;
import mc.monacotelecom.tecrep.equipments.state.StateMachineService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.*;

@Slf4j
@Component
@Profile("epic")
public class SimCardSearcherWithTabs extends AbstractSimCardSearcher {
    private final LocalizedMessageBuilder localizedMessageBuilder;
    private final TabsConnector tabsConnector;
    private final WarehouseRepository warehouseRepository;
    private final ProviderRepository providerRepository;
    private final PlmnRepository plmnRepository;

    private static final String EPIC = "EPIC";

    public SimCardSearcherWithTabs(final SimCardRepository simCardRepository,
                                   final SimCardMapper simCardMapper,
                                   final LocalizedMessageBuilder localizedMessageBuilder,
                                   final TabsConnector tabsConnector,
                                   final WarehouseRepository warehouseRepository,
                                   final ProviderRepository providerRepository,
                                   final PlmnRepository plmnRepository,
                                   final StateMachineService stateMachineService) {
        super(stateMachineService, simCardRepository, simCardMapper);
        this.localizedMessageBuilder = localizedMessageBuilder;
        this.tabsConnector = tabsConnector;
        this.warehouseRepository = warehouseRepository;
        this.providerRepository = providerRepository;
        this.plmnRepository = plmnRepository;
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    @Override
    public PagedModel<SimCardDTO> searchV1(final SearchSimCardDTO searchSimCardDTO, final Pageable pageable, final PagedResourcesAssembler<SimCard> assembler) {
        return mapToDTOsV1(assembler, commonSearch(searchSimCardDTO, pageable));
    }

    @Override
    public Page<SimCardDTOV2> search(SearchSimCardDTO searchSimCardDTO, Pageable pageable) {
        return mapToDTOsV2(commonSearch(searchSimCardDTO, pageable));
    }

    private Page<SimCard> commonSearch(SearchSimCardDTO searchSimCardDTO, Pageable pageable) {
        final Specification<SimCard> specification = prepareSpecifications(searchSimCardDTO);

        Page<SimCard> simCards = simCardRepository.findAll(specification, pageable);

        if (simCards.isEmpty() && StringUtils.isNotBlank(searchSimCardDTO.getSn())) {
            log.info("Start searching on Tabs...");
            TABSSimResponse response;
            try {
                response = tabsConnector.checkSimCard(searchSimCardDTO.getSn());
                var simCard = new SimCard();
                simCard.setSerialNumber(searchSimCardDTO.getSn());
                simCard.setImsiNumber(response.getImsi());
                simCard.setPin1Code(response.getPin1());
                simCard.setPin2Code(response.getPin2());
                simCard.setPuk1Code(response.getPuk1());
                simCard.setPuk2Code(response.getPuk2());
                simCard.setAuthKey(response.getAuthKey());
                simCard.setRecyclable(false);
                simCard.setStatus(Status.AVAILABLE);
                simCard.setBrand(EPIC);
                if (Objects.nonNull(searchSimCardDTO.getAccessType())) {
                    simCard.setAccessType(searchSimCardDTO.getAccessType());
                } else {
                    simCard.setAccessType(AccessType.FTTH);
                }
                simCard.setCategory(EquipmentCategory.SIMCARD);
                var plmn = plmnRepository.findAll().stream().findFirst().orElseThrow(
                        () -> new ExternalConnectorException(localizedMessageBuilder, TABS_NO_PLMN));
                var provider = providerRepository.findAll().stream().findFirst().orElseThrow(
                        () -> new ExternalConnectorException(localizedMessageBuilder, TABS_NO_PROVIDER));
                var warehouse = warehouseRepository.findAll().stream().findFirst().orElseThrow(
                        () -> new ExternalConnectorException(localizedMessageBuilder, TABS_NO_WAREHOUSE));
                simCard.setPlmn(plmn);
                simCard.setProvider(provider);
                simCard.setWarehouse(warehouse);

                simCard = simCardRepository.save(simCard);
                List<SimCard> simCardList = Collections.singletonList(simCard);
                simCards = new PageImpl<>(simCardList, pageable, simCardList.size());
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        } else {
            log.debug("simCards isEmpty {}", simCards.isEmpty());
            log.debug("serialNumber isNotBlank {}", StringUtils.isNotBlank(searchSimCardDTO.getSn()));
            log.debug("Tabs Connector is configured {}", Objects.nonNull(tabsConnector));
        }
        return simCards;
    }
}


