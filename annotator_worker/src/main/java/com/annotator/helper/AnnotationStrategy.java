package com.annotator.helper;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class AnnotationStrategy<T> extends HeaderColumnNameTranslateMappingStrategy<T> {
    Map<String, String> columnMap = new HashMap<>();

    public AnnotationStrategy(final Class<? extends T> clazz) {

        for (final Field field : clazz.getDeclaredFields()) {
            final CsvBindByName annotation = field.getAnnotation(CsvBindByName.class);
            if (annotation != null) {

                columnMap.put(field.getName().toUpperCase(), annotation.column());
            }
        }
        setType(clazz);
    }

    @Override
    public String getColumnName(final int col) {
        final String name = headerIndex.getByPosition(col);
        return name;
    }

    public String getColumnName1(final int col) {
        String name = headerIndex.getByPosition(col);
        if (name != null) {
            name = columnMap.get(name);
        }
        return name;
    }

    @Override
    public String[] generateHeader(final T bean) throws CsvRequiredFieldEmptyException {
        final String[] result = super.generateHeader(bean);
        for (int i = 0; i < result.length; i++) {
            result[i] = getColumnName1(i);
        }
        return result;
    }
}