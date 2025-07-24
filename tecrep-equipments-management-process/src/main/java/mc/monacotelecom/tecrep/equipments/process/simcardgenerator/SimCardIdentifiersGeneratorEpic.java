package mc.monacotelecom.tecrep.equipments.process.simcardgenerator;

import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.configuration.EpicSimCardGenerationProperties;
import mc.monacotelecom.tecrep.equipments.dto.request.GenerateSimCardsRequestDTO;
import mc.monacotelecom.tecrep.equipments.entity.Batch;
import mc.monacotelecom.tecrep.equipments.entity.SimCardGenerationConfiguration;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.BATCH_GENERATE_SIM_CARD_VALUE_NOT_PRESENT;


@Component
@Profile("epic")
public class SimCardIdentifiersGeneratorEpic implements SimCardIdentifiersGenerator {

    private final EpicSimCardGenerationProperties simCardGenerationProperties;

    private final Function<String, RuntimeException> notPresent;
    public static final DateTimeFormatter MONTH_YEAR_FORMATTER = DateTimeFormatter.ofPattern("MMyy");

    public SimCardIdentifiersGeneratorEpic(final EpicSimCardGenerationProperties simCardGenerationProperties,
                                           final LocalizedMessageBuilder localizedMessageBuilder) {
        this.simCardGenerationProperties = simCardGenerationProperties;
        this.notPresent = value ->
                new EqmValidationException(localizedMessageBuilder, BATCH_GENERATE_SIM_CARD_VALUE_NOT_PRESENT, value);
    }

    /**
     * serialNumber
     * * [TR][CC][Vendor][profile][Type][MMYY][sequenceNumber]
     * * 1-2 3-5    6       7       8    9-12      13-18
     * *
     */
    @Override
    public String generateSerialNumber(final Batch batch,
                                       final String nextICCID,
                                       final String nextMSIN,
                                       final GenerateSimCardsRequestDTO generateSimCardsRequestDTO,
                                       final SimCardGenerationConfiguration configuration) {

        final String vendor = generateSimCardsRequestDTO.getProvider();
        final String profile = generateSimCardsRequestDTO.getProfile();
        final String type = generateSimCardsRequestDTO.getSimCardType();
        final String brand = generateSimCardsRequestDTO.getBrand();

        return getTr() + getCc() + getVendor(vendor) + getProfile(profile) + getType(brand, type) + getMonthAndYear(batch) + nextICCID;
    }

    /**
     * imsiNumber
     * * [MCC_MNC] [HLRid] [type] [profile] [vendor] [MSIN]
     * *   1-5        6      7       8         9     10-15
     * *
     */
    @Override
    public String generateImsiNumber(final Batch batch,
                                     final String nextICCID,
                                     final String nextMSIN,
                                     final GenerateSimCardsRequestDTO generateSimCardsRequestDTO,
                                     final SimCardGenerationConfiguration configuration) {

        final String vendor = generateSimCardsRequestDTO.getProvider();
        final String profile = generateSimCardsRequestDTO.getProfile();
        final String brand = generateSimCardsRequestDTO.getBrand();
        final String type = generateSimCardsRequestDTO.getSimCardType();

        return getMccMnc() + getHlrId() + getType(brand, type) + getProfile(profile) + getVendor(vendor) + nextMSIN;
    }

    private String getCc() {
        return simCardGenerationProperties.getCc();
    }

    private String getTr() {
        return simCardGenerationProperties.getTr();
    }

    private String getHlrId() {
        return simCardGenerationProperties.getHlrId();
    }

    private String getMccMnc() {
        return simCardGenerationProperties.getMccmnc();
    }

    private String getProfile(String key) {
        return String.valueOf(Optional.ofNullable(simCardGenerationProperties.getProfiles().get(key.toLowerCase())).orElseThrow(() -> notPresent.apply(key)));
    }

    private String getType(String brand, String key) {
        @SuppressWarnings("unchecked") final Map<String, String> brandMap = (HashMap<String, String>) Optional.ofNullable(simCardGenerationProperties.getBrands().get(brand.toLowerCase())).orElseThrow(() -> notPresent.apply(brand));

        return String.valueOf(Optional.ofNullable(brandMap.get(key.toLowerCase())).orElseThrow(() -> notPresent.apply(key)));
    }

    private String getVendor(String key) {
        return String.valueOf(Optional.ofNullable(simCardGenerationProperties.getProviders().get(key.toLowerCase())).orElseThrow(() -> notPresent.apply(key)));
    }

    private String getMonthAndYear(Batch batch) {
        return batch.getCreationDate().format(MONTH_YEAR_FORMATTER);
    }
}
