package mc.monacotelecom.tecrep.equipments.utils;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import mc.monacotelecom.tecrep.equipments.repository.EquipmentRepository;
import mc.monacotelecom.tecrep.equipments.repository.ICleanAudit;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;


@Component
@RequiredArgsConstructor
public class AuditCleaner {

    private final EquipmentRepository<?> equipmentRepository;

    /**
     * clean all audit data from a given repository, keepin last two entries
     *
     * @param id         :entity target id
     * @param repository : entity target repository
     */
    public void clean(@NotNull final long id,
                      @NonNull final ICleanAudit repository) {

        repository.cleanAudit(id);
        this.equipmentRepository.cleanAudit(id);
    }

    /**
     * Performs the cleaning of data only if the given condition is met
     *
     * @param id         : entity target id
     * @param repository : entity target repository
     * @param oldStatus
     * @param newStatus
     */
    public void cleanWithCondition(@NonNull final long id,
                                   @NonNull final ICleanAudit repository,
                                   final Status oldStatus,
                                   final Status newStatus) {

        if ((Status.REPACKAGING.equals(oldStatus) && Status.AVAILABLE.equals(newStatus))) {
            this.clean(id, repository);
        }
    }
}
