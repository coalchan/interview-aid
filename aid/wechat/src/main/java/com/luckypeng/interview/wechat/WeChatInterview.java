package com.luckypeng.interview.wechat;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.luckypeng.interview.core.Aid;
import com.luckypeng.interview.core.model.Content;
import com.luckypeng.interview.core.model.ContentType;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author coalchan
 */
public class WeChatInterview {
    private Aid aid;

    /**
     * 面试回话保存 30 分钟
     */
    private Cache<String, Boolean> interviewSession = CacheBuilder.newBuilder()
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .build();

    public WeChatInterview(String contentPath) {
        this.aid = new Aid(contentPath);
    }

    /**
     * 获取大纲
     * @return
     */
    public String getOutline() {
        return aid.getContent().getRawText();
    }

    public String getHelp() {
        return getOutline() + "\n"
                + "操作指南：\n"
                + "回复'面试'开始面试 ";
    }

    /**
     * 开启会话
     * @return
     */
    public String startSession() {
        String sessionId = UUID.randomUUID().toString();
        interviewSession.put(sessionId, true);
        return sessionId;
    }

    /**
     * 结束会话
     * @param sessionId
     */
    public void stopSession(String sessionId) {
        interviewSession.invalidate(sessionId);
    }

    /**
     * 获取指定习题
     * @param path
     * @return
     */
    public String getContent(String path) {
        Content content = aid.getContentByPath(path);
        if (!ContentType.EXERCISE.equals(content)) {
            return content.pretty();
        } else {
            return "该习题不存在";
        }
    }

    public Content randomContent(String path) {
        return aid.randomContent(path);
    }
}
