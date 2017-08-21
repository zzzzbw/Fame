package com.zbw.fame.model;

/**
 * 属性(分类和标签) Model
 *
 * @auther zbw
 * @create 2017/8/28 23:04
 */
public class Metas extends BaseEntity {

    //属性名
    private String name;

    //属性类型
    private String type;

    //归属文章id
    private Integer aId;

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

    public Integer getaId() {
        return aId;
    }

    public void setaId(Integer aId) {
        this.aId = aId;
    }
}
