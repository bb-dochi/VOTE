package com.example.user.vote;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by SKY on 2017-11-29.
 */

public class ChangePWDialog extends Dialog implements DialogInterface.OnClickListener {
    Button check;
    Button change;
    EditText before,after1,after2;
    public ChangePWDialog(Context context){
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_changepw);
        check = (Button)findViewById(R.id.check);
        change = (Button)findViewById(R.id.change);
        before = (EditText)findViewById(R.id.before);
        after1 = (EditText)findViewById(R.id.after1);
        after2 = (EditText)findViewById(R.id.after2);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}
