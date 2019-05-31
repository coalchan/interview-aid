package com.luckypeng.interview.core;

import com.luckypeng.interview.core.model.Content;
import com.luckypeng.interview.core.util.AssertionUtils;
import com.luckypeng.interview.core.util.ContentUtils;
import com.luckypeng.interview.core.util.ObjectUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Aid {
    private Content content;

    private static final String SEPARATOR_PATH = "-";

    public Aid(String contentPath) {
        this.content = ContentUtils.listContent(contentPath);
    }

    /**
     * 选择不重复的习题
     * @param size
     * @return
     */
    public List<Content> randomContent(int size) {
        return randomContent(size, ContentUtils.CONTENT_ROOT_ID);
    }

    /**
     * 选择不重复的习题，可指定路径
     * @param size
     * @return
     */
    public List<Content> randomContent(int size, String exercisePath) {
        List<Content> contents = new ArrayList<>();
        Set<String> interviewed = new HashSet<>(100);
        for (int i = 0; i < size; i++) {
            Content content = null;
            while (content == null || interviewed.contains(content.getPath())) {
                content = randomContent(exercisePath);
            }
            interviewed.add(content.getPath());
            contents.add(content);
        }
        return contents;
    }

    /**
     * 随机习题
     * @return
     */
    public Content randomContent() {
        return randomContent(content);
    }

    /**
     * 随机习题（可指定路径）
     * @param exercisePath
     * @return
     */
    public Content randomContent(String exercisePath) {
        if (ContentUtils.CONTENT_ROOT_ID.equals(exercisePath)) {
            return randomContent(content);
        }
        String[] paths = exercisePath.split(SEPARATOR_PATH);
        Content tmpContent = content;
        for(String path : paths) {
            tmpContent = tmpContent.getChildren().get(path);
        }
        return randomContent(tmpContent);
    }

    /**
     * 返回指定内容中的随机习题
     * @param tmpContent
     * @return
     */
    private Content randomContent(Content tmpContent) {
        if (ObjectUtils.isNotEmpty(tmpContent.getExercises())) {
            int index = RandomUtils.nextInt(0, tmpContent.getExercises().size());
            return tmpContent.getExercises().get(index);
        } else {
            int index = RandomUtils.nextInt(1, tmpContent.getChildren().size() + 1);
            String id = ContentUtils.fixId(index);
            Content child = tmpContent.getChildren().get(id);
            AssertionUtils.isNotEmpty(tmpContent, "找不到对应的内容: " + tmpContent.getId() + "-" + id);
            return randomContent(child);
        }
    }
}
