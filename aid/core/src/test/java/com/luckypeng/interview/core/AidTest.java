package com.luckypeng.interview.core;

import com.luckypeng.interview.core.model.Content;
import org.junit.Test;

import static org.junit.Assert.*;

public class AidTest {

    @Test
    public void randomContent() {
        Aid aid = new Aid("../../contents");
        for (int i = 0; i < 100; i++) {
            Content content = aid.randomContent();
            assertNotNull(content);
            System.out.println("exercise:");
            System.out.println(content.getTitle());
            System.out.println("solution:");
            System.out.println(content.getSolution());
        }
    }
}