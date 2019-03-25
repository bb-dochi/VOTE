package com.example.user.vote;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by USER on 2017-11-19.
 */

public class Sub_Frag extends Fragment {
    View v;
    ListView boardlist;
    ListView allboard;
    TextView votecount;
    CustomerAdapter2 adapter,adapter2;
    GetBoardThread t;
    public static Thread thread;
    TextView b_title,b_id,b_Memo;
    RadioButton b_List1,b_List2;
    ImageView b_img1,b_img2;
    Button voteBtn,deleteB;
    Bitmap bit;
    String target;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        Bundle extra = getArguments();
        int id = extra.getInt("id");

        if (id==1) { //메인
            v = inflater.inflate(R.layout.frag_main, container, false);
            votecount = (TextView)v.findViewById(R.id.votecount);
            allboard = (ListView)v.findViewById(R.id.allboard);
            new BackgroundTask().execute();
            allboard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Fragment f= new Sub_Frag();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    Bundle bundle = new Bundle(2);
                    bundle.putInt("id", 6);
                    bundle.putSerializable("boardData",adapter2.getItem(position));
                    f.setArguments(bundle);
                    ft.replace(R.id.content_main, f);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });
        }
        else if(6>id&&id>2) { //3,4,5 서브
            v = inflater.inflate(R.layout.frag_sub, container, false);
            TextView categoryName = (TextView)v.findViewById(R.id.categoryName);

            if(id==3) categoryName.setText("Social");
            else if(id==4) categoryName.setText("Shopping");
            else if(id==5) categoryName.setText("Daily");

            boardlist=(ListView)v.findViewById(R.id.sublist);

            t = new GetBoardThread();
            t.getCategory(id);
            thread=new Thread(t);
            thread.start();

            boardlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    thread.interrupt();
                    Fragment f= new Sub_Frag();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    Bundle bundle = new Bundle(2);
                    bundle.putInt("id", 6);
                    bundle.putSerializable("boardData",adapter.getItem(position));
                    f.setArguments(bundle);
                    ft.replace(R.id.content_main, f);
                    ft.addToBackStack(null);
                    ft.commit();
                }
            });
        }else if(id>5){ //게시물 상세
            v = inflater.inflate(R.layout.frag_boardview, container, false);
            final BoardData boardData = (BoardData)getArguments().get("boardData"); //클릭한 게시물의 게시물 정보
            b_title=(TextView)v.findViewById(R.id.boardTitle);
            b_id=(TextView)v.findViewById(R.id.boardId);
            b_List1=(RadioButton) v.findViewById(R.id.boardList1);
            b_List2=(RadioButton) v.findViewById(R.id.boardList2);
            b_Memo=(TextView)v.findViewById(R.id.boardMemo);
            b_img1=(ImageView)v.findViewById(R.id.boardimg1);
            b_img2=(ImageView)v.findViewById(R.id.boardimg2);
            voteBtn=(Button)v.findViewById(R.id.VoteNow);
            deleteB=(Button)v.findViewById(R.id.DeleteB);

            b_title.setText("제목: "+boardData.getTitle());
            if(boardData.getNoname()==1)
                b_id.setText("작성자: 익명");
            else
                b_id.setText("작성자: "+boardData.getId());
            b_List1.setText("1."+boardData.getList1().trim()+" ("+boardData.getVote1()+"표)");
            b_List2.setText("2."+boardData.getList2().trim()+" ("+boardData.getVote2()+"표)");
            b_Memo.setText("내용: "+boardData.getMemo());


            if(boardData.getImg1()!=null) {
                GetImg task = new GetImg();
                task.execute(boardData.getImg1(), "1");
            }
            if(boardData.getImg2()!=null){
                GetImg task = new GetImg();
                task.execute(boardData.getImg2(),"2");
            }

            final BoardData temp= boardData;
            voteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int checkNum=1;
                    if(b_List2.isChecked())
                        checkNum=2;
                    VoteNow task = new VoteNow();
                    try {
                        String result = task.execute(temp.getIndex(),IAM.getInstance().getUserID(), checkNum).get();
                        Toast.makeText(getActivity(),""+result,Toast.LENGTH_LONG).show();
                        if(result.equals("회원님의 소중한 한 표를 행사했습니다")) {
                            if (checkNum == 1)
                                b_List1.setText("1." + temp.getList1().trim() + " (" + (temp.getVote1() + 1) + "표)");
                            else if (checkNum == 2)
                                b_List2.setText("2." + temp.getList2().trim() + " (" + (temp.getVote2() + 1) + "표)");
                        }
                    }catch (Exception e){System.out.print("버튼에러:"+e);};

                }
            });
        //삭제버튼
        if(IAM.getInstance().getUserID().equals(boardData.getId())){
            deleteB.setVisibility(View.VISIBLE);
            deleteB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        DeleteBoard task = new DeleteBoard();
                        boolean dResult = task.execute(boardData.getIndex()).get();
                        if(dResult){
                            Toast.makeText(getActivity(),"게시물이 삭제되었습니다.",Toast.LENGTH_LONG).show();
                            getActivity().onBackPressed();
                        }
                    }catch (Exception e){}
                }
            });
        }
    }
        return v;
    }

    class GetBoardThread implements Runnable{
        int id;
        boolean state=true;
        public void getCategory(int c){
            id=c;
        }
        @Override
        public void run(){
            try{
                while(!Thread.currentThread().isInterrupted()) {
                    adapter = new CustomerAdapter2(); //반복할때마다 adapter생성
                    URL connectUrl = new URL("http://13.125.65.179:8080/vote_d/BoardRead.jsp");
                    HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestMethod("POST");//데이터를 POST 방식으로 전송합니다.
                    OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                    String sendMsg = "category=food";
                    ;
                    if (id == 3)
                        sendMsg = "category=food";
                    else if (id == 4)
                        sendMsg = "category=shop";
                    else if (id == 5)
                        sendMsg = "category=day";
                    osw.write(sendMsg);
                    osw.flush();

                    if (conn.getResponseCode() == 200) {
                        InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                        BufferedReader reader = new BufferedReader(tmp);
                        String line;
                        String page = "";

                        while ((line = reader.readLine()) != null) {
                            page += line;
                        }
                        JSONObject json = new JSONObject(page);
                        JSONArray jArr = json.getJSONArray("BoardData");
                        for (int i = 0; i < jArr.length(); i++) {
                            json = jArr.getJSONObject(i);

                            BoardData getJsonData = new BoardData();
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

                            System.out.println("읽어온 데이터 제목:" + getJsonData.getTitle() + "/리스트1:" + getJsonData.getList1() + "/리스트2:" + getJsonData.getList2() + "/총합:" + (getJsonData.getVote1() + getJsonData.getVote2()));
                            adapter.addItem(getJsonData);
                        }
                    } else {
                        System.out.println("통신 에러:" + conn.getResponseCode());
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //adapter.notifyDataSetChanged();
                            boardlist.setAdapter(adapter);
                        }
                    });

                    Thread.sleep(5000);
                }
            }catch (InterruptedException e) {} catch(Exception e){ System.out.println("에러확인_Thread:"+e);}
        }
    }

    private class GetImg extends AsyncTask<String, Void,Bitmap>{
        int i=1;
        @Override
        protected Bitmap doInBackground(String... params) {
            // TODO Auto-generated method stub
            try{
                URL connectUrl = new URL("http://13.125.65.179:8080/vote_d/img/"+params[0]);
                HttpURLConnection conn = (HttpURLConnection)connectUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();

                i=Integer.parseInt(params[1]);
                InputStream is = conn.getInputStream();
                bit = BitmapFactory.decodeStream(is);
            }catch(Exception e){
                System.out.println("에러확인_Task2:"+e.getMessage());
            }
            return bit;
        }

        protected void onPostExecute(Bitmap img){
            if(i==1)
                b_img1.setImageBitmap(bit);
            else
                b_img2.setImageBitmap(bit);
        }

    }

    class VoteNow extends AsyncTask<Object,Void,String>{
        String result;
        @Override
        protected String doInBackground(Object... params) {
            try{
                URL connectUrl = new URL("http://13.125.65.179:8080/vote_d/VoteNow.jsp");
                HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");//데이터를 POST 방식으로 전송합니다.
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                String sendMsg = "boardIndex="+params[0]+"&id="+params[1]+"&CheckNum="+params[2];
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
                    result = page.toString();
                }else{
                    System.out.println("통신 에러:"+conn.getResponseCode());
                }
            }catch (Exception e){
                System.out.println("에러확인_Task:"+e.getMessage());
                return null;
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
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

    class BackgroundTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            target = "http://13.125.65.179:8080/vote_d/Main.jsp";
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
/*
                // Send post request
                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(postParams);
                Log.d("Params Value", postParams);
                wr.flush();
                wr.close();*/

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
            adapter2 = new CustomerAdapter2();
            try {
                JSONObject jsonObject = new JSONObject(result);
                //boolean success = jsonObject.getBoolean("result");
                int total = jsonObject.getInt("votetotal");
                votecount.setText(total+"");
                JSONArray jArr = jsonObject.getJSONArray("BoardData");
                JSONObject json;
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
                allboard.setAdapter(adapter2);
            }catch(Exception e){
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            //Log.d("밸류값 = ",values[1]);
            super.onProgressUpdate(values);
        }
    }

    class DeleteBoard extends AsyncTask<Integer,Void,Boolean>{
        @Override
        protected Boolean doInBackground(Integer... params) {
            boolean result=false;
            try{
                URL connectUrl = new URL("http://13.125.65.179:8080/vote_d/BoardDelete.jsp");
                HttpURLConnection conn = (HttpURLConnection)connectUrl.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");//데이터를 POST 방식으로 전송합니다.
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                String sendMsg = "idx="+params[0];
                osw.write(sendMsg);
                osw.flush();

                if (conn.getResponseCode() == 200) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    String line;
                    String page = "";

                    while ((line = reader.readLine()) != null) {
                        page += line;
                    }
                    if(page.equals("성공")) result=true;
                } else {
                    System.out.println("통신 에러:" + conn.getResponseCode());
                }
            }catch(Exception e){
                System.out.println("에러확인_Task2:"+e.getMessage());
            }
            return result;
        }
    }
}