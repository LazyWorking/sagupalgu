package com.lazyworking.sagupalgu.user.form;

import com.lazyworking.sagupalgu.category.domain.Category;
import com.lazyworking.sagupalgu.user.domain.ReportedUsers;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/*
 * author: JehyunJung
 * purpose: reportUserForm for UsedItem
 * version: 1.0
 */
@Data
@NoArgsConstructor
public class ReportUserForm {
    private Long reporterUserId;

    @NotBlank
    @Length(max=20)
    private String targetUserEmail;

    @NotBlank
    @Length(max=255)
    private String reportContext;

    public ReportUserForm(Long reporterUserId) {
        this.reporterUserId = reporterUserId;
    }

    public ReportUserForm(Long reporterUserId, String targetUserEmail, String reportContext) {
        this.reporterUserId = reporterUserId;
        this.targetUserEmail = targetUserEmail;
        this.reportContext = reportContext;
    }
}
