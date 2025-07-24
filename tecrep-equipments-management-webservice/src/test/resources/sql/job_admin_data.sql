SET
    FOREIGN_KEY_CHECKS = 0;

insert into job_configuration (id, days, enabled, operation, status, recyclable)
values (1, 1, true, 'AUDIT_CLEAN', 'DEACTIVATED', false),
       (2, 1, false, 'AUDIT_CLEAN', 'DEPRECATED', true);

SET
    FOREIGN_KEY_CHECKS = 1;