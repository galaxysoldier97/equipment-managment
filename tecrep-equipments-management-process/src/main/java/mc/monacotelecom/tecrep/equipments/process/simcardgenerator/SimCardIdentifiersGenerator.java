package mc.monacotelecom.tecrep.equipments.process.simcardgenerator;

import mc.monacotelecom.tecrep.equipments.dto.request.GenerateSimCardsRequestDTO;
import mc.monacotelecom.tecrep.equipments.entity.Batch;
import mc.monacotelecom.tecrep.equipments.entity.SimCardGenerationConfiguration;

public interface SimCardIdentifiersGenerator {
    String generateSerialNumber(final Batch batch,
                                final String nextICCID,
                                final String nextMSIN,
                                final GenerateSimCardsRequestDTO generateSimCardsRequestDTO,
                                final SimCardGenerationConfiguration configuration);

    String generateImsiNumber(final Batch batch,
                              final String nextICCID,
                              final String nextMSIN,
                              final GenerateSimCardsRequestDTO generateSimCardsRequestDTO,
                              final SimCardGenerationConfiguration configuration);
}
