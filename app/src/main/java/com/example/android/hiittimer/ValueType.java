package com.example.android.hiittimer;

public enum ValueType {

    PREPARE(0,"prepare"),
    WORKOUT(0,"workout"),
    INTERVAL(0,"interval"),
    COOL_DOWN(0,"coolDown"),
    CYCLE(1,"cycle"),
    SET(1,"set");

    int type;
    String valueType;

    ValueType(int type , String valuetype) {
        this.type = type;
        this.valueType = valuetype;
    }

    public int getType() {
        return type;
    }

    public String getValueType() {
        return valueType;
    }
}
