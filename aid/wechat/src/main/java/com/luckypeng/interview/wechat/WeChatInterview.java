package com.luckypeng.interview.wechat;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.luckypeng.interview.core.Aid;

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
}
