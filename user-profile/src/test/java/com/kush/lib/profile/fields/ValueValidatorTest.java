package com.kush.lib.profile.fields;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.kush.lib.profile.fields.validators.ValidationFailedException;
import com.kush.lib.profile.fields.validators.standard.EmailValidator;
import com.kush.lib.profile.fields.validators.standard.MaximumLengthValidator;
import com.kush.lib.profile.fields.validators.standard.MaximumValueValidator;
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
    public void freeTextWithUpperBoundFieldBuilder_WithValidValue() throws Exception {
        Field<String> textField = Fields.createTextFieldBuilder()
            .addValidator(new MaximumLengthValidator(10))
            .build();
        valueValidator.validate(textField, "ten");
    }

    @Test
    public void freeTextWithUpperBoundFieldBuilder_WithInvalidValue() throws Exception {
        Field<String> textField = Fields.createTextFieldBuilder()
            .addValidator(new MaximumLengthValidator(10))
            .build();
        expected.expect(ValidationFailedException.class);
        valueValidator.validate(textField, "Long value will be rejected here");
    }

    @Test
    public void freeTextWithLowerBoundField_WithValidValue() throws Exception {
        Field<String> textField = Fields.createTextFieldBuilder()
            .addValidator(new MinimumLengthValidator(10))
            .build();
        valueValidator.validate(textField, "Long value will be accepted here");
    }

    @Test
    public void freeTextWithLowerBoundField_WithInvalidValue() throws Exception {
        Field<String> textField = Fields.createTextFieldBuilder()
            .addValidator(new MinimumLengthValidator(10))
            .build();
        expected.expect(ValidationFailedException.class);
        valueValidator.validate(textField, "ten");
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
    public void integerFieldBuilder() throws Exception {
        Field<Integer> intField = Fields.createIntegerFieldBuilder().build();
        valueValidator.validate(intField, 100);
    }

    @Test
    public void integerFieldBuilder_WithMaxValidation_WithValidValue() throws Exception {
        Field<Integer> intField = Fields.createIntegerFieldBuilder()
            .addValidator(new MaximumValueValidator<>(150))
            .build();
        valueValidator.validate(intField, 100);
    }

    @Test
    public void integerFieldBuilder_WithMaxValidation_WithInvalidValue() throws Exception {
        Field<Integer> intField = Fields.createIntegerFieldBuilder()
            .addValidator(new MaximumValueValidator<>(10))
            .build();
        expected.expect(ValidationFailedException.class);
        valueValidator.validate(intField, 100);
    }

    @Test
    public void numericFieldBuilder() throws Exception {
        Field<Double> numericField = Fields.createNumericFieldBuilder().build();
        valueValidator.validate(numericField, 100.5);
    }

    @Test
    public void numericFieldBuilder_WithMaxValidation_WithValidValue() throws Exception {
        Field<Double> numericField = Fields.createNumericFieldBuilder()
            .addValidator(new MaximumValueValidator<>(150.0))
            .build();
        valueValidator.validate(numericField, 100.5);
    }

    @Test
    public void numericFieldBuilder_WithMaxValidation_WithInvalidValue() throws Exception {
        Field<Double> numericField = Fields.createNumericFieldBuilder()
            .addValidator(new MaximumValueValidator<>(10.5))
            .build();
        expected.expect(ValidationFailedException.class);
        valueValidator.validate(numericField, 100.5);
    }

    @Test
    public void numericRangeFieldBuilder_WithValidValue() throws Exception {
        Field<Double> numericField = Fields.createNumericFieldBuilder()
            .addValidator(new RangeValidator<>(10.5, 150.5))
            .build();
        valueValidator.validate(numericField, 100.5);
    }

    @Test
    public void numericRangeFieldBuilder_WithInvalidValue() throws Exception {
        Field<Double> numericField = Fields.createNumericFieldBuilder()
            .addValidator(new RangeValidator<>(10.5, 50.5))
            .build();
        expected.expect(ValidationFailedException.class);
        valueValidator.validate(numericField, 100.5);
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
