package com.example.kuleumbridge;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.Stack;

public class UserInfoClass {

    String RESNO;       // 생년월일
    String DEPT_TTNM;   // 소속단과대 및 학과
    String USER_NM;     // 이름
    String USER_ID;     // 학번
    String PHOTO;       // 사진 URL,

    GradeAllClass[] grade = new GradeAllClass[100];
    //Stack<GradeAllClass> gradeStack = new Stack<GradeAllClass>();



    public void setLoginInfo(String response_string) {
        try {
            JSONObject temp = new JSONObject(response_string);
            JSONObject dmUserInfo = temp.getJSONObject("dmUserInfo");
            setRESNO(dmUserInfo.getString("RESNO"));
            setDEPT_TTNM(dmUserInfo.getString("DEPT_TTNM"));
            setUSER_NM(dmUserInfo.getString("USER_NM"));
            setUSER_ID(dmUserInfo.getString("USER_ID"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setGradeAllInfo(String response_string_grade) {
        try {
            JSONObject temp = new JSONObject(response_string_grade);
            JSONArray DS_GRAD = temp.getJSONArray("DS_GRAD");
            System.out.println("JSON배열 DS_GRAD의 길이: " + DS_GRAD.length());
            for (int i = 0; i < DS_GRAD.length(); i++) {
                JSONObject subject = DS_GRAD.getJSONObject(i);

                grade[i].setYY(subject.getString("YY"));
                grade[i].setHAKSU_NM(subject.getString("HAKSU_NM"));
                grade[i].setPOBT_DIV(subject.getString("POBT_DIV"));
                grade[i].setSHTM_NM(subject.getString("SHTM_NM"));
                grade[i].setPNT(subject.getString("PNT"));
                grade[i].setGRD(subject.getString("GRD"));
                grade[i].setDETM_CD(subject.getString("DETM_CD"));


                //System.out.println("과목 이름 : " + grade[i+1].getHAKSU_NM());
                /* 세부성적조회 대비해서 일단 학기평균, 전체평균 이외에 모든과목 정보들도 다 끌어오는 형태,
                   학기평균, 전체평균만 쓸 경우 HAKSU_NM 값에 따라 해당 정보들만 끌어오도록 할 예정.*/
            }
            JSONObject dmPhoto = temp.getJSONObject("dmPhoto");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkSemesterGrade(GradeAllClass[] grc) {
        for (int i = 0; i < grade.length; i++) {
            if (grade[i].getHAKSU_NM().equals("평점평균")) {
                System.out.println("학기평균학점 :" + grade[i].getPOBT_DIV());
            } else if (grade[i].getHAKSU_NM().equals("총평점평균")) {
                System.out.println("총평균학점 :" + grade[i].getPOBT_DIV());
            }
        }
    }

    public void setRESNO(String resno) {
        RESNO = resno;
    }

    public void setDEPT_TTNM(String dept_ttnm) {
        DEPT_TTNM = dept_ttnm;
    }

    public void setUSER_NM(String user_nm) {
        USER_NM = user_nm;
    }

    public void setUSER_ID(String user_id) {
        USER_ID = user_id;
    }

    public void setPHOTO(String photo) {
        PHOTO = photo;
    }

    public String getRESNO() {
        return RESNO;
    }

    public String getDEPT_TTNM() {
        return DEPT_TTNM;
    }

    public String getUSER_NM() {
        return USER_NM;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public String getPHOTO() {
        return PHOTO;
    }
}
