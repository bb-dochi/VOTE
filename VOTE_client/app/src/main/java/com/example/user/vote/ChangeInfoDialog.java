package com.example.user.vote;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by SKY on 2017-11-30.
 */

public class ChangeInfoDialog extends Dialog implements DialogInterface.OnClickListener {
    Button check;
    Button change;
    TextView userID;
    EditText spw,sname,sphone,smail;
    public ChangeInfoDialog(Context context){
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_changeinfo);
        userID = (TextView) findViewById(R.id.userID);
        check = (Button)findViewById(R.id.check);
        change = (Button)findViewById(R.id.change);
        spw = (EditText)findViewById(R.id.spw);
        sname = (EditText)findViewById(R.id.sname);
        smail = (EditText)findViewById(R.id.smail);
        sphone = (EditText)findViewById(R.id.sphone);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}
