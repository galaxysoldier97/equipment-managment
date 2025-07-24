SET
    FOREIGN_KEY_CHECKS = 0;

INSERT INTO provider (id, name, access_type)
VALUES (1, 'Technicolor', 'DOCSIS');

INSERT INTO warehouse(id, name, reseller_code)
VALUES (1, 'TOTO', 'TOTO');

INSERT INTO plmn (id, plmn_code, network_name, tadig_code, country_iso_code, country_name, ranges_prefix)
VALUES (1, '20201', 'Cosmote Mobile Telecommunications S.A.', 'GRCCO', 'GRC', 'Greece', '3368900890252');

INSERT INTO equipment_model (id, name, currentFirmware, access_type, category, provider_id)
VALUES (1, 'name-1', ' test-1', 'DOCSIS', 'CPE', 1),
       (8, 'name-1', ' test-1', 'DOCSIS', 'ANCILLARY', 1);

insert into equipment (id, serial_number, external_number, access_type, nature, recyclable, batch_number, service_id,
                       warehouse_id, preactivated, status, order_id, category, activity, activation_date,
                       assigment_date)
values (20, '8937710330000000064', null, 'FREEDHOME', 'MAIN', false, '00005', 47, 1, false, 'REPACKAGING', null,
        'SIMCARD', null, '2019-06-26 14:36:24.0', '2019-06-26 07:49:11.0'),
       (42192, 'GFAB02600051', '7612205998872', 'FTTH', 'MAIN', true, '25.202', null, 1, false, 'REPACKAGING', null,
        'CPE', null, '2020-10-21 09:18:28.0', '2020-10-21 09:18:18.0'),
       (42193, 'GFAB02600051', null, 'FTTH', 'MAIN', true, null, null, 1, false, 'REPACKAGING', null, 'ANCILLARY',
        null, '2020-10-20 00:38:21.0', '2020-10-20 00:39:05.0');

insert into simcard (id, imsi_number, imsi_sponsor_number, puk1_code, pin1_code, puk2_code, pin2_code,
                     authentification_key, access_control_class, sim_profile, ota_ciphering_key, ota_signature_key,
                     put_description_key, admin_code, number, plmn_id, inventorypool_id, pack_id, transportKey,
                     algorithmVersion, checkDigit, allotment_id, is_esim, ota_salt, paired_equipment_id, provider_id)
values (20, '212103300000006', '208011502920006', '51854921', '0000', '77279455', null,
        'DBACFD53A861B67CA4FF3C86EC5D97DA', null, 'DEFAULT', null, '820C6A957F1DCF038637835EE9516D41', null,
        '80445A64C5263ED6', null, 1, null, null, null, null, null, null, false, null, null, 1);

insert into cpe (id, mac_address_cpe, mac_address_router, mac_address_voip, chipset_id, mac_address_lan,
                 mac_address2_4g, mac_address_5g, hw_version, wpa_key, equipment_model_id, number_recycles)
values (42192, '6C:99:61:00:04:F1', '6C:99:61:00:04:F0', '6C:99:61:00:04:F9', null, '6C:99:61:00:04:F7',
        '6C:99:61:00:04:F8', '6C:99:61:00:04:F5', '01.0', 'JKCCEYYFCHECHEPQNA', 1, 0);

insert into ancillary_equipment (id, mac_address, paired_equipment_id, independent, equipment_name, sfp_version,
                                 password, equipment_model_id, number_recycles)
values (42193, '6C:99:61:00:04:F6', null, true, 'ONT', 'PO2020071700001', '3GWPUGUXCE7N', 8, 0);

insert into equipment_aud (id, revision_id, revision_type, serial_number, external_number, access_type, nature,
                           recyclable, batch_number, service_id, preactivated, status, order_id, category, activity)
values (20, 5, 1, '8937710330000000064', null, null, 'MAIN', false, '00005', null, false, 'INSTORE', null, 'SIMCARD',
        null),
       (20, 8, 1, '8937710330000000064', null, null, 'MAIN', false, '00005', null, false, 'AVAILABLE', null, 'SIMCARD',
        null),
       (20, 119, 1, '8937710330000000064', null, null, 'MAIN', false, '00005', null, false, 'BOOKED', null, 'SIMCARD',
        null),
       (20, 407, 1, '8937710330000000064', null, null, 'MAIN', false, '00005', 47, false, 'ASSIGNED', null, 'SIMCARD',
        null),
       (42192, 49251, 1, 'GFAB02600051', '7612205998872', 'FTTH', 'MAIN', true, '25.202', null, false, 'INSTORE', null,
        'CPE', null),
       (42192, 49284, 1, 'GFAB02600051', '7612205998872', 'FTTH', 'MAIN', true, '25.202', null, false, 'AVAILABLE',
        null, 'CPE', null),
       (42192, 49285, 1, 'GFAB02600051', '7612205998872', 'FTTH', 'MAIN', true, '25.202', null, false, 'BOOKED', null,
        'CPE', null),
       (42192, 49286, 1, 'GFAB02600051', '7612205998872', 'FTTH', 'MAIN', true, '25.202', null, false, 'ASSIGNED', null,
        'CPE', null),
       (42193, 49252, 1, 'GFAB02600051', null, 'FTTH', 'MAIN', true, '25.202', null, false, 'INSTORE', null,
        'ANCILLARY', null),
       (42193, 49895, 1, 'GFAB02600051', null, 'FTTH', 'MAIN', true, '25.202', null, false, 'BOOKED', null, 'ANCILLARY',
        null),
       (42193, 49896, 1, 'GFAB02600051', null, 'FTTH', 'MAIN', true, '25.202', null, false, 'ASSIGNED', null,
        'ANCILLARY', null),
       (42193, 49897, 1, 'GFAB02600051', null, 'FTTH', 'MAIN', true, '25.202', null, false, 'ASSIGNED', null,
        'ANCILLARY', null);

