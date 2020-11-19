package com.exermon.core.service;

import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.*;
import javax.websocket.server.*;

import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSONObject;

import com.exermon.core.runtime.GameConsumer;

@ServerEndpoint("/webSocket/{uuid}")
@Component
public class GameServer {

    // Session 池
    private static final ConcurrentHashMap<String, GameConsumer>
            consumerPools = new ConcurrentHashMap<>();

    // region 基本操作

    /**
     * 建立连接回调
     * @param session 对话
     * @param uuid UUID
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "uuid") String uuid){

        consumerPools.put(uuid, new GameConsumer(uuid, session));

        System.out.printf("uuid: %s is connected! [%d]\n",
                uuid, consumerPools.size());
    }

    /**
     * 断开连接回调
     * @param uuid UUID
     */
    @OnClose
    public void onClose(@PathParam(value = "uuid") String uuid){

        consumerPools.remove(uuid);

        System.out.printf("uuid: %s is disconnected! [%d]\n",
                uuid, consumerPools.size());
    }

    /**
     * 接收客户端信息
     * @param message 接收的JSON字符串
     */
    @OnMessage
    public void onMessage(String message) {
        System.out.println("Receive: " + message);

        var json = JSONObject.parseObject(message);

        var uuid = json.getString("uuid");

        var consumer = consumerPools.get(uuid);
        if (consumer == null) return; // 如果没有找到 Consumer 测无效

        consumer.receive(json);
    }

    //错误时调用
    @OnError
    public void onError(Session session, Throwable throwable){
        System.out.println("发生错误");
        throwable.printStackTrace();
    }

    // endregion

    // region 数据获取

    /**
     * 获取 Consumer
     * @param session 会话
     * @return 返回 Consumer
     */
    public GameConsumer getGameConsumer(Session session) {
        for (var consumer : consumerPools.values())
            if (consumer.session == session) return consumer;
        return null;
    }

    /**
     * 获取 UUID
     * @param session 会话
     * @return 返回 UUID
     */
    public String getUUID(Session session) {
        for (var consumer : consumerPools.values())
            if (consumer.session == session) return consumer.uuid;
        return null;
    }

    // endregion
}