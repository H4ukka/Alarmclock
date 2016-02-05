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
import android.widget.TextView;
import android.widget.Toast;


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

        mediaPlayer = MediaPlayer.create(this, R.raw.alarm_2);
        mediaPlayer.setLooping(true);

        setContentView(R.layout.activity_main);

        /* Retrieve a PendingIntent that will perform a broadcast */
        Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, 0);

    }

    public static MainActivity getInstace() {
        return ins;
    }

    public void updateTheTextView(final String t, final int id) {
        MainActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                TextView textV1 = (TextView) findViewById(id);
                textV1.setText(t);
            }
        });
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimeFragment();
        newFragment.show(getFragmentManager(), "TimePicker");
    }

    public void beAnnoying () {
        System.out.println("Playin audio");
        mediaPlayer.start();
    }

    public void startAlarm(int hours, int minutes) {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        //calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);

        System.out.println("Setting Alarm for: " + hours + ":" + minutes);

        manager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), pendingIntent), pendingIntent);
        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
    }

    public void cancel(View v) {
        System.out.println("Stop Alarm");
        TextView textV1 = (TextView) findViewById(R.id.textView);
        textV1.setText(":)");
        mediaPlayer.pause();
    }

    public void snooze(View v) {

        cancel(v);

        System.out.println("Snoozing for 2 minutes");

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());

        startAlarm(c.get(Calendar.HOUR), c.get(Calendar.MINUTE) + 2);

    }

}
