package mc.monacotelecom.tecrep.equipments.process;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.EquipmentsStatsDto;
import mc.monacotelecom.tecrep.equipments.enums.Status;
import mc.monacotelecom.tecrep.equipments.repository.StatsRepository;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class StatsProcess {
    private final StatsRepository statsRepository;

    public EquipmentsStatsDto getDashboard() {
        EquipmentsStatsDto stats = new EquipmentsStatsDto();
        Map<Status, Long> statusOfEquipments = new EnumMap<>(Status.class);

        statsRepository.statusOfEquipments().forEach(line -> statusOfEquipments.put(Status.valueOf(line[0].toString()), Long.valueOf(line[1].toString())));

        stats.setStatusOfEquipments(statusOfEquipments);
        return stats;
    }
}
