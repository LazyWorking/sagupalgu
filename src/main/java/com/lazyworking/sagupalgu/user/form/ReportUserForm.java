package com.lazyworking.sagupalgu.user.form;

import com.lazyworking.sagupalgu.category.domain.Category;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/*
 * author: JehyunJung
 * purpose: reportUserForm for UsedItem
 * version: 1.0
 */
@Data
public class ReportUserForm {
    @NotBlank
    @Length(max=20)
    private String id;

    @NotBlank
    @Length(max=255)
    private String context;

}
