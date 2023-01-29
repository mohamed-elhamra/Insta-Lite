package com.instalite.api.commons.utils.enums;

import java.util.Objects;

public enum EVisibility {

    PUBLIC("0"),
    PRIVATE("1"),
    HIDDEN("2");

    private final String value;

    EVisibility(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }

    public static EVisibility fromValue(String value){
        for(EVisibility visibility: EVisibility.values()){
            if(Objects.equals(visibility.getValue(), value)){
                return visibility;
            }
        }
        throw new IllegalArgumentException("No visibility with value " + value + " found");
    }

}
