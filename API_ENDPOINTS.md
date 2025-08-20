# 🔧 Equipment Management System - API Endpoints

## 📖 Descripción
Sistema de gestión técnica de equipos (tarjetas SIM, CPE, equipos auxiliares, etc.) desarrollado para Monaco Telecom.

**Versión:** 2.29.0-SNAPSHOT  
**Descripción:** Technical repository application to manage equipments (simcard, cpe...)

---

## 🚀 Versiones de API Disponibles

### **V1 (Deprecated desde v2.21.0)**
- `private/auth/{resource}` 
- `api/v1/private/auth/{resource}`

### **V2 (Actual)**
- `api/v2/private/auth/{resource}`

### **Endpoints Públicos**
- `public/enums`
- `api/v1/public/enums`

---

## 📋 Endpoints por Recursos

### 🔌 **1. ANCILLARY EQUIPMENT (Equipos Auxiliares)**

#### **V1 (Deprecated)** - `/private/auth/ancillaryequipments` | `/api/v1/private/auth/ancillaryequipments`
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/` | Obtener todos los equipos auxiliares (paginado) |
| `GET` | `/{id}` | Obtener equipo auxiliar por ID interno |
| `GET` | `/serialnumber/{serialNumber}` | Obtener por número de serie |
| `GET` | `/pairedEquipment/{serialNumber}` | Obtener por número de serie del equipo emparejado |
| `GET` | `/search` | Buscar equipos auxiliares por criterios múltiples |
| `GET` | `/{id}/revisions` | Obtener revisiones por ID interno |
| `POST` | `/` | Agregar equipo auxiliar |
| `PUT` | `/{id}` | Actualizar equipo auxiliar por ID interno |
| `PATCH` | `/{id}` | Actualización parcial por ID interno |
| `PATCH` | `/{id}/{event}` | Cambiar estado del equipo auxiliar |
| `DELETE` | `/{id}` | Eliminar equipo auxiliar por ID interno |
| `GET` | `/export` | Exportar equipos auxiliares a Excel |

#### **V2** - `/api/v2/private/auth/ancillaryequipments`
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/` | Buscar equipos auxiliares por criterios múltiples |
| `GET` | `/{id}` | Obtener equipo auxiliar por ID interno |
| `GET` | `/serialnumber/{serialNumber}` | Obtener por número de serie |
| `GET` | `/pairedEquipment/{serialNumber}` | Obtener por número de serie del equipo emparejado |
| `GET` | `/{id}/revisions` | Obtener revisiones por ID interno |
| `POST` | `/` | Agregar equipo auxiliar |
| `PUT` | `/{id}` | Actualizar equipo auxiliar por ID interno |
| `PATCH` | `/{id}` | Actualización parcial por ID interno |
| `PATCH` | `/{id}/{event}` | Cambiar estado del equipo auxiliar |
| `DELETE` | `/{id}` | Eliminar equipo auxiliar por ID interno |
| `GET` | `/export` | Exportar equipos auxiliares a Excel |

---

### 🔧 **2. EQUIPMENT MODELS (Modelos de Equipos)**

#### **V1 (Deprecated)** - `/private/auth/equipmentModels` | `/api/v1/private/auth/equipmentModels`
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/` | Obtener modelos de equipos (paginado) |
| `GET` | `/{id}` | Buscar modelo de equipo por ID |
| `POST` | `/` | Crear modelo de equipo |
| `PUT` | `/{id}` | Actualizar modelo de equipo por ID |
| `DELETE` | `/{id}` | Eliminar modelo de equipo por ID |

#### **V2** - `/api/v2/private/auth/equipmentModels`
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/` | Buscar modelos de equipos por múltiples criterios |
| `GET` | `/{id}` | Buscar modelo de equipo por ID |
| `POST` | `/` | Crear modelo de equipo |
| `PUT` | `/{id}` | Actualizar modelo de equipo por ID |
| `DELETE` | `/{id}` | Eliminar modelo de equipo por ID |

---

### 📡 **3. PLMN (Public Land Mobile Network)**

