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
VALUES (1, 'test_0', ' test-0', 'DOCSIS', 'ANCILLARY', 1),
       (2, 'test_1', ' test-1', 'DOCSIS', 'ANCILLARY', 1),
       (3, 'test_2', ' test-2', 'DOCSIS', 'ANCILLARY', 1),
       (4, 'test_3', ' test-3', 'DOCSIS', 'ANCILLARY', 1),
       (5, 'test_4', ' test-4', 'DOCSIS', 'ANCILLARY', 2);

INSERT INTO equipment (id, serial_number, external_number, access_type, nature,
                       recyclable, batch_number, service_id, warehouse_id, preactivated, status, order_id,
                       category)
VALUES (1, 'GFAB02600017', null, 'DOCSIS', 'MAIN', false, null, null, 1, true, 'BOOKED', '1', 'ANCILLARY'),
       (2, 'GFAB02600018', null, 'DOCSIS', 'MAIN', false, null, null, 1, true, 'BOOKED', '3', 'ANCILLARY'),
       (3, 'GFAB02600019', '7612205998872', 'DOCSIS', 'MAIN', false, 'B01', 5, 1, true, 'ASSIGNED', null,
        'ANCILLARY'),
       (4, 'GFAB02600020', '37799900002', 'FTTH', 'MAIN', false, null, null, 2, true, 'BOOKED', '2', 'ANCILLARY'),
       (5, 'GFAB02600021', null, 'DOCSIS', 'ADDITIONAL', false, null, null, 2, true, 'AVAILABLE', null, 'ANCILLARY');

INSERT INTO ancillary_equipment (id, mac_address, equipment_model_id, independent, paired_equipment_id, equipment_name,
                                 sfp_version, password, number_recycles)
VALUES (1, '00:0A:95:9D:68:14', 1, false, null, 'BRDBOX', null, null, 0),
       (2, '00:0A:95:9D:68:15', 2, true, null, 'BRDBOX', null, null, 0),
       (3, '00:0A:95:9D:68:16', 3, true, 1, 'BRDBOX', null, null, 0),
       (4, '00:0A:95:9D:68:17', 4, true, null, 'HDD', null, null, 0),
       (5, '00:0A:95:9D:68:18', 5, false, null, 'BRDBOX', 'PO2020071700001', null, 0);

SET
    FOREIGN_KEY_CHECKS = 1;