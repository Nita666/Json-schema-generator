package org.ran.jsonschema.suppliers;

import org.everit.json.schema.FormatValidator;
import org.everit.json.schema.StringSchema;
import org.everit.json.schema.internal.EmailFormatValidator;
import org.ran.jsonschema.JsonGenerationException;

import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import static org.ran.jsonschema.RandomUtils.getRandomString;

public class StringDataSupplier extends AbstractDataSupplier<StringSchema, String> {

    @Override
    public String generateDataFromSchema(StringSchema schema) {
        int min = Optional.ofNullable(schema.getMinLength()).orElse(1);
        int max = Optional.ofNullable(schema.getMaxLength()).orElse(10);

        // TODO add support for pattern
        // regexp -> string
        // https://github.com/mifmif/Generex
        // https://github.com/MukulShukla/xeger

        if (max < min) {
            throw new JsonGenerationException("String: can't generate string that satisfy maxLength and minLength");
        }

        Random random = new Random();
        int maxDerived = Math.max(random.nextInt(max+1 - min) + min, 1);

        FormatValidator formatValidator = schema.getFormatValidator();
        if (formatValidator instanceof EmailFormatValidator) {
            if (maxDerived < 3) {
                throw new JsonGenerationException("String: can't generate email less than 3 symbols long");
            }
            maxDerived = (maxDerived - 1) / 2;
            return getRandomString(maxDerived) + "@" + getRandomString(maxDerived);
        }

        return getRandomString(maxDerived);
    }
}
