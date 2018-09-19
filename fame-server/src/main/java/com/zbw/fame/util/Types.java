package com.zbw.fame.util;

/**
 * 通用的Type类型
 *
 * @author zbw
 * @since 2017/8/27 22:06
 */
public interface Types {
    String PUBLISH = "publish";
    String DRAFT = "draft";

    String CATEGORY = "category";
    String TAG = "tag";

    String POST = "post";
    String PAGE = "page";

    String AGREE = "agree";
    String DISAGREE = "disagree";

    String LOG_ACTION_DELETE = "删除";
    String LOG_ACTION_SEND_EMAIL = "发送邮件";

    String LOG_MESSAGE_DELETE_ARTICLE = "删除文章";
    String LOG_MESSAGE_DELETE_PAGE = "删除自定义页面";
    String LOG_MESSAGE_SEND_EMAIL_SUCCESS = "发送邮件成功";
    String LOG_MESSAGE_SEND_EMAIL_FAIL = "发送邮件失败";

    String LOG_TYPE_VISIT = "visit";
    String LOG_TYPE_OPERATE = "operate";
    String LOG_TYPE_EMAIL = "email";


}
