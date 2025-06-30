package com.example.rd.autocode.assessment.appliances.misc.infrastructure.validation;

import jakarta.validation.GroupSequence;
import jakarta.validation.groups.Default;

@GroupSequence({Default.class, ValidationGroupOne.class})
public interface ValidationGroupSequence {
}
