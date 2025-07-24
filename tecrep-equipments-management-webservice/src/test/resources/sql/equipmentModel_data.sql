SET
    FOREIGN_KEY_CHECKS = 0;

INSERT INTO provider(id, name, access_type)
VALUES (1, 'Toto', 'DOCSIS'),
       (2, 'Tata', 'DOCSIS');

INSERT INTO equipment_model (id, name, currentFirmware, access_type, category, provider_id)
VALUES (1, 'name-1', ' test-1', 'DOCSIS', 'CPE', 1),
       (2, 'name-2', ' test-2', 'DOCSIS', 'CPE', 2),
       (3, 'name-3', ' test-3', 'DOCSIS', 'CPE', 1),
       (4, 'name-4', ' test-4', 'DOCSIS', 'CPE', 2);

INSERT INTO equipment(id, serial_number, external_number, access_type, nature, recyclable, batch_number, service_id,
                      warehouse_id, preactivated, status, order_id, activation_date, assigment_date,
                      category)
VALUES (1, '8937710330000000007', null, 'DOCSIS', 'MAIN', false, null, null, null, false, 'BOOKED', 1, null, null,
        'CPE');

INSERT INTO cpe (id, mac_address_cpe, mac_address_router, mac_address_voip, chipset_id, equipment_model_id,
                 mac_address_lan, mac_address2_4g, mac_address_5g, hw_version, wpa_key, number_recycles)
VALUES (1, '00:0A:95:9D:68:14', '00:0A:95:9D:68:20', '00:0A:95:9D:68:34', null, 2, '00:0A:95:9D:68:20',
        '00:0A:95:9D:68:45', '00:0A:95:9D:68:70', null, null, 0);

SET
    FOREIGN_KEY_CHECKS = 1;