package ar.tvplayer.brosiptvassist.receiver;

import static androidx.core.app.ActivityCompat.finishAffinity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;

import ar.tvplayer.brosiptvassist.MainActivity;

public class ForceKillReceiver extends BroadcastReceiver {

    private StringBuilder inputSequence = new StringBuilder();
    private final Handler timerHandler = new Handler(Looper.getMainLooper());
    private final Runnable timeoutRunnable = this::resetInputSequence;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
            KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);

            if (event != null && event.getAction() == KeyEvent.ACTION_DOWN) {
                int keyCode = event.getKeyCode();

                if ((keyCode - KeyEvent.KEYCODE_1) == 1 || keyCode == KeyEvent.KEYCODE_1) {
                    inputSequence.append("1");

                    timerHandler.removeCallbacks(timeoutRunnable);
                    timerHandler.postDelayed(timeoutRunnable, 1000);
                }

                if (inputSequence.toString().equals("1111111")) {
                    resetInputSequence();

                    finishApplication(context);
                }
            }
        }
    }

    private void finishApplication(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);

        finishAffinity(MainActivity.instance);
    }

    public void resetInputSequence() {
        this.inputSequence = new StringBuilder();
    }
}