insert into simcard_aud (id, revision_id, imsi_number, imsi_sponsor_number, puk1_code, pin1_code, puk2_code, pin2_code,
                         authentification_key, access_control_class, sim_profile, ota_ciphering_key, ota_signature_key,
                         put_description_key, admin_code, number, plmn_id, inventorypool_id, pack_id, transportKey,
                         algorithmVersion, checkDigit, allotment_id, is_esim, ota_salt, paired_equipment_id)
values (20, 5, '212103300000006', '208011502920006', '51854921', '0000', '77279455', null,
        'DBACFD53A861B67CA4FF3C86EC5D97DA', null, 'DEFAULT', null, '820C6A957F1DCF038637835EE9516D41', null,
        '80445A64C5263ED6', null, 53, null, null, null, null, null, null, false, null, null),
       (20, 8, '212103300000006', '208011502920006', '51854921', '0000', '77279455', null,
        'DBACFD53A861B67CA4FF3C86EC5D97DA', null, 'DEFAULT', null, '820C6A957F1DCF038637835EE9516D41', null,
        '80445A64C5263ED6', null, 53, null, null, null, null, null, null, false, null, null),
       (20, 119, '212103300000006', '208011502920006', '51854921', '0000', '77279455', null,
        'DBACFD53A861B67CA4FF3C86EC5D97DA', null, 'DEFAULT', null, '820C6A957F1DCF038637835EE9516D41', null,
        '80445A64C5263ED6', null, 53, null, null, null, null, null, null, false, null, null),
       (20, 407, '212103300000006', '208011502920006', '51854921', '0000', '77279455', null,
        'DBACFD53A861B67CA4FF3C86EC5D97DA', null, 'DEFAULT', null, '820C6A957F1DCF038637835EE9516D41', null,
        '80445A64C5263ED6', null, 53, null, null, null, null, null, null, false, null, null);

insert into cpe_aud (id, revision_id, mac_address_cpe, mac_address_router, mac_address_voip, chipset_id,
                     mac_address_lan, mac_address2_4g, mac_address_5g, hw_version, wpa_key, number_recycles)
values (42192, 49251, '6C:99:61:00:04:F1', '6C:99:61:00:04:F0', '6C:99:61:00:04:F3', null, '6C:99:61:00:04:F7',
        '6C:99:61:00:04:F4', '6C:99:61:00:04:F5', '01.0', 'JKCCEYYFCHECHEPQNA', null),
       (42192, 49284, '6C:99:61:00:04:F1', '6C:99:61:00:04:F0', '6C:99:61:00:04:F3', null, '6C:99:61:00:04:F7',
        '6C:99:61:00:04:F4', '6C:99:61:00:04:F5', '01.0', 'JKCCEYYFCHECHEPQNA', null),
       (42192, 49285, '6C:99:61:00:04:F1', '6C:99:61:00:04:F0', '6C:99:61:00:04:F3', null, '6C:99:61:00:04:F7',
        '6C:99:61:00:04:F4', '6C:99:61:00:04:F5', '01.0', 'JKCCEYYFCHECHEPQNA', null),
       (42192, 49286, '6C:99:61:00:04:F1', '6C:99:61:00:04:F0', '6C:99:61:00:04:F3', null, '6C:99:61:00:04:F7',
        '6C:99:61:00:04:F4', '6C:99:61:00:04:F5', '01.0', 'JKCCEYYFCHECHEPQNA', null);

insert into ancillary_equipment_aud (id, revision_id, mac_address, paired_equipment_id, independent, equipment_name,
                                     sfp_version, password, number_recycles)
values (42193, 49252, '6C:99:61:00:04:F7', null, true, 'ONT', 'PO2020071700001', '3GWPUGUXCE7N', null),
       (42193, 49895, '6C:99:61:00:04:F7', null, true, 'ONT', 'PO2020071700001', '3GWPUGUXCE7N', null),
       (42193, 49896, '6C:99:61:00:04:F7', null, true, 'ONT', 'PO2020071700001', '3GWPUGUXCE7N', null),
       (42193, 49897, '6C:99:61:00:04:F7', null, true, 'ONT', 'PO2020071700001', '3GWPUGUXCE7N', null);

insert into auditenversinfo (id, timestamp, user_id)
values (5, 1560764863266, ''),
       (8, 1560765146528, ''),
       (119, 1560765171142, ''),
       (407, 1560765203718, ''),
       (49251, 1560765215285, ''),
       (49252, 1560765215285, ''),
       (49284, 1560773850341, ''),
       (49285, 1560779052683, ''),
       (49286, 1560837827027, ''),
       (49895, 1560840273905, ''),
       (49896, 1560840273905, ''),
       (49897, 1560840273905, '');

SET
    FOREIGN_KEY_CHECKS = 1;