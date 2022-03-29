package com.KonDuckJoa.kuleumbridge.Data;

import com.KonDuckJoa.kuleumbridge.Grade.Grade;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserInfoClass{
    private static UserInfoClass instance = new UserInfoClass();

    private String RESNO;       // 생년월일
    private String DEPT_TTNM;   // 소속단과대 및 학과
    private String USER_NM;     // 이름
    private String USER_ID;     // 학번
    private String PHOTO;       // 사진 URL,

    private ArrayList<Grade> grade_all;  // 전체 성적 저장
    private ArrayList<Grade> grade_now;  // 금학기 성적 저장

    private int DS_GRAD_length; // grade_all 배열에서 실질적으로 정보가 들어있는 칸의 개수
    private int DS_GRADOFSTUDENT_length; // grade_now 배열에서 실질적으로 정보가 들어있는 칸의 개수

    private StringBuilder grade_all_txt = new StringBuilder(); // 화면 상에서 보여지는 전체 성적 텍스트
    private StringBuilder grade_now_txt = new StringBuilder(); // 화면 상에서 보여지는 금학기 성적 텍스트

    private UserInfoClass()
    {
        grade_all = new ArrayList<>();
        grade_now = new ArrayList<>();
    }

    public static UserInfoClass getInstance()
    {
        return instance;
    }

    public void setLoginInfo(String response_string) {
        try {
            JSONObject responseJson = new JSONObject(response_string);

            JSONObject dmUserInfo = responseJson.getJSONObject("dmUserInfo");

            setRESNO(dmUserInfo.getString("RESNO"));
            setDEPT_TTNM(dmUserInfo.getString("DEPT_TTNM"));
            setUSER_NM(dmUserInfo.getString("USER_NM"));
            setUSER_ID(dmUserInfo.getString("USER_ID"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ApiGradeAllClass를 통해서 얻어온 데이터를 저장하는 메소드
    public void setGradeAllInfo(String response_string_grade) {
        try {
            JSONObject responseJson = new JSONObject(response_string_grade);
            JSONArray DS_GRAD = responseJson.getJSONArray("DS_GRAD");
            setDS_GRAD_length(DS_GRAD.length());


            for (int i = 0; i < DS_GRAD.length(); i++) {
                JSONObject subject = DS_GRAD.getJSONObject(i);
                grade_all.add(new Grade(subject.getString("YY"),
                        subject.getString("HAKSU_NM"),
                        subject.getString("POBT_DIV"),
                        subject.getString("SHTM_NM"),
                        subject.getString("SHTM"),
                        subject.getString("PNT"),
                        subject.getString("GRD"),
                        subject.getString("DETM_CD")));
            }
            JSONObject dmPhoto = responseJson.getJSONObject("dmPhoto");

            setPHOTO(dmPhoto.getString("PHOTO"));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ApiGradeNowClass를 통해서 얻어온 데이터를 저장하는 메소드
    // Grade.java에 설명되어있는 3가지의 ELEMENTS중 첫번째인 이수(중인) 과목에 해당.
    // 전체성적을 가져올때랑 JSON배열 내 ELEMENTS의 변수 이름이 대부분 다르다.
    public void setGradeNowInfo(String response_string_grade) {
        try {
            JSONObject responseJson = new JSONObject(response_string_grade);
            JSONArray DS_GRADEOFSTUDENT = responseJson.getJSONArray("DS_GRADEOFSTUDENT");
            setDS_GRADOFSTUDENT_length(DS_GRADEOFSTUDENT.length());

            for (int i = 0; i < DS_GRADEOFSTUDENT.length(); i++)
            {
                JSONObject subject = DS_GRADEOFSTUDENT.getJSONObject(i);

                grade_now.add(new Grade(subject.getString("LT_YY"),
                        subject.getString("TYPL_KOR_NM"),
                        subject.getString("POBT_NM"),
                        subject.getString("COMM_NM"),
                        subject.getString("LT_SHTM"),
                        subject.getString("PNT"),
                        subject.getString("CALCU_GRD"),
                        ""));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setRESNO(String resno) {
        int year = Integer.parseInt(resno.substring(0, 2));

        if(year < 50)
            year += 100;
        year += 1900;

        RESNO = year + "년 " +  resno.substring(2, 4) + "월 " + resno.substring(4) + "일";
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

    public void setDS_GRAD_length(int ds_grad_length) {
        DS_GRAD_length = ds_grad_length;
    }

    public void setDS_GRADOFSTUDENT_length(int ds_gradOfStudent_length) {
        DS_GRADOFSTUDENT_length = ds_gradOfStudent_length;
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

    public int getDS_GRAD_length() {
        return DS_GRAD_length;
    }

    public int getDS_GRADOFSTUDENT_length() {
        return DS_GRADOFSTUDENT_length;
    }

    // 전체 성적 ArrayList 리턴
    public ArrayList<Grade> getGradeAll() {
        return grade_all;
    }

    // 금학기 성적 ArrayList 리턴
    public ArrayList<Grade> getGradeNow() {
        return grade_now;
    }

    // 전체 성적 하나의 문자열로 리턴
    public String getGrade_all_txt()
    {
        for (int i = 0; i<DS_GRAD_length; i++)
        {
            if (!(grade_all.get(i).getHAKSU_NM().equals("평점평균")) && !(grade_all.get(i).getHAKSU_NM().equals("총평점평균")))
            {
                grade_all_txt.append(grade_all.get(i).toString());
            }
        }
        return grade_all_txt.toString();
    }

    // 금학기 성적 하나의 문자열로 리턴
    public String getGrade_now_txt()
    {
        for (int i = 0; i<DS_GRADOFSTUDENT_length; i++)
        {
            grade_now_txt.append(grade_now.get(i).toString());
        }
        return grade_now_txt.toString();
    }
}
