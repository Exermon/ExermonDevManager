package com.exermon.core.controller;

import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import com.exermon.core.annotation.WebSocketMapping;
import com.exermon.core.runtime.GameConsumer;

@RestController
@WebSocketMapping("/websocket")
public class SocketController {

    @WebSocketMapping("/test")
    public JSONObject test(GameConsumer consumer, int p1) {
        var res = new JSONObject();
        res.put("p1", p1 * p1);
        return res;
    }
}