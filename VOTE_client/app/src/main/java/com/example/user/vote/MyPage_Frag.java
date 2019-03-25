package com.example.user.vote;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by USER on 2017-11-29.
 */

public class MyPage_Frag extends Fragment {
    View v;
    SettingDialog settingDialog;
    String target;
    String postParams;
    Button check;
    Button change,change2;
    EditText before;
    EditText after1;
    EditText after2;
    String beforePW,afterPW1,afterPW2,pw,name,phone,mail;
    ChangePWDialog changePWDialog;
    ChangeInfoDialog changeInfoDialog;
    TextView idTv;
    EditText spw;
    EditText sname;
    EditText sphone;
    EditText smail;
    LinearLayout iwrite,votewrite;
    ListView write_v;
    CustomerAdapter2 adapter,adapter2;
    TextView writecnt;
    TextView votecnt;
    int votecount,writecount;
    int count = 0; // 1이면 정보변경 가능.

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.frag_mypage, container, false);
        // main= getActivity();
        if(Sub_Frag.thread!=null&&Sub_Frag.thread.isAlive()) Sub_Frag.thread.interrupt();//스레드종료
        writecnt = (TextView)v.findViewById(R.id.writecnt);
        votecnt = (TextView)v.findViewById(R.id.votecnt);
        TextView userID = (TextView)v.findViewById(R.id.userId);
        userID.setText(IAM.getInstance().getUserID()+"님");
        write_v = (ListView)v.findViewById(R.id.write);
        votewrite = (LinearLayout)v.findViewById(R.id.votewrite);
        iwrite = (LinearLayout)v.findViewById(R.id.iwrite);
        adapter2 = new CustomerAdapter2();
        GetBoardData2 task2 = new GetBoardData2();
        task2.execute();
        adapter = new CustomerAdapter2();
        GetBoardData task = new GetBoardData();
        task.execute();

        iwrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter = new CustomerAdapter2();
                GetBoardData task = new GetBoardData();
                task.execute();
                //db에서 글을 가져와 adapter에 넣어주는 클래스
                write_v.setAdapter(adapter);
                write_v.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Fragment f= new Sub_Frag();
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        Bundle bundle = new Bundle(2);
                        bundle.putInt("id", 6);
                        bundle.putSerializable("boardData",adapter.getItem(position));
                        f.setArguments(bundle);
                        ft.replace(R.id.content_main, f);
                        ft.commit();
                    }
                });

            }
        });

        votewrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write_v.setAdapter(adapter2);
            }
        });
        ImageView setting  = (ImageView)v.findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingDialog = new SettingDialog(getActivity());
                settingDialog.show();
                Button changePW = (Button)settingDialog.findViewById(R.id.changePW);
                final Button changeInfo = (Button)settingDialog.findViewById(R.id.changeInfo);
                Button back = (Button)settingDialog.findViewById(R.id.back);
                Button auto =(Button)settingDialog.findViewById(R.id.auto);
                if(IAM.getInstance().getAutoLogin()==1){
                    auto.setText("자동로그인 해제");
                }else{
                    auto.setText("자동로그인 설정");
                }
                auto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(IAM.getInstance().getAutoLogin()==1) {
                            String a = "cancel";
                            Uri.Builder builder = new Uri.Builder()
                                    .appendQueryParameter("id", IAM.getInstance().getUserID())
                                    .appendQueryParameter("autologin", a);
                            postParams = builder.build().getEncodedQuery();
                        } else {
                            String a = "ok";
                            Uri.Builder builder = new Uri.Builder()
                                    .appendQueryParameter("id", IAM.getInstance().getUserID())
                                    .appendQueryParameter("autologin",a);
                            postParams = builder.build().getEncodedQuery();
                        }
                        new AutoLogin().execute();
                    }
                });
                changePW.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changePWDialog = new ChangePWDialog(getActivity());
                        changePWDialog.show();
                        check = changePWDialog.check;
                        change = changePWDialog.change;
                        before = changePWDialog.before;
                        after1 = changePWDialog.after1;
                        after2 = changePWDialog.after2;

                        change.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pw = before.getText().toString();
                                if(pw.equals(IAM.getInstance().getUserPW())){//현재비밀번호가 일치하는지
                                    afterPW1 = after1.getText().toString();
                                    afterPW2 = after2.getText().toString();
                                    if(afterPW1.equals(afterPW2)){ //새비밀번호와 비밀번호확인이 일치하는지
                                        Intent it = new Intent(getActivity(), Login.class);
                                        Uri.Builder builder = new Uri.Builder()
                                                .appendQueryParameter("id", IAM.getInstance().getUserID())
                                                .appendQueryParameter("pw",afterPW1);
                                        postParams = builder.build().getEncodedQuery();
                                        new BackgroundTask().execute();
                                    }else{
                                        Toast.makeText(getActivity(),"바꿀 비밀번호를 확인하세요",Toast.LENGTH_LONG).show();
                                    }
                                } else{
                                    Toast.makeText(getActivity(),"현재 비밀번호를 확인하세요",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
                changeInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeInfoDialog = new ChangeInfoDialog(getActivity());
                        changeInfoDialog.show();
                        idTv = changeInfoDialog.userID;
                        check = changeInfoDialog.check;
                        change2 = changeInfoDialog.change;
                        spw = changeInfoDialog.spw;
                        sname = changeInfoDialog.sname;
                        sphone = changeInfoDialog.sphone;
                        smail = changeInfoDialog.smail;
                        idTv.setText(IAM.getInstance().getUserID()+"님 개인정보 변경");
                        check.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pw = spw.getText().toString();
                                if(pw.equals(IAM.getInstance().getUserPW())){
                                    change2.setVisibility(View.VISIBLE);
                                } else{
                                    Toast.makeText(getActivity(),"비밀번호를 확인하세요.",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        change2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                name = sname.getText().toString();
                                phone = sphone.getText().toString();
                                mail = smail.getText().toString();

                                Intent it = new Intent(getActivity(), Login.class);
                                Uri.Builder builder = new Uri.Builder()
                                        .appendQueryParameter("id", IAM.getInstance().getUserID())
                                        .appendQueryParameter("name",name)
                                        .appendQueryParameter("phone",phone)
                                        .appendQueryParameter("mail",mail);
                                postParams = builder.build().getEncodedQuery();
                                new BackgroundTask2().execute();
                            }
                        });
                    }
                });
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        settingDialog.hide();
                    }
                });
            }
        });

        return v;
    }

    class BackgroundTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            target = "http://13.125.65.179:8080/vote_d/changePW.jsp";
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
                //boolean success = jsonObject.getBoolean("result");
                boolean success = false;
                success = jsonObject.getBoolean("result");
                System.out.println("성공값 : "+success+"");
                if (success) {
                    Toast.makeText(getActivity(),"비밀번호가 변경되었습니다.\n다시 로그인해주세요",Toast.LENGTH_LONG).show();
                    Intent it = new Intent(getActivity(), Login.class);
                    startActivity(it);
                    getActivity().finish();
                }

            }catch(Exception e){
                System.out.println(count+"에러에러 : "+e);
                Toast.makeText(getActivity(), "비밀번호를 확인하세요", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            //Log.d("밸류값 = ",values[1]);
            super.onProgressUpdate(values);
        }
    }
    class BackgroundTask2 extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            target = "http://13.125.65.179:8080/vote_d/changeInfo.jsp";
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
                //boolean success = jsonObject.getBoolean("result");
                boolean success = false;
                success = jsonObject.getBoolean("result");
                System.out.println("성공값 : "+success+"");
                if (success) {
                    Toast.makeText(getActivity(),"다시 로그인해주세요",Toast.LENGTH_LONG).show();
                    Intent it = new Intent(getActivity(), Login.class);
                    startActivity(it);
                    getActivity().finish();
                }

            }catch(Exception e){
                System.out.println(count+"에러에러 : "+e);
                Toast.makeText(getActivity(), "비밀번호를 확인하세요", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            //Log.d("밸류값 = ",values[1]);
            super.onProgressUpdate(values);
        }
    }

    class GetBoardData extends AsyncTask<Integer,Void,String>{

        @Override
        protected String doInBackground(Integer... params) {
            try{
                URL connectUrl = new URL("http://13.125.65.179:8080/vote_d/BoardRead2.jsp");
                HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");//데이터를 POST 방식으로 전송합니다.
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                String sendMsg="id="+IAM.getInstance().getUserID();
                osw.write(sendMsg);
                osw.flush();

                if (conn.getResponseCode() == 200) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    String line;
                    String page="";

                    while ((line = reader.readLine()) != null) {
                        page+=line;
                    }
                    JSONObject json = new JSONObject(page);
                    JSONArray jArr = json.getJSONArray("BoardData");
                    writecount = json.getInt("count");
                    for(int i =0;i<jArr.length();i++){
                        json = jArr.getJSONObject(i);

                        BoardData getJsonData=new BoardData();
                        getJsonData.setIndex(json.getInt("index"));
                        getJsonData.setCategory(json.getString("category"));
                        getJsonData.setId(json.getString("id"));
                        getJsonData.setTitle(json.getString("title"));
                        getJsonData.setMemo(json.getString("memo"));
                        getJsonData.setList1(json.getString("list1"));
                        getJsonData.setList2(json.getString("list2"));
                        getJsonData.setImg1(json.getString("img1"));
                        getJsonData.setImg2(json.getString("img2"));
                        getJsonData.setVote1(json.getInt("vote1"));
                        getJsonData.setVote2(json.getInt("vote2"));
                        getJsonData.setNoname(json.getInt("noname"));
                        getJsonData.setAlarm(json.getString("alarm"));

                        System.out.println("읽어온 데이터 제목:"+getJsonData.getTitle()+"/리스트1:"+getJsonData.getList1()+"/리스트2:"+getJsonData.getList2()+"/총합:"+(getJsonData.getVote1()+getJsonData.getVote2()));
                        adapter.addItem(getJsonData);
                    }

                }else{
                    System.out.println("통신 에러:"+conn.getResponseCode());
                }
            }catch (Exception e){
                System.out.println("에러확인_Task:"+e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);
            adapter.notifyDataSetChanged();
            writecnt.setText(writecount+"개");
        }
    }
    class GetBoardData2 extends AsyncTask<Integer,Void,String>{

        @Override
        protected String doInBackground(Integer... params) {
            try{
                URL connectUrl = new URL("http://13.125.65.179:8080/vote_d/BoardRead3.jsp");
                HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");//데이터를 POST 방식으로 전송합니다.
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                String sendMsg="id="+IAM.getInstance().getUserID();
                osw.write(sendMsg);
                osw.flush();

                if (conn.getResponseCode() == 200) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    String line;
                    String page="";

                    while ((line = reader.readLine()) != null) {
                        page+=line;
                    }
                    JSONObject json = new JSONObject(page);
                    JSONArray jArr = json.getJSONArray("BoardData");
                    votecount = json.getInt("count");
                    for(int i =0;i<jArr.length();i++){
                        json = jArr.getJSONObject(i);

                        BoardData getJsonData=new BoardData();
                        getJsonData.setIndex(json.getInt("index"));
                        getJsonData.setCategory(json.getString("category"));
                        getJsonData.setId(json.getString("id"));
                        getJsonData.setTitle(json.getString("title"));
                        getJsonData.setMemo(json.getString("memo"));
                        getJsonData.setList1(json.getString("list1"));
                        getJsonData.setList2(json.getString("list2"));
                        getJsonData.setImg1(json.getString("img1"));
                        getJsonData.setImg2(json.getString("img2"));
                        getJsonData.setVote1(json.getInt("vote1"));
                        getJsonData.setVote2(json.getInt("vote2"));
                        getJsonData.setNoname(json.getInt("noname"));
                        getJsonData.setAlarm(json.getString("alarm"));

                        System.out.println("읽어온 데이터 제목:"+getJsonData.getTitle()+"/리스트1:"+getJsonData.getList1()+"/리스트2:"+getJsonData.getList2()+"/총합:"+(getJsonData.getVote1()+getJsonData.getVote2()));
                        adapter2.addItem(getJsonData);
                    }

                }else{
                    System.out.println("통신 에러:"+conn.getResponseCode());
                }
            }catch (Exception e){
                System.out.println("에러확인_Task:"+e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);
            adapter2.notifyDataSetChanged();
            votecnt.setText(votecount+"개");
        }
    }
    public class CustomerAdapter2 extends BaseAdapter {
        private ArrayList<BoardData> boardDataList = new ArrayList<BoardData>();
        public CustomerAdapter2(){

        }
        public View getView(final int position, View convertView, ViewGroup parent) {
            try{
                final Context context = parent.getContext();
                if(convertView == null){
                    LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(context.LAYOUT_INFLATER_SERVICE);
                    convertView = vi.inflate(R.layout.custom_list2, null);
                }

                TextView title =(TextView)convertView.findViewById(R.id.title);
                TextView list1 =(TextView)convertView.findViewById(R.id.list1);
                TextView list2 =(TextView)convertView.findViewById(R.id.list2);
                TextView voteSum =(TextView)convertView.findViewById(R.id.voteSum);

                BoardData boardData = boardDataList.get(position);
                title.setText("<"+boardData.getTitle().trim()+">");
                list1.setText("1."+boardData.getList1());
                list2.setText("2."+boardData.getList2());
                int sum = boardData.getVote1()+boardData.getVote2();
                voteSum.setText("총 투표 수: "+sum);

            }catch (Exception e){
                System.out.println("리스트오류"+e.getMessage());
            }
            return convertView;
        }

        public void addItem(BoardData data){
            boardDataList.add(data);
        }

        @Override
        public int getCount() {
            return boardDataList.size();
        }
        public BoardData getItem(int position)
        {
            return boardDataList.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    class AutoLogin extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            target = "http://13.125.65.179:8080/vote_d/AutoLogin.jsp";
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
                success = jsonObject.getBoolean("success");
                System.out.println("성공값 : "+success+"");
                if (success) {
                    Toast.makeText(getActivity(),"자동로그인 적용완료",Toast.LENGTH_LONG).show();
                    settingDialog.hide();
                }

            }catch(Exception e){
                System.out.println(count+"에러에러 : "+e);
                Toast.makeText(getActivity(), "비밀번호를 확인하세요", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            //Log.d("밸류값 = ",values[1]);
            super.onProgressUpdate(values);
        }
    }
}
