package com.example.ivan.test2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.lang.reflect.AccessibleObject;

/**
 * Created by Ivan on 2016/11/24.
 */

public class FriendActivity extends Activity {

    //UI
    private Button addBtn;
    private ListView listView;
    private String friendName;
    private String friendNum;


    @Override
    public void onCreate(Bundle sasavedInstanceState)
    {

        super.onCreate(sasavedInstanceState);
        setContentView(R.layout.activity_friend);
        listView = (ListView) findViewById(R.id.listview);
//
        final FriendAdapter adapter = new FriendAdapter(FriendActivity.this,R.layout.item_layout,MainActivity.friendArrayList);
        listView.setAdapter(adapter);
////
        addBtn = (Button) findViewById(R.id.addbtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAlertDialog();
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void CreateAlertDialog()
    {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        final View view = layoutInflater.inflate(R.layout.alertdialog_layout,null);
        Dialog dialog = new AlertDialog.Builder(this).
                setTitle("输入信息").
                setView(view).
                setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //将edit信息add到arraylist
                        EditText FriendName = (EditText)view.findViewById(R.id.edit_name);
                        EditText FriendNum= (EditText)view.findViewById(R.id.edit_number);
                        Friend friend = new Friend();
                        friend.setName(FriendName.getText().toString());
                        friend.setPhoneNum(FriendNum.getText().toString());
//                        friend.setName("kason2nd");//for test
//                        friend.setPhoneNum("123456789");//for test
                        MainActivity.friendArrayList.add(friend);

                    }
                }).
                setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //取消
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();




    }
}
