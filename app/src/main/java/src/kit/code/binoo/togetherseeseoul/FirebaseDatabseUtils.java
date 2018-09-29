package src.kit.code.binoo.togetherseeseoul;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import src.kit.code.binoo.togetherseeseoul.dummy.DummyContent;

import static android.content.Context.TELEPHONY_SERVICE;

/*
 * 2018-09-29
 * botbinoo@naver.com
 * */
public final class FirebaseDatabseUtils {
    private DatabaseReference mDatabase;
    public FirebaseDatabseUtils(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public static class ViewsOfKeys{
        public Integer vtimes = 0;
        public Integer stimes = 0;
        public ViewsOfKeys(){}
        public ViewsOfKeys(Integer vtimes, Integer stimes){
            this.vtimes = vtimes;
            this.stimes = stimes;
        }
        public String toString(){
            return vtimes+" "+stimes;
        }
    }

    ViewsOfKeys tmp = new ViewsOfKeys();
    private void addViewTimes(final String key) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            boolean isOnce = true;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(isOnce){
                    tmp = dataSnapshot.getValue(ViewsOfKeys.class);
                    isOnce = false;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Log.w("333333", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.child("table").child("viewsOfKeys").child(key).addListenerForSingleValueEvent(valueEventListener);
        Map<String, Object> childUpdates = new HashMap<String, Object>();
        childUpdates.put("/table/viewsOfKeys/" + key + "/vtimes", (tmp.vtimes+1));
        childUpdates.put("/table/viewsOfKeys/" + key + "/stimes", tmp.stimes);

        mDatabase.updateChildren(childUpdates);
    }
    public void getView(String key, final TextView txtView, final ImageView imageView, final ImageView imageView2){
        key = key.replaceAll("([.])|([#])|([$])|([\\[])|([\\]])","");
        ValueEventListener valueEventListener = new ValueEventListener() {
            boolean isOnce = true;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(isOnce){
                    tmp = dataSnapshot.getValue(ViewsOfKeys.class);
                    if (tmp == null)
                        tmp = new ViewsOfKeys(0,0);

                    txtView.setText("누적 조회수 : "+tmp.vtimes+" , 누적 공유횟수 : "+tmp.stimes);
                    imageView.setVisibility(View.INVISIBLE);
                    imageView2.setVisibility(View.INVISIBLE);

                    if (tmp.vtimes >= 1000){
                        imageView.setImageResource(R.drawable.search1000);
                        imageView.setVisibility(View.VISIBLE);
                    } else if (tmp.vtimes >= 500){
                        imageView.setImageResource(R.drawable.search500);
                        imageView.setVisibility(View.VISIBLE);
                    } else if (tmp.vtimes >= 100){
                        imageView.setImageResource(R.drawable.search100);
                        imageView.setVisibility(View.VISIBLE);
                    }
                    if (tmp.stimes >= 1000){
                        imageView2.setImageResource(R.drawable.share1000);
                        imageView2.setVisibility(View.VISIBLE);
                    } else if (tmp.stimes >= 500){
                        imageView2.setImageResource(R.drawable.share500);
                        imageView2.setVisibility(View.VISIBLE);
                    } else if (tmp.stimes >= 100){
                        imageView2.setImageResource(R.drawable.share100);
                        imageView2.setVisibility(View.VISIBLE);
                    }
                    isOnce = false;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Log.w("333333", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.child("table").child("viewsOfKeys").child(key).addListenerForSingleValueEvent(valueEventListener);
    }
    public void view(String key){
        key = key.replaceAll("([.])|([#])|([$])|([\\[])|([\\]])","");
        addViewTimes(key);
    }
    public void share(String key){
        key = key.replaceAll("([.])|([#])|([$])|([\\[])|([\\]])","");
        addShareTimes(key);
        addSharePhone(key);
    }

    private void addShareTimes(final String key) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            boolean isOnce = true;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(isOnce){
                    tmp = dataSnapshot.getValue(ViewsOfKeys.class);
                    isOnce = false;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Log.w("333333", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.child("table").child("viewsOfKeys").child(key).addListenerForSingleValueEvent(valueEventListener);
        Map<String, Object> childUpdates = new HashMap<String, Object>();
        childUpdates.put("/table/viewsOfKeys/" + key + "/vtimes", tmp.vtimes);
        childUpdates.put("/table/viewsOfKeys/" + key + "/stimes", (tmp.stimes+1));

        mDatabase.updateChildren(childUpdates);
    }

    private void addSharePhone(final String key) {
        TelephonyManager telManager = (TelephonyManager) ActivitiesManager.getInstance().getCurActivity().getSystemService(TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String PhoneNum = telManager.getLine1Number();

        Map<String,Object> map = new HashMap<String, Object>();
        map.put(key, ActivitiesManager.getInstance().appData.get("vo"));
        mDatabase.child("table").child("shareOfPhones").child(PhoneNum).updateChildren(map);
    }

    public boolean isBusy = false;

    public void getSharePhone(){
        TelephonyManager telManager = (TelephonyManager) ActivitiesManager.getInstance().getCurActivity().getSystemService(TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String PhoneNum = telManager.getLine1Number();

        isBusy = true;
        DummyContent.ITEMS.clear();

        ValueEventListener valueEventListener = new ValueEventListener() {
            boolean isOnce = true;
            @Override
            public void onDataChange(DataSnapshot dataSnapshots) {
                if(isOnce){
                    if (dataSnapshots == null){
                        isOnce = false;
                        ActivitiesManager.getInstance().makeAlert( "아직 공유한 행사가 없어요!" )
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
                        return;
                    }else if (dataSnapshots.getValue() == null){
                        isOnce = false;
                        ActivitiesManager.getInstance().makeAlert( "아직 공유한 행사가 없어요!" )
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
                        return;
                    }
                    int cnt = 0;
                    for (DataSnapshot dataSnapshot: dataSnapshots.getChildren()){
                        if(dataSnapshot.getKey() == null || dataSnapshot.getValue() == null) continue;
                        DummyContent.ITEMS.add(new DummyContent.DummyItem(++cnt, dataSnapshot.getKey(), CulturalInfo.makeCulturalInfo( (String)dataSnapshot.getValue() )));
                    }
                    isBusy = false;
                    isOnce = false;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Log.w("333333", "loadPost:onCancelled", databaseError.toException());
            }
        };
        mDatabase.child("table").child("shareOfPhones").child(PhoneNum).addListenerForSingleValueEvent(valueEventListener);
    }
}
