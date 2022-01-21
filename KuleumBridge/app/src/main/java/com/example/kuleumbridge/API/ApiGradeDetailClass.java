package com.example.kuleumbridge.API;

import android.os.AsyncTask;

import com.example.kuleumbridge.Common.CallBack;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiGradeDetailClass extends AsyncTask<String, String, Boolean> {
    private String std_num, year, shtm, id, result;
    private CallBack cb;

    public ApiGradeDetailClass(String std_num, String year, String shtm, String id, CallBack cb)
    {
        this.std_num = std_num;
        this.year = year;
        this.shtm = shtm;
        this.id = id;
        this.cb = cb;
    }

    @Override
    protected void onPostExecute(java.lang.Boolean success) {
        super.onPostExecute(success);

        if(success)
        {
            cb.callback_success(result);
        }
        else
        {
            return;
        }

    }

    @Override
    protected java.lang.Boolean doInBackground(String... strings) {
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        JSONObject json = new JSONObject();
        try {
            json.put("std_num", std_num);
            json.put("year", year);
            json.put("shtm", shtm);
            json.put("sub_id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // rest api 로그인 post로 보냄
        RequestBody body = RequestBody.create(JSON, json.toString());
        Request request = new Request.Builder()
                .url("http://3.37.235.212:5000/grade/detail")
                .addHeader("Connection", "close")
                .post(body)
                .build();

        Response response;

        for(int i = 0; i < 5; i++)
        {
            try {
                response = client.newCall(request).execute();
                result = response.body().string();

                if (result.contains("ERRMSGINFO")) {
                    return false;
                } else {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    public java.lang.String getResult() {
        return result;
    }
}

