package com.example.user.vote;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SignActivity extends AppCompatActivity {
    EditText idEdt,pwEdt,pwEdt2,nameEdt,phoneEdt,mailEdt;
    RadioButton man,woman;
    RadioGroup rg;
    String sid,spw,spw2,sname,sphone,smail,sgender=null,fcm;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        idEdt = (EditText)findViewById(R.id.sid);
        pwEdt = (EditText)findViewById(R.id.spw);
        pwEdt2 = (EditText)findViewById(R.id.spw2);
        nameEdt = (EditText)findViewById(R.id.sname);
        phoneEdt = (EditText)findViewById(R.id.sphone);
        mailEdt = (EditText)findViewById(R.id.smail);
        rg = (RadioGroup)findViewById(R.id.rg);
        man = (RadioButton)findViewById(R.id.man);
        woman = (RadioButton)findViewById(R.id.woman);

        ///////////////네이버////////////////////

        Intent it = getIntent();
        token = it.getStringExtra("accessToken");
        if(token!=null){
            //네이버로 가입한 경우 추가정보 다이얼로그 띄움
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("").setMessage("네이버 로그인시에는 \n추가정보가 필요합니다.")
                    .setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new MemberProfile().execute();

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        ////////////////네이버////////////////////

    }

    ////////////////////////////////////
    // 네이버 API 예제 - 회원프로필 조회
    public class MemberProfile extends AsyncTask<Integer,Integer,Integer> {
        String json_email;
        String[] json_id;
        String json_name;
        String json_gender;

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

                    json_name = resObject.getString("name");
                    json_email = resObject.getString("email");
                    json_id = json_email.split("@"); //이메일통해 아이디 추출
                    json_gender = resObject.getString("gender");


                    publishProgress();
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
        @Override
        protected void onProgressUpdate(Integer... values) {//UI 접근
            super.onProgressUpdate(values);
            nameEdt.setText(json_name);
            idEdt.setText(json_id[0]);
            mailEdt.setText(json_email);


        }
    }


    ////////////////네이버////////////////////




        public void okClick(View v) {
            FirebaseApp.initializeApp(this);
            fcm = FirebaseInstanceId.getInstance().getToken();
            sid = idEdt.getText().toString();
            spw = pwEdt.getText().toString();
            spw2 = pwEdt2.getText().toString();
            sname = nameEdt.getText().toString();
            sphone = phoneEdt.getText().toString();
            smail = mailEdt.getText().toString();
            if (!spw.equals(spw2)) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(SignActivity.this);
                builder1.setMessage("비밀번호를 확인하세요");
                builder1.setNegativeButton("다시 시도", null);
                builder1.create();
                builder1.show();
            } else {
                if (man.isChecked()) {
                    sgender = "남";
                    BackgroundTask task = new BackgroundTask();
                    task.execute(sid, spw, sname, sphone, smail, sgender, fcm);
                } else if (woman.isChecked()) {
                    sgender = "여";
                    BackgroundTask task = new BackgroundTask();
                    task.execute(sid, spw, sname, sphone, smail, sgender, fcm);
                } else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(SignActivity.this);
                    builder1.setMessage("성별을 선택하세요.");
                    builder1.setNegativeButton("다시 시도", null);
                    builder1.create();
                    builder1.show();
                }
            }
        }

    class BackgroundTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://13.125.65.179:8080/vote_d/register.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "id="+strings[0]+"&pw="+strings[1]+"&name="+strings[2]+"&phone="+strings[3]+"&mail="+strings[4]+"&gender="+strings[5]+"&fcm="+strings[6];
                osw.write(sendMsg);
                osw.flush();
                //System.out.println(conn.getResponseCode()+"결과");
                if(conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "EUC-KR");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();

                } else {
                    Log.i("통신 결과", conn.getResponseCode()+"에러");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg;
        }

        @Override
        protected void onPostExecute(String s) {
            Intent it = new Intent(SignActivity.this, Login.class);
            System.out.println("결과 : "+s);
            Toast.makeText(SignActivity.this, "회원가입에 성공하였습니다. 로그인 해주세요", Toast.LENGTH_LONG).show();
            SignActivity.this.startActivity(it);
            SignActivity.this.finish();
        }
    }

}
