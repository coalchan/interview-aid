package com.luckypeng.interview.wechat;

import io.github.biezhi.wechat.WeChatBot;
import io.github.biezhi.wechat.api.annotation.Bind;
import io.github.biezhi.wechat.api.constant.Config;
import io.github.biezhi.wechat.api.enums.AccountType;
import io.github.biezhi.wechat.api.enums.MsgType;
import io.github.biezhi.wechat.api.model.WeChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author coalchan
 */
@Slf4j
public class WeChatInterviewBot extends WeChatBot {
    public WeChatInterviewBot(Config config) {
        super(config);
    }

    @Bind(msgType = MsgType.TEXT, accountType = AccountType.TYPE_FRIEND)
    public void handleText(WeChatMessage message) {
        if (StringUtils.isNotEmpty(message.getName())) {
            log.info("接收到 [{}] 的消息: {}", message.getName(), message.getText());
            this.sendMsg(message.getFromUserName(), "自动回复: " + message.getText());
        }

    }

    public static void main(String[] args) {
        new WeChatInterviewBot(Config.me().autoLogin(true).showTerminal(true)).start();
    }

}
