package com.lazyworking.sagupalgu.user.converter;

import com.lazyworking.sagupalgu.user.domain.Gender;

import javax.persistence.AttributeConverter;
import java.util.EnumSet;
import java.util.NoSuchElementException;

public class GenderConverter implements AttributeConverter<Gender,String> {
    @Override
    public String convertToDatabaseColumn(Gender attribute) {
        return attribute.getCode();
    }

    @Override
    public Gender convertToEntityAttribute(String dbData) {
        return EnumSet.allOf(Gender.class).stream()
                .filter((g) -> g.getCode().equals(dbData))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException());
    }
}
