SET
    FOREIGN_KEY_CHECKS = 0;

INSERT INTO plmn (id, plmn_code, network_name, tadig_code, country_iso_code, country_name, ranges_prefix)
VALUES (1, '27203', 'Meteor Mobile Telecommunications Limited', 'IRLME', 'IRL', 'Ireland', '3368900890401');

INSERT INTO inventory_pool (id, code, description, mvno, sim_profile)
VALUES (1, 'METEOR_PAIRED_CUSTOMER_POOL', 'Meteor Paired Customer Pool', 0, 'DEFAULT'),
       (2, 'ATTACHED_TO_SIM', 'Meteor Paired Customer Pool 2', 1, 'DEFAULT'),
       (3, 'ATTACHED_TO_BATCH', 'Meteor Paired Customer Pool 3', 2, 'REPLACEMENT'),
       (4, 'ATTACHED_TO_ALLOTMENT', 'Meteor Paired Customer Pool 4', 3, 'DEFAULT');

INSERT INTO provider(id, name, access_type)
VALUES (1, 'Toto', 'DOCSIS');

INSERT INTO warehouse(id, name, reseller_code)
VALUES (1, 'TOTO', 'TOTO');

INSERT INTO equipment(id, serial_number, external_number, access_type, nature, recyclable, batch_number, service_id,
                      warehouse_id, preactivated, status, order_id, category)
VALUES (1, '893771033000000007', null, 'DOCSIS', 'MAIN', false, 1, null, 1, false, 'BOOKED', 1, 'SIMCARD');

INSERT INTO simcard(id, imsi_number, imsi_sponsor_number, puk1_code, pin1_code, puk2_code, pin2_code,
                    authentification_key, access_control_class, sim_profile, ota_ciphering_key, ota_signature_key,
                    put_description_key, admin_code, number, plmn_id, inventorypool_id, pack_id, transportKey,
                    algorithmVersion, checkDigit, allotment_id, paired_equipment_id, is_esim, ota_salt, provider_id)
VALUES (1, '123456789012345', null, null, null, null, null, null, null, null, null, null, null, null, null, 1, 2, null,
        0, 0, 0, null, null, false, null, 1);

INSERT INTO file_configuration (id, name, prefix, suffix, header_format, record_format)
VALUES (1, 'default_import', 'MMC', '.out', null, null),
       (2, 'default_export', 'MMC', '.inp', '<div>${CONTENT}</div>', '<div>${CONTENT}</div>');

INSERT INTO simcard_generation (id, name, transportKey, algorithmVersion, plmn_id, provider_id, export_file_config,
                                import_file_config, msin_sequence)
VALUES (1, 'EIRCOM_FCS_MOB_LANDLINE_UNPAIR', 6, 1, 1, 1, 2, 1, 'DEFAULT');

INSERT INTO batch(batch_number, export_file_name, import_file_name, inventory_pool_id,
                  simcard_generation_configuration_id, creation_date, returned_date, return_processed_date)
VALUES (6, 'TEST.out', 'TEST.out', 2, 1, '2021-01-18 08:45:58', '2021-01-18 08:45:58', null),
       (61, 'TEST2.out', 'TEST2.in', 3, 1, '2021-01-18 08:45:58', null, null);

INSERT INTO allotment_summary(allotment_id, allotment_number, batch_number, allotment_type, inventorypool_id, quantity,
                              pack_with_handset,
                              price_plan, initial_credit, pre_provisioning_required, sent_to_logistics_date,
                              provisioned_date, creation_date, pre_provisioning_failures)
VALUES (6, 1, 6, 'PREPAID', 4, 1, 0, 'SOMETHING', 15, 0, null, null, '2021-01-18 08:45:58', 0);

SET
    FOREIGN_KEY_CHECKS = 1;