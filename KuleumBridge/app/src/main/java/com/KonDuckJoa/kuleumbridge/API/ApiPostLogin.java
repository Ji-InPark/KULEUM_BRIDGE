package com.KonDuckJoa.kuleumbridge.API;

import static org.apache.commons.lang3.StringEscapeUtils.unescapeJava;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.KonDuckJoa.kuleumbridge.Common.CallBack;
import com.KonDuckJoa.kuleumbridge.Common.Data.UserInfo;

import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiPostLogin extends AsyncTask<String, String, Boolean> {
    private String result;
    private CallBack callBack;

    public ApiPostLogin(CallBack callBack)
    {
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
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .readTimeout(5, TimeUnit.SECONDS)
                    .writeTimeout(5, TimeUnit.SECONDS)
                    .build();

            FormBody.Builder formBuilder = new FormBody.Builder();

            for(Map.Entry<String, String> entry : ApiResource.parameterMap.entrySet())
            {
                formBuilder.add(unescapeJava(entry.getKey()), unescapeJava(entry.getValue()));
            }

            Request request = new Request.Builder()
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
                    .post(formBuilder.build())
                    .build();

            Response postLoginResponse = okHttpClient.newCall(request).execute();

            result = postLoginResponse.body().string();

            return !result.contains("ERRMSGINFO");
        }
        catch (SocketTimeoutException e)
        {
            return false;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }
}
