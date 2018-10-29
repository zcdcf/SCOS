package es.source.code.service;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;
import android.os.Handler;
import android.view.Menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import dalvik.system.PathClassLoader;
import es.source.code.model.FoodStockInfo;
import es.source.code.model.GlobalConst;
import es.source.code.model.MenuData;


public class SeverObserverService extends Service {

    private boolean run = true;
    private UpdateThread updateThread;

    @SuppressLint("HandlerLeak")
    private Handler cMessageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalConst.BIND_MSG:
                    Log.i("Observer Service State:","get activity messenger");
                    activityMessenger = msg.replyTo;
                    break;
                case GlobalConst.START_UPDATE_FOODINFO:
                    run = true;
                    updateThread = new UpdateThread();
                    updateThread.start();
                    Log.i("Observer Service State:", "start update");
                    break;
                case GlobalConst.STOP_UPDATE_FOODINFO:
                    run = false;
                    updateThread = null;
                    Log.i("Observer Service state:","stop update");
                    break;
            }
        }
    };


    class UpdateThread extends Thread{

        @Override
        public void run() {
            Log.i("Observer Thread state:","start");
            while(run) {
                try {
                    Log.i("Observer Thread state:", "sleep");
                    Bundle bundle = randomGenerateFoodStock();
                    if(bundle==null) {
                        Log.i("bundle content is ", "null");
                    } else {
                        Log.i("Observer service state:","get stockInfo");
                        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                        assert mActivityManager != null;
                        List<ActivityManager.RunningAppProcessInfo> lists = mActivityManager.getRunningAppProcesses();

                        for (ActivityManager.RunningAppProcessInfo info : lists) {
                            if (info.pid == android.os.Process.myPid()) {
                                Message msg = new Message();
                                msg.what = GlobalConst.STOCK_HAS_UPDATE;
                                msg.setData(bundle);
                                try {
                                    Log.i("Observer service state:", "try to send msg to FoodView"+bundle.size());
                                    activityMessenger.send(msg);
                                    Log.i("Observer service state:", "has send msg");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    Thread.sleep(300);
                                }
                                break;
                            }
                        }

                        Log.i("Thread state:", "wake");
                    }
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private Messenger activityMessenger;
    private Messenger serviceMessenger;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Observer Service state:","onCreate()");

/*        HandlerThread handlerThread = new HandlerThread("checkUpdate");
        handlerThread.start();*/

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Observer Service state:","onStartCommand()");
        if(activityMessenger !=null) {
            activityMessenger = (Messenger) intent.getExtras().get("messenger");
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("Observer Service state:","onDestroy()");
    }



    public IBinder onBind(Intent intent) {
        Log.i("Observer Service state:","onBind()");
        serviceMessenger = new Messenger(cMessageHandler);
        return serviceMessenger.getBinder();
    }

    private Bundle randomGenerateFoodStock() {
        double hasUpdate = Math.random();
        MenuData menuData = (MenuData) getApplication();
        int stock;

        Bundle foodsStock;
        //test if need update
        Log.i("hasUpdate value",String.valueOf(hasUpdate));
        if(hasUpdate>0.4 && hasUpdate<0.6) {
            foodsStock = new Bundle();
            ArrayList<ArrayList<String>> foodNameLists;
            foodNameLists = menuData.getFoodNameList();
            for (int i = 0; i < foodNameLists.size(); i++) {
                ArrayList<FoodStockInfo> arrayList = new ArrayList<>();
                for (int j = 0; j < foodNameLists.get(i).size(); j++) {
                    Log.i("size = ", String.valueOf(foodNameLists.get(i).size()));
                    stock = (int) ( (Math.random()*100) % 5);   //generate a random stock
                    FoodStockInfo foodStockInfo = new FoodStockInfo(foodNameLists.get(i).get(j), stock);
                    arrayList.add(foodStockInfo);
                }
                foodsStock.putSerializable(String.valueOf(i),arrayList);
            }
        } else {
            Log.i("Observer service state:","not update");
            return null;
        }

        return foodsStock;
    }

}
