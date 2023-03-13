package com.lazyworking.sagupalgu.item.form;

import com.lazyworking.sagupalgu.category.domain.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
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