#### **V1 (Deprecated)** - `/private/auth/plmns` | `/api/v1/private/auth/plmns`
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/` | Obtener todos los PLMNs (paginado) |
| `GET` | `/{plmnId}` | Buscar PLMN por ID |
| `GET` | `/search` | Buscar PLMN por código, prefijo de rangos parcial |
| `POST` | `/` | Agregar PLMN |
| `PUT` | `/{plmnId}` | Actualizar PLMN |
| `DELETE` | `/{plmnId}` | Eliminar PLMN por ID |

#### **V2** - `/api/v2/private/auth/plmns`
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/` | Buscar PLMN por múltiples criterios |
| `GET` | `/{plmnId}` | Buscar PLMN por ID |
| `POST` | `/` | Agregar PLMN |
| `PUT` | `/{id}` | Actualizar PLMN |
| `DELETE` | `/{id}` | Eliminar PLMN por ID |

---

### 🏢 **4. PROVIDERS (Proveedores)**

#### **V1 (Deprecated)** - `/private/auth/providers` | `/api/v1/private/auth/providers`
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/` | Obtener todos los proveedores (paginado) |
| `GET` | `/{providerId}` | Buscar proveedor por ID |
| `GET` | `/search` | Buscar proveedor por código, tipo de acceso, etc. |
| `POST` | `/` | Agregar proveedor |
| `PUT` | `/{providerId}` | Actualizar proveedor |
| `DELETE` | `/{providerId}` | Eliminar proveedor por ID |

#### **V2** - `/api/v2/private/auth/providers`
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/` | Buscar proveedor por múltiples criterios |
| `GET` | `/{providerId}` | Buscar proveedor por ID |
| `POST` | `/` | Agregar proveedor |
| `PUT` | `/{providerId}` | Actualizar proveedor |
| `DELETE` | `/{providerId}` | Eliminar proveedor por ID |

---

### 🏠 **5. WAREHOUSES (Almacenes)**

#### **V1 (Deprecated)** - `/private/auth/warehouses` | `/api/v1/private/auth/warehouses`
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/` | Obtener todos los almacenes (paginado) |
| `GET` | `/{warehouseId}` | Buscar almacén por ID |
| `GET` | `/search` | Buscar almacén por múltiples criterios |
| `POST` | `/` | Agregar almacén |
| `PUT` | `/{warehouseId}` | Actualizar almacén |
| `DELETE` | `/{warehouseId}` | Eliminar almacén por ID |

#### **V2** - `/api/v2/private/auth/warehouses`
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/` | Buscar almacén por múltiples criterios |
| `GET` | `/{warehouseId}` | Buscar almacén por ID |
| `POST` | `/` | Agregar almacén |
| `PUT` | `/{warehouseId}` | Actualizar almacén |
| `DELETE` | `/{warehouseId}` | Eliminar almacén por ID |

---

### 💳 **6. SIM CARDS (Tarjetas SIM)**

#### **V1 (Deprecated)** - `/private/auth/simcards` | `/api/v1/private/auth/simcards`
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/` | Obtener todas las tarjetas SIM (paginado) |
| `GET` | `/{id}` | Obtener tarjeta SIM por ID |
| `GET` | `/search` | Buscar tarjetas SIM por criterios múltiples |
| `GET` | `/{id}/revisions` | Obtener revisiones por ID |
| `POST` | `/` | Agregar tarjeta SIM |
| `POST` | `/import` | Importar tarjetas SIM |
| `PUT` | `/{id}` | Actualizar tarjeta SIM |
| `PATCH` | `/{id}` | Actualización parcial |
| `PATCH` | `/{id}/{event}` | Cambiar estado de la tarjeta SIM |
| `DELETE` | `/{id}` | Eliminar tarjeta SIM |
| `GET` | `/export` | Exportar tarjetas SIM a Excel |

#### **V2** - `/api/v2/private/auth/simcards`
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/` | Buscar tarjetas SIM por criterios múltiples |
| `GET` | `/{id}` | Obtener tarjeta SIM por ID |
| `GET` | `/{id}/revisions` | Obtener revisiones por ID |
| `POST` | `/` | Agregar tarjeta SIM |
| `POST` | `/import` | Importar tarjetas SIM |
| `PUT` | `/{id}` | Actualizar tarjeta SIM |
| `PATCH` | `/{id}` | Actualización parcial |
| `PATCH` | `/{id}/{event}` | Cambiar estado de la tarjeta SIM |
| `DELETE` | `/{id}` | Eliminar tarjeta SIM |
| `GET` | `/export` | Exportar tarjetas SIM a Excel |

