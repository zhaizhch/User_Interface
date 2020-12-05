package com.example.demo.common.conf;

public enum DisplayErrorCode {
    USER_LOGOUT_FAILURE(234, "注销失败"),
    USER_STATUS_CHECK(230, "请登录"),
    USER_RESULTFAILURE(234, "用户名或密码错误"),
    LOGIN_MESSAGE_UPDATE_FAILURE(235, "登录信息更新失败"),
    USER_NAME_LENGTH_ERROR(1021, "用户名最长32位，已超过长度限制"),
    USER_NAME_EXIST_ERROR(1022, "用户名已存在"),
    USER_PWD_FORMAT_ERROR(1023, "密码不符合要求，长度大于等于8位数、包含大写字母、小写字母、数字和特殊字符"),
    USER_AUTHORITY_CHECK_ERROR(1025, "用户权限格式错误"),
    USER_NO_OPERATION_AUTHORITY(1027, "用户无操作权限"),
    USER_RESETPWD_ERROR(1029, "用户重置密码失败"),
    USER_RAWPWD_CHECK_ERROR(1030, "用户初始密码错误"),
    USER_RAWPWD_NULL_ERROR(1031, "用户初始密码不能为空"),
    DB_QUERY_ERROR(1005, "数据查询失败"),
    TOKEN_QUERY_ERROR(231, "认证失败"),
    DB_UPDATE_ERROR(1006, "数据修改失败"),
    DB_DELETE_ERROR(1007, "数据删除失败"),
    DB_INSERT_ERROR(1008, "数据增加失败"),
    PARAM_INVALID_ERROR(1002, "输入参数校验不通过");

    private final Integer val;
    private String message;

    DisplayErrorCode(Integer val) {
        this.val = val;
    }

    DisplayErrorCode(Integer val, String message) {
        this.val = val;
        this.message = message;
    }

    public Integer getVal() {
        return val;
    }

    public String getMessage() {
        return message;
    }


}
