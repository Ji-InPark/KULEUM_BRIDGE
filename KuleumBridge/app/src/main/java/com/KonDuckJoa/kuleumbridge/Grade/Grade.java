package com.KonDuckJoa.kuleumbridge.Grade;

import android.os.Parcel;
import android.os.Parcelable;

public class Grade implements Parcelable {
    /* JSON배열안에 들어있는 ELEMENTS는 크게 3가지로 나뉨.
    이수했던 과목 OR 해당학기의 평점(학점)평균 OR 전체학기를 합산한 총평점(학점)평균 */
    private String YY = "";          // 이수년도 | 해당년도 | 0000
    private String HAKSU_NM = "";    // 과목이름 | 평점평균 | 총평점평균
    private String POBT_DIV = "";    // 이수구분 | 4.00    | 3.00
    private String SHTM_NM = "";     // 학기    | 학기별소계 | 전체 소계
    private String SHTM = "";        // 학기 코드| 해당 학기 | B01011
    private String PNT = "";         // 학점 수 | null     | null
    private String GRD = "";         // 등급    | 취득학점  | 총취득학점
    private String DETM_CD = "";     // null   | 이수학점  | 총이수학점


    public Grade(String yy,  String haksu_nm, String pobt_div, String shtm_nm, String shtm, String pnt, String grd, String detm_cd) {
        this.YY = yy;
        this.HAKSU_NM = haksu_nm;
        this.POBT_DIV = pobt_div;
        this.SHTM_NM = shtm_nm;
        this.SHTM = shtm;
        this.PNT = pnt;
        this.GRD = grd;
        this.DETM_CD = detm_cd;
    }

    protected Grade(Parcel in) {
        YY = in.readString();
        HAKSU_NM = in.readString();
        POBT_DIV = in.readString();
        SHTM_NM = in.readString();
        SHTM = in.readString();
        PNT = in.readString();
        GRD = in.readString();
        DETM_CD = in.readString();
    }

    public static final Creator<Grade> CREATOR = new Creator<Grade>() {
        @Override
        public Grade createFromParcel(Parcel in) {
            return new Grade(in);
        }

        @Override
        public Grade[] newArray(int size) {
            return new Grade[size];
        }
    };

    public void setYY(String yy) {
        YY = yy;
    }

    public void setHAKSU_NM(String haksu_nm) {
        HAKSU_NM = haksu_nm;
    }

    public void setPOBT_DIV(String pobt_div) {
        POBT_DIV = pobt_div;
    }

    public void setSHTM_NM(String shtm_nm) {
        SHTM_NM = shtm_nm;
    }

    public void setSHTM(String shtm) {SHTM = shtm;}

    public void setPNT(String pnt) {
        PNT = pnt;
    }

    public void setGRD(String grd) {
        GRD = grd;
    }

    public void setDETM_CD(String detm_cd) {
        DETM_CD = detm_cd;
    }

    public String getYY() {
        return YY;
    }

    public String getHAKSU_NM() {
        return HAKSU_NM;
    }

    public String getPOBT_DIV() {
        return POBT_DIV;
    }

    public String getSHTM_NM() {
        return SHTM_NM;
    }

    public String getSHTM() {return SHTM;}

    public String getPNT() {
        return PNT;
    }

    public String getGRD() {
        return GRD;
    }

    public String getDETM_CD() {
        return DETM_CD;
    }

    public String toString() {
        return YY + " " + SHTM_NM + " " + HAKSU_NM + " " + PNT + " " + POBT_DIV + " " + GRD + "\n";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(YY);
        parcel.writeString(HAKSU_NM);
        parcel.writeString(POBT_DIV);
        parcel.writeString(SHTM_NM);
        parcel.writeString(SHTM);
        parcel.writeString(PNT);
        parcel.writeString(GRD);
        parcel.writeString(DETM_CD);
    }
}
