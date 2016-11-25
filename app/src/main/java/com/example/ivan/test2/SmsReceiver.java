package com.example.ivan.test2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

/**
 * Created by Ivan on 2016/11/24.
 */

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Bundle bundle = intent.getExtras();
        SmsMessage smsMessage = null;
        if(null==bundle)
        {
            Object[] smsObj = (Object[]) bundle.get("pdus");
            for (Object object : smsObj)
            {
                smsMessage = SmsMessage.createFromPdu((byte[])object);
                String sender = smsMessage.getOriginatingAddress();
                String content = smsMessage.getMessageBody();
            }
        }

    }
}
