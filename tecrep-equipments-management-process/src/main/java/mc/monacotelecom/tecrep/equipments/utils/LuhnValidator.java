package mc.monacotelecom.tecrep.equipments.utils;

import org.hibernate.validator.internal.constraintvalidators.hv.LuhnCheckValidator;

import java.util.ArrayList;
import java.util.List;

public class LuhnValidator {

    private int checkDigitIndex;

    public LuhnValidator(int checkDigitIndex) {
        this();
        this.checkDigitIndex = checkDigitIndex;
    }

    public LuhnValidator() {
        super();
    }

    public int getCheckDigitIndex() {
        return checkDigitIndex;
    }

    public void setCheckDigitIndex(int checkDigitIndex) {
        this.checkDigitIndex = checkDigitIndex;
    }

    /**
     * Validates the check digit for the number using the Luhn Algorithm
     * (@link https://en.wikipedia.org/wiki/Luhn_algorithm)
     * <p>
     * Note that if the check digit index is not provided, it is assumed that the check digit is the last char in the parameter String
     *
     * @param numberString String of the number including the check digit
     * @return boolean
     */
    public boolean isCheckDigitValid(String numberString) {
        // if not provided, assume check digit it last char in the string
        if (getCheckDigitIndex() == 0)
            setCheckDigitIndex(numberString.length() - 1);
        return isCheckDigitValid(numberString, getCheckDigitIndex());
    }

    /**
     * Validates the check digit for the number using the Luhn Algorithm
     * (@link https://en.wikipedia.org/wiki/Luhn_algorithm)
     * <p>
     * Note that the algorithm will only be applied to the digits preceding the checkDigitIndex in the parameter String
     * and any character following the checkDigitIndex will be ignored.
     *
     * @param numberString    String of the number including the check digit
     * @param checkDigitIndex int The position in the string where the check digit is.
     * @return boolean
     */
    public boolean isCheckDigitValid(String numberString, int checkDigitIndex) {
        String number = numberString.substring(0, checkDigitIndex);
        char checkDigit = numberString.charAt(checkDigitIndex);

        List<Integer> digits = new ArrayList<>(number.length());
        for (int i = 0; i < number.length(); i++) {
            digits.add(Integer.valueOf(number.substring(i, i + 1)));
        }

        LuhnCheckValidator validator = new LuhnCheckValidator();
        return validator.isCheckDigitValid(digits, checkDigit);
    }
}
