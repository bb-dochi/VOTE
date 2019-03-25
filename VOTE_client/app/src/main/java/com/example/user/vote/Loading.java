package com.example.user.vote;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Loading extends AppCompatActivity  {
    String userID,userPW,userName,userPhone,userMail,userGender,userFCM;
    int userAuto;
    int count;


    String target,postParams,fcm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("fcm", FirebaseInstanceId.getInstance().getToken());
                postParams = builder.build().getEncodedQuery();
                new BackgroundTask().execute();

                /*if(IAM.getInstance().getUserFcm().equals(FirebaseInstanceId.getInstance().getToken())){
                    Intent intent = new Intent(getBaseContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                }*/
                Intent intent = new Intent(getBaseContext(),Login.class);
                startActivity(intent);
                finish();
            }
        },3000); //로딩 시간,4초

    }
    class BackgroundTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            target = "http://13.125.65.179:8080/vote_d/fcm_auto.jsp";
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {

                BufferedReader bufferedReader = null;
                Thread.sleep(100);
                URL url = new URL(target);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                StringBuilder sb = new StringBuilder();
                sb.setLength(0);

                if (conn != null) { // 연결되었으면
                    //add request header
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                }

                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                conn.setUseCaches(false);
                conn.setDefaultUseCaches(false);
                conn.setDoOutput(true); // POST 로 데이터를 넘겨주겠다는 옵션
                conn.setDoInput(true);

                // Send post request
                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(postParams);
                Log.d("Params Value", postParams);
                wr.flush();
                wr.close();

                int responseCode = conn.getResponseCode();
                System.out.println("GET Response Code : " + responseCode);
                if (responseCode == HttpURLConnection.HTTP_OK) { // 연결 코드가 리턴되면
                    bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                }
                bufferedReader.close();

                return sb.toString().trim();
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // 서버에서 전송받은 결과를 파싱하여 처리
            System.out.println("까까까까까까 : " + result);
            try {
                JSONArray jsonArray = new JSONArray(result);
                count = jsonArray.length();
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                //JSONObject jsonObject = new JSONObject(result);
                //JSONObject jsonObject = new JSONObject(result);
                //boolean success = jsonObject.getBoolean("result");
                boolean success = false;
                success = jsonObject.getBoolean("result");
                System.out.println("성공값 : "+success+"");
                if (success) {
                    userID = jsonObject.getString("userid");
                    userPW = jsonObject.getString("userpw");
                    userName = jsonObject.getString("username");
                    userPhone = jsonObject.getString("userphone");
                    userMail = jsonObject.getString("usermail");
                    userGender = jsonObject.getString("usergender");
                    userFCM = jsonObject.getString("userfcm");
                    userAuto = jsonObject.getInt("auto");
                }
                System.out.println(count+"JSONObject 값: "+userID+"/"+userPW+"/"+userName+"/"+userFCM+"/"+userGender);
                IAM.getInstance().setUserID(userID);
                IAM.getInstance().setUserPW(userPW);
                IAM.getInstance().setUserName(userName);
                IAM.getInstance().setUserEmail(userMail);
                IAM.getInstance().setUserPhone(userPhone);
                IAM.getInstance().setUserGender(userGender);
                IAM.getInstance().setUserFcm(userFCM);
                IAM.getInstance().setAutoLogin(userAuto);

                if(IAM.getInstance().getAutoLogin()==1){
                    Intent intent = new Intent(getBaseContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }catch(Exception e){
                System.out.println(count+"에러에러 : "+e);
                Toast.makeText(Loading.this, "자동로그인이 설정되어있지 않습니다.", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            //Log.d("밸류값 = ",values[1]);
            super.onProgressUpdate(values);
        }
    }
}
