package com.example.kuleumbridge;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiLoginClass extends AsyncTask<String, String, Boolean> {
    private String id, pwd, result;
    private Response response;
    private Context context;
    private CallBack cb;

    public ApiLoginClass(String id, String pwd, Context context, CallBack cb)
    {
        this.context = context;
        this.id = id;
        this.pwd = pwd;
        this.cb = cb;
    }


    public String getResult() throws IOException {
        return result;
    }


    @Override
    protected void onPostExecute(Boolean success) {
        super.onPostExecute(success);

        if(success)
        {
            cb.callback(result);
        }
        else
        {
            // json으로 받은 에러메세지에서 원하는 부분만 파싱하는 과정
            JSONObject err_json = null;
            try {
                err_json = new JSONObject(result);

                err_json = err_json.getJSONObject("ERRMSGINFO");

                String msg = err_json.getString("ERRMSG");

                // 토스트로 에러메세지 출력
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    protected Boolean doInBackground(String... strings) {
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
                continue;
            }
        }
        return null;
    }
}
