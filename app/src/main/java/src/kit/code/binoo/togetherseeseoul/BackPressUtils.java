package src.kit.code.binoo.togetherseeseoul;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by binoo on 2018-01-03.
 */

public final class BackPressUtils {
    public long backKeyPressedTime = 0;
    private Toast toast;

    public static final long SHOW_LENGTH_LONG = 2400;

    private AppCompatActivity activity;

    public BackPressUtils(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void onBackPressed() {
        if (isAfter2Seconds()) {
            backKeyPressedTime = System.currentTimeMillis();
            // 현재시간을 다시 초기화
            toast = Toast.makeText(activity,
                    "한번더 뒤로가기를 누르시면 앱이 종료됩니다.",
                    Toast.LENGTH_SHORT);
            toast.show();
            activity.onBackPressed();
            return;
        }
        if (isBefore2Seconds()) {
            programShutdown();
            toast.cancel();
        }
    }
    public Boolean isAfter2Seconds() {
        return System.currentTimeMillis() > backKeyPressedTime + SHOW_LENGTH_LONG;
    }
    public Boolean isBefore2Seconds() {
        return System.currentTimeMillis() <= backKeyPressedTime + SHOW_LENGTH_LONG;
    }
    public void programShutdown() {
        activity .moveTaskToBack(true);
        activity .finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}
