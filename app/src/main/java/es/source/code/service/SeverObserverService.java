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
import es.source.code.activity.FoodView;
import es.source.code.model.FoodStockInfo;
import es.source.code.model.GlobalConst;
import es.source.code.model.MenuData;


public class SeverObserverService extends Service {

    private static final String TAG = "SeverObserverService state:";
    private boolean run = false;
    private boolean getResponse = false;
    private UpdateThread updateThread;
    private ArrayList<Integer> foodListSize;
    private MenuData menuData;
    private Context context;
    private int clientProcessID;

    @SuppressLint("HandlerLeak")
    private Handler cMessageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalConst.BIND_MSG:
                    Log.i(TAG,"get activity messenger");
                    activityMessenger = msg.replyTo;
                    clientProcessID = msg.getData().getInt("ProcessID");
                    break;
                case GlobalConst.START_UPDATE_FOODINFO:
                    run = true;
                    menuData = (MenuData) getApplication();
                    updateThread = new UpdateThread();
                    updateThread.start();
                    Log.i(TAG, "start update");
                    break;
                case GlobalConst.STOP_UPDATE_FOODINFO:
                    run = false;
                    updateThread = null;
                    Log.i(TAG,"stop update");
                    break;
                case GlobalConst.RESPONSE_OF_FOOD_SIZE:
                    Log.i(TAG,"get FoodView Response");
                    Bundle bundle = msg.getData();
                    foodListSize = bundle.getIntegerArrayList(FoodView.FOOD_LIST_SIZE);
                    getResponse = true;
                    break;
            }
        }
    };


    class UpdateThread extends Thread{

        @Override
        public void run() {
            Log.i("Observer Thread state:","start");
            Log.i(TAG, "pid is"+String.valueOf(android.os.Process.myPid()));
            while(run) {
                try {
                    Log.i("Observer Thread state:", "sleep");
                    Bundle bundle = randomGenerateFoodStock();
                    if(bundle==null) {
                        Log.i("bundle content is ", "null");
                    } else {
                        Log.i("Observer service state:","get stockInfo");
                        // check if process is running
                        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                        assert mActivityManager != null;
                        List<ActivityManager.RunningAppProcessInfo> lists = mActivityManager.getRunningAppProcesses();

                        for (ActivityManager.RunningAppProcessInfo info : lists) {
                            if (info.pid == clientProcessID) {
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
                } catch (RemoteException e) {
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
        flags = START_STICKY;
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

    private Bundle randomGenerateFoodStock() throws RemoteException {
        double hasUpdate = Math.random();
        Message msgRequireFoodListSize = new Message();
        msgRequireFoodListSize.what = GlobalConst.REQURIRE_FOOD_SIZE;
        activityMessenger.send(msgRequireFoodListSize);

//        menuData = (MenuData) getApplication();
//        foodListSize = menuData.getFoodListSize();
        int stock;

        getResponse = false;
        Bundle foodsStock;
        //test if need update
        Log.i("hasUpdate value",String.valueOf(hasUpdate));

        while (!getResponse) {
            Log.i(TAG,"wait for the response");
        }
        if(hasUpdate>0.4 || hasUpdate<0.6) {
            foodsStock = new Bundle();
            for (int i = 0; i < GlobalConst.FOOD_TYPE_SIZE; i++) {
                ArrayList<FoodStockInfo> arrayList = new ArrayList<>();
                Log.i("size = ", String.valueOf(foodListSize.get(i)));
                for (int j = 0; j < foodListSize.get(i); j++) {
                    stock = (int) ( (Math.random()*100) % 5);   //generate a random stock
                    FoodStockInfo foodStockInfo = new FoodStockInfo(j, stock);
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
