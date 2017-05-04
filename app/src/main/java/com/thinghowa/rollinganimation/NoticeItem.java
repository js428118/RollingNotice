package com.thinghowa.rollinganimation;

/**
 * Created by jinseop on 2017. 4. 28..
 */
public class NoticeItem {
    private String title = "";
    private String link_url = "";
    private Boolean ie_new = false;

    public void setTitle(String value) {
        title = value;
    }
    public String getTitle() {
        return title;
    }

    public void setLinkUrl(String value) {
        link_url = value;
    }
    public String getLinkUrl() {
        return link_url;
    }

    public void setIsNew(Boolean value) {
        ie_new = value;
    }
    public Boolean getIsNew() {
        return ie_new;
    }

}
