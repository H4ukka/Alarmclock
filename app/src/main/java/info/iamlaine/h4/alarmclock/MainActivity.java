package info.iamlaine.h4.alarmclock;

import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends FragmentActivity {

    private static MainActivity ins;

    private PendingIntent pendingIntent;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ins = this;

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 20, 0);

        setContentView(R.layout.activity_main);

        hideButton(R.id.snooze);
        hideButton(R.id.stopAlarm);
        hideButton(R.id.cancelButton);

        updateTextView("", R.id.alertTime);
    }

    public static MainActivity getInstace() {
        return ins;
    }

    public String toTwoDigits (int t) {
        String result = "";

        if (t < 10) {
            result = "0" + t;
        }else{
            result = "" + t;
        }
        return result;
    }

    public void updateTextView(final String t, final int id) {
        MainActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                TextView uiTextView = (TextView) findViewById(id);
                uiTextView.setText(t);
            }
        });
    }

    public void updateButtonText(final  String t, final int id) {
        Button button = (Button) findViewById(id);
        button.setText(t);
    }

    public void hideButton (final int id) {
        Button uiButton = (Button) findViewById(id);
        uiButton.setVisibility(View.GONE);
    }

    public void showButton (final int id) {
        Button uiButton = (Button) findViewById(id);
        uiButton.setVisibility(View.VISIBLE);
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimeFragment();
        newFragment.show(getFragmentManager(), "TimePicker");
    }

    public void beAnnoying () {
        System.out.println("Playin' audio");
        mediaPlayer = MediaPlayer.create(this, R.raw.alarm_2);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    public void startAlarm(int hours, int minutes) {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        /* Retrieve a PendingIntent that will perform a broadcast */
        Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        /*
            Create a calendar object to represent the alarm date; set seconds to 0
            so alarm will start at the beginning of the minute.
         */

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);

        String daytag = "today";

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            // Alarm is before current time, set it for tomorrow
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            System.out.println("setting for tomorrow");
            daytag = "tomorrow";
        }

        String tag = "";

        if (calendar.get(Calendar.AM_PM) == 0) {
            // AM
            tag = " AM";
        }else{
            // PM
            tag = " PM";
        }

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(calendar.getTime());

        System.out.println("Alarm set: " + formattedDate);

        updateTextView("Alarm set for " + calendar.get(Calendar.HOUR) + ":" + toTwoDigits(calendar.get(Calendar.MINUTE)) + tag, R.id.alertTime);
        updateButtonText("Change Alarm", R.id.timePicker);

        MainActivity.getInstace().showButton(R.id.cancelButton);

        manager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), pendingIntent), pendingIntent);

        Toast.makeText(this, "Alarm set for " + daytag, Toast.LENGTH_SHORT).show();
    }

    public void cancel(View v) {
        System.out.println("Stop Alarm");

        updateTextView("", R.id.alertTime);

        hideButton(R.id.stopAlarm);
        hideButton(R.id.cancelButton);
        hideButton(R.id.snooze);
        showButton(R.id.timePicker);

        updateButtonText("Set Alarm", R.id.timePicker);

        Intent intentstop = new Intent(this, AlarmReceiver.class);
        PendingIntent senderstop = PendingIntent.getBroadcast(MainActivity.this,
                0, intentstop, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManagerstop = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManagerstop.cancel(senderstop);

        // Stop the playback, reset and set to null
        // This is a silly but bombproof way of stopping audio
        if (mediaPlayer != null ) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }

    }

    public void snooze(View v) {

        cancel(v);

        System.out.println("Snoozing for 2 minutes");

        hideButton(R.id.snooze);

        Calendar c = Calendar.getInstance();

        // Get the current time and add 2 minutes in milliseconds to it
        c.setTimeInMillis(System.currentTimeMillis() + 2 * 60 * 1000);

        // Calendar.HOUR = bad, I hate 12 hour clocks.
        startAlarm(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));

    }

}
