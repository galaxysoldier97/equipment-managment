package mc.monacotelecom.tecrep.equipments.annotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = ExactlyOneTypeValidator.class)
public @interface ExactlyOneType {
    String message() default "Exactly one of idModel, idMaterial or idGroup must be provided";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
