package mc.monacotelecom.tecrep.equipments.importer.implementations.context;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.importer.ImportParameters;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.enums.AccessType;
import mc.monacotelecom.tecrep.equipments.enums.SimCardSimProfile;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;

import java.util.Optional;

@RequiredArgsConstructor
public class SimCardDefaultImporterContext {
    static final String ACCESS_TYPE_KEY = "accessType";
    static final String BATCH_NUMBER_KEY = "batchNumber";
    static final String PLMN_ID_KEY = "plmnId";
    static final String PREACTIVATED_KEY = "preactivated";
    static final String SIM_PROFILE_KEY = "simProfile";
    static final String TYPE_KEY = "Type";
    static final String BRAND = "brand";
    static final String STATUS = "status";

    static final String CONFIGURATION = "configuration";

    private final ImportParameters importParameters;
    private final LocalizedMessageBuilder localizedMessageBuilder;

    public AccessType getAccessType() {
        return importParameters.getParameter(ACCESS_TYPE_KEY)
                .map(AccessType::valueOf)
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, "Missing accessType in request"));
    }

    public Optional<String> getBatchNumber() {
        return importParameters.getParameter(BATCH_NUMBER_KEY);
    }

    public boolean isPreactivated() {
        return importParameters.getParameter(PREACTIVATED_KEY)
                .map(Boolean::parseBoolean)
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, "Missing preactivated in request"));
    }

    public Long getPlmnId() {
        return importParameters.getParameter(PLMN_ID_KEY)
                .map(Long::parseLong)
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, "Missing plmnId in request"));
    }

    public String getSimProfile() {
        return importParameters.getParameter(SIM_PROFILE_KEY)
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, "Missing simProfile in request"));
    }

    public Status getStatus() {
        return importParameters.getParameter(STATUS)
                .map(Status::valueOf)
                .orElse(Status.INSTORE);
    }

    public Optional<String> getType() {
        return importParameters.getParameter(TYPE_KEY);
    }

    public Optional<String> getBrand() {
        return importParameters.getParameter(BRAND);
    }

    public String getConfiguration(){
        return importParameters.getParameter(CONFIGURATION)
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, "Missing configuration in request"));
    }
}
