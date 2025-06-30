package com.example.rd.autocode.assessment.appliances.user.manageAccount;

import com.example.rd.autocode.assessment.appliances.misc.infrastructure.validation.Password;
import com.example.rd.autocode.assessment.appliances.misc.infrastructure.validation.PasswordsMatch;
import com.example.rd.autocode.assessment.appliances.misc.infrastructure.validation.ValidationGroupOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@PasswordsMatch
public class EnterPasswordForm {
    @NotBlank
    @Password(groups = ValidationGroupOne.class)
    String password;
    @NotBlank
    String passwordRepeated;
}
