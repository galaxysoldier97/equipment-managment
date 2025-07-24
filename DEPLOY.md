# Technical Repository - Equipments Management (TECREP-EQM)

[[_TOC_]]

## Database
EQM is designed to work with MariaDB 10.7+.
Earlier versions of MySQL and MariaDB could work but are not advised.

## Keycloak
- Create a client named `tecrep-equipments-management`.
- Set `Authorization Enabled` to `ON`.
- Create roles: EQUIPMENT_DELETE, EQUIPMENT_READ, EQUIPMENT_WRITE
- Give these roles to users, groups, clients or roles that need access to the service.
- An authorization model configuration file is available [here](tecrep-equipments-management-authz-config.json). Import it in `tecrep-equipments-management` > `Authorization` > `Settings` > `Import`.
- Remove resource `Default Resource` and policy `Default policy`.

## Environment variables (data model migration)
On the container, put these environment variables and replace the entry point with the command `liquibase update`

| Environment variable name              | Description | Format | Example / Default Value                       | Required |
|:---------------------------------------|-------------|--------|:----------------------------------------------|:---------|
| LIQUIBASE_COMMAND_URL                  |             | string | `jdbc:mariadb://localhost:3306/tec_rep_eqm`   | Y        |
| LIQUIBASE_COMMAND_USERNAME (secret)    |             | string | `admin`                                       | Y        |
| LIQUIBASE_COMMAND_PASSWORD (secret)    |             | string | `password`                                    | Y        |
| LIQUIBASE_COMMAND_CHANGELOG_FILE       |             | string | `db/changelog/db.changelog-master.xml`        | Y        |
| LIQUIBASE_SEARCH_PATH                  |             | string | `/liquibase/changelog`                        | Y        |

## Environment Variables (main application)
### Database connection
| Environment variable name                | Description                                                                 | Format | Example / Default Value                      | Required |
|:-----------------------------------------|-----------------------------------------------------------------------------|--------|:---------------------------------------------|:---------|
| DATASOURCE_URL                           |                                                                             | string | `jdbc:mariadb://localhost:3306/tec_rep_eqm`  | Y        |
| DATASOURCE_USERNAME (secret)             |                                                                             | string | `admin`                                      | Y        |
| DATASOURCE_PASSWORD (secret)             |                                                                             | string | `password`                                   | Y        |
| SPRING_DATASOURCE_DRIVERCLASSNAME        |                                                                             | string | `org.mariadb.jdbc.Driver`                    | N        |
| SPRING_DATASOURCE_HIKARI_MAXIMUMPOOLSIZE |                                                                             | number | `10`                                         | N        |
| SPRING_DATASOURCE_HIKARI_MINIMUMIDLE     |                                                                             | number | `3`                                          | N        |

### Integration with Keycloak
#### Mono-realm
If active profiles do not contain `keycloak-multitenant`

| Environment variable name             | Description | Format | Example / Default Value        | Required  |
|:--------------------------------------|-------------|--------|:-------------------------------|:----------|
| KEYCLOAK_AUTH_SERVER_URL              |             | string | `http://localhost:5555/auth`   | Y         |
| KEYCLOAK_REALM                        |             | string | `monaco-telecom-dev`           | Y         |
| KEYCLOAK_RESOURCE (secret)            |             | string | `tecrep-equipments-management` | Y         |
| KEYCLOAK_CREDENTIALS_SECRET (secret)  |             | string | `0123456789`                   | Y         |

#### Multi-realm
If active profiles contain `keycloak-multitenant`

| Environment variable name              | Description | Format | Example / Default Value        | Required |
|:---------------------------------------|-------------|--------|:-------------------------------|:---------|
| KEYCLOAK_0_AUTH_SERVER_URL             |             | string | `http://localhost:5555/auth`   | Y        |
| KEYCLOAK_0_REALM                       |             | string | `monaco-telecom-dev`           | Y        |
| KEYCLOAK_0_RESOURCE (secret)           |             | string | `tecrep-equipments-management` | Y        |
| KEYCLOAK_0_CREDENTIALS_SECRET (secret) |             | string | `0123456789`                   | Y        |
| KEYCLOAK_1_AUTH_SERVER_URL             |             | string | `http://localhost:5556/auth`   | Y        |
| KEYCLOAK_1_REALM                       |             | string | `epic-ci`                      | Y        |
| KEYCLOAK_1_RESOURCE (secret)           |             | string | `tecrep-equipments-management` | Y        |
| KEYCLOAK_1_CREDENTIALS_SECRET (secret) |             | string | `9876543210`                   | Y        |

### Operator-specific variables

#### EIR
| Environment variable name                | Description                                                                 | Format | Example / Default Value                      | Required |
|:-----------------------------------------|-----------------------------------------------------------------------------|--------|:---------------------------------------------|:---------|
| SIMCARDGENERATION_FORMATICCID            | Formatting of the ICCID sequence                                            | string | `%07d`                                       | N        |
| SIMCARDGENERATION_FORMATMSIN             | Formatting of the MSIDN sequence                                            | string | `%09d`                                       | N        |

