package mc.monacotelecom.tecrep.equipments.importer.implementations;

import mc.monacotelecom.importer.ImportMapper;
import mc.monacotelecom.inventory.common.importer.process.NamedAbstractImporter;
import mc.monacotelecom.tecrep.equipments.entity.Provider;
import mc.monacotelecom.tecrep.equipments.importer.tags.Tag;
import mc.monacotelecom.tecrep.equipments.repository.ProviderRepository;
import org.springframework.stereotype.Service;

@Service
public class ProviderImporter extends NamedAbstractImporter<Provider, ImportMapper.MappedLine<Provider>> {

    private final ProviderRepository providerRepository;

    protected ProviderImporter(ProviderRepository providerRepository) {
        super(Tag.EQUIPMENTSADMIN);
        this.providerRepository = providerRepository;
    }

    @Override
    public void onParseLine(ImportMapper.MappedLine<Provider> providerMappedLine) {
        providerMappedLine.getNodes().forEach(provider -> {
            providerRepository.findByName(provider.getName()).ifPresent(dbProvider -> provider.setProviderId(dbProvider.getProviderId()));
            validate(provider, providerMappedLine.getSaveDepth().get());
        });

    }
}