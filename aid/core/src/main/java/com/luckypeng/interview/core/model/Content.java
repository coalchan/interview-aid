package com.luckypeng.interview.core.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author coalchan
 * @since 1.0
 */
@Data
@NoArgsConstructor
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
     * 路径，唯一标识
     */
    private String path;

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
    private Map<String, Content> children = new HashMap<>(16);

    public Content(ContentType type, String id, String path, String title) {
        this.type = type;
        this.id = id;
        this.path = path;
        this.title = title;
    }

    public String prettyTitle() {
        return path + "\n" + title;
    }

    public String pretty() {
        return path + "\n" + title + "\n\n答案：\n" + solution + "\n";
    }
}
