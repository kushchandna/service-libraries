package com.kush.lib.profile.fields;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.kush.lib.profile.fields.validators.ValidationFailedException;
import com.kush.lib.profile.fields.validators.standard.EmailValidator;
import com.kush.lib.profile.fields.validators.standard.MaximumLengthValidator;
import com.kush.lib.profile.fields.validators.standard.MinimumLengthValidator;

public class ValueValidatorTest {

    private final ValueValidator valueValidator = new ValueValidator();

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Test
    public void freeTextFieldBuilder() throws Exception {
        Field<String> textField = Fields.createTextFieldBuilder().build();
        valueValidator.validate(textField, "This is a text field with no bounds");
    }

    @Test
    public void freeTextWithUpperBoundFieldBuilder() throws Exception {
        Field<String> textField = Fields.createTextFieldBuilder()
            .addValidator(new MaximumLengthValidator(5))
            .build();
        expected.expect(ValidationFailedException.class);
        valueValidator.validate(textField, "A value with length more than 5 will be rejected here");
    }

    @Test
    public void freeTextWithLowerBoundField_WithInvalidValue() throws Exception {
        Field<String> textField = Fields.createTextFieldBuilder()
            .addValidator(new MinimumLengthValidator(20))
            .build();
        expected.expect(ValidationFailedException.class);
        valueValidator.validate(textField, "small text");
    }

    @Test
    public void emailTextField_WithInvalidValue() throws Exception {
        Field<String> textField = Fields.createTextFieldBuilder()
            .addValidator(EmailValidator.INSTANCE)
            .build();
        expected.expect(ValidationFailedException.class);
        valueValidator.validate(textField, "invalid-email");
    }

    @Test
    public void emailTextField_WithValidValue() throws Exception {
        Field<String> textField = Fields.createTextFieldBuilder()
            .addValidator(EmailValidator.INSTANCE)
            .build();
        valueValidator.validate(textField, "testuser@domain.org");
    }

    @Test
    public void formattedTextFieldBuilder() throws Exception {
        // phone number
    }

    @Test
    public void numericFieldBuilder() throws Exception {
        // age
    }

    @Test
    public void numericRangeFieldBuilder() throws Exception {
        // age range
    }

    @Test
    public void numericUpperBoundRangeFieldBuilder() throws Exception {
        // maximum mails
    }

    @Test
    public void numericLowerBoundRangeFieldBuilder() throws Exception {
        // minimum mails
    }

    @Test
    public void booleanFieldBuilder() throws Exception {
        // opt for a feature
    }

    @Test
    public void dateFieldBuilder() throws Exception {
        // birth date
    }

    @Test
    public void dateTimeFieldBuilder() throws Exception {
        // birth date time
    }

    @Test
    public void yearFieldBuilder() throws Exception {
        // birth year
    }

    @Test
    public void fixedValuesFieldBuilder() throws Exception {
        // gender - MALE / FEMALE
    }

    @Test
    public void fixedAndFreeValueFieldBuilder() throws Exception {
        // gender - MALE / FEMALE / Others (text)
    }

    @Test
    public void locationFieldBuilder() throws Exception {
        // home address
    }
}
