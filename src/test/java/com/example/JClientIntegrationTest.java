package com.example;

import com.akkademy.JClient;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

/**
 * Created by eric on 28/07/2017.
 */
public class JClientIntegrationTest {

    JClient client = new JClient("127.0.0.1:2552");

    @Test
    public void itShouldSetRecord() throws Exception {
        client.set("123", 123);
        Integer result = (Integer) ((CompletableFuture) client.get("123")).get();
        assert(result == 123);
    }

}
