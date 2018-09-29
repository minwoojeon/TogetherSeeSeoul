package src.kit.code.binoo.togetherseeseoul;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private FrameLayout mainLinear;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainLinear, new CulturalEventSearchTypeA(), "CulturalEventSearchTypeA").commit();
                    return true;
                case R.id.navigation_notifications:
                    // 공유
                    mainLinear.buildDrawingCache();
                    Bitmap viewCapture = mainLinear.getDrawingCache();
                    FileOutputStream fos = null;
                    String fileName = null;
                    try {
                        File f = new File(Environment.getExternalStorageDirectory().toString() + "/data");
                        if (!f.exists()) f.mkdirs();
                        f = new File(Environment.getExternalStorageDirectory().toString() + "/data/img");
                        if (!f.exists()) f.mkdirs();
                        fileName = "/data/img/MHB_CG_"+new SimpleDateFormat("yyyyMMddHHMMssSSS", Locale.KOREA).format(new Date())+".jpg";
                        fos = new FileOutputStream(Environment.getExternalStorageDirectory().toString()+ fileName);

                        viewCapture.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        fos.flush();
                        fos.close();
                    } catch (FileNotFoundException e) {
                        Log.d("공유","1");
                    } catch (IOException e) {
                        Log.d("공유","2");
                        e.printStackTrace();
                    } finally {
                        Log.d("공유","3");
                        if (fos!=null) fos = null;
                    }
                    File file = new File(Environment.getExternalStorageDirectory() + fileName);
                    if (!file.canRead() || "".equals(fileName)){
                        Toast.makeText(ActivitiesManager.getInstance().getCurActivity(), "공유실패 : 파일을 찾을 수 없습니다.", Toast.LENGTH_LONG);
                        Log.d("공유","실패");
                        return false;
                    }

                    Uri uri = FileProvider.getUriForFile(
                            ActivitiesManager.getInstance().getCurActivity().getBaseContext(),
                            ActivitiesManager.getInstance().getCurActivity().getBaseContext().getApplicationContext().getPackageName() + ".fileprovider",
                            file);
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, ActivitiesManager.getInstance().appData.get("url"));
                    shareIntent.setType("image/jpeg");
                    ActivitiesManager.getInstance().getCurActivity().startActivity(Intent.createChooser(shareIntent, "함께해요 서울 문화!"));
                    Log.d("공유","성공");
                    return false;
            }
            return false;
        }
    };

    private BackPressUtils backPressUtils;
    public void onBackPressed(){
        if (backPressUtils.isAfter2Seconds()) {
            if(mBackListener != null)
                mBackListener.onBack();
            backPressUtils.backKeyPressedTime = System.currentTimeMillis();
            // 현재시간을 다시 초기화
            Toast.makeText(this,
                    "한번 더 \'뒤로가기\'버튼을 누르시면, 앱이 종료됩니다.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (backPressUtils.isBefore2Seconds()) {
            backPressUtils.programShutdown();
        }
    }
    public interface OnBackPressedListener {
        public void onBack();
    }
    private OnBackPressedListener mBackListener;

    public void setOnBackPressedListener(OnBackPressedListener listener) {
        mBackListener = listener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivitiesManager.getInstance().pushActivity(this);
        backPressUtils = new BackPressUtils(this);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        mainLinear = (FrameLayout) findViewById(R.id.mainLinear);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().add(R.id.mainLinear, new CulturalEventSearchTypeA(), "CulturalEventSearchTypeA").commit();
    }

}