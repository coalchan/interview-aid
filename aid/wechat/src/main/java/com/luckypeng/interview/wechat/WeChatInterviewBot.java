package com.luckypeng.interview.wechat;

import com.luckypeng.interview.core.model.Content;
import com.luckypeng.interview.core.util.ContentUtils;
import com.luckypeng.interview.core.util.ObjectUtils;
import io.github.biezhi.wechat.WeChatBot;
import io.github.biezhi.wechat.api.annotation.Bind;
import io.github.biezhi.wechat.api.constant.Config;
import io.github.biezhi.wechat.api.enums.AccountType;
import io.github.biezhi.wechat.api.enums.MsgType;
import io.github.biezhi.wechat.api.model.WeChatMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author coalchan
 */
@Slf4j
public class WeChatInterviewBot extends WeChatBot {
    private WeChatInterview interview;

    public WeChatInterviewBot(Config config, String contentPath) {
        super(config);
        interview = new WeChatInterview(contentPath);
    }

    private static final Pattern KEYWORD_PATH = Pattern.compile("[\\d+\\-]+\\d+");
    private static final Pattern KEYWORD_REVIEW = Pattern.compile("复习 *(.*)?");
    private static final Pattern KEYWORD_INTERVIEW = Pattern.compile("面试 *(.*)?");
    private static final Pattern KEYWORD_SOLUTION = Pattern.compile("答案|solution");
    private static final Pattern KEYWORD_NEXT = Pattern.compile("下一题|next");

    /**
     * 当前答题内容
     */
    private static Content currentContent;

    /**
     * 当前答题路径
     */
    private static String currentPath;

    @Bind(msgType = MsgType.TEXT, accountType = AccountType.TYPE_FRIEND)
    public void handleText(WeChatMessage message) {
        String msg = message.getText();

        // 仅当信息是自己发送时
        if (message.getMineUserName().equals(message.getFromUserName())) {
            if (KEYWORD_PATH.matcher(msg).matches()) {
                sendMsgToFileHelper(interview.getContent(msg));
            } else if (KEYWORD_REVIEW.matcher(msg).matches()) {
                Matcher matcher = KEYWORD_REVIEW.matcher(msg);
                String path = null;
                if (matcher.find()) {
                    path = matcher.group(1);
                }
                if (ObjectUtils.isEmpty(path)) {
                    path = ContentUtils.CONTENT_ROOT_ID;
                }
                currentPath = path;
                currentContent = interview.randomContent(path);
                sendMsgToFileHelper(currentContent.prettyTitle());
            } else if (KEYWORD_SOLUTION.matcher(msg).matches()) {
                sendMsgToFileHelper(currentContent.getSolution());
            } else if (KEYWORD_NEXT.matcher(msg).matches()) {
                currentContent = interview.randomContent(currentPath);
                sendMsgToFileHelper(currentContent.prettyTitle());
            } else if (KEYWORD_INTERVIEW.matcher(msg).matches()) {
                // TODO
            } else {
                this.sendMsg(message.getFromUserName(), interview.getHelp());
            }
        }
    }

    public static void main(String[] args) {
        new WeChatInterviewBot(
                Config.me().autoLogin(true).showTerminal(true), "../contents"
        ).start();
    }

}
