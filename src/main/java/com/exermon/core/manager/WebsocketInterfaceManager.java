package com.exermon.core.manager;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

import com.exermon.core.runtime.GameConsumer;
import com.exermon.player_module.entity.Player;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSONObject;

import com.exermon.core.annotation.WebSocketMapping;
import com.exermon.core.exception.*;

import javax.persistence.Entity;

/**
 * 函数信息
 */
class MethodInfo {
    public Method method;
    public Object controller;
    public WebSocketMapping attr;

    public String fullRoute;

    public MethodInfo(String fullRoute, Method method,
                      Object controller, WebSocketMapping attr){
        this.fullRoute = fullRoute;
        this.method = method;
        this.controller = controller;
        this.attr = attr;
    }
}

@Component
public class WebsocketInterfaceManager extends ApplicationObjectSupport implements CommandLineRunner {

    // WS接口映射
    private static final Map<String, MethodInfo> interfaces = new HashMap<>();

    // 上下文对象
    private static ApplicationContext applicationContext;

    /**
     * 实例化之后执行
     */
    @Override
    public void run(String... args) throws Exception {

        applicationContext = getApplicationContext();
        if (applicationContext == null) return;

        var controllers =
                applicationContext.getBeansWithAnnotation(Controller.class);

        for (var value : controllers.values()) {
            var type = AopUtils.getTargetClass(value);
            var attr = type.getAnnotation(WebSocketMapping.class);
            if (attr == null) continue;

            System.out.printf("Scanning: %s\n", value);
            registerControllerInterfaces(attr.value(), type, value);
        }
        System.out.println("Scanning finished!");
    }

    /**
     * 注册 Controller 的 WebSocket 接口
     * @param type Controller 类型
     */
    void registerControllerInterfaces(String baseRoute, Class<?> type, Object value) {
        var methods = type.getMethods();
        for(var method : methods){
            var attr = method.getDeclaredAnnotation(WebSocketMapping.class);
            if (attr == null) continue;

            var route = baseRoute + attr.value();
            var info = new MethodInfo(route, method, value, attr);
            interfaces.put(route, info);

            System.out.printf("%s => %s\n", route, method);
        }
    }

    /**
     * 执行
     * @param consumer Consumer
     * @param route 路由
     * @param data 数据
     * @return 返回结果数据
     */
    public static JSONObject invoke(GameConsumer consumer, String route, JSONObject data) throws GameException {
        var info = interfaces.get(route);
        if (info == null)
            throw new GameException(ErrorType.InvalidRoute);

        var params = makeParams(consumer, data, info);

        return doInvoke(info.method, params);
    }

    /**
     * 装配参数
     */
    static Object[] makeParams(GameConsumer consumer,
                               JSONObject data, MethodInfo info) throws GameException {
        var method = info.method;
        var params = method.getParameters();
        var invokeParams = new Object[params.length];
        invokeParams[0] = consumer;

        for(int i = 1; i < params.length; ++i){
            // 单个参数
            var param = params[i];
            var type = param.getType();
            var name = param.getName();

            try {
                // 如果是 Player，需要特殊处理
                var iParam = (type == Player.class ?
                        consumer.player : data.getObject(name, type));

                if (iParam == null)
                    throw new GameException(ErrorType.ParameterError);

                invokeParams[i] = iParam;
            } catch (Exception e) {
                throw new GameException(ErrorType.ParameterError);
            }
        }
        return invokeParams;
    }

    /**
     * 实际执行函数
     */
    static JSONObject doInvoke(Method method, Object[] invokeParams) throws GameException {
        try {
            var controller = applicationContext.getBean(method.getDeclaringClass());
            return (JSONObject) method.invoke(controller, invokeParams);
        } catch (InvocationTargetException e) {
            var exce = e.getTargetException();
            if (exce instanceof GameException)
                throw (GameException)exce;

            throw new GameException(ErrorType.UnknownError);
        } catch (IllegalAccessException | BeansException e) {
            throw new GameException(ErrorType.UnknownError);
        }
    }

}
