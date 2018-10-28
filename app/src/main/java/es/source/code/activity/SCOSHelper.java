package es.source.code.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.se.omapi.Session;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.source.code.model.GlobalConst;

public class SCOSHelper extends AppCompatActivity {

    public static final int MAIL_SEND_SUCCESS = 1;
    @BindView(R.id.gvHelpItem)
    GridView gvHelpItem;

    private String[] helpItemName = {"用户使用协议", "关于系统", "电话人工帮助", "短信帮助", "邮件帮助"};
    private int[] helpItemIcon = {R.drawable.document_png, R.drawable.information_png, R.drawable.phone_png, R.drawable.sms_png, R.drawable.email_png};
    private ArrayList<Map<String, Object>> helpItemList = new ArrayList<>();

    private String[] from = {"icon", "name"};
    private int[] to = {R.id.ibHelpItemImage, R.id.bHelpItem};

    private String SMS_SEND_ACTION = "SMS_SEND_ACTION";

    SimpleAdapter simpleAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoshelper);
        ButterKnife.bind(this);

        initData();
        simpleAdapter = new SimpleAdapter(this, helpItemList, R.layout.activity_scos_helper_gridview_item, from, to);
        gvHelpItem.setAdapter(simpleAdapter);

        gvHelpItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 2://Phone
                        if (ContextCompat.checkSelfPermission(SCOSHelper.this, Manifest.permission.CALL_PHONE)==PackageManager.PERMISSION_GRANTED) {
                            call();
                        } else {
                            ActivityCompat.requestPermissions(SCOSHelper.this,new String[]{Manifest.permission.CALL_PHONE},GlobalConst.CALL_PHONE_PERMISSION_REQUEST_CODE);
                        }
                        break;
                    case 3://SMS
                        if (ContextCompat.checkSelfPermission(SCOSHelper.this,Manifest.permission.SEND_SMS)==PackageManager.PERMISSION_GRANTED) {
                            sendMessage();
                        } else {
                            ActivityCompat.requestPermissions(SCOSHelper.this,new String[]{Manifest.permission.SEND_SMS},GlobalConst.SMS_PERMISSION_REQUEST_CODE);
                        }
                        break;
                    case 4://mail
                        final int testThread = 3;
                        if (ContextCompat.checkSelfPermission(SCOSHelper.this,Manifest.permission.INTERNET)==PackageManager.PERMISSION_GRANTED) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Log.i("in thread:",Thread.currentThread().getName());
                                        Log.i("get testThread ",String.valueOf(testThread));
                                        sendMail();
                                        handler.sendEmptyMessage(1);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        } else {
                            ActivityCompat.requestPermissions(SCOSHelper.this,new String[]{Manifest.permission.SEND_SMS},GlobalConst.INTERNET_PERMISSION_REQUEST_CODE);
                        }
                }
            }
        });
    }

    private void initData() {
        for (int i = 0; i < helpItemName.length; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put(from[0], helpItemIcon[i]);
            item.put(from[1], helpItemName[i]);
            helpItemList.add(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case GlobalConst.CALL_PHONE_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call();
                } else {
                    Toast toast = Toast.makeText(this,GlobalConst.CALL_PHONE_PERMISSION_DENIED_INFO,Toast.LENGTH_SHORT);
                    toast.show();
                }
                return;
            }
            case GlobalConst.SMS_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendMessage();
                } else {
                    Toast toast = Toast.makeText(this,GlobalConst.SEND_SMS_PERMISSION_DENIED_INFO,Toast.LENGTH_SHORT);
                    toast.show();
                }
                return;
            }
            case GlobalConst.INTERNET_PERMISSION_REQUEST_CODE: {
                if (grantResults.length>0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                sendMail();
                                handler.sendEmptyMessage(1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else {
                    Toast toast = Toast.makeText(this,GlobalConst.INTERNET_PERMISSION_DENIED_INFO,Toast.LENGTH_SHORT);
                    toast.show();
                }
                return;
            }
        }
    }

    private void call() {
        Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+GlobalConst.HELP_PHONE_NUMBER));
        startActivity(intent);
    }

    private void sendMessage() {
        ReceiverListener receiverListener = new ReceiverListener();
        IntentFilter intentfilter = new IntentFilter();

        SmsManager smsManager = SmsManager.getDefault();
        try{
            // 创建ACTION常数的Intent，作为PendingIntent的参数
            Intent SendIt = new Intent(SMS_SEND_ACTION);

            // 接收消息传送后的广播信息SendPendIt，作为信息发送sendTextMessage方法的sentIntent参数
            PendingIntent SendPendIt = PendingIntent.getBroadcast(getApplicationContext(), 0, SendIt, PendingIntent.FLAG_CANCEL_CURRENT);

            // 发送短信
            smsManager.sendTextMessage(GlobalConst.HELP_PHONE_NUMBER, null, GlobalConst.SMS_CONTENT, SendPendIt, null);

        }catch (Exception e) {
            // 异常提醒
            Toast.makeText(SCOSHelper.this, e.toString(), Toast.LENGTH_LONG).show();
        }

        // 广播注册
        intentfilter.addAction(SMS_SEND_ACTION);
        registerReceiver(receiverListener, intentfilter);
    }

    public class ReceiverListener extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            try
            {
                // 获取短信状态
                switch (getResultCode()) {
                    // 短信发送成功
                    case Activity.RESULT_OK:
                        Toast.makeText(SCOSHelper.this, "求助短信发送成功", Toast.LENGTH_LONG).show();
                        break;
                    // 短信发送不成功
                    default:
                        Toast.makeText(SCOSHelper.this, "发送失败，请重新发送！", Toast.LENGTH_LONG).show();
                        break;
                }
            }catch (Exception e) {
                Toast.makeText(SCOSHelper.this, "发送出现异常，请重新发送！", Toast.LENGTH_LONG).show();
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what== MAIL_SEND_SUCCESS) {
                Toast toast = Toast.makeText(SCOSHelper.this,"邮件发送成功",Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    };

    private void sendMail() {
        HtmlEmail email = new HtmlEmail();
        try {
            Log.i("send state:","start sending");
            email.setHostName("smtp.qq.com");
            email.setSmtpPort(587);
            email.setAuthentication("712402268@qq.com", "xxxx");
            email.setCharset("utf-8");
            email.addTo("zcdcffh@163.com");
            email.setFrom("712402268@qq.com");
            email.setSubject("SCOS");
            email.setMsg("help");
            Log.i("send state","have set information");
            email.send();
            Log.i("send state:","Send finish");
        } catch (EmailException e) {
            Log.i("send state","something wrong");
            e.printStackTrace();
        }
    }

}
