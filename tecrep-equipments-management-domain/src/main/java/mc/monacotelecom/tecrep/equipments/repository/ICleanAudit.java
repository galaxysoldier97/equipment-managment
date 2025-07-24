package mc.monacotelecom.tecrep.equipments.repository;

public interface ICleanAudit {

    void cleanAudit(final long id);

    void cleanAuditIdIn(Iterable<? extends Long> ids);
}
