package mc.monacotelecom.tecrep.equipments.connectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.inventory.common.connectors.AbstractConnector;
import mc.monacotelecom.inventory.common.connectors.exceptions.ExternalConnectorException;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.connectors.dto.IFSBoxResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.IFS_BOX_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
@Profile(value = "epic")
public class IfsConnector implements AbstractConnector {
    private final LocalizedMessageBuilder localizedMessageBuilder;

    @Value("${ifs.webservice.uri}")
    private String ifsUri;

    public IFSBoxResponse checkBox(final String serialNumber) {
        final String url = ifsUri + "/GetAvailableDevice?serialNo=" + serialNumber;
        log.info("Retrieving BBox serialNumber={} from URL={}", serialNumber, url);
        final ResponseEntity<IFSBoxResponse> response = getResponseEntity(url, IFSBoxResponse.class);
        if (HttpStatus.OK.equals(response.getStatusCode())) {
            final IFSBoxResponse body = response.getBody();
            log.info("Request Successful... {} ", body);
            return body;
        } else {
            log.error("Request Failed - {} ", response.getStatusCode());
            throw new ExternalConnectorException(localizedMessageBuilder, IFS_BOX_NOT_FOUND, serialNumber);
        }
    }
}
