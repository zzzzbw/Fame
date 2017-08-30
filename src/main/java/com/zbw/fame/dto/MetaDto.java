package com.zbw.fame.dto;

/**
 * 属性 Dto
 *
 * @auther zbw
 * @create 2017/8/30 15:15
 */
public class MetaDto {

    private String name;
    private String type;
    private Integer articleCount;

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

    public Integer getArticleCount() {
        return articleCount;
    }

    public void setArticleCount(Integer articleCount) {
        this.articleCount = articleCount;
    }

    @Override
    public String toString() {
        return "MetaDto{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", articleCount=" + articleCount +
                '}';
    }
}
