package es.source.code.br;

import android.app.NotificationManager;
import android.app.admin.DeviceAdminService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import es.source.code.service.UpdateService;

public class DeviceStartedListener extends BroadcastReceiver {

    private static final String TAG = "DeviceStartedListener state:";
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "receive broadcast");
        String action = intent.getAction();
        if(action!=null && action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.i(TAG,"receive boot broadcast");
            Intent intent1 = new Intent(context,UpdateService.class);
            if(android.os.Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
                Log.i("sdk version:",">=26");
                context.startForegroundService(intent1);
            } else {
                Log.i("sdk version:","<26");
                context.startService(intent1);
            }
            Log.i(TAG,"have send intent");
        } else if(intent.getStringExtra("action").equals("close notification")) {
            Log.i(TAG,"get the intent to cancel the notification");
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(intent.getIntExtra("notification_id",0));
        }

    }
}