---

### 📦 **7. CPE (Customer Premises Equipment)**

#### **V1 (Deprecated)** - `/private/auth/cpes` | `/api/v1/private/auth/cpes`
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/` | Obtener todos los CPEs (paginado) |
| `GET` | `/{id}` | Obtener CPE por ID |
| `GET` | `/search` | Buscar CPEs por criterios múltiples |
| `GET` | `/{id}/revisions` | Obtener revisiones por ID |
| `POST` | `/` | Agregar CPE |
| `PUT` | `/{id}` | Actualizar CPE |
| `PATCH` | `/{id}` | Actualización parcial |
| `PATCH` | `/{id}/{event}` | Cambiar estado del CPE |
| `DELETE` | `/{id}` | Eliminar CPE |
| `GET` | `/export` | Exportar CPEs a Excel |

#### **V2** - `/api/v2/private/auth/cpes`
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/` | Buscar CPEs por criterios múltiples |
| `GET` | `/{id}` | Obtener CPE por ID |
| `GET` | `/{id}/revisions` | Obtener revisiones por ID |
| `POST` | `/` | Agregar CPE |
| `PUT` | `/{id}` | Actualizar CPE |
| `PATCH` | `/{id}` | Actualización parcial |
| `PATCH` | `/{id}/{event}` | Cambiar estado del CPE |
| `DELETE` | `/{id}` | Eliminar CPE |
| `GET` | `/export` | Exportar CPEs a Excel |

---

### 📊 **8. BATCH MANAGEMENT (Gestión de Lotes)**

#### **V1 (Deprecated)** - `/private/auth/batch` | `/api/v1/private/auth/batch`
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/` | Obtener todos los lotes (paginado) |
| `GET` | `/{id}` | Obtener lote por ID |
| `GET` | `/search` | Buscar lotes por criterios |
| `POST` | `/` | Crear lote |
| `POST` | `/generate` | Generar tarjetas SIM |
| `GET` | `/{id}/download` | Descargar archivo de lote |

#### **V2** - `/api/v2/private/auth/batch`
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/` | Buscar lotes por criterios múltiples |
| `GET` | `/{id}` | Obtener lote por ID |
| `POST` | `/` | Crear lote |
| `POST` | `/generate` | Generar tarjetas SIM |
| `GET` | `/{id}/download` | Descargar archivo de lote |

---

### 📈 **9. STATISTICS (Estadísticas)**

#### **Dashboard** - `/private/auth/equipmentsDashboard` | `/api/v1/private/auth/equipmentsDashboard`
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/` | Obtener estadísticas del dashboard de equipos |

---

### 🔧 **10. CONFIGURATION & UTILITIES**

#### **Enums (Públicos)** - `/public/enums` | `/api/v1/public/enums`
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/accesstype` | Obtener tipos de acceso |
| `GET` | `/activity` | Obtener valores de actividad |
| `GET` | `/allotmenttype` | Obtener tipos de asignación |

#### **Inventory Pool** - `/private/auth/inventorypool` | `/api/v1/private/auth/inventorypool`
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/` | Gestión de pools de inventario |
| `POST` | `/` | Crear pool de inventario |
| `PUT` | `/{id}` | Actualizar pool de inventario |
| `DELETE` | `/{id}` | Eliminar pool de inventario |

#### **Allotment** - `/private/auth/allotments` | `/api/v1/private/auth/allotments`
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/` | Gestión de asignaciones |
| `POST` | `/` | Crear asignación |
| `PUT` | `/{id}` | Actualizar asignación |
| `DELETE` | `/{id}` | Eliminar asignación |

