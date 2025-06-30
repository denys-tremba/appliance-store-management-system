package com.example.rd.autocode.assessment.appliances;

import org.junit.platform.suite.api.IncludeClassNamePatterns;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.example.rd.autocode.assessment.appliances")
@IncludeClassNamePatterns(".*ServiceTest")
public class ServiceTestSuite {
}
