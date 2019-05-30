package com.luckypeng.interview.core.model;

import lombok.Data;

import java.util.List;

/**
 * @author coalchan
 */
@Data
public class Content {
    public static final String FILE_KEY_TITLE = "title";
    public static final String FILE_KEY_DESC = "desc";
    public static final String FILE_KEY_TAGS = "tags";

    /**
     * 内容类型
     */
    private ContentType type;

    /**
     * 序号
     */
    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 介绍
     */
    private String desc;

    /**
     * 标签
     */
    private List<String> tags;

    /**
     * 原文
     */
    private String rawText;

    /**
     * 答案
     */
    private String solution;

    /**
     * 下级
     */
    private List<Content> children;
}
