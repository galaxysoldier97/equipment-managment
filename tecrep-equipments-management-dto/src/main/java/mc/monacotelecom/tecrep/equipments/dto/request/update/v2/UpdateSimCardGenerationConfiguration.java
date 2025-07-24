package mc.monacotelecom.tecrep.equipments.dto.request.update.v2;

public interface UpdateSimCardGenerationConfiguration {
    String getName();

    String getIccidSequence();

    String getMsinSequence();

    String getSequencePrefix();

    String getFixedPrefix();

    String getArtwork();

    String getSimReference();

    String getTransportKey();

    Integer getAlgorithmVersion();

    String getType();
}

