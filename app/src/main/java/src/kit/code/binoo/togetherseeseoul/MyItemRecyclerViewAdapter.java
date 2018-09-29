package src.kit.code.binoo.togetherseeseoul;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import kr.go.seoul.culturalevents.Common.CulturalInfo;
import src.kit.code.binoo.togetherseeseoul.ItemFragment.OnListFragmentInteractionListener;
import src.kit.code.binoo.togetherseeseoul.dummy.DummyContent;
import src.kit.code.binoo.togetherseeseoul.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private List<DummyItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapter(OnListFragmentInteractionListener listener) {
        mValues = DummyContent.ITEMS;
        mListener = listener;
        Log.d("cnt",""+mValues.size());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        Log.d("view",""+(view == null));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText((mValues.get(position).id+""));
        holder.mContentView.setText(mValues.get(position).title);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number2);
            mContentView = (TextView) view.findViewById(R.id.content);
            mContentView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN){
                        CulturalEventDetail culturalEventDetail = (CulturalEventDetail)ActivitiesManager.getInstance().fragments.get("CulturalEventDetail");
                        culturalEventDetail.setCulturalInfo( mItem.culturalInfo );
                        ActivitiesManager.getInstance().getCurActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.mainLinear, culturalEventDetail, "CulturalInfo").commit();
                    }
                    return false;
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
