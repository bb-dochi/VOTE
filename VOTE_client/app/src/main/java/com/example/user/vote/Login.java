package com.example.user.vote;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.nhn.android.naverlogin.OAuthLogin.mOAuthLoginHandler;

public class Login extends AppCompatActivity {
    EditText id;
    EditText pwd;
    Button login;
    TextView find;
    TextView sign;
    int count =0;
    String userID,userPW,userName,userGender,userPhone,userMail,userFCM;
    String target,postParams,sendMsg,result2;
    OAuthLogin mOAuthLoginModule;
    OAuthLoginButton authLoginButton;
    Context mContext;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        id = (EditText)findViewById(R.id.id);
        pwd = (EditText)findViewById(R.id.pwd);

        //  /*------------------------------------------------------------------------------------------*/
        authLoginButton = (OAuthLoginButton) findViewById(R.id.buttonOAuthLoginImg);
        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(
                Login.this
                ,"ApJrv_xd7RHr8OkDJwOD"
                ,"VUbAkudXr2"
                ,"Client Name"
                //,OAUTH_CALLBACK_INTENT
                // SDK 4.1.4 버전부터는 OAUTH_CALLBACK_INTENT변수를 사용하지 않습니다.

        );
        if (mOAuthLoginModule.getAccessToken(this) != null) {
            String accessToken = mOAuthLoginModule.getAccessToken(mContext);
            Intent it = new Intent(this, SignActivity.class);
            it.putExtra("accessToken",accessToken);
            startActivity(it);
            finish();
        } else {
            authLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);

            //위 코드가 네이버 아이디 로그인을 요청하는 코드입니다.
        }
//   /*------------------------------------------------------------------------------------------*/
    }

    //  /*------------------------------------------------------------------------------------------*/
    protected void onDestroy() {
        super.onDestroy();
        //값 삭제하기
        removePreferences();
        mOAuthLoginModule.logout(mContext);
    }

    // 값(Key Data) 삭제하기
    private void removePreferences(){
        SharedPreferences pref = getSharedPreferences("session_file", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("session");
        editor.commit();
    }

    // 네이버 API 예제 - 회원프로필 조회
    public class MemberProfile extends AsyncTask<Integer,Integer,Integer> {
        String email;
        String[] id;
        String name;
        String gender;

        public MemberProfile(){}

        @Override
        protected Integer doInBackground(Integer... params) {
            String header = "Bearer " + token; // Bearer 다음에 공백 추가
            try {
                String apiURL = "https://openapi.naver.com/v1/nid/me";
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Authorization", header);
                int responseCode = con.getResponseCode();
                BufferedReader br;
                if(responseCode==200) { // 정상 호출
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {  // 에러 발생
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }

                //JSON 파싱
                try {
                    JSONObject object = new JSONObject(response.toString());
                    String res = object.getString("response");
                    System.out.println(res);
                    JSONObject resObject = new JSONObject(res);

                    email = resObject.getString("email");
                    id = email.split("@"); //이메일통해 아이디 추출

                    //네이버로그인 세션파일 저장
                    savePreferences(id[0]);

                    //값 불러오기
                    SharedPreferences pref = getSharedPreferences("session_file", MODE_PRIVATE);
                    System.out.println("session!!!!!!!!"+pref.getString("session", ""));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                br.close();
                System.out.println(response.toString());
            } catch (Exception e) {
                System.out.println(e);
            }
            return null;
        }


    }

    // 값 저장하기
    private void savePreferences(String id){
        SharedPreferences pref = getSharedPreferences("session_file", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("session", id);
        editor.commit();


        //값 불러오기
        //SharedPreferences pref = getSharedPreferences("session_file", MODE_PRIVATE);
        //pref.getString("session", "");
    }


    //  /*------------------------------------------------------------------------------------------*/

    public void goMain(View v){
        String sid = id.getText().toString();
        String spwd = pwd.getText().toString();
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("id", sid)
                .appendQueryParameter("pw",spwd);
        postParams = builder.build().getEncodedQuery();
        new BackgroundTask().execute();

    }

    public void signClick(View v) {

        Intent intent = new Intent(this, SignActivity.class);
        startActivity(intent);
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            target = "http://13.125.65.179:8080/vote_d/Login.jsp";
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
                JSONObject jsonObject = new JSONObject(result);
                //JSONObject jsonObject = new JSONObject(result);
                //boolean success = jsonObject.getBoolean("result");
                boolean success = false;
                success = jsonObject.getBoolean("result");
                System.out.println("성공값 : "+success+"");
                if (success) {
                    Intent it = new Intent(Login.this, MainActivity.class);
                    Login.this.startActivity(it);
                    userID = jsonObject.getString("userid");
                    userPW = jsonObject.getString("userpw");
                    userName = jsonObject.getString("username");
                    userPhone = jsonObject.getString("userphone");
                    userMail = jsonObject.getString("usermail");
                    userGender = jsonObject.getString("usergender");
                    userFCM = jsonObject.getString("userfcm");
                } else{
                    Toast.makeText(Login.this, "아이디/ 비밀번호를 확인하세요", Toast.LENGTH_LONG).show();
                }
                IAM.getInstance().setUserID(userID);
                IAM.getInstance().setUserPW(userPW);
                IAM.getInstance().setUserName(userName);
                IAM.getInstance().setUserEmail(userMail);
                IAM.getInstance().setUserPhone(userPhone);
                IAM.getInstance().setUserGender(userGender);
                IAM.getInstance().setUserFcm(userFCM);

            }catch(Exception e){
                System.out.println(count+"에러에러 : "+e);
                Toast.makeText(Login.this, "아이디/ 비밀번호를 확인하세요", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            //Log.d("밸류값 = ",values[1]);
            super.onProgressUpdate(values);
        }
    }
}
