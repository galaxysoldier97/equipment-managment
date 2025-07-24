SET
    FOREIGN_KEY_CHECKS = 0;

ALTER TABLE allotment_summary
    ALTER COLUMN allotment_id RESTART WITH 1;
ALTER TABLE equipment
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE equipment_model
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE file_configuration
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE import_error
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE import_history
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE inventory_pool
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE job_configuration
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE provider
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE plmn
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE warehouse
    ALTER COLUMN id RESTART WITH 1;
ALTER TABLE esim_notification
    ALTER COLUMN id RESTART WITH 1;

delete
from SEQUENCE_ICCID;
delete
from SEQUENCE_MSIN;
delete
from SEQUENCE_BATCHNUMBER;
delete
from allotment_summary;
delete
from allotment_summary_aud;
delete
from ancillary_equipment;
delete
from ancillary_equipment_aud;
delete
from auditenversinfo;
delete
from cpe;
delete
from cpe_aud;
delete
from equipment;
delete
from equipment_aud;
delete
from equipment_model;
delete
from file_configuration;
delete
from import_error;
delete
from import_history;
delete
from inventory_pool;
delete
from job_configuration;
delete
from plmn;
delete
from plmn_aud;
delete
from batch;
delete
from batch_aud;
delete
from provider;
delete
from provider_aud;
delete
from simcard;
delete
from simcard_aud;
delete
from simcard_generation;
delete
from warehouse;
delete
from esim_notification;

SET
    FOREIGN_KEY_CHECKS = 1;