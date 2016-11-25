package com.example.ivan.test2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Ivan on 2016/11/25.
 */

public class FriendAdapter extends ArrayAdapter<Friend> {
    private int resourceId;

    public FriendAdapter(Context context, int textViewResourceId, List<Friend> objects)
    {
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Friend friend = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        TextView friendName = (TextView) view.findViewById(R.id.friendName);
        TextView friendNum = (TextView) view.findViewById(R.id.friendNum);
        friendName.setText(friend.getName());
        friendNum.setText(friend.getPhoneNum());
        return view;
    }
}
