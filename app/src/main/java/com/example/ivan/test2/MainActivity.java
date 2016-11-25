package com.example.ivan.test2;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.location.Location;
import android.location.LocationListener;

import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.ArrayList;



public class MainActivity extends Activity {

    private MapView mapView;
    private BaiduMap baiduMap;

    private boolean isFirstLocate = true;
    private LocationClient locationClient;
    public static BDLocation MyBDlocation = new BDLocation();
    private MLocationListener mLocationListener = new MLocationListener();

    //receiver
    private IntentFilter intentFilter;
    private SmsReceiver smsReceiver;


    //UI part
    private Button RefreshBtn;
    private Button FriendBtn;


    //data
    public static ArrayList<Friend> friendArrayList = new ArrayList<Friend>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
//        mapView = (MapView) findViewById(R.id.map_view);
//        baiduMap = mapView.getMap();
//        baiduMap.setMyLocationEnabled(true);
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        Location location = locationManager.getLastKnownLocation(provider);
//        if(location != null)
//        {
//            navigateTo(location);
//        }
//        locationManager.requestLocationUpdates(provider,5000,1,locationListener);
        //***************************************
        //UI部分 btn测试
        //***************************************
        //************************************
        load();
//        Friend friend = new Friend();
//        friend.setName("kason1st");
//        friend.setPhoneNum("123456789");
//        MainActivity.friendArrayList.add(friend);
        //************************************
        RefreshBtn = (Button) findViewById(R.id.refresh);
        RefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //向所有的朋友发“where are you”
                if (friendArrayList.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"大兄弟你的朋友列表是空的",Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    for (Friend friend : friendArrayList) {
                        Send(friend.getPhoneNum(), "where are you");
                        Toast.makeText(getApplicationContext(),"短信发完了",Toast.LENGTH_SHORT).show();
                    }
                }
                //开启SmsReceiver
                intentFilter = new IntentFilter();
                intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
                smsReceiver = new SmsReceiver();
                registerReceiver(smsReceiver,intentFilter);
                AddAllPoint();

            }
        });
        FriendBtn = (Button) findViewById(R.id.friendbtn);
        FriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,FriendActivity.class);
                startActivity(intent);
            }
        });

        init();
        locationClient.registerLocationListener(mLocationListener);
        locationClient.start();

    }


    public void Send(String phoneNum,String content)
    {
        SmsManager manager = SmsManager.getDefault();
        ArrayList<String> list = manager.divideMessage(content);
        for(String text:list)
        {
            manager.sendTextMessage(phoneNum,null,text,null,null);
        }
    }



    private void init()
    {
        mapView = (MapView) findViewById(R.id.map_view);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        locationClient = new LocationClient(this);
        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setScanSpan(999);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIsNeedLocationDescribe(true);
        option.setIsNeedLocationPoiList(false);
        option.setIgnoreKillProcess(true);
        option.SetIgnoreCacheException(false);
        option.setEnableSimulateGps(false);

        locationClient.setLocOption(option);

    }

    public class MLocationListener implements BDLocationListener
    {
        @Override
        public void onReceiveLocation(BDLocation location)
        {
            if(location == null)
            {
                return;
            }
            MyLocationData myLocationData = new MyLocationData.Builder().accuracy(location.getRadius()).direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
            baiduMap.setMyLocationData(myLocationData);
            MyBDlocation = location;
            if(isFirstLocate)
            {
                CenterMe(location);
                isFirstLocate = false;
            }

        }
    }

    private void CenterMe(BDLocation location)
    {
        LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(baiduMap.getMapStatus().zoom);
        builder.zoom(16f);
        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }


    private void navigateTo(Location location)
    {
        if (isFirstLocate)
        {
            LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            baiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(16f);
            baiduMap.animateMapStatus(update);
            isFirstLocate = false;
        }
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        MyLocationData locationData = locationBuilder.build();
        baiduMap.setMyLocationData(locationData);
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null)
            {
                navigateTo(location);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    protected void onDestroy()
    {
        save(friendArrayList);
        super.onDestroy();
        mapView.onDestroy();


    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mapView.onResume();
    }

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
                }
                String sender = smsMessage.getOriginatingAddress();
                String content = smsMessage.getMessageBody();
                if (content.equals("where are you"))
                {
                    Send(sender,MyBDlocation.getLatitude()+"/"+MyBDlocation.getLongitude());
                }
                if(content.substring(8,9).equals("/"))
                {
                    String senderLatitude = content.substring(0,8);
                    String senderLongtude = content.substring(9,16);
                    for(int i =0 ; i<=friendArrayList.size();i++)
                    {
                        if (sender==friendArrayList.get(i).getPhoneNum())
                        {
                            friendArrayList.get(i).setLatitude(senderLatitude);
                            friendArrayList.get(i).setLongitude(senderLongtude);
                        }
                    }
                }
            }

        }
    }

    public void AddPoint(Friend friend)
    {
        LatLng point = new LatLng(Double.valueOf(friend.getLatitude()),Double.valueOf(friend.getLongitude()));
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_openmap_focuse_mark);
        OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
        baiduMap.addOverlay(option);

    }

    public void onStop()
    {
        save(friendArrayList);
        super.onStop();
    }

    public void AddAllPoint()
    {
        if (friendArrayList.size()>0)
        {
            for(Friend friend : friendArrayList)
            {
                AddPoint(friend);
            }
        }
        else
        {
            Toast.makeText(MainActivity.this,"大兄弟你的朋友列表是空的，这条提示来自AddAllPoint",Toast.LENGTH_SHORT).show();
        }
    }

    private void save(ArrayList<Friend> friendArrayList)
    {
        try {
            FileOutputStream fileStream = new FileOutputStream("MyGame.ser");
            ObjectOutputStream os = new ObjectOutputStream(fileStream);
            os.writeObject(friendArrayList);
            os.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


    }

    private void load()
    {
        try {
            FileInputStream fileStream = new FileInputStream("MyGame.ser");
            ObjectInputStream os = new ObjectInputStream(fileStream);
            Object one = os.readObject();
            friendArrayList = (ArrayList<Friend>) one;
            os.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}
