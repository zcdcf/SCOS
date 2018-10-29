package es.source.code.br;

import android.app.admin.DeviceAdminService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import es.source.code.service.UpdateService;

public class DeviceStartedListener extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("DeviceStartedListener state:", "receive start broadcast");
        String action = intent.getAction();
        assert action != null;
        if(action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent intent1 = new Intent(context,UpdateService.class);
            if(android.os.Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
                Log.i("sdk version:",">=26");
                context.startForegroundService(intent1);
            } else {
                Log.i("sdk version:","<26");
                context.startService(intent1);
            }
            Log.i("DeviceStartedListener state:","have send intent");
        }

    }
}
