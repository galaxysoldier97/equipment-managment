package mc.monacotelecom.tecrep.equipments.annotations;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

@RequiredArgsConstructor
public class IsEpicProfileValidator implements ConstraintValidator<IsEpicProfile, String> {
    private final Environment environment;

    @Override
    public boolean isValid(final String s, final ConstraintValidatorContext constraintValidatorContext) {
        return !Arrays.asList(environment.getActiveProfiles()).contains("epic") || StringUtils.isNotBlank(s);
    }
}
