package com.KonDuckJoa.kuleumbridge.Notice;

public class Notice{
    private String POSTED_DT = ""; // 공지글 게시일
    private String SUBJECT = "";   // 공지글 제목
    private String ARTICLE_ID = ""; // URL에 있는 공지글 고유 ID
    private String URL = ""; // 공지글 URL

    public Notice(String posted_dt, String subject, String article_id, String url) {
        this.POSTED_DT = posted_dt;
        this.SUBJECT = subject;
        this.ARTICLE_ID = article_id;
        this.URL = url;
    }

    public void setPOSTED_DT(String posted_dt) {
        POSTED_DT = posted_dt;
    }

    public void setSUBJECT(String subject) {
        SUBJECT = subject;
    }

    public void setARTICLE_ID(String article_id) {
        ARTICLE_ID = article_id;
    }

    public void setURL(String url) {
        URL = url;
    }

    public String getPOSTED_DT() {
        return POSTED_DT;
    }

    public String getSUBJECT() {
        return SUBJECT;
    }

    public String getARTICLE_ID() {
        return ARTICLE_ID;
    }

    public String getURL() {
        return URL;
    }
}
