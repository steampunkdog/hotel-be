package com.netcracker.hotelbe.service;

import com.google.common.base.CaseFormat;
import com.netcracker.hotelbe.entity.ApartmentClass;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class EntityService {
    private final static Logger LOG = Logger.getLogger("ApartmentClassService");

    public Object fillFields(final Map<String, Object> fields, final Object targetObject) {
        Object object = getCopyFromObject(targetObject);
        Class clazz = object.getClass();

        fields.forEach((k, v) -> {
            StringBuilder methodName = new StringBuilder()
                    .append("set").append(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, k));
            try {
                Field field = clazz.getDeclaredField(k);
                Class fieldClass = field.getType();
                Method method = clazz.getDeclaredMethod(String.valueOf(methodName), fieldClass);
                method.invoke(object, v);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
                LOG.warning("Method " + methodName.toString() + " not found or illegal argument!");
            }

        });
        return object;
    }

    private Object getCopyFromObject(final Object object) {
        try {
            Object copy = object.getClass().newInstance();
            Class clazz = copy.getClass();
            Field[] fields = object.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                StringBuilder setMethodName = new StringBuilder()
                        .append("set").append(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, fields[i].getName()));
                StringBuilder getMethodName = new StringBuilder()
                        .append("get").append(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, fields[i].getName()));
                Class fieldClass = fields[i].getType();
                try {
                    Method setMethod = clazz.getDeclaredMethod(String.valueOf(setMethodName), fieldClass);
                    Method getMethod = clazz.getDeclaredMethod(String.valueOf(getMethodName));
                    setMethod.invoke(copy, getMethod.invoke(object));
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    LOG.warning("Method " + setMethodName.toString() + " not found or illegal argument!");
                }
            }
            return copy;
        } catch (InstantiationException | IllegalAccessException e) {
            LOG.warning("Error copying object!");
            return object;
        }
    }
}
