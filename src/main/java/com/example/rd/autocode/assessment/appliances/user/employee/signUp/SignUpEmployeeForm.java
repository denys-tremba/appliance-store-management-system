package com.example.rd.autocode.assessment.appliances.user.employee.signUp;

import com.example.rd.autocode.assessment.appliances.misc.infrastructure.validation.NotExistingUser;
import com.example.rd.autocode.assessment.appliances.misc.infrastructure.validation.Password;
import com.example.rd.autocode.assessment.appliances.misc.infrastructure.validation.PasswordsMatch;
import com.example.rd.autocode.assessment.appliances.misc.infrastructure.validation.ValidationGroupOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@NotExistingUser(groups = ValidationGroupOne.class)
@PasswordsMatch(groups = ValidationGroupOne.class)
public class SignUpEmployeeForm {
    @NotBlank String name;
    @NotBlank String email;
    @NotBlank
    @Password(groups = ValidationGroupOne.class)
    String password;
    @NotBlank String passwordRepeated;
    @NotBlank String department;
}
