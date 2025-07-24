SET
    FOREIGN_KEY_CHECKS = 0;

INSERT INTO provider (id, name, access_type)
VALUES (1, 'Technicolor', 'DOCSIS'),
       (2, 'Sagemcom', 'FTTH'),
       (3, 'Orange', 'DISE'),
       (4, 'Gemalto', 'FREEDHOME'),
       (5, 'test', 'FTTH');

SET
    FOREIGN_KEY_CHECKS = 1;