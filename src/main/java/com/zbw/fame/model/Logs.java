package com.zbw.fame.model;

/**
 * 日志 Model
 *
 * @auther zbw
 * @create 2017/10/11 9:57
 */
public class Logs extends BaseEntity {

    // 操作动作
    private String action;

    // 操作数据
    private String data;

    // 操作信息
    private String message;

    // 操作类型
    private String type;

    // ip地址
    private String ip;

    // 操作人
    private Integer user_id;

    // 操作时间
    private Integer created;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getCreated() {
        return created;
    }

    public void setCreated(Integer created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "Logs{" +
                "action='" + action + '\'' +
                ", data='" + data + '\'' +
                ", message='" + message + '\'' +
                ", type='" + type + '\'' +
                ", ip='" + ip + '\'' +
                ", user_id=" + user_id +
                ", created=" + created +
                '}';
    }
}
