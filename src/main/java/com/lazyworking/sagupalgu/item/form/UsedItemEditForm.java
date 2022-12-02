package com.lazyworking.sagupalgu.item.form;
import com.lazyworking.sagupalgu.category.domain.Category;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;

/*
 * author: JehyunJung
 * purpose: editForm for UsedItem
 * version: 1.0
 */
@Data
public class UsedItemEditForm {
    @NotNull
    private long id;

    @NotNull
    @Length(max=20)
    private String name;

    @NotNull
    private Integer price;

    @NotNull
    private boolean ifSelled;

    @Length(max = 40)
    private String context;

    @NotNull
    private Category category;
}
