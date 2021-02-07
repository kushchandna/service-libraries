package com.kush.lib.expressions.gen;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kush.lib.expressions.Expression;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

public class ExpressionsGenerator {

    private final String expressionsPackage;

    public ExpressionsGenerator(String expressionsPackage) {
        this.expressionsPackage = expressionsPackage;
    }

    public void generate(Stream<File> jsonFilesStream) {
        List<Class<? extends Expression>> expressionClasses = new LinkedList<>();
        ObjectMapper mapper = new ObjectMapper();
        jsonFilesStream.forEach(jsonFile -> {
            GenExpressionFields fields = readFields(mapper, jsonFile);
            String fileNameWithoutExtension = getFileNameWithoutExtension(jsonFile);
            GenExpression expression = new GenExpression(fileNameWithoutExtension, fields);
            Class<? extends Expression> exprClass = generateExpressionClass(expression);
            expressionClasses.add(exprClass);
        });
    }

    private Class<? extends Expression> generateExpressionClass(GenExpression expression) {
        Iterable<MethodSpec> methodSpecs = getMethodSpecs(expression);
        TypeSpec typeSpec = TypeSpec.interfaceBuilder(generateExpressionInterfaceName(expression))
            .addSuperinterface(Expression.class)
            .addMethods(methodSpecs)
            .build();

        JavaFile javaFile = JavaFile.builder(expressionsPackage, typeSpec)
            .skipJavaLangImports(true)
            .build();

        javaFile.toJavaFileObject();
        return null;
    }

    private Iterable<MethodSpec> getMethodSpecs(GenExpression expression) {

        List<MethodSpec> methodSpecs = new LinkedList<>();

        GenExpressionFields fields = expression.getExpressionFields();
        fields.getChildExpressions().forEach(expr -> {
            MethodSpec methodSpec = MethodSpec.methodBuilder(generateMethodName(expr))
                .returns(getReturnType(expr))
                .build();
            methodSpecs.add(methodSpec);
        });
        return null;
    }

    private TypeName getReturnType(GenExpressionField expr) {
        String type = expr.getType();
        if (type == null) {
            return TypeName.get(Expression.class);
        }
        return null;
    }

    private String generateMethodName(GenExpressionField expr) {
        return getGetterName(expr);
    }

    private String getGetterName(GenExpressionField expr) {
        String name = expr.getName();
        char firstChar = name.charAt(0);
        char firstCharInUpperCase = Character.toUpperCase(firstChar);
        return new StringBuilder()
            .append("get")
            .append(firstCharInUpperCase)
            .append(name.substring(1))
            .toString();
    }

    private String generateExpressionInterfaceName(GenExpression expression) {
        return expression.getName() + "Expression";
    }

    private String getFileNameWithoutExtension(File jsonFile) {
        String fileName = jsonFile.getName();
        if (fileName.endsWith(".json")) {
            fileName = fileName.substring(0, fileName.length() - ".json".length());
        }
        return fileName;
    }

    private GenExpressionFields readFields(ObjectMapper mapper, File jsonFile) {
        try {
            return mapper.readValue(jsonFile, GenExpressionFields.class);
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
