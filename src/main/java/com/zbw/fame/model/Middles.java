package com.zbw.fame.model;

/**
 * 关联标签和文章的中间 Model
 *
 * @auther zbw
 * @create 2017/9/17 23:37
 */
public class Middles extends BaseEntity {

    private Integer aId;

    private Integer mId;

    public Middles() {
    }

    public Middles(Integer aId, Integer mId) {
        this.aId = aId;
        this.mId = mId;
    }

    public Integer getaId() {
        return aId;
    }

    public void setaId(Integer aId) {
        this.aId = aId;
    }

    public Integer getmId() {
        return mId;
    }

    public void setmId(Integer mId) {
        this.mId = mId;
    }
}
