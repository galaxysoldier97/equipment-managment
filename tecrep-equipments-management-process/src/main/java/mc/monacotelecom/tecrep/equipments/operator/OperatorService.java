package mc.monacotelecom.tecrep.equipments.operator;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@RequiredArgsConstructor
@Component
public class OperatorService {
    private final Environment environment;

    public boolean isEir() {
        return Arrays.asList(environment.getActiveProfiles()).contains("eir");
    }

    public boolean isEpic() {
        return Arrays.asList(environment.getActiveProfiles()).contains("epic");
    }
}
