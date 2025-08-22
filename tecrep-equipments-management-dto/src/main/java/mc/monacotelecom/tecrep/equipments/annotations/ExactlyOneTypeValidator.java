package mc.monacotelecom.tecrep.equipments.annotations;

import mc.monacotelecom.tecrep.equipments.dto.StandardLoadItemDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ExactlyOneTypeValidator implements ConstraintValidator<ExactlyOneType, StandardLoadItemDTO> {

    @Override
    public boolean isValid(StandardLoadItemDTO value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        int count = 0;
        if (value.getIdModel() != null) {
            count++;
        }
        if (value.getIdMaterial() != null) {
            count++;
        }
        if (value.getIdGroup() != null) {
            count++;
        }
        return count == 1;
    }
}
