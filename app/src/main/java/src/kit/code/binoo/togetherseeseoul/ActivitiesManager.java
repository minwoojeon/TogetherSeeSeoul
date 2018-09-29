package src.kit.code.binoo.togetherseeseoul;
/*
 * 2018-09-29
 * botbinoo@naver.com
 * */
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class ActivitiesManager {
    private static ActivitiesManager instance = new ActivitiesManager();
    public static ActivitiesManager getInstance(){
        return instance;
    }
    private List<AppCompatActivity> activities = new ArrayList<AppCompatActivity>();
    public void pushActivity(AppCompatActivity activity){
        activities.add(activity);
    }
    public AppCompatActivity getCurActivity(){
        return (activities.size() == 0 ? null : activities.get(activities.size()-1));
    }
    public HashMap<String, Fragment> fragments = new HashMap<String, Fragment>();
    private AlertDialog.Builder alertbuilder = null;
    public void setAlertbuilder(AlertDialog.Builder alertbuilder){
        this.alertbuilder = alertbuilder;
    }
    private FirebaseDatabseUtils firebaseDatabseUtils = new FirebaseDatabseUtils();

    public FirebaseDatabseUtils getFirebaseDatabseUtils() {
        return firebaseDatabseUtils;
    }
    public boolean isDetail = false;

    public AlertDialog.Builder makeAlert(String say){
        alertbuilder
                .setMessage(say)
                .setCancelable(false)
                .setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                .setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
        return alertbuilder;
    }
    public HashMap<String,Object> appData = new HashMap<String,Object>();
}