#### **Sequence** - `/private/auth/sequences` | `/api/v1/private/auth/sequences`
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/` | Gestión de secuencias |
| `POST` | `/` | Crear secuencia |
| `PUT` | `/{id}` | Actualizar secuencia |
| `DELETE` | `/{id}` | Eliminar secuencia |

#### **File Configuration** - `/private/auth/fileConfiguration` | `/api/v1/private/auth/fileConfiguration`
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/` | Obtener configuraciones de archivos |
| `POST` | `/` | Crear configuración de archivo |
| `PUT` | `/{id}` | Actualizar configuración de archivo |
| `DELETE` | `/{id}` | Eliminar configuración de archivo |

#### **eSIM Notifications** - `/private/auth/esimNotifications` | `/api/v1/private/auth/esimNotifications`
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/` | Obtener notificaciones eSIM |
| `POST` | `/` | Crear notificación eSIM |
| `PUT` | `/{id}` | Actualizar notificación eSIM |

---

## 🔒 **Autenticación y Autorización**

- **Endpoints Privados**: Requieren autenticación
  - `/private/auth/*`
  - `/api/v*/private/auth/*`
  
- **Endpoints Públicos**: No requieren autenticación
  - `/public/*`
  - `/api/v*/public/*`

---

## 📄 **Documentación y Estándares**

- **OpenAPI 3.0**: Documentación con Swagger
- **Paginación**: Soporte para paginación en endpoints de listado
- **Versionado**: Sistema de versionado con V1 (deprecated) y V2 (actual)
- **CORS**: Habilitado para todos los controladores
- **Validación**: Validación de entrada con Bean Validation
- **Auditoria**: Sistema de revisiones para tracking de cambios

---

## 🚀 **Códigos de Respuesta HTTP**

| Código | Descripción |
|--------|-------------|
| `200` | OK - Operación exitosa |
| `201` | Created - Recurso creado exitosamente |
| `204` | No Content - Operación exitosa sin contenido |
| `400` | Bad Request - Error en los parámetros |
| `404` | Not Found - Recurso no encontrado |
| `500` | Internal Server Error - Error interno del servidor |

---

## 📦 **Formatos de Datos**

- **Input**: JSON
- **Output**: JSON, Excel (para exports)
- **Paginación**: HATEOAS (V1) / Page (V2)
- **Fechas**: ISO 8601
- **Encoding**: UTF-8

---

## 🔄 **Estados y Eventos**

El sistema maneja diferentes estados para los equipos y soporta transiciones de estado mediante eventos:

- **Estados**: ACTIVE, INACTIVE, PENDING, etc.
- **Eventos**: CREATE, UPDATE, DELETE, ACTIVATE, DEACTIVATE, etc.
- **Transiciones**: Validadas según reglas de negocio

---

## 🏗️ **Arquitectura del Proyecto**

```
tecrep-equipments-management/
├── tecrep-equipments-management-base/
├── tecrep-equipments-management-client/
├── tecrep-equipments-management-domain/
├── tecrep-equipments-management-dto/
├── tecrep-equipments-management-process/
├── tecrep-equipments-management-report/
├── tecrep-equipments-management-service/
└── tecrep-equipments-management-webservice/  # Controladores REST
```

---
### ℹ️ Cómo funciona la importación de Equipos Auxiliares

Cuando se llama al endpoint `POST /api/v2/private/auth/ancillaryequipments/import`, se activa el flujo asíncrono de importación de equipamiento auxiliar:

1. **scheduleImportJob (sincrónico)**
   - Valida que exista un importador para el formato indicado (`getImporterForFormat(format)`).  
   - Crea un registro de `AncillaryImportJob` en estado `PENDING`, con:
     - Nombre original del archivo
     - Formato
     - Bandera `continueOnError`
     - Timestamp de inicio
   - Guarda el archivo CSV en disco (dentro de `<resultBasePath>/inputs/`).
   - Persiste el `AncillaryImportJob` (ya con la ruta de archivo).
   - Lanza la tarea asíncrona `launchImportJobAsync(jobId)` para procesar el CSV en segundo plano.
   - Devuelve inmediatamente `{ "jobId": <id>, "status": "PENDING" }` con HTTP 202.

2. **executeImportJob (asíncrono)**
   - Recupera el registro `AncillaryImportJob` por `jobId`. Si no existe, registra error y termina.
   - Cambia estado a `PROCESSING` y actualiza el timestamp de inicio.
   - Lee el CSV usando Apache Commons CSV (`CSVParser`), exige cabeceras obligatorias (`BOX_SN`, `POD_SN`, `NODE_MODEL`). Si faltan, marca el job como `FAILED`.
   - Para cada fila (incrementa `totalLines`):
     1. Valida que `BOX_SN`, `POD_SN` y `NODE_MODEL` no estén vacíos.
     2. Verifica duplicados (`ancillaryRepository.existsBySerialNumber()`, `existsByMacAddress()`); si hay duplicado:
        - Inserta un registro en `ancillary_import_error` con línea, mensaje y contenido original.
        - Si `continueOnError` es `false`, interrumpe el loop; de lo contrario, continúa.
     3. Busca el `EquipmentModel` por nombre; si no existe, registra error igual que en duplicados.
     4. Carga el `Warehouse` por defecto (`defaultWarehouseId`); si no existe, marca job como `FAILED`.
     5. Construye un `AncillaryEquipment` nuevo:
        - Serial, MAC, almacén, modelo, `equipmentName` (desde `equipmentModel.getEquipmentName()`), `accessType`, `recyclable=true`, `numberRecycles=0`, `status=INSTORE`
        - Guarda la entidad en base de datos.
     6. Si todo ok, incrementa `successfulLines` y agrega la fila exitosa a la lista de “successfulRows” (para el CSV de resultados).
   - Al terminar el loop:
     - Persiste todos los `AncillaryImportError` recolectados en `ancillary_import_error`.
     - Genera un CSV de resultados (`writeCsvResult(...)`) en `<resultBasePath>`, con:
       - Filas “SUCCESS” (serial, MAC, warehouseId, modelId)
       - Filas “ERROR” (número de línea, mensaje, línea original)
     - Actualiza en `AncillaryImportJob`:
       - `totalLines`, `successfulLines`, `errorCount`, `finishedAt`
       - Cambia estado a:
         - `SUCCESS` (si no hubo errores)
         - `SUCCESS_WITH_ERRORS` (si hubo éxito y errores parciales)
         - `FAILED` (si no se insertó ninguna fila o hubo error fatal)
     - Guarda nuevamente el job con la ruta del CSV de resultados (“resultFilePath”).

3. **getImportJobStatus (sincrónico)**
   - Cuando el cliente llama `GET /api/v2/private/auth/ancillaryequipments/import/status/{jobId}`, recupera el `AncillaryImportJob`:
     - Si no existe, devuelve HTTP 404.
     - Si existe, retorna:
       ```json
       {
         "jobId": <id>,
         "status": "<PENDING|PROCESSING|SUCCESS|SUCCESS_WITH_ERRORS|FAILED>",
         "totalLines": <int>,
         "successfulLines": <int>,
         "errorCount": <int>,
         "startedAt": "<timestamp>",
         "finishedAt": "<timestamp|null>",
         "resultFilePath": "<ruta_al_csv_resultante|null>"
       }
       ```

De este modo, el cliente siempre recibe de inmediato un `jobId`. Luego puede consultar periódicamente `/import/status/{jobId}` para ver el avance, obtener conteos y, cuando finalice, descargar el CSV con el desglose de filas exitosas y errores.  


### ⚖️ **5. STANDARD LOADS**

#### **V2** - `/api/v2/private/auth/standard-loads`
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/` | Listar cargas estándar (paginado) |
| `GET` | `/{id}` | Obtener carga estándar por ID |
| `POST` | `/` | Crear carga estándar |
| `PUT` | `/{id}` | Actualizar carga estándar |
