package com.example.kuleumbridge;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiUserInfoClass extends Thread{
    private String std_num, result;

    public ApiUserInfoClass(String std_num)
    {
        this.std_num = std_num;
    }

    @Override
    public void run() {
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        JSONObject json = new JSONObject();
        try {
            json.put("std_num", std_num);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // rest api 로그인 post로 보냄
        RequestBody body = RequestBody.create(JSON, json.toString());
        Request request = new Request.Builder()
                .url("http://3.37.235.212:5000/user-info")
                .addHeader("Connection", "close")
                .post(body)
                .build();

        Response response;

        try {
            response = client.newCall(request).execute();
            result = response.body().string();
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getResult() {
        return result;
    }

}
