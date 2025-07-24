package mc.monacotelecom.tecrep.equipments.process.simcardgenerator;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.inventory.common.nls.LocalizedMessageBuilder;
import mc.monacotelecom.tecrep.equipments.dto.request.GenerateSimCardsRequestDTO;
import mc.monacotelecom.tecrep.equipments.entity.Batch;
import mc.monacotelecom.tecrep.equipments.entity.SimCardGenerationConfiguration;
import mc.monacotelecom.tecrep.equipments.exceptions.EqmValidationException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import static mc.monacotelecom.tecrep.equipments.translation.TranslationMessages.NOT_IMPLEMENTED;

@RequiredArgsConstructor
@Component
@Profile(value = {"!eir & !epic"})
public class SimCardIdentifiersGeneratorDefault implements SimCardIdentifiersGenerator {
    private final LocalizedMessageBuilder localizedMessageBuilder;

    @Override
    public String generateSerialNumber(final Batch batch, final String nextICCID, final String nextMSIN, final GenerateSimCardsRequestDTO generateSimCardsRequestDTO, final SimCardGenerationConfiguration configuration) {
        throw new EqmValidationException(localizedMessageBuilder, NOT_IMPLEMENTED);
    }

    @Override
    public String generateImsiNumber(final Batch batch, final String nextICCID, final String nextMSIN, final GenerateSimCardsRequestDTO generateSimCardsRequestDTO, final SimCardGenerationConfiguration configuration) {
        throw new EqmValidationException(localizedMessageBuilder, NOT_IMPLEMENTED);
    }
}
