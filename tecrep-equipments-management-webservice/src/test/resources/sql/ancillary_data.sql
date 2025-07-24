SET
    FOREIGN_KEY_CHECKS = 0;

INSERT INTO warehouse (id, name, reseller_code)
VALUES (1, 'TOTO', 'TOTO'),
       (2, 'TATA', 'TATA');

INSERT INTO provider (id, name, access_type)
VALUES (1, 'Toto', 'DOCSIS');

INSERT INTO plmn (id, plmn_code, network_name, tadig_code, country_iso_code, country_name, ranges_prefix)
VALUES (1, '20201', 'Cosmote Mobile Telecommunications S.A.', 'GRCCO', 'GRC', 'Greece', '3368900890252');

INSERT INTO equipment (id, serial_number, external_number, access_type, nature,
                       recyclable, batch_number, service_id, warehouse_id, preactivated, status, order_id,
                       activation_date, assigment_date, category)
VALUES (1, 'GFAB02600017', null, 'DOCSIS', 'MAIN', false, 20001, null, 1, true, 'BOOKED', '1', null, null,
        'ANCILLARY'),
       (2, 'GFAB02600018', null, 'DOCSIS', 'MAIN', false, null, null, 1, true, 'DEACTIVATED', '3', null, null,
        'ANCILLARY'),
       (3, 'GFAB02600019', null, 'DOCSIS', 'MAIN', true, null, 5, 1, true, 'ASSIGNED', null, null,
        '2021-01-18 08:45:58', 'ANCILLARY'),
       (4, 'GFAB02600020', '37799900002', 'FTTH', 'MAIN', false, null, null, 1, true, 'DEACTIVATED', '2', null, null,
        'ANCILLARY'),
       (5, 'GFAB02600021', null, 'DOCSIS', 'ADDITIONAL', false, null, null, 1, true, 'AVAILABLE', null, null, null,
        'ANCILLARY'),
       (6, '893771033000000007', null, 'DOCSIS', 'MAIN', false, 1, null, 1, false, 'BOOKED', 1, null, null,
        'SIMCARD'),
       (7, '893771033000000008', null, 'DOCSIS', 'MAIN', false, null, null, 1, false, 'AVAILABLE', null, null, null,
        'SIMCARD'),
       (8, '893771033000000009', '37799900001', 'DOCSIS', 'MAIN', false, 1, 2, 1, true, 'DEACTIVATED', null, null,
        null, 'SIMCARD'),
       (9, '893771033000000010', null, 'FREEDHOME', 'ADDITIONAL', false, 2, null, 1, false, 'BOOKED', 2, null, null,
        'SIMCARD'),
       (10, 'GFAB02600022', null, 'DOCSIS', 'MAIN', true, null, 3, 1, true, 'ASSIGNED', null, null,
        '2021-01-18 08:45:58', 'ANCILLARY');


INSERT INTO ancillary_equipment (id, mac_address, equipment_model_id, independent, paired_equipment_id, equipment_name,
                                 sfp_version, password, number_recycles)
VALUES (1, '00:0A:95:9D:68:14', 1, false, null, 'BRDBOX', null, null, 0),
       (2, '00:0A:95:9D:68:15', 2, true, null, 'BRDBOX', null, null, 0),
       (3, '00:0A:95:9D:68:16', 3, true, 1, 'BRDBOX', null, null, 0),
       (4, '00:0A:95:9D:68:17', 2, true, null, 'HDD', null, null, 0),
       (5, '00:0A:95:9D:68:18', 1, true, null, 'BRDBOX', 'PO2020071700001', 1, 0),
       (10, '00:0A:95:9D:68:19', 3, true, null, 'BRDBOX', null, null, 0);

INSERT INTO equipment_model (id, name, currentFirmware, access_type, category, provider_id)
VALUES (1, 'test', ' test-2', 'DOCSIS', 'ANCILLARY', 1),
       (2, 'sagem', ' test-3', 'DOCSIS', 'ANCILLARY', 1),
       (3, 'HDD500', ' test-4', 'DOCSIS', 'ANCILLARY', 1);

INSERT INTO inventory_pool(id, code, mvno, sim_profile, description)
VALUES (1, 'METEOR_PAIRED_CUSTOMER_POOL', 0, 'DEFAULT', null);

INSERT INTO simcard (id, imsi_number, imsi_sponsor_number, puk1_code, pin1_code, puk2_code, pin2_code,
                     authentification_key, access_control_class, sim_profile, ota_ciphering_key, ota_signature_key,
                     put_description_key, admin_code, number, plmn_id, inventorypool_id, pack_id, transportKey,
                     algorithmVersion, checkDigit, allotment_id, paired_equipment_id, is_esim, ota_salt)
VALUES (6, '123456789012345', null, null, null, null, null, null, null, null, null, null, null, null, null, 1, 1, null,
        0, 0, 0, null, null, false, null),
       (7, '123456789012346', '123457', null, null, null, null, null, null, null, null, null, null, null, '456821', 1,
        1, null, 0, 0, 0, null, null, false, null),
       (8, '123456789012347', '123456', null, null, null, null, null, null, null, null, null, null, null, '456789', 1,
        1, 7, 0, 0, 0, 6, 4, true, null),
       (9, '123456789012348', null, '10015953', null, null, null, null, null, null, null, null, null, null, null, 1, 1,
        null,
        0, 0, 0, null, null, true, 'BCEB92ACD3E2CCC72FB4ADDD258AD981');

INSERT INTO auditenversinfo (id, timestamp, user_id)
VALUES (1, '1582624357882', 'g.,fantappie'),
       (2, '1582624357888', 'g.fantappie');

INSERT INTO ancillary_equipment_aud (id, revision_id, mac_address, independent,
                                     equipment_name, sfp_version, password)
VALUES (1, 1, '6C:99:61:00:02:01', true, 'ONT', 'No SFP module ', null);

insert into equipment_aud (id, revision_id, revision_type, serial_number, external_number, access_type, nature,
                           recyclable, batch_number, service_id, preactivated, status,
                           order_id, category, warehouse_id)
values (1, 1, 1, 'GFAB02600070', null, 'FTTH', 'MAIN', true, '20001', null, false, 'AVAILABLE', null, 'ANCILLARY',1);

SET
    FOREIGN_KEY_CHECKS = 1;