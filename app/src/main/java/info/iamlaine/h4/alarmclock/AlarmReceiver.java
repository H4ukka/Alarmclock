package info.iamlaine.h4.alarmclock;

/**
 * Created by H4 on 3.2.2016.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println("Timer done");
        Toast.makeText(context, "WAKE UP!", Toast.LENGTH_SHORT).show();
        MainActivity.getInstace().beAnnoying();
        MainActivity.getInstace().updateTextView("", R.id.alertTime);
        MainActivity.getInstace().showButton(R.id.snooze);
        MainActivity.getInstace().showButton(R.id.stopAlarm);
        MainActivity.getInstace().hideButton(R.id.cancelButton);
        MainActivity.getInstace().hideButton(R.id.timePicker);
    }
}
