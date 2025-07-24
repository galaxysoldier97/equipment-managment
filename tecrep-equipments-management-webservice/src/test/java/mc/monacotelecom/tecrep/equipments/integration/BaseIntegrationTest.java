package mc.monacotelecom.tecrep.equipments.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import mc.monacotelecom.importer.ImporterProcessStatus;
import mc.monacotelecom.inventory.common.importer.domain.repository.ImportHistoryRepository;
import mc.monacotelecom.tecrep.equipments.importer.implementations.CPEDocsisSagemcomImporter;
import mc.monacotelecom.tecrep.equipments.importer.implementations.CPEFTTHSagemcomImporter;
import mc.monacotelecom.tecrep.equipments.importer.implementations.ancillary.*;
import mc.monacotelecom.tecrep.equipments.process.*;
import mc.monacotelecom.tecrep.equipments.process.ancillary.searcher.IAncillarySearcher;
import mc.monacotelecom.tecrep.equipments.process.cpe.CPESearcher;
import mc.monacotelecom.tecrep.equipments.process.simcard.searcher.ISimCardSearcher;
import mc.monacotelecom.tecrep.equipments.repository.*;
import mc.monacotelecom.tecrep.equipments.service.ConfigurationService;
import mc.monacotelecom.tecrep.equipments.utils.IncomingFileResolver;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;

@ActiveProfiles({"test"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public abstract class BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    // Mock Authentication

    @Mock
    protected Authentication authentication;

    @Mock
    protected SecurityContext securityContext;

    // Spy process classes

    @SpyBean
    public IAncillarySearcher ancillarySearcher;

    @SpyBean
    public BatchProcess batchProcess;

    @SpyBean
    public EsimNotificationProcess esimNotificationProcess;

    @SpyBean
    public CPESearcher cpeSearcher;

    @SpyBean
    public EquipmentModelProcess equipmentModelProcess;

    @SpyBean
    public FileConfigurationProcess fileConfigurationProcess;

    @SpyBean
    public InventoryPoolProcess inventoryPoolProcess;

    @SpyBean
    public PlmnProcess plmnProcess;

    @SpyBean
    public ProviderProcess providerProcess;

    @SpyBean
    public SimCardGenerationConfigurationProcess simCardGenerationConfigurationProcess;

    @SpyBean
    public ISimCardSearcher simCardSearcher;

    @SpyBean
    public WarehouseProcess wareHouseProcess;

    // Spy service classes

    @Autowired
    public ConfigurationService configurationService;

    // Spy repositories

    @SpyBean
    public EquipmentRepository<?> equipmentRepository;

    @SpyBean
    public SimCardRepository simCardRepository;

    @SpyBean
    public AncillaryRepository ancillaryRepository;

    @SpyBean
    public CPERepository cpeRepository;

    @SpyBean
    public EquipmentModelRepository equipmentModelRepository;

    @SpyBean
    public WarehouseRepository warehouseRepository;

    @SpyBean
    public ProviderRepository providerRepository;

    @SpyBean
    public PlmnRepository plmnRepository;

    @SpyBean
    public InventoryPoolRepository inventoryPoolRepository;

    @SpyBean
    public BatchRepository batchRepository;

    @SpyBean
    public SimCardGenerationConfigurationRepository simCardGenerationConfigurationRepository;

    @SpyBean
    public SequenceBatchNumberRepository sequenceBatchNumberRepository;

    @SpyBean
    public SequenceMSINRepository sequenceMSINRepository;

    @SpyBean
    public SequenceICCIDRepository sequenceICCIDRepository;

    @SpyBean
    public AllotmentSummaryRepository allotmentRepository;

    @SpyBean
    public ImportHistoryRepository importHistoryRepository;

    @SpyBean
    public JobConfigurationRepository jobConfigurationRepository;

    // Spy importer classes

    @SpyBean
    public AncillaryEquipmentONTHUaweiImporter ancillaryEquipmentDefaultImporter;

    @SpyBean
    public AncillaryEquipmentHDDSagecomImporter ancillaryEquipmentHDDmporter;

    @SpyBean
    public AncillaryEquipmentSTBSkyworthImporter ancillaryEquipmentSTBSegemcomImporter;

    @SpyBean
    public AncillaryEquipmentONTGenexisImporter ancillaryEquipmentONTGenexisImporter;

    @SpyBean
    public CPEFTTHSagemcomImporter cpeDefaultImporter;

    @SpyBean
    public CPEDocsisSagemcomImporter cpeDOCSISImporter;

    // Miscellaneous

    @SpyBean
    public IncomingFileResolver incomingFileResolver;

    @Autowired
    public ObjectMapper objectMapper;

    public final Pageable pageable = PageRequest.of(0, 10);

    @BeforeEach
    public void setup() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(null);
    }

    public boolean allImportsCompleted() {
        return importHistoryRepository.findAll().stream().allMatch(x -> ImporterProcessStatus.ImportStatus.COMPLETED.equals(x.getImportStatus()));
    }
}
