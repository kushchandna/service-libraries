package com.kush.lib.group.service;

import java.util.Set;

import org.junit.Test;
import org.reflections.Reflections;

import com.kush.lib.service.server.annotations.Service;

public class TestClass {

    @Test
    public void testName() throws Exception {
        Reflections reflections = new Reflections();
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Service.class);
        System.out.println(typesAnnotatedWith);
    }
}
