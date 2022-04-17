package com.KonDuckJoa.kuleumbridge.API;

import static org.apache.commons.lang3.StringEscapeUtils.unescapeJava;

import android.os.AsyncTask;

import com.KonDuckJoa.kuleumbridge.Common.CallBack;
import com.KonDuckJoa.kuleumbridge.Common.Data.UserInfo;
import com.KonDuckJoa.kuleumbridge.Notice.NoticeHandler;
import com.KonDuckJoa.kuleumbridge.Notice.NoticeInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiNotice extends AsyncTask<String, String, Boolean> {
    private String std_num, result;
    private CallBack callBack;

    public ApiNotice(String std_num, CallBack callBack)
    {
        this.std_num = std_num;
        this.callBack = callBack;
    }

    @Override
    protected void onPostExecute(Boolean success)
    {
        super.onPostExecute(success);

        if(success)
        {
            callBack.callbackSuccess(result);
        }
    }

    @Override
    protected Boolean doInBackground(String... strings)
    {
        try
        {
            OkHttpClient okHttpClient = new OkHttpClient();

            for (int i = 0; i < 7; i++)
            {
                String category = NoticeHandler.getCategory(i);

                FormBody.Builder formBuilder = new FormBody.Builder();

                for (Map.Entry<String, String> entry : ApiResource.parameterMap.entrySet())
                {
                    formBuilder.add(unescapeJava(entry.getKey()), unescapeJava(entry.getValue()));
                }

                formBuilder.add(unescapeJava("@d1#forum_id"), unescapeJava(NoticeHandler.getNoticeId(i)));
                formBuilder.add(unescapeJava("@d1#subject_code"), unescapeJava(NoticeHandler.getNoticeSubject(i)));
                formBuilder.add(unescapeJava("@d#"), unescapeJava("@d1#"));
                formBuilder.add(unescapeJava("@d1#"), unescapeJava("dmBoardNoticeParam"));
                formBuilder.add(unescapeJava("@d1#tp"), unescapeJava("dm"));

                // rest api 로그인 post로 보냄
                Request request = new Request.Builder()
                        .url("https://kuis.konkuk.ac.kr/CmmnOneStop/find.do")
                        .addHeader("Cookie", "JSESSIONID=" + UserInfo.getInstance().getJSESSIONID())
                        .addHeader("Host", "kuis.konkuk.ac.kr")
                        .addHeader("Referer", "https://kuis.konkuk.ac.kr/index.do")
                        .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36")
                        .addHeader("sec-ch-ua-platform", "Windows")
                        .addHeader("Sec-Fetch-Mode", "cors")
                        .addHeader("Sec-Fetch-Site", "same-origin")
                        .addHeader("X-Requested-With", "XMLHttpRequest")
                        .addHeader("Pragma", "no-cache")
                        .addHeader("Sec-Fetch-Dest", "empty")
                        .addHeader("Cache-Control", "no-cache")
                        .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                        .addHeader("Accept-Language", "en-US,en;q=0.9,ko-KR;q=0.8,ko;q=0.7")
                        .addHeader("Accept", "*/*")
                        .addHeader("Connection", "keep-alive")
                        .addHeader("Origin", "https://kuis.konkuk.ac.kr")
                        .post(formBuilder.build())
                        .build();

                Response response = okHttpClient.newCall(request).execute();
                result = response.body().string();

                if (result.contains("ERRMSGINFO"))
                {
                    return false;
                }
                else
                {
                    NoticeInfo.getInstance().setNoticeInfo(result, category);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return true;
    }
}
