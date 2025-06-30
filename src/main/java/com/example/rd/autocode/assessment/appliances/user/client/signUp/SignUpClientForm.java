package com.example.rd.autocode.assessment.appliances.user.client.signUp;

import com.example.rd.autocode.assessment.appliances.misc.infrastructure.validation.NotExistingUser;
import com.example.rd.autocode.assessment.appliances.misc.infrastructure.validation.PasswordsMatch;
import com.example.rd.autocode.assessment.appliances.misc.infrastructure.validation.ValidationGroupOne;
import com.example.rd.autocode.assessment.appliances.user.manageAccount.EnterPasswordForm;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@NotExistingUser(groups = ValidationGroupOne.class)
@PasswordsMatch(groups = ValidationGroupOne.class)
public class SignUpClientForm extends EnterPasswordForm {
    @NotBlank String name;
    @NotBlank String email;
    @NotBlank String card;
}
