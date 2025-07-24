SET
    FOREIGN_KEY_CHECKS = 0;

INSERT INTO inventory_pool(id, code, mvno, sim_profile, description)
VALUES (1, '1234', null, null, null),
       (2, '1235', null, null, null);

INSERT INTO plmn(id, plmn_code, network_name, tadig_code, country_iso_code, country_name, ranges_prefix)
VALUES (1, '12345', 'NET', '1234', '123', 'France', '33');

INSERT INTO plmn_aud(id, revision_id, revision_type, plmn_code, network_name, tadig_code, country_iso_code,
                     country_name, ranges_prefix)
VALUES (1, 1, 1, '12345', 'NET', '1234', '123', 'France', '33');

INSERT INTO provider(id, name, access_type)
VALUES (1, 'Toto', 'DOCSIS'),
       (2, 'Tata', 'DOCSIS'),
       (3, 'GEMALTO', 'DOCSIS'),
       (4, 'IDEMIA','DOCSIS');

INSERT INTO warehouse(id, name, reseller_code)
VALUES (1, 'TOTO', 'TOTO'),
       (2, 'TATA', 'TATA');

INSERT INTO auditenversinfo (id, timestamp, user_id)
VALUES (1, '1582624357882', 'g.,fantappie');

INSERT INTO equipment(id, serial_number, external_number, access_type, nature, recyclable, batch_number, service_id,
                      warehouse_id, preactivated, status, order_id, activation_date, assigment_date,
                      category)
VALUES (1, '893771033000000007', null, 'DOCSIS', 'MAIN', false, 1, null, 2, false, 'BOOKED', 1, null, null,
        'SIMCARD'),
       (2, '893771033000000008', null, 'DOCSIS', 'MAIN', false, null, null, 1, false, 'AVAILABLE', null, null, null,
        'SIMCARD'),
       (3, '893771033000000009', '37799900001', 'DOCSIS', 'MAIN', false, 1, 2, 1, true, 'DEACTIVATED', null, null,
        null, 'SIMCARD'),
       (4, '893771033000000010', null, 'FREEDHOME', 'ADDITIONAL', false, 2, null, 1, false, 'BOOKED', 2, null, null,
        'SIMCARD'),
       (5, '893771033000000011', null, 'FREEDHOME', 'ADDITIONAL', false, 2, null, 1, false, 'ASSIGNED', 2, null,
        '2021-01-18 08:45:58', 'SIMCARD');

insert into equipment_aud (id, revision_id, revision_type, serial_number, external_number, access_type, nature,
                           recyclable, batch_number, service_id, preactivated, status, order_id, category)
values (1, 1, 1, '893771033000000007', null, 'DOCSIS', 'MAIN', false, null, null, false, 'BOOKED', 1, 'SIMCARD');

INSERT INTO simcard(id, imsi_number, imsi_sponsor_number, puk1_code, pin1_code, puk2_code, pin2_code,
                    authentification_key, access_control_class, sim_profile, ota_ciphering_key, ota_signature_key,
                    put_description_key, admin_code, number, plmn_id, inventorypool_id, pack_id, transportKey,
                    algorithmVersion, checkDigit,brand, allotment_id, paired_equipment_id, is_esim, ota_salt, provider_id,
                    activation_code, confirmation_code, qr_code)
VALUES (1, '123456789012345', null, null, null, null, null, null, null, null, null, null, null, null, null, 1, 1, null,
        0, 0, 0,'test', null, null, false, null, 1, 1, null,null),
       (2, '123456789012346', '123457', null, null, null, null, null, null, null, null, null, null, null, '456821', 1,
        1, null, 0, 0, 0,'test', null, null, false, null, 1, 1, null, null),
       (3, '123456789012347', '123456', null, null, null, null, null, null, null, null, null, null, null, '456789', 1,
        2, 7, 0, 0, 0,'test', 6, 4, true, null, 2, 2, null, null),
       (4, '123456789012348', null, '10015953', null, null, null, null, null, null, null, null, null, null, null, 1, 1,
        null,
        0, 0, 0,'test', null, null, true, 'BCEB92ACD3E2CCC72FB4ADDD258AD981', 1, null, null, null),
       (5, '123456789012349', null, '10015955', '1001', '10015954', '1002', null, null, null, null, null, null, null,
        null, 1, 1,
        null,
        0, 0, 0,'test', null, null, true, 'BCEB92ACD3E2CCC72FB4ADDD258AD981', 1, null, null, null);

insert into simcard_aud (id, revision_id, imsi_number, imsi_sponsor_number, puk1_code, pin1_code,
                         puk2_code, pin2_code, authentification_key, access_control_class, sim_profile,
                         ota_ciphering_key, ota_signature_key, put_description_key, admin_code, number, plmn_id,
                         inventorypool_id, pack_id, transportKey, algorithmVersion, checkDigit,brand, allotment_id,
                         paired_equipment_id, is_esim, ota_salt)
values (1, 1, '123456789012345', null, null, null, null, null, null, null,null, null, null, null, null, null,
        null, 1, 1, null, 0, 0, null, null, null, false, null);

INSERT INTO inventory_pool(id, code, mvno, sim_profile, description)
VALUES (5, 'METEOR_PAIRED_CUSTOMER_POOL', 0, 'DEFAULT', null),
       (51, 'TEST_REPLACEMENT', 0, 'REPLACEMENT', null);

INSERT INTO file_configuration (id, name, prefix, suffix, header_format, record_format)
VALUES (1, 'default_import', 'MMC', '.out', null, null),
       (2, 'default_export', 'MMC', '.inp', '<div>${CONTENT}</div>', '<div>${CONTENT}</div>');

INSERT INTO simcard_generation (id, name, transportKey, algorithmVersion, plmn_id, provider_id, export_file_config,
                                import_file_config, msin_sequence)
VALUES (3, 'EIRCOM_FCS_MOB_LANDLINE_UNPAIR', 6, 1, 1, 1, 2, 1, 'DEFAULT'),
       (4, 'TEST_SCGC', 6, 1, 1, 1, 2, 1, 'DEFAULT');

INSERT INTO batch(batch_number, export_file_name, import_file_name, inventory_pool_id,
                  simcard_generation_configuration_id, creation_date, returned_date, return_processed_date)
VALUES (1, 'TEST.out', 'TEST.out', 5, 4, '2021-01-18 08:45:58', '2021-01-18 08:45:58', null),
       (2, 'TEST2.out', 'TEST2.in', 5, 3, '2021-01-18 08:45:58', null, null),
       (3, 'TEST3.out', 'TEST3.in', 51, 3, '2021-01-18 08:45:58', '2021-01-18 08:45:58', '2021-01-18 08:45:58');

INSERT INTO allotment_summary(allotment_id, allotment_number, batch_number, allotment_type, inventorypool_id, quantity,
                              pack_with_handset,
                              price_plan, initial_credit, pre_provisioning_required, sent_to_logistics_date,
                              provisioned_date, creation_date, pre_provisioning_failures)
VALUES (6, 1, 1, 'PREPAID', 1, 1, 0, 'SOMETHING', 15, 0, null, null, '2021-01-18 08:45:58', 0);

INSERT INTO esim_notification (id, equipment_id, iccid, profile_type, timestamp, notification_point_id, check_point, status, message, reason_code, date)
VALUES  (1, 1, '34554323456543', 'test-1', null, null, null, null, null, null, null),
        (2, 1, '34554323456544', 'test-2', null, null, null, null, null, null, null);

SET
    FOREIGN_KEY_CHECKS = 1;