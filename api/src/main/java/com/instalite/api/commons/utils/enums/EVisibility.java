package com.instalite.api.commons.utils.enums;

public enum EVisibility {

    PUBLIC(0),
    PRIVATE(1),
    HIDDEN(2);

    private final int value;

    EVisibility(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }

}
