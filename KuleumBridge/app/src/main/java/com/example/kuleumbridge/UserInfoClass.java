package com.example.kuleumbridge;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class UserInfoClass {

    String RESNO;       // 생년월일
    String DEPT_TTNM;   // 소속단과대 및 학과
    String USER_NM;     // 이름

    String YY;          // 년도
    String POBT_DIV;    // 이수구분
    String SHTM_NM;     // 학기
    String HAKSU_NM;    // 해당학기 학점(평균)평균
    String DETM_CD;     // 해당학기 이수학점점



    public void saveLoginRes(String input_string) // 학생증에 들어갈 정보
    {


    }


    public void saveGradeAllRes(String input_string) // 성적조회에 들어갈 정보
    {

    }

    public void setInfo(String response_string) {
        try {
            JSONObject temp = new JSONObject(response_string);
            JSONObject dmUserInfo = temp.getJSONObject("dmUserInfo");
            RESNO = dmUserInfo.getString("RESNO");
            DEPT_TTNM = dmUserInfo.getString("DEPT_TTNM");
            USER_NM = dmUserInfo.getString("USER_NM");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
