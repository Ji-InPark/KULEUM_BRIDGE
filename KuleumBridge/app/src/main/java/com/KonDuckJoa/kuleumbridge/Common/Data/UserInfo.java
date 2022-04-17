package com.KonDuckJoa.kuleumbridge.Common.Data;

import com.KonDuckJoa.kuleumbridge.Grade.Grade;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserInfo {
    private static UserInfo instance = new UserInfo();

    private String JSESSIONID;

    private String birthDate;               // 생년월일
    private String departmentTotalName;     // 소속단과대 및 학과
    private String userName;                // 이름
    private String userId;                  // 학번
    private String photoUrl;                // 사진 URL

    private ArrayList<Grade> gradeAllArray;  // 전체 성적 저장
    private ArrayList<Grade> gradeNowArray;  // 금학기 성적 저장

    private int gradeAllLength;     // grade_all 배열에서 실질적으로 정보가 들어있는 칸의 개수
    private int gradeNowLength;     // grade_now 배열에서 실질적으로 정보가 들어있는 칸의 개수

    private UserInfo()
    {
        gradeAllArray = new ArrayList<>();
        gradeNowArray = new ArrayList<>();
    }

    public static UserInfo getInstance()
    {
        return instance;
    }

    public void setLoginInfo(String loginResponse)
    {
        try
        {
            JSONObject responseJson = new JSONObject(loginResponse);

            JSONObject userInfoJson = responseJson.getJSONObject("dmUserInfo");

            setBirthDate(userInfoJson.getString("RESNO"));
            setDepartmentTotalName(userInfoJson.getString("DEPT_TTNM"));
            setUserName(userInfoJson.getString("USER_NM"));
            setUserId(userInfoJson.getString("USER_ID"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // ApiGradeAllClass를 통해서 얻어온 데이터를 저장하는 메소드
    public void setGradeAllInfo(String gradeAllResponse)
    {
        try
        {
            JSONObject responseJson = new JSONObject(gradeAllResponse);

            JSONArray gradeAllJson = responseJson.getJSONArray("DS_GRAD");
            setGradeAllLength(gradeAllJson.length());

            for (int i = 0; i < gradeAllJson.length(); i++)
            {
                JSONObject subject = gradeAllJson.getJSONObject(i);
                gradeAllArray.add(new Grade(subject.getString("YY"),
                        subject.getString("HAKSU_NM"),
                        subject.getString("POBT_DIV"),
                        subject.getString("SHTM_NM"),
                        subject.getString("SHTM"),
                        subject.getString("PNT"),
                        subject.getString("GRD"),
                        subject.getString("DETM_CD")));
            }

            JSONObject photoJson = responseJson.getJSONObject("dmPhoto");

            setPhotoUrl(photoJson.getString("PHOTO"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // ApiGradeNowClass를 통해서 얻어온 데이터를 저장하는 메소드
    // Grade.java에 설명되어있는 3가지의 ELEMENTS중 첫번째인 이수(중인) 과목에 해당.
    // 전체성적을 가져올때랑 JSON배열 내 ELEMENTS의 변수 이름이 대부분 다르다.
    public void setGradeNowInfo(String gradeNowResponse)
    {
        try
        {
            JSONObject responseJson = new JSONObject(gradeNowResponse);

            JSONArray gradeNowJson = responseJson.getJSONArray("DS_GRADEOFSTUDENT");
            setGradeNowLength(gradeNowJson.length());

            for (int i = 0; i < gradeNowJson.length(); i++)
            {
                JSONObject subject = gradeNowJson.getJSONObject(i);

                gradeNowArray.add(new Grade(subject.getString("LT_YY"),
                        subject.getString("TYPL_KOR_NM"),
                        subject.getString("POBT_NM"),
                        subject.getString("COMM_NM"),
                        subject.getString("LT_SHTM"),
                        subject.getString("PNT"),
                        subject.getString("CALCU_GRD"),
                        ""));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void setBirthDate(String birthDate) {
        int year = Integer.parseInt(birthDate.substring(0, 2));

        if(year < 50)
            year += 100;
        year += 1900;

        this.birthDate = year + "년 " +  birthDate.substring(2, 4) + "월 " + birthDate.substring(4) + "일";
    }

    public void setJSESSIONID(String JSESSIONID)
    {
        this.JSESSIONID = JSESSIONID;
    }

    public void setDepartmentTotalName(String departmentTotalName)
    {
        this.departmentTotalName = departmentTotalName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setGradeAllLength(int gradeAllLength) {
        this.gradeAllLength = gradeAllLength;
    }

    public void setGradeNowLength(int gradeNowLength) {
        this.gradeNowLength = gradeNowLength;
    }

    public String getJSESSIONID()
    {
        return JSESSIONID;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getDepartmentTotalName() {
        return departmentTotalName;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserId() {
        return userId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public int getGradeAllLength() {
        return gradeAllLength;
    }

    public int getGradeNowLength() {
        return gradeNowLength;
    }

    public ArrayList<Grade> getGradeAll() {
        return gradeAllArray;
    }

    public ArrayList<Grade> getGradeNow() {
        return gradeNowArray;
    }

}
