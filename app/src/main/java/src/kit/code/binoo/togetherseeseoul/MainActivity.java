package src.kit.code.binoo.togetherseeseoul;
/*
 * 2018-09-29
 * botbinoo@naver.com
 * */
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import src.kit.code.binoo.togetherseeseoul.dummy.DummyContent;

public final class MainActivity extends AppCompatActivity  implements ItemFragment.OnListFragmentInteractionListener{

    private FrameLayout mainLinear;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_bbs:
                    class IntentsChange extends AsyncTask<Void,Void,Void>{
                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            // TODO : 로딩바 ON
                            ActivitiesManager.getInstance().getFirebaseDatabseUtils().getSharePhone();
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            // TODO : 로딩바 OFF
                            ActivitiesManager.getInstance().getCurActivity().
                            getSupportFragmentManager().beginTransaction().replace(R.id.mainLinear, ActivitiesManager.getInstance().fragments.get("ItemFragment"), "ItemFragment").commit();
                        }

                        @Override
                        protected Void doInBackground(Void... voids) {
                            while(true){
                                if (!ActivitiesManager.getInstance().getFirebaseDatabseUtils().isBusy) break;
                                try {
                                    Thread.sleep(250);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            return null;
                        }
                    }
                    new IntentsChange().execute();
                    return true;
                case R.id.navigation_dashboard:
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainLinear, ActivitiesManager.getInstance().fragments.get("CulturalEventSearchTypeA"), "CulturalEventSearchTypeA").commit();
                    return true;
                case R.id.navigation_notifications:
                    // 공유
                    if (ActivitiesManager.getInstance().isDetail){
                        // URL 공유
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
                            Log.d("공유",""+e.getStackTrace());
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (fos!=null) fos = null;
                        }
                        File file = new File(Environment.getExternalStorageDirectory() + fileName);
                        if (!file.canRead() || "".equals(fileName)){
                            Toast.makeText(ActivitiesManager.getInstance().getCurActivity(), "공유실패 : 파일을 찾을 수 없습니다.", Toast.LENGTH_LONG);
                            return false;
                        }

                        Uri uri = FileProvider.getUriForFile(
                                ActivitiesManager.getInstance().getCurActivity().getBaseContext(),
                                ActivitiesManager.getInstance().getCurActivity().getBaseContext().getApplicationContext().getPackageName() + ".fileprovider",
                                file);
                        String key = (String)ActivitiesManager.getInstance().appData.get("url");
                        if(key != null && !"".equals(key)){
                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                            shareIntent.putExtra(Intent.EXTRA_TEXT, "나와 이번 문화 행사를 같이 갈래요?\n\n"
                                    +Uri.parse((String)ActivitiesManager.getInstance().appData.get("url")) + "\n\n *)주의 : 링크를 보낸 사람은 아는 사람인가요?");
                            shareIntent.putExtra(Intent.EXTRA_TITLE, "함께해요 서울 문화 행사!");
                            shareIntent.setType("image/jpeg");
                            ActivitiesManager.getInstance().getFirebaseDatabseUtils().share(
                                    (String)ActivitiesManager.getInstance().appData.get("title")
                            );
                            ActivitiesManager.getInstance().getCurActivity().startActivity(Intent.createChooser(shareIntent, "함께해요 서울 문화!"));
                            return false;
                        }
                    } else {
                        // 공유할 내용이 없습니다.
                        ActivitiesManager.getInstance().makeAlert( "상세 페이지에서 문화 행사를 공유할 수 있습니다!" )
                                .setPositiveButton("확인",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        }).setNegativeButton("취소",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                                .create().show();
                    }
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

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        //
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
        ActivitiesManager.getInstance().fragments.put("CulturalEventSearchTypeA",new CulturalEventSearchTypeA());
        ActivitiesManager.getInstance().fragments.put("CulturalEventDetail",new CulturalEventDetail());
        ActivitiesManager.getInstance().fragments.put("ItemFragment",new ItemFragment());
        ActivitiesManager.getInstance().isDetail = false;
        getSupportFragmentManager().beginTransaction().add(R.id.mainLinear, ActivitiesManager.getInstance().fragments.get("CulturalEventSearchTypeA"), "CulturalEventSearchTypeA").commit();
    }

}