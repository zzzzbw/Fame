package com.zbw.fame.model;

/**
 * 属性(分类和标签) Model
 *
 * @author zbw
 * @since 2017/8/28 23:04
 */
public class Metas extends BaseEntity {

    /**
     * 属性名
     */
    private String name;

    /**
     * 属性类型
     */
    private String type;

    public Metas() {
    }

    public Metas(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Metas{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
