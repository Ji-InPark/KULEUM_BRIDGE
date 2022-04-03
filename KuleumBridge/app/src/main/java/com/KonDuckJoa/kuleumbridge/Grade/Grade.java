package com.KonDuckJoa.kuleumbridge.Grade;


public class Grade {
    /* JSON배열안에 들어있는 ELEMENTS는 크게 3가지로 나뉨.
    이수했던 과목 OR 해당학기의 평점(학점)평균 OR 전체학기를 합산한 총평점(학점)평균 */
    private String completedYear;          // 이수년도 | 해당년도 | 0000
    private String subjectName;            // 과목이름 | 평점평균 | 총평점평균
    private String completedDivision;      // 이수구분 | 4.00    | 3.00
    private String semester;               // 학기    | 학기별소계 | 전체 소계
    private String semesterCode;           // 학기 코드| 해당 학기 | B01011
    private String gradeCount;             // 학점 수 | null     | null
    private String gradeRate;              // 등급    | 취득학점  | 총취득학점
    private String completedGradeCount;    // null   | 이수학점  | 총이수학점

    public Grade(String completedYear, String subjectName, String completedDivision,
                 String semester, String semesterCode, String gradeCount, String gradeRate, String completedGradeCount)
    {
        this.completedYear = completedYear;
        this.subjectName = subjectName;
        this.completedDivision = completedDivision;
        this.semester = semester;
        this.semesterCode = semesterCode;
        this.gradeCount = gradeCount;
        this.gradeRate = gradeRate;
        this.completedGradeCount = completedGradeCount;
    }

    public String getCompletedYear()
    {
        return completedYear;
    }

    public String getSubjectName()
    {
        return subjectName;
    }

    public String getCompletedDivision()
    {
        return completedDivision;
    }

    public String getSemester()
    {
        return semester;
    }

    public String getGradeCount()
    {
        return gradeCount;
    }

    public String getGradeRate()
    {
        return gradeRate;
    }

    @Override
    public String toString()
    {
        return completedYear + " " + semester + " " + subjectName + " " + gradeCount + " " + completedDivision + " " + gradeRate + "\n";
    }

}
