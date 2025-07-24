package mc.monacotelecom.tecrep.equipments.importer.implementations.simcard;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.entity.Provider;
import mc.monacotelecom.tecrep.equipments.entity.SimCard;
import mc.monacotelecom.tecrep.equipments.enums.EquipmentCategory;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import mc.monacotelecom.tecrep.equipments.importer.implementations.context.SimCardDefaultImporterContext;
import mc.monacotelecom.tecrep.equipments.repository.PlmnRepository;
import mc.monacotelecom.tecrep.equipments.repository.ProviderRepository;
import org.springframework.stereotype.Component;

import java.util.Map;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.PLMN_ID_NOT_FOUND;
import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.PROVIDER_NOT_FOUND_NAME;
//log 
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SimCardImporterHelper {

    private final PlmnRepository plmnRepository;
    private final ProviderRepository providerRepository;
    private final LocalizedMessageBuilder localizedMessageBuilder;

    public void setFromHeader(Map<String, String> headers, SimCard simCard) {
        final String type = "Type";
        final String eSim = "eSIM";
        final String profile = "Profile";

        simCard.setEsim(headers.containsKey(type) && headers.get(type).equals(eSim));
        simCard.setSimProfile(headers.getOrDefault(profile, ""));
    }

    public Provider getProvider(String configuration) {
        var providerName = configuration.contains("_") ? configuration.split("_")[0] : configuration;
        return providerRepository.findByName(providerName)
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, PROVIDER_NOT_FOUND_NAME, providerName));
    }

    public void fillSimCard(SimCardDefaultImporterContext simCardDefaultImporterContext,
                            SimCard simCard) {

        log.info(String.format("Considering SIM Card %s for import", simCard.getSerialNumber()));

        simCard.setAccessType(simCardDefaultImporterContext.getAccessType());
        simCardDefaultImporterContext.getBatchNumber().ifPresent(simCard::setBatchNumber);
        simCardDefaultImporterContext.getBrand().ifPresent(simCard::setBrand);
        simCard.setPlmn(plmnRepository.findById(simCardDefaultImporterContext.getPlmnId())
                .orElseThrow(() -> new EqmValidationException(localizedMessageBuilder, PLMN_ID_NOT_FOUND, simCardDefaultImporterContext.getPlmnId())));
        simCard.setPreactivated(simCardDefaultImporterContext.isPreactivated());
        simCard.setCategory(EquipmentCategory.SIMCARD);
        simCard.setProvider(getProvider(simCardDefaultImporterContext.getConfiguration()));
        simCard.setRecyclable(false);
        simCard.setStatus(simCardDefaultImporterContext.getStatus());
    }

}
