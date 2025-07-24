package mc.monacotelecom.tecrep.equipments.client.connector;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.common.restclient.AbstractConnector;
import mc.monacotelecom.common.restclient.AuthenticationManager;
import mc.monacotelecom.tecrep.equipments.client.connector.dto.AncillaryPagedResources;
import mc.monacotelecom.tecrep.equipments.client.connector.dto.PageOfObjects;
import mc.monacotelecom.tecrep.equipments.client.connector.dto.PlmnPagedResources;
import mc.monacotelecom.tecrep.equipments.client.connector.dto.SimCardPagedResources;
import mc.monacotelecom.tecrep.equipments.dto.AncillaryEquipmentDTO;
import mc.monacotelecom.tecrep.equipments.dto.CPEDTO;
import mc.monacotelecom.tecrep.equipments.dto.PlmnDTO;
import mc.monacotelecom.tecrep.equipments.dto.SimCardDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.AncillaryChangeStateDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.ChangeStatusDto;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchAncillaryEquipmentDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchCpeDto;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchPlmnDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.search.SearchSimCardDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.update.UpdateAncillaryEquipmentDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.update.UpdateSimCardDTO;
import mc.monacotelecom.tecrep.equipments.dto.request.update.v2.UpdateAncillaryEquipmentDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.request.update.v2.UpdateSimCardDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.v2.AncillaryEquipmentDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.v2.CPEDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.v2.PlmnDTOV2;
import mc.monacotelecom.tecrep.equipments.dto.v2.SimCardDTOV2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

@Slf4j
public class EquipmentsManagementConnector extends AbstractConnector {

    private final ObjectMapper mapper;

