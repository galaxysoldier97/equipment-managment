# tecrep-equipments-management changelog

## 2.31.0
* Added PGP decryption for SAP_ANCILLARY_TEMP import using Milicom key

## 2.30.0
* Added po_ancillary_equipment_sap and homologacion_material_sap tables
* Updated equipments_temp table with po_ancillaryeqm_sap_id column

## 2.29.0
* Added status and order_upload_id columns to equipments_temp table
* Added equipments_config table

## 2.28.0
* Created equipments_temp table
* [MTOSS-2232] Delete batch import file

## 2.28.0
* [MTOSS-1880] Esim Notification

## 2.26.0
* [MTOSS-2067] Added new simcard import for Gomo
* [MTOSS-2065] Removed import STB_Sagemcom for ancillary
* [MTOSs-1879] SimProfile changes + added QrCode to SimCard

## 2.25.0
* [MTOSS-1981] Added Warehouse to history 
* [MTOSS-1907] Added params brand to SimCard

## 2.24.0
* [MTOSS-187] Fix reading header importer e-sim
* [MTOSS-1861] Better error handling around common-job/common-job-web
* [MTOSS-1676] Added logic for clean up jobs

## 2.23.0
* [MTOSS-1770] Fix search PLMN V2 in connector
* [MTOSS-1635] Publish events on lifecycle simcard
* Merge with dbmdl repository
* JsonFormat on equipment activationDate/assignmentDate
* [MTOSS-1663] Better Equipment model error handling
* [MTOSS-1666] Remove CPE with paired ancillary now forceable
* Fix Job Config creation not returning ID
* [MTOSS-1665] Remove equipments INSTORE + remove @ApiResponses
* OpenAPI v3
* [MTOSS-1659] Search ancillary by batchNumber

## 2.22.0
* [MTOSS-1621] Integration OpenApi 3
* Fix rollbackOnhold/rollbackDeactivate on ancillary not setting back serviceId when no paired equipment
* [MTOSS-1581] Batch Number v2 API
* [MTOSS-1620] Using jsonPath instead of json for better test validation
* Commons 1.1.32: common-restclient
* [MTOSS-1618] Refacto code importer CPE
* [MTOSS-1475] Updated import CPE
* Merged with tecrep-equipments-management-integration

## 2.21.0
* Commons 1.1.31 (common exception handling by exception-manager)
* [MTOSS-1599] Added params macAddress to cpeSearch
* [MTOSS-1581] Inventory Pool v2 API
* [MTOSS-1529] Added activation and confirmation code to simCard
* [MTOSS-1518] Optimized tests execution
* [MTOSS-1477] Capacity to search equipment models by category
* [MTOSS-1515] Added new ancillary importers
* [MTOSS-801] Use new auth-lib instead of common-security
* [MTOSS-1519] API V2 for Model, Provider, Plmn, SCGC
  * Warehouse/Plmn/Provider ID -> id instead of "warehouseId" for instance
  * Removed HATEOAS
  * Removed sub-objects from add/update DTOs
* [MTOSS-1474] Update Import AncillaryEquipment
* [MTOSS-1473] Add serviceId business logic to AncillaryEquipment 
* [MTOSS-1472] SIM/CPE/Ancillary APIs v2
  * Removed HATEOAS
  * Remove provider from CPE/Ancillary, already returned from equipmentModel (= Moved provider_id from equipment to simcard: CPE and ancillaries get their provider from their equipment model)
  * Remove modelName from CPE/Ancillary, already returned from equipmentModel
  * Dedicated DTO for SIM Creation, with changes on warehouse, inventory pool, provider and PLMN (no sub-object)
  * Dedicated DTO for CPE Creation, with changes on warehouse and model (no sub-object)
  * Dedicated DTO for Ancillary Creation, with changes on warehouse and model (no sub-object)
  * SIM/CPE/Ancillary DTOs: equipmentId -> id
  * New error serialization for CPE, Ancillary and SIM APIs (+Import,Job,Sequence,Enum APIs)
  * Add serviceId on Ancillary DTO

## 2.20.0
* [MTOSS-1457] Import async
* [MTOSS-1459] Fix rollback_onhold issue + fix pairedEquipmentCategory not populated in ancillaries

