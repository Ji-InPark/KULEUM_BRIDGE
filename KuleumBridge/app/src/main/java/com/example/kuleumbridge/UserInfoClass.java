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



    public void setLoginInfo(String response_string) {
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

    public void setGradeAllInfo(String response_string) {

    }

}
