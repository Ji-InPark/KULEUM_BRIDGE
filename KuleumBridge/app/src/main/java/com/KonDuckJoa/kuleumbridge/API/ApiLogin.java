package com.KonDuckJoa.kuleumbridge.API;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.KonDuckJoa.kuleumbridge.Common.CallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiLogin extends AsyncTask<String, String, Boolean> {
    private String id, pwd, result;
    private Response response;
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
            callBack.callback_fail();
            return;
        }

        super.onPostExecute(success);

        if(success)
        {
            callBack.callback_success(result);
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

                callBack.callback_fail();
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
        final MediaType jsonType = MediaType.parse("application/json; charset=utf-8");

        OkHttpClient okHttpClient = new OkHttpClient();

        JSONObject parameterJson = new JSONObject();
        try
        {
            parameterJson.put("id", id);
            parameterJson.put("pwd", pwd);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        // rest api 로그인 post로 보냄
        RequestBody body = RequestBody.create(jsonType, parameterJson.toString());
        Request request = new Request.Builder()
                .url("http://3.37.235.212:5000/login")
                .addHeader("Connection", "close")
                .post(body)
                .build();

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

        return null;
    }
}
