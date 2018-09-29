package src.kit.code.binoo.togetherseeseoul;
/*
 * 2018-09-29
 * botbinoo@naver.com
 * */
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Message;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public final class LoadingActivity extends AppCompatActivity {
    TextView txtValue;
    TextView txtLoading;
    Handler nextHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActivitiesManager.getInstance().pushActivity(this);
        txtValue = (TextView)findViewById(R.id.txtValue);
        txtLoading = (TextView)findViewById(R.id.txtLoading);

        txtValue.setText("");
        ActivitiesManager.getInstance().setAlertbuilder(new AlertDialog.Builder(this));

        nextHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0){
                    new DataIninAsynk().execute();
                } else if (msg.what == 1){
                     /* 마시멜로 이상인 경우. */
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                        boolean readable = ContextCompat.checkSelfPermission(ActivitiesManager.getInstance().getCurActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
                                PackageManager.PERMISSION_GRANTED;
                        boolean writeable = ContextCompat.checkSelfPermission(ActivitiesManager.getInstance().getCurActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                PackageManager.PERMISSION_GRANTED;
                        boolean readPhoneState = ContextCompat.checkSelfPermission(ActivitiesManager.getInstance().getCurActivity(), Manifest.permission.READ_PHONE_STATE) ==
                                PackageManager.PERMISSION_GRANTED;

                        if ( readable && writeable && readPhoneState ){
                            // 셋 다 권한이 있는 경우.
                            nextHandler.sendEmptyMessageDelayed(0, 300);
                        } else {
                            ActivitiesManager.getInstance().makeAlert( "권한이 필요해요!" )
                                    .setPositiveButton("동의",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    nextHandler.sendEmptyMessageDelayed(2, 300);
                                                }
                                            }).setNegativeButton("앱 종료",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ActivitiesManager.getInstance().getCurActivity().finish();
                                            return;
                                        }
                                    })
                                    .create().show();
                        }
                    } else {
                        nextHandler.sendEmptyMessageDelayed(0, 1000);
                    }
                } else if (msg.what == 2){
                    ActivityCompat.requestPermissions(ActivitiesManager.getInstance().getCurActivity(), new String[]{
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE },
                            MY_PERMISSIONS_REQUEST_WR_CONTACTS);
                }
            }
        };
        nextHandler.sendEmptyMessage(1);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private final int MY_PERMISSIONS_REQUEST_WR_CONTACTS = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WR_CONTACTS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    nextHandler.sendEmptyMessageDelayed(0, 300);
                }
                return;
            }
        }
    }

    private class DataIninAsynk extends AsyncTask<Void, Void, Void>{

        final Context context;

        public DataIninAsynk(){
            context = ActivitiesManager.getInstance().getCurActivity().getBaseContext();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            txtValue.setText("100.0 %");
            try{
                nextHandler.removeMessages( 0 );
            }catch (NullPointerException e){
            }
            try{
                nextHandler.removeMessages( 1 );
            }catch (NullPointerException e){
            }
            try{
                nextHandler.removeMessages( 2 );
            }catch (NullPointerException e){
            }
            nextHandler = null;
            ActivitiesManager.getInstance().getCurActivity().startActivity(new Intent(ActivitiesManager.getInstance().getCurActivity(), MainActivity.class));
            super.onPostExecute(aVoid);
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
