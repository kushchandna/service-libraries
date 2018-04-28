package com.kush.lib.profile.fields;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.kush.lib.profile.fields.validators.standard.EmailValidator;
import com.kush.lib.profile.fields.validators.standard.MaximumLengthValidator;
import com.kush.lib.profile.fields.validators.standard.MaximumValueValidator;
import com.kush.lib.profile.fields.validators.standard.MinimumLengthValidator;
import com.kush.lib.profile.fields.validators.standard.RangeValidator;
import com.kush.utils.exceptions.ValidationFailedException;

public class ValueValidatorTest {

    private static final String TEST_FIELD = "testField";

    private final ValueValidator valueValidator = new ValueValidator();

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Test
    public void freeTextFieldBuilder() throws Exception {
        Field field = Fields.createTextFieldBuilder(TEST_FIELD).build();
        valueValidator.validate(field, "This is a text field with no bounds");
    }

    @Test
    public void freeTextWithUpperBoundFieldBuilder_WithValidValue() throws Exception {
        Field field = Fields.createTextFieldBuilder(TEST_FIELD)
            .addValidator(new MaximumLengthValidator(10))
            .build();
        valueValidator.validate(field, "ten");
    }

    @Test
    public void freeTextWithUpperBoundFieldBuilder_WithInvalidValue() throws Exception {
        Field field = Fields.createTextFieldBuilder(TEST_FIELD)
            .addValidator(new MaximumLengthValidator(10))
            .build();
        expected.expect(ValidationFailedException.class);
        valueValidator.validate(field, "Long value will be rejected here");
    }

    @Test
    public void freeTextWithLowerBoundField_WithValidValue() throws Exception {
        Field field = Fields.createTextFieldBuilder(TEST_FIELD)
            .addValidator(new MinimumLengthValidator(10))
            .build();
        valueValidator.validate(field, "Long value will be accepted here");
    }

    @Test
    public void freeTextWithLowerBoundField_WithInvalidValue() throws Exception {
        Field field = Fields.createTextFieldBuilder(TEST_FIELD)
            .addValidator(new MinimumLengthValidator(10))
            .build();
        expected.expect(ValidationFailedException.class);
        valueValidator.validate(field, "ten");
    }

    @Test
    public void emailTextField_WithInvalidValue() throws Exception {
        Field field = Fields.createTextFieldBuilder(TEST_FIELD)
            .addValidator(new EmailValidator())
            .build();
        expected.expect(ValidationFailedException.class);
        valueValidator.validate(field, "invalid-email");
    }

    @Test
    public void emailTextField_WithValidValue() throws Exception {
        Field field = Fields.createTextFieldBuilder(TEST_FIELD)
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
        Field field = Fields.createIntegerFieldBuilder(TEST_FIELD).build();
        valueValidator.validate(field, 100);
    }

    @Test
    public void integerFieldBuilder_WithMaxValidation_WithValidValue() throws Exception {
        Field field = Fields.createIntegerFieldBuilder(TEST_FIELD)
            .addValidator(new MaximumValueValidator<>(150))
            .build();
        valueValidator.validate(field, 100);
    }

    @Test
    public void integerFieldBuilder_WithMaxValidation_WithInvalidValue() throws Exception {
        Field field = Fields.createIntegerFieldBuilder(TEST_FIELD)
            .addValidator(new MaximumValueValidator<>(10))
            .build();
        expected.expect(ValidationFailedException.class);
        valueValidator.validate(field, 100);
    }

    @Test
    public void numericFieldBuilder() throws Exception {
        Field field = Fields.createNumericFieldBuilder(TEST_FIELD).build();
        valueValidator.validate(field, 100.5);
    }

    @Test
    public void numericFieldBuilder_WithMaxValidation_WithValidValue() throws Exception {
        Field field = Fields.createNumericFieldBuilder(TEST_FIELD)
            .addValidator(new MaximumValueValidator<>(150.0))
            .build();
        valueValidator.validate(field, 100.5);
    }

    @Test
    public void numericFieldBuilder_WithMaxValidation_WithInvalidValue() throws Exception {
        Field field = Fields.createNumericFieldBuilder(TEST_FIELD)
            .addValidator(new MaximumValueValidator<>(10.5))
            .build();
        expected.expect(ValidationFailedException.class);
        valueValidator.validate(field, 100.5);
    }

    @Test
    public void numericRangeFieldBuilder_WithValidValue() throws Exception {
        Field field = Fields.createNumericFieldBuilder(TEST_FIELD)
            .addValidator(new RangeValidator<>(10.5, 150.5))
            .build();
        valueValidator.validate(field, 100.5);
    }

    @Test
    public void numericRangeFieldBuilder_WithInvalidValue() throws Exception {
        Field field = Fields.createNumericFieldBuilder(TEST_FIELD)
            .addValidator(new RangeValidator<>(10.5, 50.5))
            .build();
        expected.expect(ValidationFailedException.class);
        valueValidator.validate(field, 100.5);
    }

    @Test
    public void booleanFieldBuilder() throws Exception {
        Field field = Fields.createBooleanFieldBuilder(TEST_FIELD).build();
        valueValidator.validate(field, true);
    }

    @Test
    public void dateFieldBuilder() throws Exception {
        Field field = Fields.createDateFieldBuilder(TEST_FIELD).build();
        valueValidator.validate(field, LocalDate.now());
    }

    @Test
    public void dateFieldBuilder_WithPassingRangeValidation() throws Exception {
        Field field = Fields.createDateFieldBuilder(TEST_FIELD)
            .addValidator(new RangeValidator<>(LocalDate.of(2018, 1, 1), LocalDate.now()))
            .build();
        valueValidator.validate(field, LocalDate.now().minusDays(1));
    }

    @Test
    public void dateFieldBuilder_WithFailingRangeValidation() throws Exception {
        Field field = Fields.createDateFieldBuilder(TEST_FIELD)
            .addValidator(new RangeValidator<>(LocalDate.of(2018, 1, 1), LocalDate.now()))
            .build();
        expected.expect(ValidationFailedException.class);
        valueValidator.validate(field, LocalDate.now().plusDays(1));
    }

    @Test
    public void dateTimeFieldBuilder() throws Exception {
        Field field = Fields.createDateTimeFieldBuilder(TEST_FIELD).build();
        valueValidator.validate(field, LocalDateTime.now());
    }

    @Test
    public void dateTimeFieldBuilder_WithPassingRangeValidation() throws Exception {
        Field field = Fields.createDateTimeFieldBuilder(TEST_FIELD)
            .addValidator(new RangeValidator<>(LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0)),
                    LocalDateTime.of(LocalDate.now(), LocalTime.now())))
            .build();
        valueValidator.validate(field, LocalDateTime.now().minusHours(1));
    }

    @Test
    public void dateTimeFieldBuilder_WithFailingRangeValidation() throws Exception {
        Field field = Fields.createDateTimeFieldBuilder(TEST_FIELD)
            .addValidator(new RangeValidator<>(LocalDateTime.of(LocalDate.now(), LocalTime.of(0, 0)),
                    LocalDateTime.of(LocalDate.now(), LocalTime.now())))
            .build();
        expected.expect(ValidationFailedException.class);
        valueValidator.validate(field, LocalDateTime.now().plusHours(1));
    }
}
