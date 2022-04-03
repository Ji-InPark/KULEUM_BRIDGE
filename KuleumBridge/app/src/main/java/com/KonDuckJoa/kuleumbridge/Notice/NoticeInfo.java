package com.KonDuckJoa.kuleumbridge.Notice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/*
0번 인덱스 - 학사
1번 인덱스 - 장학
2번 인덱스 - 취창업
3번 인덱스 - 국제
4번 인덱스 - 학생
5번 인덱스 - 산학
6번 인덱스 - 일반
*/

public class NoticeInfo {
    private static NoticeInfo instance = new NoticeInfo();

    private ArrayList<Notice>[] notices;

    private NoticeInfo()
    {
        notices = new ArrayList[7];

        for(int i = 0; i < 7; i++) notices[i] = new ArrayList<>();
    }

    public static NoticeInfo getInstance()
    {
        return instance;
    }

    // POSTED_DT는 공지글 게시일자
    // SUBJECT는 글 제목
    // ARTICLE_ID는 각 게시글 URL에 들어있는 게시글 고유의 ID

    public void setNoticeInfo(String responseNotice, String noticeCategory)
    {
        try
        {
            JSONObject responseNoticeJson = new JSONObject(responseNotice);

            JSONArray noticeJsonArray = responseNoticeJson.getJSONArray("DS_LIST");

            for (int i = 0; i < 5; i++)
            {
                JSONObject noticeJson = noticeJsonArray.getJSONObject(i);

                String articleId = noticeJson.getString("ARTICLE_ID");

                notices[NoticeHandler.getIndex(noticeCategory)].add(new Notice(
                        noticeJson.getString("POSTED_DT"),
                        noticeJson.getString("SUBJECT"), articleId,
                        NoticeHandler.getLink(noticeCategory, articleId)));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public ArrayList<Notice> getNotice(String notice_category)
    {
        return notices[NoticeHandler.getIndex(notice_category)];
    }
}