    public EquipmentsManagementConnector(final String baseUrl,
                                         final RestTemplate restTemplate,
                                         final AuthenticationManager authenticationManager) {
        super(baseUrl, restTemplate, authenticationManager);
        this.mapper = new ObjectMapper();
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO getSimcardById(Long id) {
        URI uri = this.getUri("/private/auth/simcards/" + id);
        final var method = HttpMethod.GET;
        log.info("Get SimCard Information with ID {} by {} on URL={}", id, method, uri);
        SimCardDTO simCardDTO = new SimCardDTO();
        if (id != null) {
            HttpEntity<Void> httpEntity = new HttpEntity<>(authHeaders());
            return exchange(uri, method, httpEntity, SimCardDTO.class);
        }
        return simCardDTO;
    }

    public SimCardDTOV2 getSimcardByIdV2(@NonNull Long id) {
        URI uri = this.getUri("/api/v2/private/auth/simcards/" + id);
        final var method = HttpMethod.GET;
        log.info("Get SIM Card Information with ID {} by {} on URL={}", id, method, uri);
        return exchange(uri, method, new HttpEntity<>(authHeaders()), SimCardDTOV2.class);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO getSimcardByImsi(String imsi) {
        URI url = this.getUri("/private/auth/simcards/imsi/" + imsi);
        final var method = HttpMethod.GET;
        log.info("Get SimCard Information with IMSI {} by {} on URL={}", imsi, method, url);
        return exchange(url, method, new HttpEntity<>(authHeaders()), SimCardDTO.class);
    }

    public SimCardDTOV2 getSimcardByImsiV2(@NonNull String imsi) {
        URI url = this.getUri("/api/v2/private/auth/simcards/imsi/" + imsi);
        final var method = HttpMethod.GET;
        log.info("Get SIM Card Information with IMSI {} by {} on URL={}", imsi, method, url);
        return exchange(url, method, new HttpEntity<>(authHeaders()), SimCardDTOV2.class);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO getSimcardByIccid(String iccid) {
        URI url = this.getUri("/private/auth", "/simcards/iccid/" + iccid);
        final var method = HttpMethod.GET;
        log.info("Get SimCard Information with ICCID {} by {} on URL={}", iccid, method, url);
        return exchange(url, method, new HttpEntity<>(authHeaders()), SimCardDTO.class);
    }

    public SimCardDTOV2 getSimcardByIccidV2(@NonNull String iccid) {
        URI url = this.getUri("/api/v2/private/auth/simcards/iccid/" + iccid);
        final var method = HttpMethod.GET;
        log.info("Get SIM Card Information with ICCID {} by {} on URL={}", iccid, method, url);
        return exchange(url, method, new HttpEntity<>(authHeaders()), SimCardDTOV2.class);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public CPEDTO getCpeById(Long id) {
        URI uri = this.getUri("/private/auth/cpes/" + id);
        final var method = HttpMethod.GET;
        log.info("Get CPE Information with ID {} by {} from URL={}", id, method, uri);
        CPEDTO cpeDTO = new CPEDTO();
        if (id != null) {
            HttpEntity<Void> httpEntity = new HttpEntity<>(authHeaders());
            return exchange(uri, method, httpEntity, CPEDTO.class);
        }
        return cpeDTO;
    }

    public CPEDTOV2 getCpeByIdV2(Long id) {
        URI uri = this.getUri("/api/v2/private/auth/cpes/" + id);
        final var method = HttpMethod.GET;
        log.info("Get CPE Information with ID {} by {} from URL={}", id, method, uri);
        return exchange(uri, method, new HttpEntity<>(authHeaders()), CPEDTOV2.class);
    }

    public PageOfObjects<CPEDTOV2> searchCpe(SearchCpeDto searchCpeDto) {
        final MultiValueMap<String, String> params = convert(searchCpeDto);
        URI uri = this.getUri("/api/v2/private/auth/cpes");
        uri = UriComponentsBuilder.fromUriString(uri.toString()).queryParams(params).build().toUri();
        final var method = HttpMethod.GET;
        log.info("Search CPEs with DTO {} by {} on URL={}", params, method, uri);
        return exchange(uri, method, new HttpEntity<>(authHeaders()), new ParameterizedTypeReference<>() {});
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public List<SimCardDTO> searchSimcard(final SearchSimCardDTO dto) {
        final MultiValueMap<String, String> params = convert(dto);
        URI uri = this.getUri("/private/auth/simcards/search");
        uri = UriComponentsBuilder.fromUriString(uri.toString()).queryParams(params).build().toUri();
        final var method = HttpMethod.GET;
        log.info("Search Simcards with DTO {} by {} on URL={}", params, method, uri);
        HttpEntity<Void> httpEntity = new HttpEntity<>(authHeaders());
        SimCardPagedResources response = exchange(uri, method, httpEntity, SimCardPagedResources.class);
        return response.getEmbedded() != null ? response.getEmbedded().getSimCards() : null;
    }

    public PageOfObjects<SimCardDTOV2> searchSimcardsV2(final SearchSimCardDTO dto) throws IOException {
        final MultiValueMap<String, String> params = convert(dto);
        URI uri = this.getUri("/api/v2/private/auth/simcards");
        uri = UriComponentsBuilder.fromUriString(uri.toString()).queryParams(params).build().toUri();
        final var method = HttpMethod.GET;
        log.info("Search SIM Cards with DTO {} by {} on URL={}", params, method, uri);
        return mapper.readValue((JsonParser) exchange(uri, method, new HttpEntity<>(authHeaders()), new ParameterizedTypeReference<>() {}), new TypeReference<PageOfObjects<SimCardDTOV2>>() {}) ;
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public List<PlmnDTO> searchPlmn(MultiValueMap<String, String> matrixVars) {
        URI uri = this.getUri("/private/auth/plmns/search");
        uri = UriComponentsBuilder.fromUriString(uri.toString()).queryParams(matrixVars).build().toUri();
        final var method = HttpMethod.GET;
        log.info("Search plmns with DTO {} by {} on URL={}", matrixVars, method, uri);
        HttpEntity<Void> httpEntity = new HttpEntity<>(authHeaders());
        PlmnPagedResources response = exchange(uri, method, httpEntity, PlmnPagedResources.class);
        return response.getEmbedded() != null ? response.getEmbedded().getPlmns() : null;
    }

    public PageOfObjects<PlmnDTOV2> searchPlmnV2(final SearchPlmnDTO dto) {
        final MultiValueMap<String, String> params = convert(dto);
        URI uri = this.getUri("/api/v2/private/auth/plmns");
        uri = UriComponentsBuilder.fromUriString(uri.toString()).queryParams(params).build().toUri();
        final var method = HttpMethod.GET;
        log.info("Search PLMNs with DTO {} by {} on URL={}", params, method, uri);
        return exchange(uri, method, new HttpEntity<>(authHeaders()), new ParameterizedTypeReference<>() {});
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public PlmnDTO getPlmnById(Long id) {
        URI uri = this.getUri("/private/auth/plmns/" + id);
        final var method = HttpMethod.GET;
        log.info("Get PLMN Information with ID {} by {} from URL={}", id, method, uri);
        PlmnDTO plmnDTO = new PlmnDTO();
        if (id != null) {
            HttpEntity<Void> httpEntity = new HttpEntity<>(authHeaders());
            return exchange(uri, method, httpEntity, PlmnDTO.class);
        }
        return plmnDTO;
    }

    public PlmnDTOV2 getPlmnByIdV2(Long id) {
        URI uri = this.getUri("/api/v2/private/auth/plmns/" + id);
        final var method = HttpMethod.GET;
        log.info("Get PLMN Information with ID {} by {} from URL={}", id, method, uri);
        return exchange(uri, method, new HttpEntity<>(authHeaders()), PlmnDTOV2.class);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public List<AncillaryEquipmentDTO> searchAncillaryEquipment(MultiValueMap<String, String> matrixVars) {
        URI uri = this.getUri("/private/auth/ancillaryequipments/search");
        uri = UriComponentsBuilder.fromUriString(uri.toString()).queryParams(matrixVars).build().toUri();
        final var method = HttpMethod.GET;
        log.info("Search Ancillary Equipments with DTO {} by {} on URL={}", matrixVars, method, uri);
        HttpEntity<Void> httpEntity = new HttpEntity<>(authHeaders());
        AncillaryPagedResources response = exchange(uri, method, httpEntity, AncillaryPagedResources.class);
        return response.getEmbedded() != null ? response.getEmbedded().getAncillaryEquipments() : null;
    }

    public PageOfObjects<AncillaryEquipmentDTOV2> searchAncillaryEquipmentV2(SearchAncillaryEquipmentDTO dto) throws IOException {
        final MultiValueMap<String, String> params = convert(dto);
        URI uri = this.getUri("api/v2/private/auth/ancillaryequipments/search");
        uri = UriComponentsBuilder.fromUriString(uri.toString()).queryParams(params).build().toUri();
        final var method = HttpMethod.GET;
        log.info("Search Ancillary Equipments with DTO {} by {} on URL={}", params, method, uri);
        return mapper.readValue((JsonParser) exchange(uri, method, new HttpEntity<>(authHeaders()), new ParameterizedTypeReference<>() {}), new TypeReference<PageOfObjects<AncillaryEquipmentDTOV2>>() {}) ;
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public AncillaryEquipmentDTO getAncillaryEquipmentById(Long id) {
        URI uri = this.getUri("/private/auth/ancillaryequipments/" + id);
        final var method = HttpMethod.GET;
        log.info("Get AncillaryEquipment Information with ID {} by {} on URL={}", id, method, uri);
        AncillaryEquipmentDTO ancillaryEquipmentDTO = new AncillaryEquipmentDTO();
        if (id != null) {
            HttpEntity<Void> httpEntity = new HttpEntity<>(authHeaders());
            return exchange(uri, method, httpEntity, AncillaryEquipmentDTO.class);
        }
        return ancillaryEquipmentDTO;
    }

    public AncillaryEquipmentDTOV2 getAncillaryEquipmentByIdV2(Long id) {
        URI uri = this.getUri("/api/v2/private/auth/ancillaryequipments/" + id);
        final var method = HttpMethod.GET;
        log.info("Get AncillaryEquipment Information with ID {} by {} on URL={}", id, method, uri);
        return exchange(uri, method, new HttpEntity<>(authHeaders()), AncillaryEquipmentDTOV2.class);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public AncillaryEquipmentDTO getAncillaryEquipmentBySerialNumber(String serialNumber) {
        URI url = this.getUri("/private/auth", "/ancillaryequipments/serialnumber/" + serialNumber);
        final var method = HttpMethod.GET;
        log.info("Get Ancillary Equipment with Serial Number {} by {} from URL={}", serialNumber, method, url);
        return exchange(url, method, new HttpEntity<>(authHeaders()), AncillaryEquipmentDTO.class);
    }

    public AncillaryEquipmentDTOV2 getAncillaryEquipmentBySerialNumberV2(String serialNumber) {
        URI url = this.getUri("/api/v2/private/auth/ancillaryequipments/serialnumber/" + serialNumber);
        final var method = HttpMethod.GET;
        log.info("Get Ancillary Equipment with Serial Number {} by {} from URL={}", serialNumber, method, url);
        return exchange(url, method, new HttpEntity<>(authHeaders()), AncillaryEquipmentDTOV2.class);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO changeState(Long simcardId, String event, ChangeStatusDto changeStatusDto) {
        URI url = this.getUri("/private/auth", "/simcards/" + simcardId + "/" + event);
        final var method = HttpMethod.PATCH;
        log.info("Change state of SIM Card with ID {} and DTO {} by {} from URL={}", simcardId, changeStatusDto, method, url);
        HttpEntity<ChangeStatusDto> requestEntity = new HttpEntity<>(changeStatusDto, patchHeader());
        return exchange(url, method, requestEntity, SimCardDTO.class);
    }

    public SimCardDTOV2 changeStateV2(Long simcardId, String event, ChangeStatusDto changeStatusDto) {
        URI url = this.getUri("/api/v2/private/auth/simcards/" + simcardId + "/" + event);
        final var method = HttpMethod.PATCH;
        log.info("Change state of SIM Card with ID {} and DTO {} by {} from URL={}", simcardId, changeStatusDto, method, url);
        HttpEntity<ChangeStatusDto> requestEntity = new HttpEntity<>(changeStatusDto, patchHeader());
        return exchange(url, method, requestEntity, SimCardDTOV2.class);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public AncillaryEquipmentDTO changeAncillaryEquipmentState(Long ancillaryEquipmentId, String event, AncillaryChangeStateDTO changeStateDTO) {
        URI url = this.getUri("/private/auth", "/ancillaryequipments/" + ancillaryEquipmentId + "/" + event);
        final var method = HttpMethod.PATCH;
        log.info("Change state of Ancillary Equipment with ID {} and DTO {} by {} from URL={}", ancillaryEquipmentId, changeStateDTO, method, url);
        HttpEntity<AncillaryChangeStateDTO> requestEntity = new HttpEntity<>(changeStateDTO, patchHeader());
        return exchange(url, method, requestEntity, AncillaryEquipmentDTO.class);
    }

    public AncillaryEquipmentDTOV2 changeAncillaryEquipmentStateV2(Long ancillaryEquipmentId, String event, AncillaryChangeStateDTO changeStateDTO) {
        URI url = this.getUri("/api/v2/private/auth/ancillaryequipments/" + ancillaryEquipmentId + "/" + event);
        final var method = HttpMethod.PATCH;
        log.info("Change state of Ancillary Equipment with ID {} and DTO {} by {} from URL={}", ancillaryEquipmentId, changeStateDTO, method, url);
        return exchange(url, method, new HttpEntity<>(changeStateDTO, patchHeader()), AncillaryEquipmentDTOV2.class);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public AncillaryEquipmentDTO updateAncillaryEquipment(Long ancillaryEquipmentId, UpdateAncillaryEquipmentDTO updateAncillaryEquipmentDTO) {
        URI url = this.getUri("/private/auth", "/ancillaryequipments/" + ancillaryEquipmentId);
        final var method = HttpMethod.PUT;
        log.info("Update ancillary equipment with ID {} and DTO {} by {} from URL={}", ancillaryEquipmentId, updateAncillaryEquipmentDTO, method, url);
        return exchange(url, method, new HttpEntity<>(updateAncillaryEquipmentDTO, authHeaders()), AncillaryEquipmentDTO.class);
    }

    public AncillaryEquipmentDTOV2 updateAncillaryEquipmentV2(Long ancillaryEquipmentId, UpdateAncillaryEquipmentDTOV2 updateAncillaryEquipmentDTO) {
        URI url = this.getUri("/api/v2/private/auth/ancillaryequipments/" + ancillaryEquipmentId);
        final var method = HttpMethod.PUT;
        log.info("Update ancillary equipment with ID {} and DTO {} by {} from URL={}", ancillaryEquipmentId, updateAncillaryEquipmentDTO, method, url);
        return exchange(url, method, new HttpEntity<>(updateAncillaryEquipmentDTO, authHeaders()), AncillaryEquipmentDTOV2.class);
    }

    /**
     * Change state of a CPE
     *
     * @param cpeId           CPE ID
     * @param event           Event to apply
     * @param changeStatusDto Details on the action to perform
     * @return Updated CPE DTO
     */
    @Deprecated(since = "2.21.0", forRemoval = true)
    public CPEDTO changeCpeState(Long cpeId, String event, ChangeStatusDto changeStatusDto) {
        final URI url = this.getUri("/private/auth/cpes/" + cpeId + "/" + event);
        final var method = HttpMethod.PATCH;
        log.info("Change state of CPE with ID {} and DTO {} by {} from URL={}", cpeId, changeStatusDto, method, url);
        final HttpEntity<ChangeStatusDto> requestEntity = new HttpEntity<>(changeStatusDto, patchHeader());
        return exchange(url, method, requestEntity, CPEDTO.class);
    }

    public CPEDTOV2 changeCpeStateV2(Long cpeId, String event, ChangeStatusDto changeStatusDto) {
        final URI url = this.getUri("/api/v2/private/auth/cpes/" + cpeId + "/" + event);
        final var method = HttpMethod.PATCH;
        log.info("Change state of CPE with ID {} and DTO {} by {} from URL={}", cpeId, changeStatusDto, method, url);
        return exchange(url, method, new HttpEntity<>(changeStatusDto, patchHeader()), CPEDTOV2.class);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO fullUpdateSimCardById(Long simCardId, UpdateSimCardDTO updateSimCardDTO) {
        URI url = this.getUri("/private/auth/simcards/" + simCardId);
        final var method = HttpMethod.PUT;
        log.info("Full update SIM card with ID {} and DTO {} by {} from URL={}", simCardId, updateSimCardDTO, method, url);
        return performUpdateSIM(updateSimCardDTO, url, method);
    }

    public SimCardDTOV2 fullUpdateSimCardByIdV2(Long simCardId, UpdateSimCardDTOV2 updateSimCardDTO) {
        URI url = this.getUri("/api/v2/private/auth/simcards/" + simCardId);
        final var method = HttpMethod.PUT;
        log.info("Full update SIM card with ID {} and DTO {} by {} from URL={}", simCardId, updateSimCardDTO, method, url);
        return performUpdateSIMV2(updateSimCardDTO, url, method);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO partialUpdateSimCardById(Long simCardId, UpdateSimCardDTO updateSimCardDTO) {
        URI url = this.getUri("/private/auth/simcards/" + simCardId);
        final var method = HttpMethod.PATCH;
        log.info("Partial update SIM card with ID {} and DTO {} by {} from URL={}", simCardId, updateSimCardDTO, method, url);

        return performUpdateSIM(updateSimCardDTO, url, method);
    }

    public SimCardDTOV2 partialUpdateSimCardByIdV2(Long simCardId, UpdateSimCardDTOV2 updateSimCardDTO) {
        URI url = this.getUri("/api/v2/private/auth/simcards/" + simCardId);
        final var method = HttpMethod.PATCH;
        log.info("Partial update SIM card with ID {} and DTO {} by {} from URL={}", simCardId, updateSimCardDTO, method, url);
        return performUpdateSIMV2(updateSimCardDTO, url, method);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO partialUpdateSimCardBySerialNumber(String serialNumber, UpdateSimCardDTO updateSimCardDTO) {
        URI url = this.getUri("/private/auth/simcards/serialNumber/" + serialNumber);
        final var method = HttpMethod.PATCH;
        log.info("Partial update SIM card with SN {} and DTO {} by {} from URL={}", serialNumber, updateSimCardDTO, method, url);

        return performUpdateSIM(updateSimCardDTO, url, method);
    }

    public SimCardDTOV2 partialUpdateSimCardBySerialNumberV2(String serialNumber, UpdateSimCardDTOV2 updateSimCardDTO) {
        URI url = this.getUri("/api/v2/private/auth/simcards/serialNumber/" + serialNumber);
        final var method = HttpMethod.PATCH;
        log.info("Partial update SIM card with SN {} and DTO {} by {} from URL={}", serialNumber, updateSimCardDTO, method, url);
        return performUpdateSIMV2(updateSimCardDTO, url, method);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    public SimCardDTO partialUpdateSimCardByIMSI(String imsi, UpdateSimCardDTO updateSimCardDTO) {
        URI url = this.getUri("/private/auth/simcards/imsi/" + imsi);
        final var method = HttpMethod.PATCH;
        log.info("Partial update SIM card with IMSI {} and DTO {} by {} from URL={}", imsi, updateSimCardDTO, method, url);

        return performUpdateSIM(updateSimCardDTO, url, method);
    }

    public SimCardDTOV2 partialUpdateSimCardByIMSIV2(String imsi, UpdateSimCardDTOV2 updateSimCardDTO) {
        URI url = this.getUri("/ai/v2/private/auth/simcards/imsi/" + imsi);
        final var method = HttpMethod.PATCH;
        log.info("Partial update SIM card with IMSI {} and DTO {} by {} from URL={}", imsi, updateSimCardDTO, method, url);
        return performUpdateSIMV2(updateSimCardDTO, url, method);
    }

    @Deprecated(since = "2.21.0", forRemoval = true)
    private SimCardDTO performUpdateSIM(final UpdateSimCardDTO updateSimCardDTO, final URI url, final HttpMethod method) {
        HttpHeaders headers = authHeaders();
        HttpEntity<UpdateSimCardDTO> requestEntity = new HttpEntity<>(updateSimCardDTO, headers);
        return exchange(url, method, requestEntity, SimCardDTO.class);
    }

    private SimCardDTOV2 performUpdateSIMV2(final UpdateSimCardDTOV2 updateSimCardDTO, final URI url, final HttpMethod method) {
        HttpEntity<UpdateSimCardDTOV2> requestEntity = new HttpEntity<>(updateSimCardDTO, authHeaders());
        return exchange(url, method, requestEntity, SimCardDTOV2.class);
    }

    private HttpHeaders patchHeader() {
        HttpHeaders headers = authHeaders();
        MediaType mediaType = new MediaType("application", "merge-patch+json");
        headers.setContentType(mediaType);
        return headers;
    }

    private HttpHeaders authHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticationManager.getTokenWithClientCredentials());
        return headers;
    }

    private MultiValueMap<String, String> convert(Object obj) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        Map<String, String> maps = mapper.convertValue(obj, new TypeReference<>() {});
        parameters.setAll(maps);
        return parameters;
    }
}
