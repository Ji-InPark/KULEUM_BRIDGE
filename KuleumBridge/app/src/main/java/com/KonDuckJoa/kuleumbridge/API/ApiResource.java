package com.KonDuckJoa.kuleumbridge.API;

import android.os.AsyncTask;

import com.KonDuckJoa.kuleumbridge.Common.CallBack;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ApiResource extends AsyncTask<String, String, Boolean> {
    private CallBack callBack;
    public static HashMap<String, String> parameterMap = new HashMap<>();

    public ApiResource(CallBack callBack)
    {
        this.callBack = callBack;
    }

    @Override
    protected void onPostExecute(Boolean success)
    {
        if(success)
        {
            callBack.callbackSuccess("");
        }
        else
        {
            callBack.callbackFail();
        }
    }

    @Override
    protected Boolean doInBackground(String... strings)
    {
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://kuis.konkuk.ac.kr/ui/cpr-lib/user-modules.js?p=0.5057405759389186")
                .addHeader("Connection", "close")
                .get()
                .build();

        String[] rawParameters = null;
        try
        {
            Response response = okHttpClient.newCall(request).execute();

            rawParameters = response.body().string().split("submit\\.addParameter\\(");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        rawParameters[10] = rawParameters[10].split("var")[0]; // 맨 마지막 부분만 따로 처리

        for(int i = 1; i <= 10; i++)
        {
            String rawParameter = rawParameters[i];

            for(int j = 1; j < rawParameter.length(); j++)
            {
                if(rawParameter.charAt(0) == rawParameter.charAt(j))
                {
                    parameterMap.put(rawParameter.substring(1, j), rawParameter.substring(j + 3, rawParameter.length() - 3));
                    break;
                }
            }
        }

        return true;
    }
}


