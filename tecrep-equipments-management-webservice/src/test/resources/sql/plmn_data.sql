SET
    FOREIGN_KEY_CHECKS = 0;

INSERT INTO plmn (id, plmn_code, network_name, tadig_code, country_iso_code, country_name, ranges_prefix)
VALUES (1, '20201', 'Cosmote Mobile Telecommunications S.A.', 'GRCCO', 'GRC', 'Greece', '3368900890252'),
       (2, '20202', 'Cosmote Mobile Telecommunications S.A.', 'GRCCO', 'GRC', 'Greece', '3368900890253'),
       (3, '20203', 'Cosmote Mobile Telecommunications S.A.', 'GRCCO', 'GRC', 'Greece', '3368900890254'),
       (4, '20204', 'Cosmote Mobile Telecommunications S.A.', 'GRCCO', 'GRC', 'Greece', '3368900890255'),
       (5, '20205', 'Cosmote Mobile Telecommunications S.A.', 'GRCCO', 'GRC', 'Greece', '3368900890256');


SET
    FOREIGN_KEY_CHECKS = 1;