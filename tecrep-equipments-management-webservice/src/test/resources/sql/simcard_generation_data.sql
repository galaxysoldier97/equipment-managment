SET
    FOREIGN_KEY_CHECKS = 0;

INSERT INTO SEQUENCE_BATCHNUMBER(seq_value)
VALUES (0);

INSERT INTO SEQUENCE_ICCID(name, seq_value)
VALUES ('DEFAULT', 999999),
       ('ANOTHER_SEQUENCE', 666666),
       ('FINISHED_SEQUENCE', 666665);

INSERT INTO SEQUENCE_MSIN(name, seq_value)
VALUES ('DEFAULT', 99999999),
       ('ANOTHER_SEQUENCE', 658854412),
       ('FINISHED_SEQUENCE', 658854411);

INSERT INTO file_configuration (id, name, prefix, suffix, header_format, record_format)
VALUES (1, 'default_import', 'MMC', '.out', null, null),
       (2, 'default_export', 'MMC', '.inp', '<div>${CONTENT}</div>', '<div>${CONTENT}</div>');

INSERT INTO simcard_generation (id, name, transportKey, algorithmVersion, plmn_id, provider_id, export_file_config,
                                import_file_config, msin_sequence, iccid_sequence, type, fixed_prefix, sequence_prefix)
VALUES (1, 'EIRCOM_FCS_MOB_LANDLINE_UNPAIR', 6, 1, 1, 1, 2, 1, 'DEFAULT', 'DEFAULT', 'UNIVERSAL', '8935303', '0524'),
       (2, 'ANOTHER_CONFIGURATION', 6, 1, 1, 1, 2, 1, 'ANOTHER_SEQUENCE', 'DEFAULT', null, '8935303', '0524'),
       (3, 'YET_ANOTHER_CONFIGURATION', 6, 1, 1, 1, 2, 1, 'MISSING_SEQUENCE', 'DEFAULT', 'MULTISIM', '8935303', '0524'),
       (4, 'SUCH_A_NICE_CONFIGURATION', 6, 1, 1, 1, 2, 1, 'FINISHED_SEQUENCE', 'DEFAULT', null, '8935303', '0524'),
       (5, 'WOW_CONFIGURATION', 6, 1, 1, 1, 2, 1, 'DEFAULT', 'ANOTHER_SEQUENCE', null, '8935303', '0524'),
       (6, 'MEH_CONFIGURATION', 6, 1, 1, 1, 2, 1, 'DEFAULT', 'MISSING_SEQUENCE', null, '8935303', '0524'),
       (7, 'BOOM_CONFIGURATION', 6, 1, 1, 1, 2, 1, 'DEFAULT', 'FINISHED_SEQUENCE', null, '8935303', '0524');

INSERT INTO plmn (id, plmn_code, network_name, tadig_code, country_iso_code, country_name, ranges_prefix)
VALUES (1, '27203', 'Meteor Mobile Telecommunications Limited', 'IRLME', 'IRL', 'Ireland', '3368900890401');

INSERT INTO provider (id, name, access_type)
VALUES (1, 'Technicolor', 'DOCSIS');

INSERT INTO inventory_pool(id, code, mvno, sim_profile, description)
VALUES (1, 'METERO_PAIRED_CUSTOMER_POOL', 0, 'DEFAULT', null);

INSERT INTO batch(batch_number, export_file_name, import_file_name, inventory_pool_id,
                  simcard_generation_configuration_id)
VALUES (2, 'TEST.out', 'TEST.out', 1, 1),
       (3, 'TEST3.out', 'TEST3.out', 1, 2),
       (4, 'TEST4.out', 'TEST4.out', 1, 3),
       (5, 'TEST5.out', 'TEST5.out', 1, 4),
       (6, 'TEST6.out', 'TEST6.out', 1, 5),
       (7, 'TEST7.out', 'TEST7.out', 1, 6),
       (8, 'TEST8.out', 'TEST8.out', 1, 7);

SET
    FOREIGN_KEY_CHECKS = 1;