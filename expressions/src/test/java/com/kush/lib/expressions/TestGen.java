package com.kush.lib.expressions;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kush.lib.expressions.gen.GenExpressionField;
import com.kush.lib.expressions.gen.GenExpressionFields;

public class TestGen {

    @Test
    public void toJson() throws Exception {
        GenExpressionField left = new GenExpressionField("left");
        GenExpressionField right = new GenExpressionField("right");
        List<GenExpressionField> childExpressions = Arrays.asList(left, right);
        GenExpressionFields fields = new GenExpressionFields(childExpressions);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(fields);
        System.out.println(json);
    }

    @Test
    public void fromJson() throws Exception {
        String json = "{"
                + "\"childExpressions\": ["
                + "    {"
                + "        \"name\": \"left\""
                + "    },"
                + "    {"
                + "        \"name\": \"right\""
                + "    }"
                + "]}";

        ObjectMapper mapper = new ObjectMapper();
        GenExpressionFields fields = mapper.readValue(json, GenExpressionFields.class);
        System.out.println(fields);
    }
}
