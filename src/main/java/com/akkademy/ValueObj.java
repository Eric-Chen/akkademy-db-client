package com.akkademy;

import lombok.ToString;

/**
 * Created by eric on 31/07/2017.
 */
@ToString
public class ValueObj {

    private final String key;

    private final Object value;

    public ValueObj(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }
}
