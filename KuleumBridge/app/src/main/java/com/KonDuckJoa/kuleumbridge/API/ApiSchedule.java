package com.KonDuckJoa.kuleumbridge.API;

import android.os.AsyncTask;

import com.KonDuckJoa.kuleumbridge.Common.CallBack;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiSchedule extends AsyncTask<String, String, Boolean> {
    private String std_num, year, semester, result;
    private CallBack callBack;

    public ApiSchedule(String std_num, String year, String semester, CallBack callBack)
    {
        this.std_num = std_num;
        this.year = year;
        this.semester = semester;
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
        final MediaType jsonType = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient okHttpClient = new OkHttpClient();

        JSONObject parameterJson = new JSONObject();
        try
        {
            parameterJson.put("std_num", std_num);
            parameterJson.put("year", year);
            parameterJson.put("semester", semester);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        // rest api 로그인 post로 보냄
        RequestBody body = RequestBody.create(jsonType, parameterJson.toString());
        Request request = new Request.Builder()
                .url("http://3.37.235.212:5000/schedule")
                .addHeader("Connection", "close")
                .post(body)
                .build();

        Response response;

        try
        {
            response = okHttpClient.newCall(request).execute();
            result = response.body().string();

            return !result.contains("ERRMSGINFO");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return true;
    }
}