#### EPIC
| Environment variable name                | Description                                                                 | Format | Example / Default Value                      | Required |
|:-----------------------------------------|-----------------------------------------------------------------------------|--------|:---------------------------------------------|:---------|
| SIMCARDGENERATION_FORMATICCID            | Formatting of the ICCID sequence                                            | string | `%06d`                                       | N        |
| SIMCARDGENERATION_FORMATMSIN             | Formatting of the MSIDN sequence                                            | string | `%06d`                                       | N        |
| IFS_URI                                  | Address of the IFS service                                                  | string | `http://lif.uat.test:8083/Ifs`               | N        |
| TABS_URI                                 | Address of the TABS service                                                 | string | `http://lif.uat.test:8082`                   | N        |

### Miscellaneous
| Environment variable name                                       | Description                                                                                           | Format | Example / Default Value                         | Required |
|:----------------------------------------------------------------|-------------------------------------------------------------------------------------------------------|--------|:------------------------------------------------|:---------|
| SERVER_PORT                                                     |                                                                                                       | number | `8080`                                          | N        |
| INCOMING_FILES_NOTIFYADDRESS (TECREP_EQM_RETURN_NOTIFY_ADDRESS) | Address to send notification to upon SIM provisioning file return                                     | string | `test@test.test`                                | N        |
| INCOMING_FILES_QUEUEDIRECTORY                                   | Directory containing provisioning files when returned from the provider                               | string | `/var/tecrep-equipments-management/files/queue` | N        |
| INCOMING_FILES_PROCESSEDDIRECTORY (TECREP_EQM_IMPORT_SIM_DIR)   | Directory containing provisioning files returned from the provider, once processed by the watcher job | string | `/var/tecrep-equipments-management/files`       | N        |
| PREPAY_MAXCREDIT                                                | Maximum credit allowed in the allotment process                                                       | number | `999`                                           | N        |
| ALLOTMENTFILENAME_FORMATBATCHID                                 | Formatting of the Batch ID in the generated allotment export file name                                | string | `%05d`                                          | N        |
| ALLOTMENTFILENAME_FORMATALLOTMENTNUMBER                         | Formatting of the Allotment number in the generated allotment export file name                        | string | `%02d`                                          | N        |

## Provider output files watcher job
In addition to datasource previously described.
This job needs RabbitMQ to send notifications.

| Environment variable name                                       | Description                                                                    | Format  | Example / Default Value                         | Required |
|:----------------------------------------------------------------|--------------------------------------------------------------------------------|---------|:------------------------------------------------|:---------|
| INCOMING_FILES_QUEUEDIRECTORY                                   | Directory containing provisioning files when returned from the provider        | string  | `/var/tecrep-equipments-management/files/queue` | N        |
| INCOMING_FILES_PROCESSEDDIRECTORY (TECREP_EQM_IMPORT_SIM_DIR)   | Directory to put provisioning files returned from the provider, once processed | string  | `/var/tecrep-equipments-management/files`       | N        |
| INCOMING_FILES_NOTIFY (PREPAY_RETURN_NOTIFY)                    | If true, send notification upon SIM provisioning file return                   | bool    | `false`                                         | N        |
| INCOMING_FILES_NOTIFYADDRESS (TECREP_EQM_RETURN_NOTIFY_ADDRESS) | Address to send notification to upon SIM provisioning file return              | string  | `test@test.test`                                | N        |
| INCOMING_FILES_NOTIFICATION_REFERENCE (PREPAY_RETURN_REFERENCE) | Reference of the notification in notification-center for prov file return      | string  | `returnPrepaySimCards`                          | N        |
| RABBITMQ_HOST                                                   |                                                                                | string  | `localhost`                                     | Y        |
| RABBITMQ_PORT                                                   |                                                                                | number  | `5672`                                          | Y        |
| RABBITMQ_USERNAME (secret)                                      |                                                                                | string  | `guest`                                         | Y        |
| RABBITMQ_PASSWORD (secret)                                      |                                                                                | string  | `guestpwd`                                      | Y        |
| RABBITMQ_VIRTUAL_HOST                                           |                                                                                | string  | `/`                                             | N        |
| AMQP_OUTBOUND_EXCHANGE_NOTIFICATIONCENTER                       | RabbitMQ Exchange name to send mail requests                                   | string  | `notification-center`                           | N        |
| AMQP_OUTBOUND_ROUTINGKEY_NOTIFICATIONREQUEST                    | RabbitMQ routing key to attach to mail requests                                | string  | `notification.request`                          | N        |
| DATA_CHANGED_EVENT_EXCHANGE                                     |                                                                                | string  | `queuing.equipments-management.sync`            | N        |
| DATA_CHANGED_EVENT_ROUTING_KEY                                  |                                                                                | string  | `sync`                                          | N        |
| DATA_CHANGED_EVENT_ENABLED                                      | If true, if sent event after number status changed                             | boolean | `false`                                         | N        |

## Data cleanup job
In addition to datasource variables previously described.

| Environment variable name                        | Description                                     | Format    | Example / Default Value | Required |
|:-------------------------------------------------|-------------------------------------------------|-----------|:------------------------|:---------|
| AUDIT_LINESTOKEEP                                | Number of audit lines to keep upon cleanup      | integer   | `1`                     | N        |