SET
    FOREIGN_KEY_CHECKS = 0;

INSERT INTO warehouse (id, name, reseller_code)
VALUES (1, 'Monaco Telecom Entreprise', 'MTENT'),
       (2, 'SAV Résidentiel', 'SAVMT'),
       (3, 'SAV Entreprise', 'SERVICELS'),
       (4, 'Mccom', 'MCCOM');

SET
    FOREIGN_KEY_CHECKS = 1;