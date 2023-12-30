package ar.tvplayer.brosiptvassist.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;

public class BoxSettingsReceiver extends BroadcastReceiver {

    private static StringBuilder inputSequence = new StringBuilder();
    private final Handler timerHandler = new Handler(Looper.getMainLooper());
    private final Runnable timeoutRunnable = () -> inputSequence = new StringBuilder();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
            KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if(event == null) return;

            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                int keyCode = event.getKeyCode();

                if ((keyCode - KeyEvent.KEYCODE_1) == 2) {
                    openDeviceSettings(context);

                    inputSequence.append("2");

                    timerHandler.removeCallbacks(timeoutRunnable);
                    timerHandler.postDelayed(timeoutRunnable, 3000);
                }

                if (inputSequence.toString().equals("2222222")) {
                    openDeviceSettings(context);

                    resetInputSequence();
                }
            }
        }
    }

    private void openDeviceSettings(Context context) {
        Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void resetInputSequence() {
        inputSequence = new StringBuilder();
    }
}
