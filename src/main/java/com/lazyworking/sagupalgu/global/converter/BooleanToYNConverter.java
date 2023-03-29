package com.lazyworking.sagupalgu.global.converter;

import jakarta.persistence.AttributeConverter;
/*
 * author: JehyunJung
 * purpose: BooleanToStringConverter
 * version: 1.0
 */
public class BooleanToYNConverter implements AttributeConverter<Boolean, String> {
    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
        return (attribute != null && attribute) ? "Y" : "N";
    }

    @Override
    public Boolean convertToEntityAttribute(String dbData) {
        return dbData.equals("Y");
    }
}
