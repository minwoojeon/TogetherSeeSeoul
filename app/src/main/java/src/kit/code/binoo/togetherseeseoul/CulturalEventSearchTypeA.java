//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package src.kit.code.binoo.togetherseeseoul;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;

import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import kr.go.seoul.culturalevents.Common.ConsertSubjectCatalogInfo;
import kr.go.seoul.culturalevents.Common.CustomProgressDialog;
import kr.go.seoul.culturalevents.Common.FontUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class CulturalEventSearchTypeA extends Fragment implements MainActivity.OnBackPressedListener{
    private String[] categoryName = new String[]{"명칭", "기간", "장르"};
    private ConsertSubjectCatalogInfo[] concertSubjectCatalogList;
    private String[] concertSubjectCatalogNameList;
    private AlertDialog alert;
    private int selectedCategory = 0;
    private int selectedSubCodeCatalog = 0;
    private ListView culturalListview;
    private LinearLayout btnNameLoadMore;
    private LinearLayout btnPeriodLoadMore;
    private LinearLayout btnSubCodeCatalogLoadMore;
    private ArrayList<CulturalInfo> culturalInfoArrayList = new ArrayList<CulturalInfo> ();
    private CulturalListAdapterTypeA culturalListAdapter;
    private int startNo = -1;
    private int selectCategory = -1;
    private int selectCatalog = -1;
    private String searchKey;
    private String startDate;
    private String endDate;
    private CustomProgressDialog dialogLoading;
    private LayoutInflater inflater;
    private View thisView;

    private String openAPIKey = ActivitiesManager.getInstance().getCurActivity().getResources().getString(R.string.api_key);

    public CulturalEventSearchTypeA() {
        ActivitiesManager.getInstance().appData.clear();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ActivitiesManager.getInstance().isDetail = false;
        View view = inflater.inflate(R.layout.cultural_event_search_type_a, container, false);
        if(view != null){
            this.culturalListview = (ListView)view.findViewById(R.id.cultural_listview);
            // context ch
            this.culturalListAdapter = new CulturalListAdapterTypeA(view.getContext(), R.layout.cultural_event_list_item_type_a, this.culturalInfoArrayList);
            this.culturalListview.setAdapter(this.culturalListAdapter);
            this.culturalListview.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    CulturalEventDetail culturalEventDetail = (CulturalEventDetail)ActivitiesManager.getInstance().fragments.get("CulturalEventDetail");
                    culturalEventDetail.setCulturalInfo((CulturalInfo)adapterView.getAdapter().getItem(i));
                    ActivitiesManager.getInstance().getCurActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.mainLinear, culturalEventDetail, "CulturalInfo").commit();
                }
            });
            final LinearLayout layout_category1 = (LinearLayout) view.findViewById(R.id.layout_category);
            final LinearLayout layout_category_2 = (LinearLayout) view.findViewById(R.id.layout_category_2);
            final ImageButton imgBtn1 = (ImageButton) view.findViewById(R.id.imgBtn1);
            final ImageButton imgBtn2 = (ImageButton) view.findViewById(R.id.imgBtn2);
            final ImageButton btn_top = (ImageButton) view.findViewById(R.id.btn_top);
            final TextView start_date = (TextView) view.findViewById(R.id.start_date);
            final TextView end_date = (TextView) view.findViewById(R.id.end_date);

            layout_category1.setTag(0x0001);
            imgBtn1.setTag(0x0002);
            start_date.setTag(0x0003);
            end_date.setTag(0x0004);
            imgBtn2.setTag(0x0005);
            layout_category_2.setTag(0x0006);
            btn_top.setTag(0x0007);
            View.OnTouchListener onTouchListener = new View.OnTouchListener(){
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN){
                        if (v.getTag() == layout_category1.getTag()){
                            showCategory(v);
                        } else if (v.getTag() == imgBtn1.getTag()){
                            searchName(v);
                        } else if (v.getTag() == start_date.getTag()){
                            showDatePickerDialog(v);
                        } else if (v.getTag() == end_date.getTag()){
                            showDatePickerDialog(v);
                        } else if (v.getTag() == imgBtn2.getTag()){
                            searchPeriod(v);
                        } else if (v.getTag() == layout_category_2.getTag()){
                            showSubCodeCatalog(v);
                        } else if (v.getTag() == btn_top.getTag()){
                            goTop(v);
                        }
                    }
                    return false;
                }
            };
            layout_category1.setOnTouchListener(onTouchListener);
            imgBtn1.setOnTouchListener(onTouchListener);
            start_date.setOnTouchListener(onTouchListener);
            end_date.setOnTouchListener(onTouchListener);
            imgBtn2.setOnTouchListener(onTouchListener);
            layout_category_2.setOnTouchListener(onTouchListener);
            btn_top.setOnTouchListener(onTouchListener);

            this.inflater = inflater;
            this.thisView = view;
        }
        return view;
    }
    public void goTop(View view) {
        this.culturalListview.smoothScrollToPosition(0);
    }

    public void showProgressDialog() {
        if (this.dialogLoading == null && !ActivitiesManager.getInstance().getCurActivity().isFinishing()) {
            this.dialogLoading = CustomProgressDialog.show(ActivitiesManager.getInstance().getCurActivity(), "", "");
            this.dialogLoading.setCancelable(false);
        }

        if (this.dialogLoading != null && !this.dialogLoading.isShowing()) {
            this.dialogLoading.show();
        }

    }

    public void cancelProgressDialog() {
        if (this.dialogLoading != null && this.dialogLoading.isShowing()) {
            this.dialogLoading.cancel();
        }

    }

    public void searchName(View view) {
        this.searchKey = ((EditText)this.thisView.findViewById(R.id.search_concert_name)).getText().toString();
        if (this.searchKey.length() != 0 && !this.searchKey.contains(" ")) {
            this.culturalInfoArrayList.clear();
            this.culturalListAdapter.notifyDataSetChanged();
            this.startNo = 1;
            this.showProgressDialog();
            (new CulturalEventSearchTypeA.ProcessNetworkSearchConcertNameThread()).execute(new String[]{searchKey});
            if (this.btnNameLoadMore != null) {
                this.culturalListview.removeFooterView(this.btnNameLoadMore);
                this.btnNameLoadMore = null;
            }

            if (this.btnNameLoadMore == null) {
                LayoutInflater li = this.inflater;
                this.btnNameLoadMore = (LinearLayout)li.inflate(R.layout.btn_load_more, (ViewGroup)null);
                FontUtils.getInstance(thisView.getContext()).setGlobalFont(this.btnNameLoadMore);
                this.btnNameLoadMore.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        if (CulturalEventSearchTypeA.this.culturalListview.getCount() > 0) {
                            showProgressDialog();
                            (new ProcessNetworkSearchConcertNameThread()).execute(new String[]{searchKey});
                        }

                    }
                });
            }

            this.culturalListview.addFooterView(this.btnNameLoadMore);
        } else {
            Builder alert_confirm = new Builder(thisView.getContext(), 3);
            alert_confirm.setMessage("공백을 포함하지 않는\n검색어를 입력해 주십시오.").setNegativeButton("확인", new android.content.DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            this.alert = alert_confirm.create();
            this.alert.setOnShowListener(new OnShowListener() {
                public void onShow(DialogInterface dialogInterface) {
                    CulturalEventSearchTypeA.this.alert.getButton(-2).setTypeface(FontUtils.getInstance(thisView.getContext()).getmTypeface());
                }
            });
            this.alert.show();
        }

    }

    public void showDatePickerDialog(final View view) {
        GregorianCalendar calendar = new GregorianCalendar();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        (new DatePickerDialog(thisView.getContext(), new OnDateSetListener() {
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                String msg = String.format("%d%d%d", year, monthOfYear + 1, dayOfMonth);
                ((TextView)view).setText(msg);
            }
        }, year, month, day)).show();
    }

    public void searchPeriod(View view) {
        this.startDate = ((TextView)this.thisView.findViewById(R.id.start_date)).getText().toString();
        this.endDate = ((TextView)thisView.findViewById(R.id.end_date)).getText().toString();
        if (this.startDate.length() != 0 && this.endDate.length() != 0) {
            this.culturalInfoArrayList.clear();
            this.culturalListAdapter.notifyDataSetChanged();
            this.startNo = 1;
            this.showProgressDialog();
            (new CulturalEventSearchTypeA.ProcessNetworkSearchConcertPeriodThread()).execute(new String[]{this.startDate, this.endDate});
            if (this.btnPeriodLoadMore != null) {
                this.culturalListview.removeFooterView(this.btnPeriodLoadMore);
                this.btnPeriodLoadMore = null;
            }

            if (this.btnPeriodLoadMore == null) {
                LayoutInflater li = this.inflater;
                this.btnPeriodLoadMore = (LinearLayout)li.inflate(R.layout.btn_load_more, (ViewGroup)null);
                FontUtils.getInstance(this.thisView.getContext()).setGlobalFont(this.btnPeriodLoadMore);
                this.btnPeriodLoadMore.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        if (culturalListview.getCount() > 0) {
                            showProgressDialog();
                            (new ProcessNetworkSearchConcertPeriodThread()).execute(new String[]{startDate, endDate});
                        }

                    }
                });
            }

            this.culturalListview.addFooterView(this.btnPeriodLoadMore);
        } else {
            Builder alert_confirm = new Builder(this.thisView.getContext(), 3);
            alert_confirm.setMessage("검색기간을 입력해 주십시오.").setNegativeButton("확인", new android.content.DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            this.alert = alert_confirm.create();
            this.alert.setOnShowListener(new OnShowListener() {
                public void onShow(DialogInterface dialogInterface) {
                    CulturalEventSearchTypeA.this.alert.getButton(-2).setTypeface(FontUtils.getInstance(thisView.getContext()).getmTypeface());
                }
            });
            this.alert.show();
        }

    }

    public void showSubCodeCatalog(View view) {
        Builder alert_confirm = new Builder(this.thisView.getContext(), 3);
        ((ImageView)this.thisView.findViewById(R.id.category_subcode_selector)).setSelected(true);
        alert_confirm.setSingleChoiceItems(this.concertSubjectCatalogNameList, this.selectedSubCodeCatalog, new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                CulturalEventSearchTypeA.this.selectedSubCodeCatalog = whichButton;
            }
        }).setPositiveButton("확인", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (CulturalEventSearchTypeA.this.culturalInfoArrayList != null && CulturalEventSearchTypeA.this.selectedSubCodeCatalog != CulturalEventSearchTypeA.this.selectCatalog) {
                    if (CulturalEventSearchTypeA.this.btnSubCodeCatalogLoadMore != null) {
                        CulturalEventSearchTypeA.this.culturalListview.removeFooterView(CulturalEventSearchTypeA.this.btnSubCodeCatalogLoadMore);
                        CulturalEventSearchTypeA.this.btnSubCodeCatalogLoadMore = null;
                    }

                    CulturalEventSearchTypeA.this.culturalInfoArrayList.clear();
                    CulturalEventSearchTypeA.this.culturalListAdapter.notifyDataSetChanged();
                    ((TextView)thisView.findViewById(R.id.category_subcode_name)).setText(CulturalEventSearchTypeA.this.concertSubjectCatalogNameList[CulturalEventSearchTypeA.this.selectedSubCodeCatalog]);
                    CulturalEventSearchTypeA.this.startNo = 1;
                    CulturalEventSearchTypeA.this.selectCatalog = CulturalEventSearchTypeA.this.selectedSubCodeCatalog;
                    CulturalEventSearchTypeA.this.showProgressDialog();
                    (CulturalEventSearchTypeA.this.new ProcessNetworkSearchPerformanceBySubjectThread()).execute(new String[]{CulturalEventSearchTypeA.this.concertSubjectCatalogList[CulturalEventSearchTypeA.this.selectedSubCodeCatalog].getCODE()});
                    if (CulturalEventSearchTypeA.this.btnSubCodeCatalogLoadMore == null) {
                        LayoutInflater li = inflater;
                        CulturalEventSearchTypeA.this.btnSubCodeCatalogLoadMore = (LinearLayout)li.inflate(R.layout.btn_load_more, (ViewGroup)null);
                        FontUtils.getInstance(thisView.getContext()).setGlobalFont(CulturalEventSearchTypeA.this.btnSubCodeCatalogLoadMore);
                        CulturalEventSearchTypeA.this.btnSubCodeCatalogLoadMore.setOnClickListener(new OnClickListener() {
                            public void onClick(View view) {
                                if (CulturalEventSearchTypeA.this.culturalListview.getCount() > 0) {
                                    CulturalEventSearchTypeA.this.showProgressDialog();
                                    (CulturalEventSearchTypeA.this.new ProcessNetworkSearchPerformanceBySubjectThread()).execute(new String[]{CulturalEventSearchTypeA.this.concertSubjectCatalogList[CulturalEventSearchTypeA.this.selectCatalog].getCODE()});
                                }

                            }
                        });
                    }

                    CulturalEventSearchTypeA.this.culturalListview.addFooterView(CulturalEventSearchTypeA.this.btnSubCodeCatalogLoadMore);
                }

            }
        }).setNegativeButton("취소", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        this.alert = alert_confirm.create();
        this.alert.setOnShowListener(new OnShowListener() {
            public void onShow(DialogInterface dialogInterface) {
                FontUtils.getInstance(thisView.getContext()).setGlobalFont(CulturalEventSearchTypeA.this.alert.getListView());
                CulturalEventSearchTypeA.this.alert.getButton(-2).setTypeface(FontUtils.getInstance(thisView.getContext()).getmTypeface());
                CulturalEventSearchTypeA.this.alert.getButton(-1).setTypeface(FontUtils.getInstance(thisView.getContext()).getmTypeface());
            }
        });
        this.alert.setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface dialogInterface) {
                ((ImageView)thisView.findViewById(R.id.category_subcode_selector)).setSelected(false);
            }
        });
        this.alert.show();
    }

    public void showCategory(View view) {
        Builder alert_confirm = new Builder(thisView.getContext(), 3);
        ((ImageView)thisView.findViewById(R.id.category_selector)).setSelected(true);
        alert_confirm.setSingleChoiceItems(this.categoryName, this.selectedCategory, new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                CulturalEventSearchTypeA.this.selectedCategory = whichButton;
            }
        }).setPositiveButton("확인", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (CulturalEventSearchTypeA.this.selectCategory != CulturalEventSearchTypeA.this.selectedCategory) {
                    CulturalEventSearchTypeA.this.initCondition();
                    ((TextView)thisView.findViewById(R.id.category)).setText(CulturalEventSearchTypeA.this.categoryName[CulturalEventSearchTypeA.this.selectedCategory]);
                    thisView.findViewById(R.id.layout_category_0).setVisibility(View.INVISIBLE);
                    thisView.findViewById(R.id.layout_category_1).setVisibility(View.INVISIBLE);
                    thisView.findViewById(R.id.layout_category_2).setVisibility(View.INVISIBLE);// 8
                    switch(CulturalEventSearchTypeA.this.selectedCategory) {
                        case 0:
                            thisView.findViewById(R.id.layout_category_0).setVisibility(View.VISIBLE);
                            break;
                        case 1:
                            thisView.findViewById(R.id.layout_category_1).setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            thisView.findViewById(R.id.layout_category_2).setVisibility(View.VISIBLE);
                            showProgressDialog();
                            (new ProcessNetworkSearchConcertSubjectCatalogThread()).execute(new String[0]);
                    }

                    CulturalEventSearchTypeA.this.selectCategory = CulturalEventSearchTypeA.this.selectedCategory;
                }

            }
        }).setNegativeButton("취소", new android.content.DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        this.alert = alert_confirm.create();
        this.alert.setOnShowListener(new OnShowListener() {
            public void onShow(DialogInterface dialogInterface) {
                FontUtils.getInstance(thisView.getContext()).setGlobalFont(CulturalEventSearchTypeA.this.alert.getListView());
                CulturalEventSearchTypeA.this.alert.getButton(-2).setTypeface(FontUtils.getInstance(thisView.getContext()).getmTypeface());
                CulturalEventSearchTypeA.this.alert.getButton(-1).setTypeface(FontUtils.getInstance(thisView.getContext()).getmTypeface());
            }
        });
        this.alert.setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface dialogInterface) {
                ((ImageView)thisView.findViewById(R.id.category_selector)).setSelected(false);
            }
        });
        this.alert.show();
        this.hideSoftKeyboard();
    }

    private void initCondition() {
        this.startNo = 1;
        this.selectCatalog = -1;
        ((TextView)thisView.findViewById(R.id.category_subcode_name)).setText("장르를 선택해 주세요.");
        if (this.culturalInfoArrayList != null) {
            this.culturalInfoArrayList.clear();
        }

        if (this.culturalListAdapter != null) {
            this.culturalListAdapter.notifyDataSetChanged();
        }

        if (this.btnNameLoadMore != null) {
            this.culturalListview.removeFooterView(this.btnNameLoadMore);
            this.btnNameLoadMore = null;
        }

        if (this.btnPeriodLoadMore != null) {
            this.culturalListview.removeFooterView(this.btnPeriodLoadMore);
            this.btnPeriodLoadMore = null;
        }

        if (this.btnSubCodeCatalogLoadMore != null) {
            this.culturalListview.removeFooterView(this.btnSubCodeCatalogLoadMore);
            this.btnSubCodeCatalogLoadMore = null;
        }

    }

    private void hideSoftKeyboard() {
        @SuppressLint("WrongConstant") InputMethodManager imm = (InputMethodManager)ActivitiesManager.getInstance().getCurActivity().getSystemService("input_method");
        imm.hideSoftInputFromWindow(((EditText)thisView.findViewById(R.id.search_concert_name)).getWindowToken(), 0);
    }

    public class ProcessNetworkSearchConcertDetailThread extends AsyncTask<String, Void, String> {
        public ProcessNetworkSearchConcertDetailThread() {
        }

        protected String doInBackground(String... strings) {
            String content = this.executeClient(strings);
            return content;
        }

        protected void onPostExecute(String result) {
            String main = result.toString();

            try {
                JSONObject jsonMain = new JSONObject(main);
                JSONObject jsonObject = jsonMain.getJSONObject("SearchConcertDetailService");
                JSONArray row = jsonObject.getJSONArray("row");
                if (row.length() > 0) {
                    String title = row.getJSONObject(0).getString("TITLE");
                    title = title.replaceAll("<", "&lt;");
                    title = title.replaceAll(">", "&gt;");
                    CulturalEventSearchTypeA.this.culturalInfoArrayList.add(new CulturalInfo(row.getJSONObject(0).getString("CULTCODE"), row.getJSONObject(0).getString("SUBJCODE"), row.getJSONObject(0).getString("CODENAME"), Html.fromHtml(title).toString(), row.getJSONObject(0).getString("STRTDATE"), row.getJSONObject(0).getString("END_DATE"), row.getJSONObject(0).getString("TIME"), row.getJSONObject(0).getString("PLACE"), row.getJSONObject(0).getString("ORG_LINK"), row.getJSONObject(0).getString("MAIN_IMG"), row.getJSONObject(0).getString("HOMEPAGE"), row.getJSONObject(0).getString("USE_TRGT"), row.getJSONObject(0).getString("USE_FEE"), row.getJSONObject(0).getString("SPONSOR"), row.getJSONObject(0).getString("INQUIRY"), row.getJSONObject(0).getString("SUPPORT"), row.getJSONObject(0).getString("ETC_DESC"), row.getJSONObject(0).getString("AGELIMIT"), row.getJSONObject(0).getString("IS_FREE"), row.getJSONObject(0).getString("TICKET"), row.getJSONObject(0).getString("PROGRAM"), row.getJSONObject(0).getString("PLAYER"), row.getJSONObject(0).getString("CONTENTS"), row.getJSONObject(0).getString("GCODE")));
                }

                CulturalEventSearchTypeA.this.culturalListAdapter.notifyDataSetChanged();
            } catch (JSONException var7) {
                var7.printStackTrace();
            }

        }

        public String executeClient(String[] strings) {
            HttpResponse response = null;
            new ArrayList();
            HttpClient client = new DefaultHttpClient();
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 30000);
            HttpConnectionParams.setSoTimeout(params, 30000);
            HttpGet httpGet = new HttpGet("http://openAPI.seoul.go.kr:8088/" + CulturalEventSearchTypeA.this.openAPIKey + "/json/SearchConcertDetailService/1/1/" + strings[0]);

            try {
                response = client.execute(httpGet);
                String resultJson = EntityUtils.toString(response.getEntity(), "UTF-8");
                return resultJson;
            } catch (ClientProtocolException var8) {
                var8.printStackTrace();
                return "";
            } catch (IOException var9) {
                var9.printStackTrace();
                return "";
            }
        }
    }

    @Override
    public void onBack() {
        MainActivity activity = (MainActivity)getActivity();
        activity.setOnBackPressedListener(null);
        //activity.onBackPressed();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity)context).setOnBackPressedListener(this);
    }
    public class ProcessNetworkSearchPerformanceBySubjectThread extends AsyncTask<String, Void, String> {
        public ProcessNetworkSearchPerformanceBySubjectThread() {
        }

        protected String doInBackground(String... strings) {
            String content = this.executeClient(strings);
            return content;
        }

        protected void onPostExecute(String result) {
            String main = result.toString();

            try {
                JSONObject jsonMain = new JSONObject(main);
                if (jsonMain.has("SearchPerformanceBySubjectService")) {
                    JSONObject jsonObject = jsonMain.getJSONObject("SearchPerformanceBySubjectService");
                    String listTotalCount = jsonObject.getString("list_total_count");
                    JSONArray row = jsonObject.getJSONArray("row");
                    if (row.length() > 0) {
                        for(int i = 0; i < row.length(); ++i) {
                            (CulturalEventSearchTypeA.this.new ProcessNetworkSearchConcertDetailThread()).execute(new String[]{row.getJSONObject(i).getString("CULTCODE")});
                        }
                    }

                    CulturalEventSearchTypeA.this.startNo = CulturalEventSearchTypeA.this.startNo + 20;
                } else {
                    CulturalEventSearchTypeA.this.startNo = -1;
                    CulturalEventSearchTypeA.this.cancelProgressDialog();
                    CulturalEventSearchTypeA.this.btnSubCodeCatalogLoadMore.setOnClickListener((OnClickListener)null);
                }
            } catch (JSONException var11) {
                var11.printStackTrace();
            } finally {
                (new Handler()).postDelayed(new Runnable() {
                    public void run() {
                        CulturalEventSearchTypeA.this.cancelProgressDialog();
                    }
                }, 3000L);
            }

        }

        public String executeClient(String[] strings) {
            HttpResponse response = null;
            new ArrayList();
            HttpClient client = new DefaultHttpClient();
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 30000);
            HttpConnectionParams.setSoTimeout(params, 30000);
            HttpGet httpGet = new HttpGet("http://openAPI.seoul.go.kr:8088/" + CulturalEventSearchTypeA.this.openAPIKey + "/json/SearchPerformanceBySubjectService/" + CulturalEventSearchTypeA.this.startNo + "/" + (CulturalEventSearchTypeA.this.startNo + 19) + "/" + strings[0]);

            try {
                response = client.execute(httpGet);
                String resultJson = EntityUtils.toString(response.getEntity(), "UTF-8");
                return resultJson;
            } catch (ClientProtocolException var8) {
                var8.printStackTrace();
                return "";
            } catch (IOException var9) {
                var9.printStackTrace();
                return "";
            }
        }
    }

    public class ProcessNetworkSearchConcertSubjectCatalogThread extends AsyncTask<String, Void, String> {
        public ProcessNetworkSearchConcertSubjectCatalogThread() {
        }

        protected String doInBackground(String... strings) {
            String content = this.executeClient(strings);
            return content;
        }

        protected void onPostExecute(String result) {
            String main = result.toString();

            try {
                JSONObject jsonMain = new JSONObject(main);
                JSONObject jsonObject = jsonMain.getJSONObject("SearchConcertSubjectCatalogService");
                String listTotalCount = jsonObject.getString("list_total_count");
                CulturalEventSearchTypeA.this.concertSubjectCatalogList = new ConsertSubjectCatalogInfo[Integer.parseInt(listTotalCount)];
                CulturalEventSearchTypeA.this.concertSubjectCatalogNameList = new String[Integer.parseInt(listTotalCount)];
                JSONArray row = jsonObject.getJSONArray("row");

                for(int i = 0; i < row.length(); ++i) {
                    CulturalEventSearchTypeA.this.concertSubjectCatalogList[i] = new ConsertSubjectCatalogInfo(row.getJSONObject(i).getString("CODE"), row.getJSONObject(i).getString("CODENAME"));
                    CulturalEventSearchTypeA.this.concertSubjectCatalogNameList[i] = row.getJSONObject(i).getString("CODENAME");
                }
            } catch (JSONException var11) {
                var11.printStackTrace();
            } finally {
                CulturalEventSearchTypeA.this.cancelProgressDialog();
            }

        }

        public String executeClient(String[] strings) {
            HttpResponse response = null;
            new ArrayList();
            HttpClient client = new DefaultHttpClient();
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 30000);
            HttpConnectionParams.setSoTimeout(params, 30000);
            HttpGet httpGet = new HttpGet("http://openAPI.seoul.go.kr:8088/" + CulturalEventSearchTypeA.this.openAPIKey + "/json/SearchConcertSubjectCatalogService/1/999");

            try {
                response = client.execute(httpGet);
                String resultJson = EntityUtils.toString(response.getEntity(), "UTF-8");
                return resultJson;
            } catch (ClientProtocolException var8) {
                var8.printStackTrace();
                return "";
            } catch (IOException var9) {
                var9.printStackTrace();
                return "";
            }
        }
    }

    public class ProcessNetworkSearchConcertPeriodThread extends AsyncTask<String, Void, String> {
        public ProcessNetworkSearchConcertPeriodThread() {
        }

        protected String doInBackground(String... strings) {
            String content = this.executeClient(strings);
            return content;
        }

        protected void onPostExecute(String result) {
            String main = result.toString();

            try {
                JSONObject jsonMain = new JSONObject(main);
                if (jsonMain.has("SearchConcertPeriodService")) {
                    JSONObject jsonObject = jsonMain.getJSONObject("SearchConcertPeriodService");
                    String listTotalCount = jsonObject.getString("list_total_count");
                    JSONArray row = jsonObject.getJSONArray("row");
                    if (row.length() > 0) {
                        for(int i = 0; i < row.length(); ++i) {
                            (CulturalEventSearchTypeA.this.new ProcessNetworkSearchConcertDetailThread()).execute(new String[]{row.getJSONObject(i).getString("CULTCODE")});
                        }
                    }

                    CulturalEventSearchTypeA.this.startNo = CulturalEventSearchTypeA.this.startNo + 20;
                } else {
                    CulturalEventSearchTypeA.this.startNo = -1;
                    CulturalEventSearchTypeA.this.cancelProgressDialog();
                    CulturalEventSearchTypeA.this.btnPeriodLoadMore.setOnClickListener((OnClickListener)null);
                }
            } catch (JSONException var11) {
                var11.printStackTrace();
            } finally {
                (new Handler()).postDelayed(new Runnable() {
                    public void run() {
                        CulturalEventSearchTypeA.this.cancelProgressDialog();
                    }
                }, 3000L);
            }

        }

        public String executeClient(String[] strings) {
            HttpResponse response = null;
            new ArrayList();
            HttpClient client = new DefaultHttpClient();
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 30000);
            HttpConnectionParams.setSoTimeout(params, 30000);
            HttpGet httpGet = new HttpGet("http://openAPI.seoul.go.kr:8088/" + CulturalEventSearchTypeA.this.openAPIKey + "/json/SearchConcertPeriodService/" + CulturalEventSearchTypeA.this.startNo + "/" + (CulturalEventSearchTypeA.this.startNo + 19) + "/" + strings[0] + "/" + strings[1]);

            try {
                response = client.execute(httpGet);
                String resultJson = EntityUtils.toString(response.getEntity(), "UTF-8");
                return resultJson;
            } catch (ClientProtocolException var8) {
                var8.printStackTrace();
                return "";
            } catch (IOException var9) {
                var9.printStackTrace();
                return "";
            }
        }
    }

    public class ProcessNetworkSearchConcertNameThread extends AsyncTask<String, Void, String> {
        public ProcessNetworkSearchConcertNameThread() {
        }

        protected String doInBackground(String... strings) {
            String content = this.executeClient(strings);
            return content;
        }

        protected void onPostExecute(String result) {
            String main = result.toString();

            try {
                JSONObject jsonMain = new JSONObject(main);
                if (jsonMain.has("SearchConcertNameService")) {
                    JSONObject jsonObject = jsonMain.getJSONObject("SearchConcertNameService");
                    String listTotalCount = jsonObject.getString("list_total_count");
                    JSONArray row = jsonObject.getJSONArray("row");
                    if (row.length() > 0) {
                        for(int i = 0; i < row.length(); ++i) {
                            (CulturalEventSearchTypeA.this.new ProcessNetworkSearchConcertDetailThread()).execute(new String[]{row.getJSONObject(i).getString("CULTCODE")});
                        }
                    }

                    CulturalEventSearchTypeA.this.startNo = CulturalEventSearchTypeA.this.startNo + 20;
                } else {
                    CulturalEventSearchTypeA.this.startNo = -1;
                    CulturalEventSearchTypeA.this.cancelProgressDialog();
                    CulturalEventSearchTypeA.this.btnNameLoadMore.setOnClickListener((OnClickListener)null);
                }
            } catch (JSONException var11) {
                var11.printStackTrace();
            } finally {
                (new Handler()).postDelayed(new Runnable() {
                    public void run() {
                        CulturalEventSearchTypeA.this.cancelProgressDialog();
                    }
                }, 3000L);
            }

        }

        public String executeClient(String[] strings) {
            HttpResponse response = null;
            new ArrayList();
            HttpClient client = new DefaultHttpClient();
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 30000);
            HttpConnectionParams.setSoTimeout(params, 30000);
            HttpGet httpGet = new HttpGet("http://openAPI.seoul.go.kr:8088/" + CulturalEventSearchTypeA.this.openAPIKey + "/json/SearchConcertNameService/" + CulturalEventSearchTypeA.this.startNo + "/" + (CulturalEventSearchTypeA.this.startNo + 19) + "/" + strings[0] + "/");

            try {
                response = client.execute(httpGet);
                String resultJson = EntityUtils.toString(response.getEntity(), "UTF-8");
                return resultJson;
            } catch (ClientProtocolException var8) {
                var8.printStackTrace();
                return "";
            } catch (IOException var9) {
                var9.printStackTrace();
                return "";
            }
        }
    }
}
