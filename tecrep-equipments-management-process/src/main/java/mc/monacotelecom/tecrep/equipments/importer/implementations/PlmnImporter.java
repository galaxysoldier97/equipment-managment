package mc.monacotelecom.tecrep.equipments.importer.implementations;

import mc.monacotelecom.importer.ImportMapper;
import mc.monacotelecom.inventory.common.importer.process.NamedAbstractImporter;
import mc.monacotelecom.tecrep.equipments.entity.Plmn;
import mc.monacotelecom.tecrep.equipments.importer.tags.Tag;
import mc.monacotelecom.tecrep.equipments.repository.PlmnRepository;
import org.springframework.stereotype.Service;

@Service
public class PlmnImporter extends NamedAbstractImporter<Plmn, ImportMapper.MappedLine<Plmn>> {
    private final PlmnRepository plmnRepository;

    protected PlmnImporter(PlmnRepository plmnRepository) {
        super(Tag.EQUIPMENTSADMIN);
        this.plmnRepository = plmnRepository;
    }

    @Override
    public void onParseLine(ImportMapper.MappedLine<Plmn> parsedLine) {
        parsedLine.getNodes().forEach(plmn -> {
            plmnRepository.findByCode(plmn.getCode()).ifPresent(dbPlmn -> plmn.setPlmnId(dbPlmn.getPlmnId()));
            validate(plmn, parsedLine.getSaveDepth().get());
        });
    }
}
