package cordova.plugin.hyperscanner;

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Handler;
import android.os.Looper;

public class BeepHelper {

    ToneGenerator tone = new ToneGenerator(AudioManager.STREAM_ALARM, 80);

    public void beep() {
        tone.startTone(ToneGenerator.TONE_DTMF_S, 1000);
        new Handler(Looper.getMainLooper()).postDelayed(() -> tone.release(), 100);
    }
}