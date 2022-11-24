package com.lazyworking.sagupalgu.item.form;

import com.lazyworking.sagupalgu.category.domain.Category;
import com.lazyworking.sagupalgu.global.converter.BooleanToYNConverter;
import javax.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
/*
 * author: JehyunJung
 * purpose: saveform for UsedItem
 * version: 1.0
 */
@Data
public class UsedItemSaveForm {
    @NotBlank
    @Length(max=20)
    private String name;

    @NotNull
    private Integer price;

    @Length(max = 40)
    private String context;

    @NotNull
    private Category category;
}
