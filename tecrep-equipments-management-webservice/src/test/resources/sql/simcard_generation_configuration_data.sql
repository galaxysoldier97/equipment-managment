SET
    FOREIGN_KEY_CHECKS = 0;

INSERT INTO file_configuration (id, name, prefix, suffix, header_format, record_format)
VALUES (1, 'default_import', 'MMC', '.out', null, null),
       (2, 'default_export', 'MMC', '.inp', '<div>${CONTENT3}</div>', null);

INSERT INTO simcard_generation (id, name, transportKey, algorithmVersion, plmn_id, provider_id, export_file_config,
                                import_file_config, msin_sequence, iccid_sequence, artwork, sim_reference, type,
                                fixed_prefix, sequence_prefix)
VALUES (1, 'EIRCOM_FCS_MOB_LANDLINE_UNPAIR', 6, 1, 1, 1, 2, 1, 'DEFAULT', 'DEFAULT', 'EIR_SME_REP', null, 'MULTISIM',
        '8935303', '0524'),
       (2, 'ANOTHER', 7, 1, 1, 1, 2, 1, 'DEFAULT', 'DEFAULT', null, 'MET_PP001_LTE', null, null, null);

INSERT INTO plmn (id, plmn_code, network_name, tadig_code, country_iso_code, country_name, ranges_prefix)
VALUES (1, '27203', 'Meteor Mobile Telecommunications Limited', 'IRLME', 'IRL', 'Ireland', '3368900890401'),
       (2, '27204', 'Meteor Mobile Telecommunications Limited 2', 'IRLME2', 'IRL', 'Ireland', '3368900890402');

INSERT INTO provider (id, name, access_type)
VALUES (1, 'Technicolor', 'DOCSIS');

INSERT INTO inventory_pool(id, code, mvno, sim_profile, description)
VALUES (1, 'METEOR_PAIRED_CUSTOMER_POOL', 0, 'DEFAULT', null);

INSERT INTO batch(batch_number, export_file_name, import_file_name, inventory_pool_id,
                  simcard_generation_configuration_id, creation_date, returned_date, return_processed_date)
VALUES (6, 'TEST.out', 'TEST.out', 1, 2, '2021-01-18 08:45:58', '2021-01-18 08:45:58', null);

SET
    FOREIGN_KEY_CHECKS = 1;