package com.example.kuleumbridge.Notice;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class NoticeInfoClass implements Parcelable {

    //순서는 학사 - 장학 - 취창업 - 국제 - 학생 - 산학 - 일반 잊지말자~

    private ArrayList<Notice>[] notices;
    private ArrayList<Notice> notice_Haksa; // 학사 공지사항 저장
    private ArrayList<Notice> notice_Janghak; // 장학 공지사항 저장
    private ArrayList<Notice> notice_Chwichangup; // 취창업 공지사항 저장
    private ArrayList<Notice> notice_Gukje; // 국제 공지사항 저장
    private ArrayList<Notice> notice_Haksaeng; // 학생 공지사항 저장
    private ArrayList<Notice> notice_Sanhak; // 산학 공지사항 저장
    private ArrayList<Notice> notice_Ilban; // 일반 공지사항 저장

    /* Notice 배열에서 실질적으로 정보가 들어있는 인덱스 수 */
    private int DS_LIST_length_Haksa;
    private int DS_LIST_length_Janghak;
    private int DS_LIST_length_Chwichangup;
    private int DS_LIST_length_Gukje;
    private int DS_LIST_length_Haksaeng;
    private int DS_LIST_length_Sanhak;
    private int DS_LIST_length_Ilban;


    public NoticeInfoClass() {
        notices = new ArrayList[7];
        for(int i = 0; i < 7; i++)
        {
            notices[i] = new ArrayList<Notice>();
        }

        /* 5개씩 게시글 띄워주기로 일단은 통일 */
        notice_Haksa = new ArrayList<>(); // 5
        notice_Janghak = new ArrayList<>(); // 5
        notice_Chwichangup = new ArrayList<>(); // 5
        notice_Gukje = new ArrayList<>(); // 5
        notice_Haksaeng = new ArrayList<>(); // 5
        notice_Sanhak = new ArrayList<>(); // 5
        notice_Ilban = new ArrayList<>(); // 5
    }

    protected NoticeInfoClass(Parcel in) {
        DS_LIST_length_Haksa = in.readInt();
        DS_LIST_length_Janghak = in.readInt();
        DS_LIST_length_Chwichangup = in.readInt();
        DS_LIST_length_Gukje = in.readInt();
        DS_LIST_length_Haksaeng = in.readInt();
        DS_LIST_length_Sanhak = in.readInt();
        DS_LIST_length_Ilban = in.readInt();
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

    /* POSTED_DT는 공지글 게시일자
           SUBJECT는 글 제목
           ARTICLE_ID는 각 게시글 URL에 들어있는 게시글 고유의 ID */
    public void setNoticeInfo(String response_string_notice, String notice_category) {
        try{
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
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setDS_LIST_length_Haksa(int DS_LIST_length_Haksa) {
        this.DS_LIST_length_Haksa = DS_LIST_length_Haksa;
    }

    public void setDS_LIST_length_Janghak(int DS_LIST_length_Janghak) {
        this.DS_LIST_length_Janghak = DS_LIST_length_Janghak;
    }

    public void setDS_LIST_length_Chwichangup(int DS_LIST_length_Chwichangup) {
        this.DS_LIST_length_Chwichangup = DS_LIST_length_Chwichangup;
    }

    public void setDS_LIST_length_Gukje(int DS_LIST_length_Gukje) {
        this.DS_LIST_length_Gukje = DS_LIST_length_Gukje;
    }

    public void setDS_LIST_length_Haksaeng(int DS_LIST_length_Haksaeng) {
        this.DS_LIST_length_Haksaeng = DS_LIST_length_Haksaeng;
    }

    public void setDS_LIST_length_Sanhak(int DS_LIST_length_Sanhak) {
        this.DS_LIST_length_Sanhak = DS_LIST_length_Sanhak;
    }

    public void setDS_LIST_length_Ilban(int DS_LIST_length_Ilban) {
        this.DS_LIST_length_Ilban = DS_LIST_length_Ilban;
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
        parcel.writeInt(DS_LIST_length_Haksa);
        parcel.writeInt(DS_LIST_length_Janghak);
        parcel.writeInt(DS_LIST_length_Chwichangup);
        parcel.writeInt(DS_LIST_length_Gukje);
        parcel.writeInt(DS_LIST_length_Haksaeng);
        parcel.writeInt(DS_LIST_length_Sanhak);
        parcel.writeInt(DS_LIST_length_Ilban);
    }
}
