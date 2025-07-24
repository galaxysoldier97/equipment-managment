package mc.monacotelecom.tecrep.equipments.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = IsEpicProfileValidator.class)
@Documented
public @interface IsEpicProfile {

    String message() default "Not nullable value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
