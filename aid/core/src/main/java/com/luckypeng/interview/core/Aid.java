package com.luckypeng.interview.core;

import com.luckypeng.interview.core.model.Content;
import com.luckypeng.interview.core.util.AssertionUtils;
import com.luckypeng.interview.core.util.ContentUtils;
import com.luckypeng.interview.core.util.ObjectUtils;
import org.apache.commons.lang3.RandomUtils;

public class Aid {
    private Content content;

    private static final String SEPARATOR_PATH = "-";

    public Aid(String contentPath) {
        this.content = ContentUtils.listContent(contentPath);
    }

    public Content randomContent() {
        return randomContent(content);
    }

    public Content randomContent(String exercisePath) {
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
