package com.KonDuckJoa.kuleumbridge.Data;

import android.os.Parcel;
import android.os.Parcelable;

import com.KonDuckJoa.kuleumbridge.Grade.Grade;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class UserInfoClass implements Parcelable{

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

    public UserInfoClass()
    {
        grade_all = new ArrayList<>();
        grade_now = new ArrayList<>();
    }

    protected UserInfoClass(Parcel in) {
        RESNO = in.readString();
        DEPT_TTNM = in.readString();
        USER_NM = in.readString();
        USER_ID = in.readString();
        PHOTO = in.readString();
        DS_GRAD_length = in.readInt();
        DS_GRADOFSTUDENT_length = in.readInt();
    }

    public static final Creator<UserInfoClass> CREATOR = new Creator<UserInfoClass>() {
        @Override
        public UserInfoClass createFromParcel(Parcel in) {
            return new UserInfoClass(in);
        }

        @Override
        public UserInfoClass[] newArray(int size) {
            return new UserInfoClass[size];
        }
    };

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

    // ApiGradeAllClass를 통해서 얻어온 데이터를 저장하는 메소드
    public void setGradeAllInfo(String response_string_grade) {
        try {
            JSONObject temp = new JSONObject(response_string_grade);
            JSONArray DS_GRAD = temp.getJSONArray("DS_GRAD");
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

                /*
                grade_all[i].setYY(subject.getString("YY"));
                grade_all[i].setHAKSU_NM(subject.getString("HAKSU_NM"));
                grade_all[i].setPOBT_DIV(subject.getString("POBT_DIV"));
                grade_all[i].setSHTM_NM(subject.getString("SHTM_NM"));
                grade_all[i].setSHTM(subject.getString("SHTM"));
                grade_all[i].setPNT(subject.getString("PNT"));
                grade_all[i].setGRD(subject.getString("GRD"));
                grade_all[i].setDETM_CD(subject.getString("DETM_CD"));
                */

                /* 세부성적조회 대비해서 일단 학기평균, 전체평균 이외에 모든과목 정보들도 다 끌어오는 형태,
                   학기평균, 전체평균만 쓸 경우 HAKSU_NM 값에 따라 해당 정보들만 끌어오도록 할 예정.*/
            }
            JSONObject dmPhoto = temp.getJSONObject("dmPhoto");
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
            JSONObject temp = new JSONObject(response_string_grade);
            JSONArray DS_GRADEOFSTUDENT = temp.getJSONArray("DS_GRADEOFSTUDENT");
            setDS_GRADOFSTUDENT_length(DS_GRADEOFSTUDENT.length());

            for (int i = 0; i < DS_GRADEOFSTUDENT.length(); i++) {
                JSONObject subject = DS_GRADEOFSTUDENT.getJSONObject(i);
                grade_now.add(new Grade(subject.getString("LT_YY"),
                        subject.getString("TYPL_KOR_NM"),
                        subject.getString("POBT_NM"),
                        subject.getString("COMM_NM"),
                        subject.getString("LT_SHTM"),
                        subject.getString("PNT"),
                        subject.getString("CALCU_GRD"),
                        ""));



                /*
                grade_now[i].setYY(subject.getString("LT_YY"));             // 이수년도
                grade_now[i].setHAKSU_NM(subject.getString("TYPL_KOR_NM")); // 과목이름
                grade_now[i].setPOBT_DIV(subject.getString("POBT_NM"));     // 이수구분
                grade_now[i].setSHTM_NM(subject.getString("COMM_NM"));      // 학기
                grade_now[i].setSHTM(subject.getString("LT_SHTM"));         // 학기 코드
                grade_now[i].setPNT(subject.getString("PNT"));              // 학점 수
                grade_now[i].setGRD(subject.getString("CALCU_GRD"));        // 등급
                */

                /* 세부성적조회 대비해서 일단 학기평균, 전체평균 이외에 모든과목 정보들도 다 끌어오는 형태,
                   학기평균, 전체평균만 쓸 경우 HAKSU_NM 값에 따라 해당 정보들만 끌어오도록 할 예정.*/
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
    public ArrayList<Grade> getGrade_all() {
        return grade_all;
    }

    // 금학기 성적 ArrayList 리턴
    public ArrayList<Grade> getGrade_now() {
        return grade_now;
    }

    // 전체 성적 하나의 문자열로 리턴
    public String getGrade_all_txt() {
        for (int i = 0; i<DS_GRAD_length; i++) {
            if (!(grade_all.get(i).getHAKSU_NM().equals("평점평균")) && !(grade_all.get(i).getHAKSU_NM().equals("총평점평균"))) {

                grade_all_txt.append(grade_all.get(i).toString());
            }
        }
        return grade_all_txt.toString();
    }

    // 금학기 성적 하나의 문자열로 리턴
    public String getGrade_now_txt() {
        for (int i = 0; i<DS_GRADOFSTUDENT_length; i++) {

            grade_now_txt.append(grade_now.get(i).toString());
//            System.out.println(grade_now.get(i).toString());
        }
        return grade_now_txt.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(RESNO);
        parcel.writeString(DEPT_TTNM);
        parcel.writeString(USER_NM);
        parcel.writeString(USER_ID);
        parcel.writeString(PHOTO);
        parcel.writeInt(DS_GRAD_length);
        parcel.writeInt(DS_GRADOFSTUDENT_length);
    }
}
