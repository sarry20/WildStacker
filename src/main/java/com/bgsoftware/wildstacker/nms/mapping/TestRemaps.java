package com.bgsoftware.wildstacker.nms.mapping;

import com.bgsoftware.common.reflection.ReflectField;
import com.bgsoftware.common.reflection.ReflectMethod;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class TestRemaps {

    private static final Logger logger = Logger.getLogger("TestRemaps");

    private static final ReflectField<Field> REFLECT_FIELD_INNER_FIELD = new ReflectField<>(ReflectField.class, Field.class, "field");
    private static final ReflectField<Method> REFLECT_METHOD_INNER_METHOD = new ReflectField<>(ReflectMethod.class, Method.class, "method");

    private TestRemaps() {

    }

    public static void testRemapsForClass(File mappingsFile, Class<?>... classes) throws
            IllegalAccessException, NullPointerException, IOException, RemapFailure {
        for (Class<?> clazz : classes) {
            logger.info("Starting remaps test for " + clazz.getName());

            Map<String, Remapped> remappedMap = MappingParser.parseRemappedMap(mappingsFile);

            try {
                for (Field field : clazz.getDeclaredFields()) {
                    if (Modifier.isStatic(field.getModifiers())) {
                        Remap remap = field.getAnnotation(Remap.class);
                        if (remap != null) {
                            logger.info("Testing field " + field.getName());
                            testRemap(remappedMap, remap, getRemappedName(field));
                        }
                    }
                }

                for (Method method : clazz.getDeclaredMethods()) {
                    Remap remap = method.getAnnotation(Remap.class);
                    if (remap != null) {
                        logger.info("Testing method " + method.getName());
                        testRemap(remappedMap, remap, null);
                    }
                }
            } catch (Exception error) {
                logger.info("Failed remaps test for " + clazz.getName() + ":");
                throw error;
            }

            logger.info("Finished remaps tests for " + clazz.getName());
        }
    }

    @Nullable
    private static String getRemappedName(Field field) throws IllegalAccessException {
        field.setAccessible(true);
        Object fieldValue = field.get(null);

        if (fieldValue instanceof ReflectField) {
            Field innerField = REFLECT_FIELD_INNER_FIELD.get(fieldValue);
            if (innerField != null)
                return innerField.getName();
        } else if (fieldValue instanceof ReflectMethod) {
            Method innerMethod = REFLECT_METHOD_INNER_METHOD.get(fieldValue);
            if (innerMethod != null)
                return innerMethod.getName();
        }

        return null;
    }

    private static void testRemap(Map<String, Remapped> remappedMap, Remap remap, @Nullable String remappedName) {
        String classPath = remap.classPath();

        Remapped remapped = remappedMap.get(classPath);

        if (remapped == null)
            throw new NullPointerException("Cannot find remapped object for classPath " + classPath);

        String name = remap.name();
        Remap.Type type = remap.type();

        List<String> obfuscatedNames = remapped.getObfuscatedNames(name, type);

        if (obfuscatedNames.isEmpty())
            throw new NullPointerException("Cannot find obfuscated name for " + name + ":" + type);

        String remappedNameOrDefault = remappedName == null ? remap.remappedName() : remappedName;

        if (!obfuscatedNames.contains(remappedNameOrDefault))
            throw new RemapFailure("Incorrect remap: Expected " + obfuscatedNames + ", found " + remappedNameOrDefault);
    }

}
