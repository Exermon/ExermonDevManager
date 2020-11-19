package com.exermon.core.runtime;

import java.io.IOException;
import javax.websocket.Session;

import com.alibaba.fastjson.JSONObject;

import com.exermon.core.exception.ErrorType;
import com.exermon.core.exception.GameException;
import com.exermon.core.manager.WebsocketInterfaceManager;

import com.exermon.player_module.entity.Player;

public class GameConsumer {

    public final String uuid;
    public final Session session;

    public Player player = null;

    public GameConsumer(String uuid, Session session){
        this.uuid = uuid; this.session = session;
    }

    // region 通讯

    // region 接收/发送操作

    /**
     * 发送消息
     * @param message 消息
     */
    public void send(String message) {
        try {
            System.out.println("send: " + message);
            synchronized (session) {
                session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 发送JSON数据
     * @param json JSON数据
     */
    public void send(JSONObject json) {
        send(json.toJSONString());
    }

    /**
     * 接收数据
     * @param json JSON数据
     */
    public void receive(JSONObject json) {
        var index = json.getString("index");
        var route = json.getString("route");
        var data = json.getJSONObject("data");

        processRequest(uuid, route, index, data);
    }

    // endregion

    // region 请求处理

    /**
     * 处理请求
     * @param uuid UUID
     * @param index 请求编号
     * @param route 请求路由
     * @param data 请求数据
     */
    void processRequest(String uuid, String route, String index, JSONObject data) {

        var res = generateResponseFrame(uuid, route, index);

        try {
            res.put("data", WebsocketInterfaceManager.invoke(this, route, data));
            res.put("status", ErrorType.Success.getCode());

        } catch (GameException e) {
            e.printStackTrace();

            res.put("status", e.getCode());
            res.put("errmsg", e.getDesc());
        }

        send(res);
    }

    /**
     * 生成返回数据框架
     * @param uuid UUID
     * @param index 请求编号
     * @param route 请求路由
     * @return 返回数据框架
     */
    JSONObject generateResponseFrame(String uuid, String route, String index){
        var res = new JSONObject();

        res.put("uuid", uuid); res.put("method", "response");
        res.put("route", route); res.put("index", index);

        return res;
    }

    // endregion

    // endregion


}
