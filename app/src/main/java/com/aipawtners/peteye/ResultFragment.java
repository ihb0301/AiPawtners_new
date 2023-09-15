package com.aipawtners.peteye;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static String result="";
    TextView textview1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View root;

    public ResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ResultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ResultFragment newInstance(String param1, String param2) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root=inflater.inflate(R.layout.fragment_result, container, false);

        textview1=root.findViewById(R.id.result_textView);
        ImageView imageview1=root.findViewById(R.id.emotion_imageview);
        LinearLayout linearlayout1=root.findViewById(R.id.linearlayout_help);

        LayoutInflater layoutinflater = LayoutInflater.from(getActivity().getApplicationContext());

        if (result.equals("")) {
            textview1.setText("검사 결과가 없어요\n검사 후 관련 정보를 확인할 수 있습니다");
        }else if(result.equals("증상 없음")){
            textview1.setText("이상이 없습니다\n아주 건강해요!");
            imageview1.setImageResource(R.drawable.emotion_happy);
        }else if(result.equals("결막염")){
            textview1.setText("결막염 관련 정보를 알려드릴게요");
            imageview1.setImageResource(R.drawable.emotion_ache);
            layoutinflater.inflate(R.layout.result_conjunctivitis, linearlayout1, true);
        }else if(result.equals("백내장")){
            textview1.setText("백내장 관련 정보를 알려드릴게요");
            imageview1.setImageResource(R.drawable.emotion_ache);
            layoutinflater.inflate(R.layout.result_cataract, linearlayout1, true);
        }

        return root;
    }
}