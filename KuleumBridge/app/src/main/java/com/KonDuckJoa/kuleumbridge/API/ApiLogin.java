package com.KonDuckJoa.kuleumbridge.API;

import static org.apache.commons.lang3.StringEscapeUtils.unescapeJava;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.KonDuckJoa.kuleumbridge.Common.CallBack;
import com.KonDuckJoa.kuleumbridge.Common.Data.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiLogin extends AsyncTask<String, String, Boolean> {
    private String id, pwd, result;
    private Context context;
    private CallBack callBack;

    public ApiLogin(String id, String pwd, Context context, CallBack callBack)
    {
        this.context = context;
        this.id = id;
        this.pwd = pwd;
        this.callBack = callBack;
    }

    @Override
    protected void onPostExecute(Boolean success)
    {
        if(success == null)
        {
            Toast.makeText(context, "네트워크가 불안정합니다. 다시 로그인해주세요.", Toast.LENGTH_SHORT).show();
            callBack.callbackFail();
            return;
        }

        super.onPostExecute(success);

        if(success)
        {
            callBack.callbackSuccess(result);
        }
        else
        {
            // json으로 받은 에러메세지에서 원하는 부분만 파싱하는 과정
            JSONObject errorJson;
            try
            {
                errorJson = new JSONObject(result);

                errorJson = errorJson.getJSONObject("ERRMSGINFO");

                String errorMessage = errorJson.getString("ERRMSG");

                // 토스트로 에러메세지 출력
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();

                callBack.callbackFail();
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected Boolean doInBackground(String... strings)
    {
        try
        {
            OkHttpClient okHttpClient = new OkHttpClient();

            FormBody.Builder loginFormBuilder = new FormBody.Builder();

            for(Map.Entry<String, String> entry : ApiResource.parameterMap.entrySet())
            {
                loginFormBuilder.add(unescapeJava(entry.getKey()), unescapeJava(entry.getValue()));
            }

            loginFormBuilder.add(unescapeJava("@d1#SINGLE_ID"), unescapeJava(id));
            loginFormBuilder.add(unescapeJava("@d1#PWD"), unescapeJava(pwd));
            loginFormBuilder.add(unescapeJava("@d1#default.locale"), unescapeJava("ko"));
            loginFormBuilder.add(unescapeJava("@d#"), unescapeJava("@d1#"));
            loginFormBuilder.add(unescapeJava("@d1#"), unescapeJava("dsParam"));
            loginFormBuilder.add(unescapeJava("@d1#tp"), unescapeJava("dm"));

            // rest api 로그인 post로 보냄
            Request loginRequest = new Request.Builder()
                    .url("https://kuis.konkuk.ac.kr/Login/login.do")
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
                    .post(loginFormBuilder.build())
                    .build();

            Response loginResponse = okHttpClient.newCall(loginRequest).execute();
            
            if(loginResponse.body().string().contains("ERRMSGINFO"))
            {
                return false;
            }

            UserInfo.getInstance().setJSESSIONID(loginResponse.header("set-Cookie").split(";")[0].split("=")[1]);

            FormBody.Builder postLoginFormBuilder = new FormBody.Builder();

            for(Map.Entry<String, String> entry : ApiResource.parameterMap.entrySet())
            {
                postLoginFormBuilder.add(unescapeJava(entry.getKey()), unescapeJava(entry.getValue()));
            }

            Request postLoginRequest = new Request.Builder()
                    .url("https://kuis.konkuk.ac.kr/Main/onLoad.do")
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
                    .post(loginFormBuilder.build())
                    .build();

            Response postLoginResponse = okHttpClient.newCall(postLoginRequest).execute();

            result = postLoginResponse.body().string();

            return !result.contains("ERRMSGINFO");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
