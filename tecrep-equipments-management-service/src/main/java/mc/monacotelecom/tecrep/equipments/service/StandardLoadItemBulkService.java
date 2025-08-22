package mc.monacotelecom.tecrep.equipments.service;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.*;
import mc.monacotelecom.tecrep.equipments.process.repository.StandardLoadItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StandardLoadItemBulkService {

    private final Validator validator;
    private final StandardLoadItemRepository repository;

    @Transactional
    public BulkStandardLoadItemsResponse upsertItems(BulkStandardLoadItemsRequest request) {
        List<StandardLoadItemDTO> items = Optional.ofNullable(request.getItems()).orElse(Collections.emptyList());
        List<BulkItemResult> results = new ArrayList<>(Collections.nCopies(items.size(), null));
        int skipped = 0;
        int validationErrors = 0;

        Map<String, ItemWrapper> unique = new LinkedHashMap<>();

        for (int i = 0; i < items.size(); i++) {
            StandardLoadItemDTO item = items.get(i);
            Set<ConstraintViolation<StandardLoadItemDTO>> violations = validator.validate(item);
            String target = resolveTarget(item);
            Map<String, Long> keyMap = buildKey(item);
            if (!violations.isEmpty()) {
                String message = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(", "));
                results.set(i, new BulkItemResult(i, target, keyMap, "validation-error", message));
                validationErrors++;
                continue;
            }
            String dedupKey = generateKey(item, target);
            ItemWrapper previous = unique.put(dedupKey, new ItemWrapper(item, i, target, keyMap));
            if (previous != null) {
                results.set(previous.index, new BulkItemResult(previous.index, previous.target, previous.keyMap, "skipped-duplicate-in-payload", null));
                skipped++;
            }
        }

        List<ItemWrapper> models = new ArrayList<>();
        List<ItemWrapper> materials = new ArrayList<>();
        List<ItemWrapper> groups = new ArrayList<>();
        unique.values().forEach(w -> {
            if (w.item.getIdModel() != null) {
                models.add(w);
            } else if (w.item.getIdMaterial() != null) {
                materials.add(w);
            } else {
                groups.add(w);
            }
        });

        int inserted = 0;
        int updated = 0;

        if (!models.isEmpty()) {
            int[] counts = repository.upsertModels(models.stream().map(w -> w.item).collect(Collectors.toList()));
            for (int j = 0; j < models.size(); j++) {
                ItemWrapper w = models.get(j);
                int count = counts[j];
                if (count == 1) {
                    inserted++;
                } else if (count == 2) {
                    updated++;
                }
                results.set(w.index, new BulkItemResult(w.index, w.target, w.keyMap, "upserted", null));
            }
        }

        if (!materials.isEmpty()) {
            int[] counts = repository.upsertMaterials(materials.stream().map(w -> w.item).collect(Collectors.toList()));
            for (int j = 0; j < materials.size(); j++) {
                ItemWrapper w = materials.get(j);
                int count = counts[j];
                if (count == 1) {
                    inserted++;
                } else if (count == 2) {
                    updated++;
                }
                results.set(w.index, new BulkItemResult(w.index, w.target, w.keyMap, "upserted", null));
            }
        }

        if (!groups.isEmpty()) {
            int[] counts = repository.upsertGroups(groups.stream().map(w -> w.item).collect(Collectors.toList()));
            for (int j = 0; j < groups.size(); j++) {
                ItemWrapper w = groups.get(j);
                int count = counts[j];
                if (count == 1) {
                    inserted++;
                } else if (count == 2) {
                    updated++;
                }
                results.set(w.index, new BulkItemResult(w.index, w.target, w.keyMap, "upserted", null));
            }
        }

        BulkResultSummary summary = new BulkResultSummary(inserted, updated, skipped, validationErrors);
        return new BulkStandardLoadItemsResponse(summary, results);
    }

    private String resolveTarget(StandardLoadItemDTO item) {
        if (item.getIdModel() != null) {
            return "equipmentmodel";
        } else if (item.getIdMaterial() != null) {
            return "equipmentmaterial";
        } else if (item.getIdGroup() != null) {
            return "equipmentgroups";
        } else {
            return "unknown";
        }
    }

    private Map<String, Long> buildKey(StandardLoadItemDTO item) {
        Map<String, Long> map = new LinkedHashMap<>();
        map.put("idStandard", item.getIdStandard());
        if (item.getIdModel() != null) {
            map.put("idModel", item.getIdModel());
        } else if (item.getIdMaterial() != null) {
            map.put("idMaterial", item.getIdMaterial());
        } else if (item.getIdGroup() != null) {
            map.put("idGroup", item.getIdGroup());
        }
        return map;
    }

    private String generateKey(StandardLoadItemDTO item, String target) {
        Long specificId = item.getIdModel();
        if (specificId == null) {
            specificId = item.getIdMaterial();
        }
        if (specificId == null) {
            specificId = item.getIdGroup();
        }
        return target + ':' + item.getIdStandard() + ':' + specificId;
    }

    private static class ItemWrapper {
        private final StandardLoadItemDTO item;
        private final int index;
        private final String target;
        private final Map<String, Long> keyMap;

        private ItemWrapper(StandardLoadItemDTO item, int index, String target, Map<String, Long> keyMap) {
            this.item = item;
            this.index = index;
            this.target = target;
            this.keyMap = keyMap;
        }
    }
}
