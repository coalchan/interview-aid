package com.luckypeng.interview.core;

import com.luckypeng.interview.core.model.Content;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class AidTest {
    private Aid aid;

    @Before
    public void before() {
        aid = new Aid("../../contents");
    }

    @Test
    public void randomContent() {
        for (int i = 0; i < 100; i++) {
            Content content = aid.randomContent();
            assertNotNull(content);
            System.out.println("exercise:");
            System.out.println(content.getTitle());
            System.out.println("solution:");
            System.out.println(content.getSolution());
        }
    }

    @Test
    public void randomContentWithPath() {
        for (int i = 0; i < 100; i++) {
            Content content = aid.randomContent("01-01");
            assertNotNull(content);
            System.out.println("exercise:");
            System.out.println(content.getTitle());
            System.out.println("solution:");
            System.out.println(content.getSolution());
        }
    }

    @Test
    public void randomContentWithSize() {
        for (int i = 0; i < 10; i++) {
            System.out.println("第 " + i + " 几次测试");
            List<Content> contents = aid.randomContent(3);
            contents.forEach(content -> {
                System.out.println("exercise:");
                System.out.println(content.getTitle());
                System.out.println("solution:");
                System.out.println(content.getSolution());
            });
        }
    }

    @Test
    public void randomContentWithSizeAndPath() {
        for (int i = 0; i < 10; i++) {
            System.out.println("\n第 " + i + " 几次测试");
            List<Content> contents = aid.randomContent(2, "01");
            contents.forEach(content -> {
                System.out.println("exercise:");
                System.out.println(content.getTitle());
                System.out.println("solution:");
                System.out.println(content.getSolution());
            });
        }
    }
}