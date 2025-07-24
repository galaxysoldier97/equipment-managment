package mc.monacotelecom.tecrep.equipments.connectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.inventory.common.connectors.AbstractConnector;
import mc.monacotelecom.inventory.common.connectors.exceptions.ExternalConnectorException;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.connectors.dto.TABSSimResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.TABS_SIM_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
@Profile(value = "epic")
public class TabsConnector implements AbstractConnector {
    private final LocalizedMessageBuilder localizedMessageBuilder;

    @Value("${tabs.webservice.uri}")
    private String tabsUri;

    public TABSSimResponse checkSimCard(final String serialNumber) {
        String url = tabsUri + "/Simcard/GetAvailableSimcard?iccid=" + serialNumber;
        log.info("Calling {}", url);
        final ResponseEntity<TABSSimResponse> response = getResponseEntity(url, TABSSimResponse.class);
        if (HttpStatus.OK.equals(response.getStatusCode())) {
            final TABSSimResponse body = response.getBody();
            log.info("Request Successful ... {} ", body);
            return body;
        } else {
            log.error("Request Failed - {} ", response.getStatusCode());
            throw new ExternalConnectorException(localizedMessageBuilder, TABS_SIM_NOT_FOUND, serialNumber);
        }
    }
}
