package com.byteshaft.callnote;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PhoneBootStateReader extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Helpers helpers = new Helpers(context.getApplicationContext());
        if (helpers.isServiceSettingEnabled()) {
            context.startService(new Intent(context, OverlayService.class));
        }
    }
}
