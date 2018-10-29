package es.source.code.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import es.source.code.activity.FoodDetailed;
import es.source.code.activity.R;
import es.source.code.fragment.MealFragment;
import es.source.code.model.Food;
import es.source.code.model.GlobalConst;
import es.source.code.model.MenuData;

public class UpdateService extends IntentService {

    private static final int NOTIFICATION_ID = 15;
    public static final String COM_EXAMPLE_HFANG = "com.example.hfang";
    private boolean run = true;
    private MenuData menuData;
    private String[] tabName = {"冷菜","热菜","海鲜","酒水"};
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
        Log.i("UpdateService state:","get into onCreate");

        Log.i("System version", String.valueOf(Build.VERSION.SDK_INT));
        if( android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.i("UpdateService state:","starForeground");
            startMyOwnForeground();
        }
        menuData = (MenuData) getApplicationContext();
        Log.i("UpdateService state:","onCreate");
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
        Log.i("UpdateService state:", "handle Intent");
        updateThread = new UpdateThread();
        updateThread.start();

    }

    class UpdateThread extends Thread {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void run() {
            Log.i("UpdateService state:","UpdateThread is running");
            while(run) {
                Food newFood = randomGenerateNewFood();
                if (newFood!=null) {
                    updateMenu(newFood);
                    updateView(newFood);
                    newFood.setFoodID(menuData.getFoodLists().get(newFood.getType()).get(newFood.getPosition()).getFoodID());
                    notifyUser(newFood);
                    Log.i("UpdateService state:","add a new food");
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updateMenu(Food newFood) {
        menuData.addNewFood(newFood);
    }

    private void updateView(Food newFood) {
        GlobalConst.NewFoodMessageEvent newFoodMessageEvent = new GlobalConst.NewFoodMessageEvent(true,newFood);
        EventBus.getDefault().postSticky(newFoodMessageEvent);
    }

    private Food randomGenerateNewFood() {
        double hasUpdate = Math.random();
        Food newFood;
        if(hasUpdate<0.6 && hasUpdate>0.4) {
            int randomType = (int) (Math.random()*100) %4;
            int length = menuData.getMealNames()[randomType].length;
            int randomPosition = (int) (Math.random()*100)%length;
            String name = menuData.getMealNames()[randomType][randomPosition];
            int imageId = menuData.getFoodImageID()[randomType][randomPosition];
            int id = MenuData.getFoodNums();
            double price = menuData.getMealPrice()[randomType][randomPosition];
            newFood = new Food("新"+name,price+10.0,randomType,imageId,length,id+1);
        } else {
            return null;
        }
        return newFood;
    }

    private void notifyUser(Food newFood) {
        Notification.Builder notificationBuilder;
        if(android.os.Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
            notificationBuilder = new Notification.Builder(this, COM_EXAMPLE_HFANG);
        } else {
            notificationBuilder = new Notification.Builder(this);
        }
        notificationBuilder.setContentTitle("新品上架");
        notificationBuilder.setContentText(newFood.getName() + " " + newFood.getPrice() + "元 " + tabName[newFood.getType()]);

        Intent intent = new Intent(this, FoodDetailed.class);
        intent.putExtra("page",newFood.getFoodID());
        Log.i("Food ID = ",String.valueOf(newFood.getFoodID()));
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setAutoCancel(true);

        Notification notification = notificationBuilder.build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        assert notificationManager != null;
        notificationManager.notify(NOTIFICATION_ID, notification);

    }
}
