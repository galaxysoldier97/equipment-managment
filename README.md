# TecRep - Equipments Management

TecRep (Technical Repository) Equipments Management is a microservice managing equipments needing provisioning.
An equipment could then be a SIM card, a CPE, or other types of equipment called "Ancillary equipments" in the model. 
For example for the EPIC BBHB project, the homebox is an ancillary equipment.

More information [here](https://confluence.steelhome.internal/display/ION/Tecrep+Equipements+Management)

# Sonarqube

Information about code quality can be found on [this related sonarqube link](https://sonarqube.steelhome.internal/dashboard?id=tecrep-equipments-management)

# Liquibase

To run liquibase locally, execute this command from equipments-management-webservice module:
```
mvn liquibase:update -Dliquibase.url=jdbc:mariadb://localhost:3306/tec_rep_eqm -Dliquibase.changeLogFile=db/changelog/db.changelog-master.xml -Dliquibase.password=rootpwd -Dliquibase.username=root
```