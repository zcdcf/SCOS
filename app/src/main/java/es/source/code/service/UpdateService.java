package es.source.code.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import es.source.code.activity.FoodDetailed;
import es.source.code.activity.R;
import es.source.code.bean.BeanFood;
import es.source.code.br.DeviceStartedListener;
import es.source.code.model.Food;
import es.source.code.model.GlobalConst;
import es.source.code.model.MenuData;

public class UpdateService extends IntentService {

    public static final int NOTIFICATION_ID = 15;
    private static final int DEFAULT_FOODID = 0;
    private static final int FLAG_CANCEL = 101;
    private static final int DEFAULT_FOOD_IMAGEID = R.drawable.scos_logo;
    private static final String COM_EXAMPLE_HFANG = "com.example.hfang";
    private static final String TAG = "UpdateService state:";
    private boolean run = true;
    private MenuData menuData;
    private String[] tabName = {"冷菜","热菜","海鲜","酒水"};
    private String requestUrl = GlobalConst.BASE_URL+"/SCOSServer/FoodUpdateService";
    private static final int REQUESTCODE = GlobalConst.TEST_XML;
    UpdateThread updateThread;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public UpdateService() {
        super("UpdateService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"get into onCreate");

        Log.i("System version", String.valueOf(Build.VERSION.SDK_INT));
        if( android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.i(TAG,"starForeground");
            startMyOwnForeground();
        }
        menuData = (MenuData) getApplicationContext();
        Log.i(TAG,"onCreate");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground(){
        CharSequence channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(COM_EXAMPLE_HFANG, channelName, NotificationManager.IMPORTANCE_DEFAULT);
        chan.setLightColor(Color.BLUE);
        chan.setDescription("用于Pie");
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        Notification.Builder notificationBuilder = new Notification.Builder(this, COM_EXAMPLE_HFANG);
        notificationBuilder.setContentTitle("SCOS Update Service Running");
        notificationBuilder.setContentText("SCOS正在更新菜单");
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setAutoCancel(true);
        Notification notification = notificationBuilder.build();

        startForeground(2, notification);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "handle Intent");
        updateThread = new UpdateThread();
        updateThread.start();

    }

    class UpdateThread extends Thread {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void run() {
            Log.i(TAG,"UpdateThread is running");
            while(run) {
                JSONObject newFoodInfo;
                try {
                    String result = connectToServer();
                    if(!result.equals("null")) {
                        Log.i(TAG,result);
                        newFoodInfo = new JSONObject(result);
                        Log.i(TAG,newFoodInfo.toString());
                        String name = null;
                        try {
                            name = URLDecoder.decode(newFoodInfo.getString("NAME"),"UTF-8");
                            Log.i(TAG,"name is "+name);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        double price = newFoodInfo.getDouble("PRICE");
                        int type = newFoodInfo.getInt("TYPE");
                        int position = menuData.getFoodNameList().get(type).size();
                        int stock = newFoodInfo.getInt("STOCK");
                        Food newFood = new Food(name,price,type,DEFAULT_FOOD_IMAGEID,position,DEFAULT_FOODID);
                        newFood.setStock(stock);
                        updateMenuData(newFood);
                        updateView(newFood);
                        newFood = menuData.getFoodInfo(newFood.getName(),newFood.getType());
                        if(newFood==null) {
                            Log.i(TAG,"didn't find the food info in the MenuData");
                        }
                        notifyUser(newFood);
                        Log.i(TAG,"add a new food");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updateMenuData(Food newFood) {
         menuData.addNewFood(newFood);
    }

    private void updateView(Food newFood) {
        GlobalConst.NewFoodMessageEvent newFoodMessageEvent = new GlobalConst.NewFoodMessageEvent(true,newFood);
        EventBus.getDefault().postSticky(newFoodMessageEvent);
    }


    private void notifyUser(Food newFood) {
        Notification.Builder notificationBuilder;
        if(android.os.Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            notificationBuilder = new Notification.Builder(this, COM_EXAMPLE_HFANG);
        } else {
            notificationBuilder = new Notification.Builder(this);
        }

        // intent used to cancel the notification
        Intent closeIntent = new Intent(this,DeviceStartedListener.class);
        closeIntent.putExtra("notification_id", NOTIFICATION_ID);
        closeIntent.putExtra("action","close notification");
        PendingIntent closeNotificationIntent = PendingIntent.getBroadcast(UpdateService.this, FLAG_CANCEL, closeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentTitle("新品上架");
        notificationBuilder.setContentText(newFood.getName() + " " + newFood.getPrice() + "元 " + tabName[newFood.getType()]);
        //noinspection deprecation
        notificationBuilder.addAction(R.drawable.close_notification,"清除",closeNotificationIntent);

        // intent to jump to the FoodDetail
        Intent intent = new Intent(this, FoodDetailed.class);
        intent.putExtra("page",newFood.getFoodID());
        Log.i(TAG,"Food ID = "+newFood.getFoodID());
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setAutoCancel(true);

        Notification notification = notificationBuilder.build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        assert notificationManager != null;
        notificationManager.notify(NOTIFICATION_ID, notification);
        playNotificationSound();
    }

    private void playNotificationSound() {
        MediaPlayer mp = new MediaPlayer();
        try {
            mp.setDataSource(getApplicationContext(), getSystemDefaultRingtoneUri());
            mp.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);//音量跟随系统通知音量
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp.setVolume(1f, 1f);
        mp.setLooping(false);
        mp.start();
    }

    private Uri getSystemDefaultRingtoneUri() {
        return RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION);
    }


    private String connectToServer() {
        StringBuilder sb = new StringBuilder();
        try {
            Log.i(TAG,"start the connect");
            URL url = new URL(requestUrl+"?needUpdate="+REQUESTCODE);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            Log.i(TAG,"opened the connect");
            Log.i(TAG,"response code = "+connection.getResponseCode());
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Log.i(TAG,"Http OK");
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String temp;
                while ((temp = reader.readLine()) != null) {
                    Log.i(TAG, "get response " + temp);
                    sb.append(temp);
                }
                reader.close();
                switch (REQUESTCODE) {
                    case GlobalConst.GET_FOOD_UPDATE: {
                        Log.i(TAG,"get food Update");
                        break;
                    }
                    case GlobalConst.TEST_JSON:{
                        Log.i(TAG,"test json");

                        Log.i(TAG,"JSON stream length is "+sb.length());
                        long startTime=System.currentTimeMillis();
                        List<BeanFood> foodList = JSON.parseArray(sb.toString(),BeanFood.class);
                        long finishTime=System.currentTimeMillis();

                        Log.i(TAG,"parse json runtime = "+(finishTime-startTime)+"ms");
                        Log.i(TAG,"size = "+foodList.size());

                        for(int i=0; i<foodList.size(); i++) {
                            Log.i(TAG,foodList.get(i).toString());
                        }

                        // return the first JSONobj as update information
                        JSONObject firstObj = getFirstEleAsJsonObject(foodList);
                        return firstObj.toString();
                    }
                    case GlobalConst.TEST_XML:{
                        Log.i(TAG,"test xml");
                        Log.i(TAG,"XML stream length is "+sb.length());

                        // parse the xml string
                        long startTime = System.currentTimeMillis();
                        Document document = DocumentHelper.parseText(sb.toString());
                        Element root = document.getRootElement();
                        Iterator iterator = root.elementIterator();
                        List<BeanFood> foodList = new ArrayList<>();
                        while (iterator.hasNext()) {
                            Element son = (Element) iterator.next();
                            BeanFood newFood = new BeanFood();
                            newFood.setNAME(son.elementText("NAME"));
                            newFood.setPRICE(Double.parseDouble(son.elementText("PRICE")));
                            newFood.setSTOCK(Integer.parseInt(son.elementText("STOCK")));
                            newFood.setTYPE(Integer.parseInt(son.elementText("TYPE")));
                            foodList.add(newFood);
                        }
                        long finishTime = System.currentTimeMillis();
                        Log.i(TAG,"parse xml runtime = "+(finishTime-startTime)+"ms");
                        Log.i(TAG,"size = "+foodList.size());
                        JSONObject firstObj = getFirstEleAsJsonObject(foodList);
                        return firstObj.toString();
                    }
                }
            } else {
                return "";
            }
            connection.disconnect();
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    private JSONObject getFirstEleAsJsonObject(List<BeanFood> foodList) {
        JSONObject firstObj = new JSONObject();
        try {
            firstObj.put("NAME",foodList.get(0).getNAME());
            firstObj.put("STOCK",foodList.get(0).getSTOCK());
            firstObj.put("PRICE",foodList.get(0).getPRICE());
            firstObj.put("TYPE",foodList.get(0).getTYPE());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return firstObj;
    }
}
