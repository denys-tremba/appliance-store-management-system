package com.example.rd.autocode.assessment.appliances.order;

public enum OrderSort {
    EMPLOYEE_NAME_ASC("employee.name,asc"),
    EMPLOYEE_NAME_DESC("employee.name,desc"),
    //        CLIENT_NAME_ASC("client.name,asc"),
    CLIENT_NAME_DESC("client.name,desc"),
    CREATION_TIMESTAMP_ASC("createdAt,asc"),
    CREATION_TIMESTAMP_DESC("createdAt,desc");

    private final String paramValue;

    OrderSort(String paramValue) {
        this.paramValue = paramValue;
    }

    public String paramValue() {
        return paramValue;
    }
}
