package com.example.user.vote;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Write extends AppCompatActivity {
    private static int GET_PICTURE_URI=2526;
    String[] category={"사회","쇼핑","일상"};

    String[] imgPath=new String[2];
    CustomerAdapter adapter; //리스트뷰를 위한 커스텀어댑터
    EditText title,memo;
    String userID,userPW,userName,userPhone,userGender,userMail,userFCM;
    int count,userAuto;
    String cur_id="gusrud2526";
    //나중에 로그인 시 전역변수로 설정해주고 어디서든 접근 가능하게 하기
    String target,postParams;
    Spinner spinner;
    ListView list;
    ImageView img;
    ArrayAdapter spiAdapter;
    CheckBox time;
    TextView mDate,mTime;
    Button checkAlarm;
    int myear,mMonth,mDay,mHour,mMinute;

    private AlarmManager mManager;
    // 설정 일시
    private GregorianCalendar mCalendar;
    private NotificationManager mNotification;
    private DatePicker mDate1;
    //시작 설정 클래스
    private TimePicker mTime1;
    Calendar cal = new GregorianCalendar();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("fcm", FirebaseInstanceId.getInstance().getToken());
        postParams = builder.build().getEncodedQuery();
        new BackgroundTask().execute();
        setTitle("");

        mNotification = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        mManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        mCalendar = new GregorianCalendar();

        title =(EditText)findViewById(R.id.title_wr);
        memo=(EditText)findViewById(R.id.memo);

        spinner=(Spinner)findViewById(R.id.category);
        spiAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,category);
        spinner.setAdapter(spiAdapter);

        list = (ListView)findViewById(R.id.list);

        adapter = new CustomerAdapter();
        adapter.addItem("항목 입력 1",BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.picture));
        adapter.addItem("항목 입력 2",BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.picture));
        list.setAdapter(adapter);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }
        }

        //날짜선택을 위한 설정들
        time=(CheckBox)findViewById(R.id.timeset);
        mDate=(TextView)findViewById(R.id.txtdate);
        mTime=(TextView)findViewById(R.id.txttime);
        checkAlarm = (Button)findViewById(R.id.set);

        mDate.setVisibility(View.GONE);
        mTime.setVisibility(View.GONE);
        checkAlarm.setVisibility(View.GONE);

        cal = new GregorianCalendar();
        myear=cal.get(Calendar.YEAR);
        mMonth=cal.get(Calendar.MONTH);
        mDay=cal.get(Calendar.DAY_OF_MONTH);
        mHour=cal.get(Calendar.HOUR_OF_DAY);
        mMinute=cal.get(Calendar.MINUTE);

        UpdateNow();
        checkAlarm.setOnClickListener (new View.OnClickListener() {
            public void onClick (View v) {
                UpdateNow();
                setAlarm();
            }
        });

        //이미지 전송을 위한 설정
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());
    }

    public void Write_db(View v){ //작성버튼 눌렀을때
        try{
            boolean result;
            CustomTask task = new CustomTask();

            String category="food";
            //한글 인코딩 되기 전까지 영어로 넣어놓게함
            if(spinner.getSelectedItem().toString().equals("음식"))
                category="food";
            else if(spinner.getSelectedItem().toString().equals("쇼핑"))
                category="shop";
            else if(spinner.getSelectedItem().toString().equals("일상"))
                category="day";
            String str_id = IAM.getInstance().getUserID();
            String str_title = title.getText().toString();
            String str_memo = memo.getText().toString();

            View temp=list.getChildAt(0);
            String list1=((EditText)temp.findViewById(R.id.listedit)).getText().toString();
            temp=list.getChildAt(1);
            String list2=((EditText)temp.findViewById(R.id.listedit)).getText().toString();

            CheckBox anonymous = (CheckBox)findViewById(R.id.anonymous);
            int noname =0;
            if(anonymous.isChecked())
                noname=1;

            String Alarm="nodate";
            if(time.isChecked()) {
                Alarm = mDate.getText().toString().trim() + " " + mTime.getText().toString().trim();
            }

            result = task.execute(category,str_id,str_title,str_memo,list1,list2,imgPath[0],imgPath[1],noname,Alarm).get();
            if(result){
                Toast.makeText(this,"투표안을 게시하였습니다.",Toast.LENGTH_LONG).show();
                Intent it = new Intent(this, MainActivity.class);
                this.startActivity(it);
                this.finish();
            }else{
                Toast.makeText(this,"다시 시도해주세요.",Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            System.out.println("에러..슬프닷:"+e.getMessage());
        }
    }

    class CustomTask extends AsyncTask<Object, Void, Boolean>{//파일 두개다 올려야 정상작동
        boolean success=false;
        @Override
        protected Boolean doInBackground(Object... params) {
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            File sourceFile=null,sourceFile2=null;
            try {
                DataOutputStream dos;
                URL connectUrl = new URL("http://13.125.65.179:8080/vote_d/BoardWrite.jsp");
                // open connection

                HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                if(params[6]!=null)
                    conn.setRequestProperty("uploaded_file", params[6].toString());
                if(params[7]!=null)
                    conn.setRequestProperty("uploaded_file2", params[7].toString());

                // write data
                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes("\r\n--" + boundary + "\r\n");
                dos.writeBytes("Content-Disposition: form-data; name=\"category\"\r\n\r\n" + params[0]);
                dos.writeBytes(lineEnd);
                dos.writeBytes("\r\n--" + boundary + "\r\n");
                dos.writeBytes("Content-Disposition: form-data; name=\"id\"\r\n\r\n" + params[1]);
                dos.writeBytes(lineEnd);
                dos.writeBytes("\r\n--" + boundary + "\r\n");
                dos.writeBytes("Content-Disposition: form-data; name=\"title\"\r\n\r\n" + params[2]);
                dos.writeBytes(lineEnd);
                dos.writeBytes("\r\n--" + boundary + "\r\n");
                dos.writeBytes("Content-Disposition: form-data; name=\"memo\"\r\n\r\n" + params[3]);
                dos.writeBytes(lineEnd);
                dos.writeBytes("\r\n--" + boundary + "\r\n");
                dos.writeBytes("Content-Disposition: form-data; name=\"list1\"\r\n\r\n" + params[4]);
                dos.writeBytes(lineEnd);
                dos.writeBytes("\r\n--" + boundary + "\r\n");
                dos.writeBytes("Content-Disposition: form-data; name=\"list2\"\r\n\r\n" + params[5]);
                dos.writeBytes(lineEnd);
                dos.writeBytes("\r\n--" + boundary + "\r\n");
                dos.writeBytes("Content-Disposition: form-data; name=\"noname\"\r\n\r\n" + params[8]);
                dos.writeBytes(lineEnd);
                dos.writeBytes("\r\n--" + boundary + "\r\n");
                dos.writeBytes("Content-Disposition: form-data; name=\"alarm\"\r\n\r\n" + params[9]);
                dos.writeBytes(lineEnd);

                int bytesAvailable;
                int bufferSize;
                byte[] buffer;
                int maxBufferSize=1024 * 1024;;
                int bytesRead;

                for(int i=0;i<2;i++) {
                    if (params[6 + i] != null) {
                        sourceFile = new File(params[6+i].toString());
                        if (!sourceFile.isFile()) {
                            Log.e("uploadFile", "Source File not exist :" + params[6+i]);
                        } else {
                            FileInputStream fis = new FileInputStream(sourceFile);
                            dos.writeBytes(twoHyphens + boundary + lineEnd);
                            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile+" + i + "\";filename=\"" + sourceFile + "\"" + lineEnd);
                            dos.writeBytes(lineEnd);

                            bytesAvailable = fis.available();
                            bufferSize = Math.min(bytesAvailable, maxBufferSize);
                            buffer = new byte[bufferSize];
                            // read file and write it into form...
                            bytesRead = fis.read(buffer, 0, bufferSize);
                            while (bytesRead > 0) {
                                dos.write(buffer, 0, bufferSize);
                                bytesAvailable = fis.available();
                                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                                bytesRead = fis.read(buffer, 0, bufferSize);
                            }
                            // send multipart form data necesssary after file data...
                            dos.writeBytes(lineEnd);
                            //======================end
                        }
                    }
                }

                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                System.out.println("보낸 값:"+dos);
                dos.flush(); // finish upload...

                System.out.println("보낸 값:category="+params[0]+"&id="+params[1]+"&title="+params[2]+"&memo="+params[3]+"&list1="+params[4]+"&list1="+params[5]+"&img1="+params[6]+"&img2="+params[7]+"&noname="+params[8]+"&alarm="+params[9]);

                if (conn.getResponseCode() == 200) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer stringBuffer = new StringBuffer();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        stringBuffer.append(line);
                    }
                    System.out.println("(성공)받은 값:"+stringBuffer);
                    success=true;
                }else{
                    System.out.println("통신 에러:"+conn.getResponseCode());
                    success=false;
                }
                dos.close();

            } catch (Exception e) {
                System.out.println("에러..슬프닷3:"+e.getMessage());
                e.printStackTrace();
            }
            return success;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_PICTURE_URI) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    adapter.modifyImg(GET_PICTURE_URI,bitmap);
                    adapter.notifyDataSetChanged();
                    //Glide.with(mContext).load(data.getData()).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView); // OOM 없애기위해 그레들사용

                    //이미지의 절대 경로 획득
                    String[] proj = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(Uri.parse(data.getData().toString()), proj,null,null,null);
                    c.moveToFirst();
                    imgPath[GET_PICTURE_URI] = c.getString(0).toString();
                    System.out.println("클릭:"+GET_PICTURE_URI+"/이미지:"+imgPath[GET_PICTURE_URI]);

                } catch (SecurityException e){
                    Log.e("test2", e.getMessage());
                }
                catch (Exception e) {
                    System.out.println("test3"+e);
                }
            }
        }
    }

    public class CustomerAdapter extends BaseAdapter {
        private ArrayList<ListViewItem> listViewitemList = new ArrayList<ListViewItem>();
        public CustomerAdapter(){

        }
        public View getView(final int position, View convertView, ViewGroup parent) {
            final int pos = position;
            final Context context = parent.getContext();

            if(convertView == null){
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.custom_list, null);
            }

            EditText txt = (EditText)convertView.findViewById(R.id.listedit);
            txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            img=(ImageView) convertView.findViewById(R.id.getImage);

            ListViewItem listViewItem = listViewitemList.get(position);
            txt.setHint(listViewItem.getListmemo());
            img.setImageBitmap(listViewItem.getBitmap());
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //추후 OOM문제 시 http://blog.eyegoodsoft.com/entry/Android-Glide-Library-%EC%9D%B4%EB%AF%B8%EC%A7%80-%EB%9D%BC%EC%9D%B4%EB%B8%8C%EB%9F%AC%EB%A6%AC?category=690228 사이트 참고
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                    intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    GET_PICTURE_URI = position;
                    startActivityForResult(intent, GET_PICTURE_URI);
                }
            });
            return convertView;

        }

        public void addItem(String txt, Bitmap bitmap){
            ListViewItem item = new ListViewItem();
            item.setListmemo(txt);
            item.setBitmap(bitmap);

            listViewitemList.add(item);
        }

        public void modifyImg(int position,Bitmap bitmap){
            ListViewItem item = listViewitemList.get(position);
            item.setBitmap(bitmap);
            listViewitemList.set(position,item);
        }
        @Override
        public int getCount() {
            return listViewitemList.size();
        }
        public ListViewItem getItem(int position)
        {
            return listViewitemList.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    //data,time Picker 관련 메소드
    public void clickCheck(View v)
    {
        if(time.isChecked()){
            checkAlarm.setVisibility(View.VISIBLE);
            mDate.setVisibility(View.VISIBLE);
            mTime.setVisibility(View.VISIBLE);
        }
        else {
            checkAlarm.setVisibility(View.GONE);
            mDate.setVisibility(View.GONE);
            mTime.setVisibility(View.GONE);
        }
    }
    public void clickdate(View v) {
        switch (v.getId()) {
            case R.id.txtdate:
                new DatePickerDialog(Write.this,mDateSetListener,myear,mMonth,mDay).show();
                break;
        }
    }
    public void clicktime(View v) {
        switch (v.getId()) {
            case R.id.txttime:
                new TimePickerDialog(Write.this,mTimeSetListener,mHour,mMinute,false).show();
                break;
        }
    }
    DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener(){
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                {
                    myear=year;
                    mMonth=monthOfYear;
                    mDay=dayOfMonth;

                    UpdateNow();
                }
            };
    TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener(){

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                {
                    mHour=hourOfDay;
                    mMinute=minute;

                    UpdateNow();
                }
            };
    void UpdateNow(){
        mDate.setText(String.format("   %d-%d-%d",myear,mMonth+1,mDay));
        mTime.setText(String.format("   %d:%d",mHour,mMinute));
    }

    private void setAlarm() {
        Calendar cal2= Calendar.getInstance();
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            cal2.setTime(sdf.parse(mDate.getText().toString().trim()+" "+mTime.getText().toString().trim()));
        }catch (Exception e){e.printStackTrace();}

        mManager.set(AlarmManager.RTC_WAKEUP, cal2.getTimeInMillis(), pendingIntent());
        Log.i("HelloAlarmActivity", cal2.getTime().toString());
        Toast.makeText(getApplicationContext(), "알람 설정 완료" + cal2.getTime().toString(), Toast.LENGTH_LONG).show();
    }

    private PendingIntent pendingIntent() {
        Intent i = new Intent(getApplicationContext(), BroadcastD.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        return pi;
    }

    public void home(View v){
        Intent it = new Intent(this, MainActivity.class);
        this.startActivity(it);
        this.finish();
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
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            //Log.d("밸류값 = ",values[1]);
            super.onProgressUpdate(values);
        }
    }
}
