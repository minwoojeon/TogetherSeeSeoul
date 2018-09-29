package src.kit.code.binoo.togetherseeseoul;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import kr.go.seoul.culturalevents.R.drawable;
import kr.go.seoul.culturalevents.R.id;

public final class CulturalEventDetail extends Fragment implements MainActivity.OnBackPressedListener{
    private CulturalInfo culturalInfo = null;
    private ImageView mainImg;
    private TextView culturalTitle;
    private TextView culturalCodeName;
    private TextView culturalDate;
    private TextView culturalTime;
    private TextView culturalPlace;
    private TextView culturalUseTrgt;
    private TextView culturalUseFee;
    private TextView culturalSponsor;
    private TextView culturalInquiry;

    public CulturalEventDetail() {
    }
    public void setCulturalInfo(CulturalInfo culturalInfo) {
        this.culturalInfo = culturalInfo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.cultural_event_detail, container, false);
        if(view != null){
            this.mainImg = (ImageView)view.findViewById(id.main_img);
            this.culturalTitle = (TextView)view.findViewById(id.cultural_title);
            this.culturalCodeName = (TextView)view.findViewById(id.cultural_code_name);
            this.culturalDate = (TextView)view.findViewById(id.cultural_date);
            this.culturalTime = (TextView)view.findViewById(id.cultural_time);
            this.culturalPlace = (TextView)view.findViewById(id.cultural_place);
            this.culturalUseTrgt = (TextView)view.findViewById(id.cultural_use_trgt);
            this.culturalUseFee = (TextView)view.findViewById(id.cultural_use_fee);
            this.culturalSponsor = (TextView)view.findViewById(id.cultural_sponsor);
            this.culturalInquiry = (TextView)view.findViewById(id.cultural_inquiry);
            if (this.culturalInfo == null) {
                ActivitiesManager.getInstance().getCurActivity().
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainLinear, ActivitiesManager.getInstance().fragments.get("CulturalEventSearchTypeA"), "CulturalEventSearchTypeA").commit();
            }
            final TextView txtEventInfo = (TextView) view.findViewById(R.id.txtEventInfo);

            txtEventInfo.setTag(0x1001);

            View.OnTouchListener onTouchListener = new View.OnTouchListener(){
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN){
                        if (v.getTag() == txtEventInfo.getTag()){
                            showDetailBRS(v);
                        }
                    }
                    return false;
                }
            };
            txtEventInfo.setOnTouchListener(onTouchListener);

            this.setData();
        }
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setData() {
        String url = "";
        String[] mainImgUrl;
        if (this.culturalInfo.getMAIN_IMG() != null){
            mainImgUrl = this.culturalInfo.getMAIN_IMG().split("\\/");
            for(int i = 0; i < mainImgUrl.length; ++i) {
                if (i != 0 && i != 1 && i != 2) {
                    if (i == mainImgUrl.length - 1) {
                        url = url + mainImgUrl[i];
                    } else {
                        url = url + mainImgUrl[i] + "/";
                    }
                } else {
                    url = url + mainImgUrl[i].toLowerCase() + "/";
                }
            }
        }
        Glide.with(this).load(url).error(drawable.bg_bigimg).into(this.mainImg);
        this.culturalTitle.setText(this.culturalInfo.getTITLE());
        this.culturalCodeName.setText(this.culturalInfo.getCODENAME());
        this.culturalDate.setText(this.culturalInfo.getSTRTDATE() + " ~ " + this.culturalInfo.getEND_DATE());
        this.culturalTime.setText(this.culturalInfo.getTIME());
        this.culturalPlace.setText(this.culturalInfo.getPLACE());
        this.culturalUseTrgt.setText(this.culturalInfo.getUSE_TRGT());
        if (!this.culturalInfo.getIS_FREE().equals("") && this.culturalInfo.getIS_FREE().equals("1")) {
            this.culturalUseFee.setText("무료");
        } else {
            this.culturalUseFee.setText(this.culturalInfo.getUSE_FEE());
        }

        this.culturalSponsor.setText(this.culturalInfo.getSPONSOR());
        this.culturalInquiry.setText(this.culturalInfo.getINQUIRY());

        String key = this.culturalInfo.getTITLE();
        ActivitiesManager.getInstance().getFirebaseDatabseUtils().view(key);
        ActivitiesManager.getInstance().isDetail = true;
        ActivitiesManager.getInstance().appData.put("url",this.culturalInfo.getORG_LINK());
        ActivitiesManager.getInstance().appData.put("title",key);
        ActivitiesManager.getInstance().appData.put("vo",this.culturalInfo.toString());
    }
    @Override
    public void onBack() {
        MainActivity activity = (MainActivity)getActivity();
        activity.setOnBackPressedListener(null);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.mainLinear, ActivitiesManager.getInstance().fragments.get("CulturalEventSearchTypeA"), "CulturalEventSearchTypeA").commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity)context).setOnBackPressedListener(this);
    }

    public void showDetailBRS(View view) {
        if (this.culturalInfo != null) {
            Intent intent = new Intent("android.intent.action.VIEW");
            Uri uri = Uri.parse(this.culturalInfo.getORG_LINK());
            intent.setData(uri);
            this.startActivity(intent);
        }

    }
}
