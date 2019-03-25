package com.example.user.vote;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView votecount;
    FragmentTransaction ft;
    CustomerAdapter2 adapter;
    ListView allboard;
    String target;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("");
        allboard = (ListView)findViewById(R.id.allboard);
        votecount = (TextView)findViewById(R.id.votecount);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*-------------ㅡ메인 플레그먼트 설정 (xml에서 고정해버리면 화면전환 시 중복됨)--------------*/
        ft = getSupportFragmentManager().beginTransaction();
        Fragment m = new Sub_Frag();
        Bundle bundle = new Bundle();
        bundle.putInt("id", 1);
        m.setArguments(bundle);
        ft.replace(R.id.content_main, m);
        ft.commit();
        /*------------------------------------------------------------------------------------------*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                Intent it = new Intent(getApplicationContext(),Write.class);
                getApplicationContext().startActivity(it);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void home(View v){
        if(Sub_Frag.thread!=null&&Sub_Frag.thread.isAlive()) Sub_Frag.thread.interrupt();//스레드종료
        ft = getSupportFragmentManager().beginTransaction();
        Fragment m = new Sub_Frag();
        Bundle bundle = new Bundle();
        bundle.putInt("id", 1);
        m.setArguments(bundle);
        ft.replace(R.id.content_main, m);
        ft.commit();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment f=new Sub_Frag();
        Bundle bundle = new Bundle();
        int id = item.getItemId();

        if (id == R.id.nav_mypage) {
            f= new MyPage_Frag();
            bundle.putInt("id", 2);
        } else if (id == R.id.nav_food) {
            bundle.putInt("id", 3);
        } else if (id == R.id.nav_shop) {
            bundle.putInt("id", 4);
        }else if (id == R.id.nav_day) {
            bundle.putInt("id", 5);
        }

        f.setArguments(bundle);
        String fragmentTag = f.getClass().getSimpleName();
        getSupportFragmentManager().popBackStack(fragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        if(f!=null)
        {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, f);
            ft.addToBackStack(fragmentTag);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    public class CustomerAdapter2 extends BaseAdapter {
        private ArrayList<BoardData> boardDataList = new ArrayList<BoardData>();
        public CustomerAdapter2(){

        }
        public View getView(final int position, View convertView, ViewGroup parent) {
            try{
                final Context context = parent.getContext();
                /*if(convertView == null){
                    LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(context.LAYOUT_INFLATER_SERVICE);
                    convertView = vi.inflate(R.layout.custom_list2, null);
                }*/

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

}
