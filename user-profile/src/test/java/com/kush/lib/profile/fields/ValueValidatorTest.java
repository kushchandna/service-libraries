package com.kush.lib.profile.fields;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
        Field<String> field = Fields.createTextFieldBuilder().build();
        valueValidator.validate(field, "This is a text field with no bounds");
    }

    @Test
    public void freeTextWithUpperBoundFieldBuilder_WithValidValue() throws Exception {
        Field<String> field = Fields.createTextFieldBuilder()
            .addValidator(new MaximumLengthValidator(10))
            .build();
        valueValidator.validate(field, "ten");
    }

    @Test
    public void freeTextWithUpperBoundFieldBuilder_WithInvalidValue() throws Exception {
        Field<String> field = Fields.createTextFieldBuilder()
            .addValidator(new MaximumLengthValidator(10))
            .build();
        expected.expect(ValidationFailedException.class);
        valueValidator.validate(field, "Long value will be rejected here");
    }

    @Test
    public void freeTextWithLowerBoundField_WithValidValue() throws Exception {
        Field<String> field = Fields.createTextFieldBuilder()
            .addValidator(new MinimumLengthValidator(10))
            .build();
        valueValidator.validate(field, "Long value will be accepted here");
    }

    @Test
    public void freeTextWithLowerBoundField_WithInvalidValue() throws Exception {
        Field<String> field = Fields.createTextFieldBuilder()
            .addValidator(new MinimumLengthValidator(10))
            .build();
        expected.expect(ValidationFailedException.class);
        valueValidator.validate(field, "ten");
    }

    @Test
    public void emailTextField_WithInvalidValue() throws Exception {
        Field<String> field = Fields.createTextFieldBuilder()
            .addValidator(new EmailValidator())
            .build();
        expected.expect(ValidationFailedException.class);
        valueValidator.validate(field, "invalid-email");
    }

    @Test
    public void emailTextField_WithValidValue() throws Exception {
        Field<String> field = Fields.createTextFieldBuilder()
            .addValidator(new EmailValidator())
            .build();
        valueValidator.validate(field, "testuser@domain.org");
    }

    @Test
    public void formattedTextFieldBuilder() throws Exception {
        // phone number
    }

    @Test
    public void integerFieldBuilder() throws Exception {
        Field<Integer> field = Fields.createIntegerFieldBuilder().build();
        valueValidator.validate(field, 100);
    }

    @Test
    public void integerFieldBuilder_WithMaxValidation_WithValidValue() throws Exception {
        Field<Integer> field = Fields.createIntegerFieldBuilder()
            .addValidator(new MaximumValueValidator<>(150))
            .build();
        valueValidator.validate(field, 100);
    }

    @Test
    public void integerFieldBuilder_WithMaxValidation_WithInvalidValue() throws Exception {
        Field<Integer> field = Fields.createIntegerFieldBuilder()
            .addValidator(new MaximumValueValidator<>(10))
            .build();
        expected.expect(ValidationFailedException.class);
        valueValidator.validate(field, 100);
    }

    @Test
    public void numericFieldBuilder() throws Exception {
        Field<Double> field = Fields.createNumericFieldBuilder().build();
        valueValidator.validate(field, 100.5);
    }

    @Test
    public void numericFieldBuilder_WithMaxValidation_WithValidValue() throws Exception {
        Field<Double> field = Fields.createNumericFieldBuilder()
            .addValidator(new MaximumValueValidator<>(150.0))
            .build();
        valueValidator.validate(field, 100.5);
    }

    @Test
    public void numericFieldBuilder_WithMaxValidation_WithInvalidValue() throws Exception {
        Field<Double> field = Fields.createNumericFieldBuilder()
            .addValidator(new MaximumValueValidator<>(10.5))
            .build();
        expected.expect(ValidationFailedException.class);
        valueValidator.validate(field, 100.5);
    }

    @Test
    public void numericRangeFieldBuilder_WithValidValue() throws Exception {
        Field<Double> field = Fields.createNumericFieldBuilder()
            .addValidator(new RangeValidator<>(10.5, 150.5))
            .build();
        valueValidator.validate(field, 100.5);
    }

    @Test
    public void numericRangeFieldBuilder_WithInvalidValue() throws Exception {
        Field<Double> field = Fields.createNumericFieldBuilder()
            .addValidator(new RangeValidator<>(10.5, 50.5))
            .build();
        expected.expect(ValidationFailedException.class);
        valueValidator.validate(field, 100.5);
    }

    @Test
    public void booleanFieldBuilder() throws Exception {
        Field<Boolean> field = Fields.createBooleanFieldBuilder().build();
        valueValidator.validate(field, true);
    }

    @Test
    public void dateFieldBuilder() throws Exception {
        Field<LocalDate> field = Fields.createDateFieldBuilder().build();
        valueValidator.validate(field, LocalDate.now());
    }

    @Test
    public void dateTimeFieldBuilder() throws Exception {
        Field<LocalDateTime> field = Fields.createDateTimeFieldBuilder().build();
        valueValidator.validate(field, LocalDateTime.now());
    }
}
