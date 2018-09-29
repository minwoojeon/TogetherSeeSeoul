package src.kit.code.binoo.togetherseeseoul;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import kr.go.seoul.culturalevents.Common.ConsertSubjectCatalogInfo;
import kr.go.seoul.culturalevents.Common.CulturalInfo;
import kr.go.seoul.culturalevents.Common.CustomProgressDialog;
import kr.go.seoul.culturalevents.CulturalEventButtonTypeA;
import kr.go.seoul.culturalevents.CulturalEventSearchTypeA;


public class BlankFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private String key = "507442474f626f7431323376534c454b";

    private CulturalEventButtonTypeA typeA;

    public BlankFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        if(view != null){/*
            typeA = (CulturalEventButtonTypeA) view.findViewById(R.id.type_a);
            typeA.setOpenAPIKey(key);

            Intent intent = new Intent(this.getContext(), CulturalEventSearchTypeA.class);
            intent.putExtra("OpenAPIKey", this.openAPIKey);
            this.getContext().startActivity(intent);*/
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