## 2.18.0
* [MTOSS-1392] Fix OpenAPI doc around generics (forCodeGeneration = true)
* [MTOSS-1343] Added api getCpeIdsIn
* [MTOSS-1398] equipment.nature now non null
* [MTOSS-1398] Capacity to change nature in SIM update endpoints
* [MTOSS-1376] ServiceId now unique on SIM based on Main/Additional nature 
* [MTOSS-1352] Export columns translation
* [MTOSS-1351] Error messages review
* [MTOSS-867] Added index on equipment.batch_number
* [MTOSS-1307] Remove allotment file storage on server
* [MTOSS-1329] Clean audit when status changes from REPACKAGING -> AVAILABLE
* [MTOSS-827] Admin API to configure SEQUENCE tables

## 2.17.0
* [MTOSS-1318] Added assignedDate to equipment entity
* [MTOSS-1295] Parent 4.0.14 + TraceId Logging using Sleuth
* [MTOSS-1266] Added export excel file api for all equipments
* [MTOSS-1265] Add activationDate to equipment entity
* [MTOSS-779] Admin controller common-job/common-job-web
* [MTOSS-860] Audit cleaning data job 
* [GI-843] Enums in base submodule, moved annotations packages, fix enum test

## 2.16.0
* [GI-824] New allotment types for B2B/B2C
* [GI-785] Set Esim while simcards batch
* [GI-790] Ancillary POST method does not set recyclable
* [GI-757] Reset externalNumber after the repackaging event
* [GI-519] Modified GenerateSimCardsResponseDTO reducing information returned 
* [GI-726] Added numberRecycles value to equipment entity
* [GI-699] STB Ancillary equipments Importer

## 2.15.0
* [GI-724] Review of RabbitMQ integration for notification-center messaging + removed dependency on websockets  
* [GI-715] Added search ancillary by externalNumber
* [GI-710] Add allotmentId in export file naming
* [GI-695] Add the possibility to change isEsim field in SIM endpoints
* [GI-710] Add allotmentId in export file naming 
* [GI-695] Add the possibility to change isEsim field in SIM endpoints 
* [GI-719] Aligned audit tables fields with Entities
* [GI-712] Make export allotment endpoint directly return the file
* [GI-693] Store checkDigit field during SIM Card Batch upload
* [GI-675] Better change state logs to investigate on SIM state change shenanigans
* [GI-673] Ancillary pairedEquipment can be changed in independent state

