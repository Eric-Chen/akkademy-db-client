package com.akkademy;

import lombok.ToString;

/**
 * Created by eric on 31/07/2017.
 */
@ToString
public class GetValue {

    public final String key;

    public GetValue(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
