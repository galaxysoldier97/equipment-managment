SET
    FOREIGN_KEY_CHECKS = 0;

INSERT INTO file_configuration (id, name, prefix, suffix, header_format, record_format)
VALUES (1, 'default_export', 'MMC', '.out', null, '<div>${CONTENT2}</div>'),
       (2, 'test_export', 'MMCC', '.out2', null, '<div>${CONTENT2}</div>'),
       (3, 'test_export2', 'MMCC', '.out3', null, '<div>${CONTENT3}</div>'),
       (4, 'test_export3', 'MMCC', '.out4', null, '<div>${CONTENT3}</div>');

INSERT INTO simcard_generation (id, name, transportKey, algorithmVersion, plmn_id, provider_id, export_file_config,
                                import_file_config, msin_sequence)
VALUES (1, 'EIRCOM_FCS_MOB_LANDLINE_UNPAIR', 6, 1, 1, 1, 2, 1, 'DEFAULT'),
       (2, 'EIRCOM_FCS_MOB_LANDLINE_UNPAIR2', 6, 1, 1, 1, 3, 2, 'DEFAULT');

INSERT INTO plmn (id, plmn_code, network_name, tadig_code, country_iso_code, country_name, ranges_prefix)
VALUES (1, '27203', 'Meteor Mobile Telecommunications Limited', 'IRLME', 'IRL', 'Ireland', '3368900890401');

INSERT INTO provider (id, name, access_type)
VALUES (1, 'Technicolor', 'DOCSIS');

SET
    FOREIGN_KEY_CHECKS = 1;