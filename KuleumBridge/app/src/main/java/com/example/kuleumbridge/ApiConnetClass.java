package com.example.kuleumbridge;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiConnetClass extends Thread{

    private String id, pwd, result;

    public ApiConnetClass(String id, String pwd)
    {
        this.id = id;
        this.pwd = pwd;
    }

    @Override
    public void run() {
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
            json.put("pwd", pwd);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // rest api 로그인 post로 보냄
        RequestBody body = RequestBody.create(JSON, json.toString());
        Request request = new Request.Builder()
                .url("http://3.37.235.212:5000/login")
                .addHeader("Connection", "close")
                .post(body)
                .build();

        Response response;

        while(true)
        {
            try {
                response = client.newCall(request).execute();
                result = response.body().string();
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getResult() {
        return result;
    }
}
