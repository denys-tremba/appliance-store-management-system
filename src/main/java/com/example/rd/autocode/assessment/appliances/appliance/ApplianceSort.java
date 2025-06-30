package com.example.rd.autocode.assessment.appliances.appliance;

public enum ApplianceSort {
    NAME_ACS("name,asc"), NAME_DESC("name,desc"), PRICE_ACS("price,asc"), PRICE_DESC("price,desc");

    private final String paramValue;

    ApplianceSort(String paramValue) {
        this.paramValue = paramValue;
    }

    public String paramValue() {
        return paramValue;
    }
}
