package mc.monacotelecom.tecrep.equipments.service;

import lombok.RequiredArgsConstructor;
import mc.monacotelecom.tecrep.equipments.dto.EquipmentsStatsDto;
import mc.monacotelecom.tecrep.equipments.process.StatsProcess;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StatsService {
    private final StatsProcess statsProcess;

    @Transactional(readOnly = true)
    public EquipmentsStatsDto getDashboard() {
        return statsProcess.getDashboard();
    }
}