## 2.14.0
* [GI-656] Removed 'PREPAY' value from Network values - [JIRA](https://jira.itsf.io/browse/GI-656)
* [GI-651] Added client connectors for update SIM - [JIRA](https://jira.itsf.io/browse/GI-651)

## 2.13.0
* [GI-645] Optimize performances of EQM queries - [JIRA](https://jira.itsf.io/browse/GI-645)
* [GI-604] Support multiple ICCID sequence values - [JIRA](https://jira.itsf.io/browse/GI-604)
* [GI-605] Update the watcher logic to cater for multiple prefix & suffix values - [JIRA](https://jira.itsf.io/browse/GI-605)
* [GI-629] Using ubi8-derived base image with fontconfig - [JIRA](https://jira.itsf.io/browse/GI-629)
* [GI-621] Performance improvement for Monaco SIM import - [JIRA](https://jira.itsf.io/browse/GI-621)
* [GI-594] Push artifacts to Nexus, Spring Boot 2.6.7 - [JIRA](https://jira.itsf.io/browse/GI-594)
* [GI-612] Transport Key as String instead of Integer - [JIRA](https://jira.itsf.io/browse/GI-612)
* [GI-623] Rollback from: Fixed Gemalto SIMCard importer using 19 chars for SN - [JIRA](https://jira.itsf.io/browse/GI-623)
* [GI-602] Allotment should book SIMs for prepay case only - [JIRA](https://jira.itsf.io/browse/GI-602)
* [GI-567] Fixed ancillary model being incorrectly set in CPE importer - [JIRA](https://jira.itsf.io/browse/GI-567)
* [GI-565] Fixed documentation for importer controller - [JIRA](https://jira.itsf.io/browse/GI-565)
* [GI-568] Fix CPE import with ancillary - [JIRA](https://jira.itsf.io/browse/GI-568)
* [GI-566] Increase packId to 32 - [JIRA](https://jira.itsf.io/browse/GI-566)
* [GI-548] Split Process classes into subclasses - [JIRA](https://jira.itsf.io/browse/GI-548)

## 2.12.0
* [GI-473] Idempotency on update state endpoints - [JIRA](https://jira.itsf.io/browse/GI-473)
* [GI-534] Fixed ancillary change state to assign when no paired equipment - [JIRA](https://jira.itsf.io/browse/GI-534)
* [GI-484] Fixed Clock/ZoneId using UTC instead of system - [JIRA](https://jira.itsf.io/browse/GI-484)
* [GI-468] add StringToEquipmentEventsConverter on RestConfiguration - [JIRA](https://jira.itsf.io/browse/GI-468)
* [GI-474] Update Ancillary equipment process on independentMode  - [JIRA](https://jira.itsf.io/browse/GI-474)
* [GOSS-1401] add StringToEquipmentEventsConverter on RestConfiguration  - [JIRA](https://jira.itsf.io/browse/GOSS-1401)
* [GOSS-1968] Update swagger config - [JIRA](https://jira.itsf.io/browse/GOSS-1968)
* [GOSS-1346] Used commons-security instead of common-keycloak - [JIRA](https://jira.itsf.io/browse/GOSS-1346)
* [GOSS-1363] Batch delete endpoint, review SEQUENCEs usage - [JIRA](https://jira.itsf.io/browse/GOSS-1363)
* [GOSS-1370] Fixed SCGC update sequencePrefix/fixedPrefix - [JIRA](https://jira.itsf.io/browse/GOSS-1370)

## 2.11.0
* [GOSS-1008] Aligned:ww entities with liquidbase - [JIRA](https://jira.itsf.io/browse/GOSS-1008)
* [GISS-1315] Updated value access_type in equipment, equipment_model, provider entities - [JIRA](https://jira.itsf.io/browse/GOSS-1315)
* [GOSS-1331] Added fixedPrefix and sequencePrefix on simcard_generation - [JIRA](https://jira.itsf.io/browse/GOSS-1331)
* [GOSS-1329] Added type to simcard_generation, tweaks on eSim - [JIRA](https://jira.itsf.io/browse/GOSS-1329)
* [GOSS-1255] Added eSim and salt to simCard entity and updated importer - [JIRA](https://jira.itsf.io/browse/GOSS-1255)
* [GOSS-1295] Removed sql-connector dependency - [JIRA](https://jira.itsf.io/browse/GOSS-1295)
* [GOSS-928]  Added swagger doc yamls - [JIRA](https://jira.itsf.io/browse/GOSS-928)
* [GOSS-1260] Added JsonIgnoresProperty to dtos - [JIRA](https://jira.itsf.io/browse/GOSS-1260)
* [GOSS-1010] Migrations env var normalization - [JIRA](https://jira.itsf.io/browse/GOSS-1010)
* [GOSS-1276] Changed body setAction to Optional for CPE,SimCard, Ancillary Controllers - [JIRA](https://jira.itsf.io/browse/GOSS-1276)

## 2.10.0
* [GOSS-1228] Keycloak multi-realm support - [JIRA](https://jira.itsf.io/browse/GOSS-1228)
* [GOSS-1245] Added nullable column activity to table equipment - [JIRA](https://jira.itsf.io/browse/GOSS-1245)
* [GOSS-1245] Added MOBILE access type, endpoint to retrieve them all - [JIRA](https://jira.itsf.io/browse/GOSS-1245)
* [GOSS-902] Keycloak authorization model refactoring - [JIRA](https://jira.itsf.io/browse/GOSS-902)

## 2.9.0
* [GOSS-1100] Nettoyer la double colonne paired equipment id entre ancillary et equipment - [JIRA](https://jira.itsf.io/browse/GOSS-1100)
* Improved file watcher error handling
* [GOSS-1099] Specification tests on CPE/Ancillary - [JIRA](https://jira.itsf.io/browse/GOSS-1099)
* [GOSS-1106] Implement importer for DEL File - [JIRA](https://jira.itsf.io/browse/GOSS-1106)

## 2.8.0
* Fixed pairedEquipment id/category not displayed in ancillary equipment DTO
* Fixed ancillary search with paired equipment id
* [GOSS-1031] Removed required constraint on body for change state endpoints - [JIRA](https://jira.itsf.io/browse/GOSS-1031)
* [GOSS-968] Added puk1Code as a search criteria - [JIRA](https://jira.itsf.io/browse/GOSS-968)
* [GOSS-809] Common Importer API - [JIRA](https://jira.itsf.io/browse/GOSS-809)
* [GOSS-887] Added eir control on simcardProcess - [JIRA](https://jira.itsf.io/browse/GOSS-887)
* [GOSS-895] Missing variables logic in the SIM Manufacturing file header - [JIRA](https://jira.itsf.io/browse/GOSS-895)
* Fixed Gemalto SIMCard importer using 19 chars for SN
* [GOSS-898] CRUD endpoints for File Configuration/SCGC/Inventory pool - [JIRA](https://jira.itsf.io/browse/GOSS-898)
* [GOSS-420] Removed useless E2E tests - [JIRA](https://jira.itsf.io/browse/GOSS-420)
* [GOSS-899] Rename prepay_batch table to batch (and corresponding audit table) - [JIRA](https://jira.itsf.io/browse/GOSS-899)

## 2.7.0
* [GOSS-422] Refactor SIM Cards Idemia import
* [GOSS-903] Epic/Eir SIM Cards import with or without number column
* Fixed formatting of Epic MSIN/ICCID sequence on 5 instead of 6 characters
* [GOSS-889] Multiple MSIN sequences - [JIRA](https://jira.itsf.io/browse/GOSS-889)
* Fixed EIR sequence formatters for IMSI/ICCID generation
* [GOSS-884] Support for admin code from 16 to 32 characters - [JIRA](https://jira.itsf.io/browse/GOSS-884)
* [GOSS-352] Managing adding existing simcard exception - [JIRA](https://jira.itsf.io/browse/GOSS-352)
* [GOSS-820] Integration of NLS refacto - [JIRA](https://jira.itsf.io/browse/GOSS-820)
* [GOSS-281] Added allotmentId to simcardDTO - [JIRA](https://jira.itsf.io/browse/GOSS-281)
* Better handling of equipment models having same name but different categories
* [GOSS-793] CPE Importer compatibility with CSV and XLS files - [JIRA](https://jira.itsf.io/browse/GOSS-793)
* [GOSS-784] Route to get all equipment models - [JIRA](https://jira.itsf.io/browse/GOSS-784)

## 2.6.0
* [GOSS-708] Aligned datasource driver with guideliness - [JIRA](https://jira.itsf.io/browse/GOSS-708)
* [GOSS-779] Importer refactor to better support multiple implementations for single Entity - [JIRA](https://jira.itsf.io/browse/GOSS-779)
* [GOSS-715] SimCardGeneration updated algorithms for serial and Imsi number creation - [JIRA](https://jira.itsf.io/browse/GOSS-715)
* [GOSS-716] 'Allotment' process Update - [JIRA](https://jira.itsf.io/browse/GOSS-716)
* [GOSS-750] Fixed increment issue with batch and sim generation - [JIRA](https://jira.itsf.io/browse/GOSS-750)
* [GOSS-466] Import d'un nouveau fichier Equipement auxiliaire au format csv - [JIRA](https://jira.itsf.io/browse/GOSS-466)
* Updated local-epic.env + Fixes in InventoryPool API
* Sonarqube integration
* Jacoco report aggregation
* [GOSS-566] Use common Auditing entity and listener in EQM - [JIRA](https://jira.itsf.io/browse/GOSS-566)
* [GOSS-650] SIM search by number, tweaks on pairedEquipment - [JIRA](https://jira.itsf.io/browse/GOSS-650)
* Fixed container running as root
* [GOSS-463] RQA: added entity EquipmentModel - [JIRA](https://jira.itsf.io/browse/GOSS-463)
* [GOSS-463] Added entity Equipment_model for CPE and Ancilliary equipment - [JIRA](https://jira.itsf.io/browse/GOSS-463)
* [GOSS-591] Make accessType optionally updatable for SIMCards - [JIRA](https://jira.itsf.io/browse/GOSS-591)
* [GOSS-574] Performance improvement on count attached SIMCards for Batch - [JIRA](https://jira.itsf.io/browse/GOSS-405)
* [GOSS-405] Added integration tests - [JIRA](https://jira.itsf.io/browse/GOSS-405)
* [GOSS-465] Implement new DOCSIS specific importer and make cpe import process generic - [JIRA](https://jira.itsf.io/browse/GOSS-465)
* Recreated Dockerfile to fix CPE importer issue with libfreetype

## 2.5.0
* [GOSS-493] Bulk ChangeWare House & status -> prohibit status change if auxiliary equipment is not independent - [JIRA](https://jira.itsf.io/browse/GOSS-493)
* [GOSS-499] Refactored Epic API usage - [JIRA](https://jira.itsf.io/browse/GOSS-499)
* [GOSS-264] Removed Dockerfile to use jib plugin - [JIRA](https://jira.itsf.io/browse/GOSS-264)
* [GOSS-491] BugFix on PUT/PATCH for simcard,cpe,ancillary [JIA](https://jira.itsf.io/browse/GOSS-491)
* [GOSS-43] Added JSON support on logs - [JIRA](https://jira.itsf.io/browse/GOSS-43)
* [GOSS-397] Added batchNumber, externalNumber to put/patch parameters, added CPEPatchDTO - [JIRA](https://jira.itsf.io/browse/GOSS-397)
* [GOSS-390] Bulk Change Status & Warehouse CPE - changement par fichier csv -[JIRA](https://jira.itsf.io/browse/GOSS-390)
* [GOOS-441] Bulk Change Status & Warehouse AncillaryEquipement - changement par fichier csv -[JIRA](https://jira.itsf.io/browse/GOSS-441)
* [GOSS-436] Fixed SIMCards generation dead lock issue - [JIRA](https://jira.itsf.io/browse/GOSS-436)
* [GOSS-408] Removed unused ModelMapper dependencies - [JIRA](https://jira.itsf.io/browse/GOSS-408)
* [GOSS-398] Better message for lifecycle error on SIMCards - [JIRA](https://jira.itsf.io/browse/GOSS-398)
* [GOSS-395] Better exception handling when import file does not exist in Epic SIM import - [JIRA](https://jira.itsf.io/browse/GOSS-395)
* [GOSS-152] New endpoints to download/upload preprovisioning related files - [JIRA](https://jira.itsf.io/browse/GOSS-152)
* Added upload to Nexus part in Jenkinsfile
* Added missing warehouses_create_permission
* [GOSS-212] Allow equipments partial update (PATCH) and full update (PUT) - [JIRA](https://jira.itsf.io/browse/GOSS-212)
* [GOOS-211] Update serviceId and orderId according to equipment status -[JIRA](https://jira.itsf.io/browse/GOSS-211)

## 2.4.0
* [GOSS-233] Watcher for multiple SIMCards Import files return - [JIRA](https://jira.itsf.io/browse/GOSS-233)

## 2.3.0
* [GOSS-141] Optimization of Bulk ChangeWarehouseOrStatus on Equipments - [JIRA](https://jira.itsf.io/browse/GOSS-141)
* [GOSS-121] Parameterized notification for SIM Cards file return - [JIRA](https://jira.itsf.io/browse/GOSS-121)
* [GOSS-210] Fix importer behavior when another import is in progress - [JIRA](https://jira.itsf.io/browse/GOSS-210)
* [GOSS-214] Accept case-insensitive values for SIMCards enums AccessType, Status and Nature - [JIRA](https://jira.itsf.io/browse/GOSS-214)
* [GOSS-49] Fix Bug CPE importer - [JIRA](https://jira.itsf.io/browse/GOSS-49)
* [GOSS-200] Optimize SIMCards generation orchestration - [JIRA](https://jira.itsf.io/browse/GOSS-200)
