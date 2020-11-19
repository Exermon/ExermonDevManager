package com.exermon.core.exception;

public enum ErrorType {

    /**
     * 异常类型
     */
    UnknownError(-1,"服务器发生错误，请联系管理员！"), // 未知错误
    Success(0,""), // 成功，无错误
    InvalidRequest(1,"非法的请求方法！"), // 非法的请求方法
    ParameterError(2,"参数错误！"), // 参数错误
    InvalidRoute(3,"非法的请求路由！"), // 非法路由
    PermissionDenied(4,"无权操作！"), // 无权操作
    NoCurVersion(5,"未设置当前版本，请联系管理员！"), // 未设置当前版本
    RequestUpdate(6,"当前客户端版本过旧，请更新游戏！"), // 需要更新
    ErrorVersion(7,"错误的客户端版本，请更新游戏！"), // 错误的游戏版本
    InvalidUserOper(10,"无效的用户操作！"), // 无效的用户操作
    ;

    ErrorType(int code) {
        this.code = code;
    }
    ErrorType(int code, String desc) {
        this.code = code; this.desc = desc;
    }

    int code; String desc = "";

    public int getCode() { return code; }
    public String getDesc() { return desc; }

}
