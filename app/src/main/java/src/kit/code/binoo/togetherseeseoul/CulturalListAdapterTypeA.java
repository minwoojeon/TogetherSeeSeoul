package src.kit.code.binoo.togetherseeseoul;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import kr.go.seoul.culturalevents.Common.CulturalInfo;
import kr.go.seoul.culturalevents.Common.FontUtils;
import kr.go.seoul.culturalevents.R.drawable;
import kr.go.seoul.culturalevents.R.id;
import kr.go.seoul.culturalevents.R.layout;

class CulturalListAdapterTypeA extends ArrayAdapter<CulturalInfo> {
    private src.kit.code.binoo.togetherseeseoul.CulturalListAdapterTypeA.ViewHolder viewHolder = null;
    private LayoutInflater inflater = null;
    private Context mContext = null;

    public CulturalListAdapterTypeA(Context c, int ResourceId, ArrayList<CulturalInfo> arrays) {
        super(c, ResourceId, arrays);
        this.inflater = LayoutInflater.from(c);
        this.mContext = c;
    }

    public int getCount() {
        return super.getCount();
    }

    public CulturalInfo getItem(int position) {
        return (CulturalInfo)super.getItem(position);
    }

    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public View getView(int position, View convertview, ViewGroup parent) {
        View v = convertview;
        if (convertview == null) {
            this.viewHolder = new src.kit.code.binoo.togetherseeseoul.CulturalListAdapterTypeA.ViewHolder();
            v = this.inflater.inflate(layout.cultural_event_list_item_type_a, (ViewGroup)null);
            this.viewHolder.mainImg = (ImageView)v.findViewById(id.main_img);
            this.viewHolder.title = (TextView)v.findViewById(id.cultural_title);
            this.viewHolder.date = (TextView)v.findViewById(id.cultural_date);
            this.viewHolder.gCode = (TextView)v.findViewById(id.cultural_g_code);
            v.setTag(this.viewHolder);
        } else {
            this.viewHolder = (src.kit.code.binoo.togetherseeseoul.CulturalListAdapterTypeA.ViewHolder)convertview.getTag();
        }

        this.viewHolder.title.setText(this.getItem(position).getTITLE());
        this.viewHolder.date.setText(this.getItem(position).getSTRTDATE() + "~" + this.getItem(position).getEND_DATE());
        this.viewHolder.gCode.setText(this.getItem(position).getGCODE());
        String url = "";
        String[] mainImg = this.getItem(position).getMAIN_IMG().split("\\/");

        for(int i = 0; i < mainImg.length; ++i) {
            if (i != 0 && i != 1 && i != 2) {
                if (i == mainImg.length - 1) {
                    url = url + mainImg[i];
                } else {
                    url = url + mainImg[i] + "/";
                }
            } else {
                url = url + mainImg[i].toLowerCase() + "/";
            }
        }

        if (!this.getItem(position).getMAIN_IMG().equals("")) {
            Glide.with(this.mContext).load(url).error(drawable.bg_bigimg).into(this.viewHolder.mainImg);
        }

        FontUtils.getInstance(this.mContext).setGlobalFont(parent);
        return v;
    }

    class ViewHolder {
        public ImageView mainImg;
        public TextView title;
        public TextView date;
        public TextView gCode;

        ViewHolder() {
        }
    }
}
