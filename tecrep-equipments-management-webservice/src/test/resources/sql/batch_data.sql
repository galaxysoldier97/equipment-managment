SET
    FOREIGN_KEY_CHECKS = 0;

INSERT INTO file_configuration (id, name, prefix, suffix, header_format, record_format)
VALUES (1, 'default_import', 'MMC', '.out', null, null),
       (2, 'default_export', 'MMC', '.inp', '<div>${CONTENT}</div>', '<div>${CONTENT}</div>');

INSERT INTO simcard_generation (id, name, transportKey, algorithmVersion, plmn_id, provider_id, export_file_config,
                                import_file_config, msin_sequence, iccid_sequence, fixed_prefix, sequence_prefix)
VALUES (3, 'EIRCOM_FCS_MOB_LANDLINE_UNPAIR', '6', 1, 4, 8, 2, 1, 'DEFAULT', 'DEFAULT', '9935304', '1525'),
       (4, 'TEST_SCGC', '06', 1, 4, 8, 2, 1, 'DEFAULT', 'DEFAULT', '8935303', '0524'),
       (5, 'EIR_NO_SEQUENCE_PREFIX', '6', 1, 4, 8, 2, 1, 'DEFAULT', 'DEFAULT', '8935303', null),
       (6, 'EIR_NO_FIXED_PREFIX', '6', 1, 4, 8, 2, 1, 'DEFAULT', 'DEFAULT', null, '0524');

INSERT INTO plmn (id, plmn_code, network_name, tadig_code, country_iso_code, country_name, ranges_prefix)
VALUES (4, '27203', 'Meteor Mobile Telecommunications Limited', 'IRLME', 'IRL', 'Ireland', '3368900890401');

INSERT INTO inventory_pool(id, code, mvno, sim_profile, description)
VALUES (5, 'METEOR_PAIRED_CUSTOMER_POOL', 0, 'DEFAULT', null),
       (51, 'TEST_REPLACEMENT', 0, 'REPLACEMENT', null);

INSERT INTO batch(batch_number, export_file_name, import_file_name, inventory_pool_id,
                  simcard_generation_configuration_id, creation_date, returned_date, return_processed_date)
VALUES (6, 'TEST.out', 'TEST.out', 5, 4, '2021-01-18 08:45:58', '2021-01-18 08:45:58', null),
       (61, 'TEST2.out', 'TEST2.in', 5, 3, '2021-01-18 08:45:58', null, null),
       (62, 'TEST3.out', 'TEST3.in', 51, 3, '2021-01-18 08:45:58', '2021-01-18 08:45:58', '2021-01-18 08:45:58'),
       (63, 'TEST4.out', 'TEST4.out', 5, 5, '2021-01-18 08:45:58', '2021-01-18 08:45:58', null),
       (64, 'TEST5.out', 'TEST5.out', 5, 6, '2021-01-18 08:45:58', '2021-01-18 08:45:58', null);

INSERT INTO SEQUENCE_BATCHNUMBER(seq_value)
VALUES (6),
       (61),
       (62);

INSERT INTO allotment_summary(allotment_id, allotment_number, batch_number, allotment_type, inventorypool_id, quantity,
                              pack_with_handset,
                              price_plan, initial_credit, pre_provisioning_required, sent_to_logistics_date,
                              provisioned_date, creation_date)
VALUES (7, 1, 6, 'PREPAID', 5, 10, true, 'pricePlan', 10, true, null, null, '2021-01-18 08:45:58');

INSERT INTO provider (id, name, access_type)
VALUES (8, 'Technicolor', 'DOCSIS');

INSERT INTO warehouse (id, name, reseller_code)
VALUES (9, 'MyWarehouse', null);

INSERT INTO equipment (id, serial_number, external_number, access_type, nature, recyclable, batch_number, service_id,
                       warehouse_id, preactivated, status, order_id, category)
VALUES (10, '1111111111111111111', null, 'MOBILE', 'MAIN', false, '61', null, 9, null, 'INSTORE', null, 'SIMCARD'),
       (11, '1111111111111111112', null, 'MOBILE', 'MAIN', false, '61', null, 9, null, 'INSTORE', null, 'SIMCARD'),
       (12, '1111111111111111113', null, 'MOBILE', 'MAIN', false, '61', null, 9, null, 'INSTORE', null, 'SIMCARD'),
       (13, '1111111111111111114', null, 'MOBILE', 'MAIN', false, '62', null, 9, null, 'INSTORE', null, 'SIMCARD'),
       (14, '1111111111111111115', null, 'MOBILE', 'MAIN', false, '62', null, 9, null, 'INSTORE', null, 'SIMCARD'),
       (15, '1111111111111111116', null, 'MOBILE', 'MAIN', false, '64', null, 9, null, 'INSTORE', null, 'SIMCARD');

INSERT INTO simcard (id, imsi_number, imsi_sponsor_number, puk1_code, pin1_code, puk2_code, pin2_code,
                     authentification_key, access_control_class, sim_profile, ota_ciphering_key, ota_signature_key,
                     put_description_key, admin_code, number, plmn_id, inventorypool_id, pack_id, transportKey,
                     algorithmVersion, checkDigit, allotment_id, is_esim, ota_salt, provider_id)
VALUES (10, '111111111111111', '1000000001', '10000001', '1001', '10000001', '1001', 'auth', 'a0', 'DEFAULT', null,
        null, null, null, '35790001261', 4, 5, null, null, null, null, null, false, null, 8),
       (11, '111111111111112', '1000000002', '10000002', '1002', '10000002', '1002', 'auth', 'a0', 'DEFAULT', null,
        null, null, null, '35790001262', 4, 5, null, null, null, null, null, false, null, 8),
       (12, '111111111111113', '1000000003', '10000003', '1003', '10000003', '1003', 'auth', 'a0', 'DEFAULT', null,
        null, null, null, null, 4, 5, null, null, null, null, null, false, null, 8),
       (13, '111111111111114', '1000000004', '10000004', '1004', '10000004', '1004', 'auth', 'a0', 'DEFAULT', null,
        null, null, null, null, 4, 51, null, null, null, null, null, false, null, 8),
       (14, '111111111111115', '1000000005', '10000005', '1005', '10000005', '1005', 'auth', 'a0', 'DEFAULT', null,
        null, null, null, null, 4, 51, null, null, null, null, null, false, null, 8),
       (15, '111111111111116', '1000000006', '10000006', '1006', '10000006', '1006', 'auth', 'a0', 'DEFAULT', null,
        null, null, null, null, 4, 51, null, null, null, null, null, false, null, 8);

SET
    FOREIGN_KEY_CHECKS = 1;