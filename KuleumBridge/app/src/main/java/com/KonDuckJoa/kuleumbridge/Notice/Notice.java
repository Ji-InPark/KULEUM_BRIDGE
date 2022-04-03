package com.KonDuckJoa.kuleumbridge.Notice;

public class Notice {
    private String postedDate = ""; // 공지글 게시일
    private String subject = "";   // 공지글 제목
    private String articleId = ""; // URL에 있는 공지글 고유 ID
    private String url = ""; // 공지글 URL

    public Notice(String postedDate, String subject, String articleId, String url)
    {
        this.postedDate = postedDate;
        this.subject = subject;
        this.articleId = articleId;
        this.url = url;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public String getSubject() {
        return subject;
    }

    public String getArticleId() {
        return articleId;
    }

    public String getUrl() {
        return url;
    }
}
