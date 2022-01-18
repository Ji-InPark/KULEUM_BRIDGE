package com.example.kuleumbridge.Data;

import com.example.kuleumbridge.Data.Notice;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;

public class NoticeInfoClass implements Serializable {

    private Notice[] notice_Haksa; // 학사 공지사항 저장
    private Notice[] notice_Janghak; // 장학 공지사항 저장
    private Notice[] notice_Chwichangup; // 취창업 공지사항 저장
    private Notice[] notice_Gukje; // 국제 공지사항 저장
    private Notice[] notice_Haksaeng; // 학생 공지사항 저장
    private Notice[] notice_Sanhak; // 산학 공지사항 저장
    private Notice[] notice_Ilban; // 일반 공지사항 저장

    /* Notice 배열에서 실질적으로 정보가 들어있는 인덱스 수 */
    private int DS_LIST_length_Haksa;
    private int DS_LIST_length_Janghak;
    private int DS_LIST_length_Chwichangup;
    private int DS_LIST_length_Gukje;
    private int DS_LIST_length_Haksaeng;
    private int DS_LIST_length_Sanhak;
    private int DS_LIST_length_Ilban;


    public NoticeInfoClass() {
        /* 주석 숫자만큼의 게시글을 띄워줄 예정(공지사항 종류별 글 게시 빈도 고려) */
        notice_Haksa = new Notice[100]; // 15
        notice_Janghak = new Notice[100]; // 30
        notice_Chwichangup = new Notice[100]; // 30
        notice_Gukje = new Notice[100]; // 10
        notice_Haksaeng = new Notice[100]; // 5
        notice_Sanhak = new Notice[100]; // 20~30
        notice_Ilban = new Notice[100]; // 20~30
    }

    /* POSTED_DT는 공지글 게시일자
       SUBJECT는 게시글 제목
       ARTICLE_ID는 아직 어디에 쓰이는 변수인지 파악 안됨. 일단 가져와봄 */
    public void setNoticeInfo(String response_string_notice, String notice_category) {

        try {
            JSONObject temp = new JSONObject(response_string_notice);
            JSONArray DS_LIST = temp.getJSONArray("DS_LIST");
            if (notice_category.equals("Haksa")) {
                setDS_LIST_length_Haksa(DS_LIST.length());
                for (int i = 0; i < DS_LIST.length(); i++) {
                    JSONObject notice = DS_LIST.getJSONObject(i);
                    notice_Haksa[i] = new Notice();
                    notice_Haksa[i].setPOSTED_DT(notice.getString("POSTED_DT"));
                    notice_Haksa[i].setSUBJECT(notice.getString("SUBJECT"));
                    notice_Haksa[i].setARTICLE_ID(notice.getString("ARTICLE_ID"));
                }
            }
            else if (notice_category.equals("Janghak")) {
                setDS_LIST_length_Janghak(DS_LIST.length());
                for (int i = 0; i < DS_LIST.length(); i++) {
                    JSONObject notice = DS_LIST.getJSONObject(i);
                    notice_Janghak[i] = new Notice();
                    notice_Janghak[i].setPOSTED_DT(notice.getString("POSTED_DT"));
                    notice_Janghak[i].setSUBJECT(notice.getString("SUBJECT"));
                    notice_Janghak[i].setARTICLE_ID(notice.getString("ARTICLE_ID"));
                }
            }
            else if (notice_category.equals("Chwichangup")) {
                setDS_LIST_length_Chwichangup(DS_LIST.length());
                for (int i = 0; i < DS_LIST.length(); i++) {
                    JSONObject notice = DS_LIST.getJSONObject(i);
                    notice_Chwichangup[i] = new Notice();
                    notice_Chwichangup[i].setPOSTED_DT(notice.getString("POSTED_DT"));
                    notice_Chwichangup[i].setSUBJECT(notice.getString("SUBJECT"));
                    notice_Chwichangup[i].setARTICLE_ID(notice.getString("ARTICLE_ID"));
                }
            }
            else if (notice_category.equals("Gukje")) {
                setDS_LIST_length_Gukje(DS_LIST.length());
                for (int i = 0; i < DS_LIST.length(); i++) {
                    JSONObject notice = DS_LIST.getJSONObject(i);
                    notice_Gukje[i] = new Notice();
                    notice_Gukje[i].setPOSTED_DT(notice.getString("POSTED_DT"));
                    notice_Gukje[i].setSUBJECT(notice.getString("SUBJECT"));
                    notice_Gukje[i].setARTICLE_ID(notice.getString("ARTICLE_ID"));
                }
            }
            else if (notice_category.equals("Haksaeng")) {
                setDS_LIST_length_Haksaeng(DS_LIST.length());
                for (int i = 0; i < DS_LIST.length(); i++) {
                    JSONObject notice = DS_LIST.getJSONObject(i);
                    notice_Haksaeng[i] = new Notice();
                    notice_Haksaeng[i].setPOSTED_DT(notice.getString("POSTED_DT"));
                    notice_Haksaeng[i].setSUBJECT(notice.getString("SUBJECT"));
                    notice_Haksaeng[i].setARTICLE_ID(notice.getString("ARTICLE_ID"));
                }
            }
            else if (notice_category.equals("Sanhak")) {
                setDS_LIST_length_Sanhak(DS_LIST.length());
                for (int i = 0; i < DS_LIST.length(); i++) {
                    JSONObject notice = DS_LIST.getJSONObject(i);
                    notice_Sanhak[i] = new Notice();
                    notice_Sanhak[i].setPOSTED_DT(notice.getString("POSTED_DT"));
                    notice_Sanhak[i].setSUBJECT(notice.getString("SUBJECT"));
                    notice_Sanhak[i].setARTICLE_ID(notice.getString("ARTICLE_ID"));
                }
            }
            else if (notice_category.equals("Ilban")) {
                setDS_LIST_length_Ilban(DS_LIST.length());
                for (int i = 0; i < DS_LIST.length(); i++) {
                    JSONObject notice = DS_LIST.getJSONObject(i);
                    notice_Ilban[i] = new Notice();
                    notice_Ilban[i].setPOSTED_DT(notice.getString("POSTED_DT"));
                    notice_Ilban[i].setSUBJECT(notice.getString("SUBJECT"));
                    notice_Ilban[i].setARTICLE_ID(notice.getString("ARTICLE_ID"));
                }
            }
            else {
                System.out.println("존재하지 않는 공지사항 구분입니다.\n");
            }

        } catch (Exception e) {
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
}
