package com.example.kuleumbridge.Notice;

import android.os.Parcel;
import android.os.Parcelable;

public class Notice implements Parcelable {
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
    protected Notice(Parcel in) {
        POSTED_DT = in.readString();
        SUBJECT = in.readString();
        ARTICLE_ID = in.readString();
        URL = in.readString();
    }

    public static final Creator<Notice> CREATOR = new Creator<Notice>() {
        @Override
        public Notice createFromParcel(Parcel in) {
            return new Notice(in);
        }

        @Override
        public Notice[] newArray(int size) {
            return new Notice[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(POSTED_DT);
        parcel.writeString(SUBJECT);
        parcel.writeString(ARTICLE_ID);
        parcel.writeString(URL);
    }
}
