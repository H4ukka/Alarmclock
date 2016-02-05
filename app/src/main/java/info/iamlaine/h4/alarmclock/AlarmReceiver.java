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
        Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();
        MainActivity.getInstace().beAnnoying();
        MainActivity.getInstace().updateTheTextView("Hello", R.id.textView);
    }
}
