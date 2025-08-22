package mc.monacotelecom.tecrep.equipments.service;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.*;
import mc.monacotelecom.tecrep.equipments.exception.StandardLoadNotFoundException;
import mc.monacotelecom.tecrep.equipments.repository.StandardLoadItemsQueryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StandardLoadQueryService {

    private final StandardLoadItemsQueryRepository repository;

    @Transactional(readOnly = true)
    public StandardLoadItemsResponse getItemsByStandard(Long idStandard) {
        if (!repository.existsStandard(idStandard)) {
            throw new StandardLoadNotFoundException(idStandard);
        }
        List<ModelItem> models = repository.findModels(idStandard);
        List<MaterialItem> materials = repository.findMaterials(idStandard);
        List<GroupItem> groups = repository.findGroups(idStandard);

        Counts counts = new Counts(models.size(), materials.size(), groups.size());

        return new StandardLoadItemsResponse(
                idStandard,
                models != null ? models : Collections.emptyList(),
                materials != null ? materials : Collections.emptyList(),
                groups != null ? groups : Collections.emptyList(),
                counts
        );
    }
}
