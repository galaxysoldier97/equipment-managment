SET
    FOREIGN_KEY_CHECKS = 0;

INSERT INTO inventory_pool(id, code, mvno, sim_profile, description)
VALUES (1, '1234', null, null, null);

INSERT INTO plmn(id, plmn_code, network_name, tadig_code, country_iso_code, country_name, ranges_prefix)
VALUES (1, '12345', 'NET', '1234', '123', 'France', '33');

INSERT INTO provider(id, name, access_type)
VALUES (1, 'Toto', 'DOCSIS'),
       (2, 'Tata', 'DOCSIS');

INSERT INTO warehouse(id, name, reseller_code)
VALUES (1, 'TOTO', 'TOTO'),
       (2, 'TATA', 'TATA');

INSERT INTO equipment_model (id, name, currentFirmware, access_type, category, provider_id)
VALUES (1, 'Toto', ' Toto-0', 'DOCSIS', 'CPE', 2),
       (2, 'Toti', ' Toto-1', 'FTTH', 'CPE', 1),
       (3, 'sagem', 'SAGEM', 'DOCSIS', 'CPE', 1),
       (4, 'sagem', 'SAGEM', 'DOCSIS', 'ANCILLARY', 1),
       (5, 'Toti', ' Toto-1', 'FTTH', 'ANCILLARY', 1);

INSERT INTO equipment(id, serial_number, external_number, access_type, nature, recyclable, batch_number, service_id,
                      warehouse_id, preactivated, status, order_id, activation_date, assigment_date,
                      category)
VALUES (1, '8937710330000000007', null, 'DOCSIS', 'MAIN', false, null, null, 2, false, 'BOOKED', 1, null, null,
        'CPE'),
       (2, '8937710330000000008', null, 'DOCSIS', 'MAIN', false, null, null, 1, false, 'AVAILABLE', 2, null, null,
        'CPE'),
       (3, '8937710330000000009', '37799900001', 'FTTH', 'MAIN', false, 1, 2, 1, false, 'ASSIGNED', null, null,
        '2021-01-18 08:45:58', 'CPE'),
       (4, '8937710330000000010', null, 'DOCSIS', 'ADDITIONAL', false, 1, null, 1, false, 'DEPRECATED', 1, null,
        null, 'CPE'),
       (5, '8937710330000000011', null, 'DOCSIS', 'ADDITIONAL', false, 1, null, 1, false, 'DEACTIVATED', 1, null,
        null, 'CPE'),
       (6, '8937710330000000011', null, 'DOCSIS', 'MAIN', true, null, null, 1, true, 'DEACTIVATED', null, null, null,
        'ANCILLARY');

insert into equipment_aud (id, revision_id, revision_type, serial_number, external_number, access_type, nature,
                           recyclable, batch_number, service_id, preactivated, status, order_id, category, warehouse_id)
values (1, 1, 1, '8937710330000000007', null, 'DOCSIS', 'MAIN', false, null, null, false, 'BOOKED', 1, 'CPE',1);

INSERT INTO cpe (id, mac_address_cpe, mac_address_router, mac_address_voip, chipset_id, equipment_model_id,
                 mac_address_lan, mac_address2_4g, mac_address_5g, hw_version, wpa_key, number_recycles)
VALUES (1, '00:0A:95:9D:68:14', '00:0A:95:9D:68:20', '00:0A:95:9D:68:34', null, 2, '00:0A:95:9D:68:20',
        '00:0A:95:9D:68:45', '00:0A:95:9D:68:70', null, null, 0),
       (2, '00:0A:95:9D:68:15', '00:0A:95:9D:68:21', '00:0A:95:9D:68:35', null, 1, '00:0A:95:9D:68:21',
        '00:0A:95:9D:68:46', '00:0A:95:9D:68:71', '1.0.5', null, 0),
       (3, '00:0A:95:9D:68:16', '00:0A:95:9D:68:22', '00:0A:95:9D:68:36', null, 2, '00:0A:95:9D:68:22',
        '00:0A:95:9D:68:47', '00:0A:95:9D:68:72', null, null, 0),
       (4, '00:0A:95:9D:68:17', '00:0A:95:9D:68:23', '00:0A:95:9D:68:37', null, 2, '00:0A:95:9D:68:23',
        '00:0A:95:9D:68:48', '00:0A:95:9D:68:73', null, null, 0),
       (5, '00:0A:95:9D:68:18', '00:0A:95:9D:68:24', '00:0A:95:9D:68:38', null, 2, '00:0A:95:9D:68:24',
        '00:0A:95:9D:68:49', '00:0A:95:9D:68:74', null, null, 0);

INSERT INTO ancillary_equipment (id, mac_address, equipment_model_id, independent, paired_equipment_id, equipment_name,
                                 sfp_version, password, number_recycles)
VALUES (6, '00:0A:95:9D:68:18', 5, false, 5, 'HDD', null, null, 0);

insert into cpe_aud (id, revision_id, mac_address_cpe, mac_address_router, mac_address_voip,
                     mac_address_lan, mac_address2_4g, mac_address_5g, hw_version, wpa_key, chipset_id)
values (1, 1, '00:0A:95:9D:68:14', '00:0A:95:9D:68:20', '00:0A:95:9D:68:34',
        '00:0A:95:9D:68:20', '00:0A:95:9D:68:45', '00:0A:95:9D:68:70', null, null, null);

INSERT INTO auditenversinfo (id, timestamp, user_id)
VALUES (1, '1582624357882', 'g.,fantappie');

SET
    FOREIGN_KEY_CHECKS = 1;