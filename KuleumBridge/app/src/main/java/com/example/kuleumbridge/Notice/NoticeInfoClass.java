package com.example.kuleumbridge.Notice;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class NoticeInfoClass implements Parcelable {

    //순서는 학사 - 장학 - 취창업 - 국제 - 학생 - 산학 - 일반 잊지말자~

    private ArrayList<Notice>[] notices;


    public NoticeInfoClass() {
        notices = new ArrayList[7];
        /*
        0번 인덱스 - 학사
        1번 인덱스 - 장학
        2번 인덱스 - 취창업
        3번 인덱스 - 국제
        4번 인덱스 - 학생
        5번 인덱스 - 산학
        6번 인덱스 - 일반
        */
        for(int i = 0; i < 7; i++) {
            notices[i] = new ArrayList<Notice>();
        }
    }

    protected NoticeInfoClass(Parcel in) {
    }

    public static final Creator<NoticeInfoClass> CREATOR = new Creator<NoticeInfoClass>() {
        @Override
        public NoticeInfoClass createFromParcel(Parcel in) {
            return new NoticeInfoClass(in);
        }

        @Override
        public NoticeInfoClass[] newArray(int size) {
            return new NoticeInfoClass[size];
        }
    };


    // POSTED_DT는 공지글 게시일자
    // SUBJECT는 글 제목
    // ARTICLE_ID는 각 게시글 URL에 들어있는 게시글 고유의 ID

    public void setNoticeInfo(String response_string_notice, String notice_category) {
        try {
            JSONObject temp = new JSONObject(response_string_notice);
            JSONArray DS_LIST = temp.getJSONArray("DS_LIST");
            for (int i = 0; i < 5; i++) {
                JSONObject notice = DS_LIST.getJSONObject(i);
                String article_id = notice.getString("ARTICLE_ID");
                notices[NoticeHandler.getIndex(notice_category)].add(new Notice(notice.getString("POSTED_DT"),
                        notice.getString("SUBJECT"), article_id,
                        NoticeHandler.getLink(notice_category, article_id)));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Notice> getNotice(String notice_category)
    {
        return notices[NoticeHandler.getIndex(notice_category)];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
