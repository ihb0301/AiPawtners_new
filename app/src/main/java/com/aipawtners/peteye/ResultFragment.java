package com.aipawtners.peteye;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

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
    TextView text_effect;

    Button button_omega3;
    Button button_astaxanthin;
    Button button_graphseed;
    Button button_hachimijiogan;

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
        LinearLayout linearlayout_cataract=root.findViewById(R.id.linearlayout_cataract);
        linearlayout_cataract.setVisibility(View.GONE);

        Button button_omega3=linearlayout_cataract.findViewById(R.id.button_omega3);
        Button button_astaxanthin=linearlayout_cataract.findViewById(R.id.button_astaxanthin);
        Button button_graphseed=linearlayout_cataract.findViewById(R.id.button_graphseed);
        Button button_hachimijiogan=linearlayout_cataract.findViewById(R.id.button_hachimijiogan);

        button_omega3.setOnClickListener(this::onClick);
        button_astaxanthin.setOnClickListener(this::onClick);
        button_graphseed.setOnClickListener(this::onClick);
        button_hachimijiogan.setOnClickListener(this::onClick);

        CardView cardview1=root.findViewById(R.id.cardview_list_1);
        CardView cardview2=root.findViewById(R.id.cardview_list_2);
        CardView cardview3=root.findViewById(R.id.cardview_list_3);
        cardview1.setOnClickListener(this::onClick);
        cardview2.setOnClickListener(this::onClick);
        cardview3.setOnClickListener(this::onClick);

        text_effect=root.findViewById(R.id.text_effect);
        text_effect.setVisibility(View.GONE);

        LayoutInflater layoutinflater = LayoutInflater.from(getActivity().getApplicationContext());

        if (result.equals("")) {
            textview1.setText("검사 결과가 없어요\n검사 후 관련 정보를 확인할 수 있습니다");
        }else if(result.equals("증상 없음")){
            textview1.setText("이상이 없습니다\n아주 건강해요!");
            imageview1.setImageResource(R.drawable.emotion_happy);
            linearlayout_cataract.setVisibility(View.GONE);
        }else if(result.equals("결막염")){
            textview1.setText("결막염 관련 정보를 알려드릴게요");
            imageview1.setImageResource(R.drawable.emotion_ache);
            layoutinflater.inflate(R.layout.result_conjunctivitis, linearlayout1, true);
            linearlayout_cataract.setVisibility(View.VISIBLE);
        }else if(result.equals("백내장")){
            textview1.setText("백내장 관련 정보를 알려드릴게요");
            imageview1.setImageResource(R.drawable.emotion_ache);
            layoutinflater.inflate(R.layout.result_cataract, linearlayout1, true);
            linearlayout_cataract.setVisibility(View.VISIBLE);
        }

        return root;
    }

    public void onClick(View v) {
        if(v.getId()==R.id.button_omega3){
            text_effect.setText("오메가3 지방산은 눈 건강에 좋은 것으로 알려져 있습니다");
            text_effect.setVisibility(View.VISIBLE);
        }else if(v.getId()==R.id.button_astaxanthin){
            text_effect.setText("항산화제 성분은 백내장에 도움을 줍니다");
            text_effect.setVisibility(View.VISIBLE);
        }else if(v.getId()==R.id.button_graphseed){
            text_effect.setText("백내장 발달을 늦추는 것으로 알려진 항산화제입니다");
            text_effect.setVisibility(View.VISIBLE);
        }else if(v.getId()==R.id.button_hachimijiogan){
            text_effect.setText("이른 단계에서 시작한다면 당뇨병성 백내장에 큰 도움이 될 수 있는 성분입니다");
            text_effect.setVisibility(View.VISIBLE);
        }else if(v.getId()==R.id.cardview_list_1){
            //button_omega3.setVisibility(View.VISIBLE);
            //button_astaxanthin.setVisibility(View.GONE);
            //button_graphseed.setVisibility(View.VISIBLE);
            //button_hachimijiogan.setVisibility(View.GONE);
            Intent intent1=new Intent(Intent.ACTION_VIEW, Uri.parse("https://smartstore.naver.com/petcome/products/7584515123?NaPm=ct%3Dlmu5k69c%7Cci%3D8114bc19671990e25bcd17159a59e8e2ae375a6f%7Ctr%3Dsls%7Csn%3D677478%7Chk%3Dcb09e53f791e13a233624fbc5264c3eb515b9e83"));
            startActivity(intent1);
        }else if(v.getId()==R.id.cardview_list_2){
            //button_omega3.setVisibility(View.VISIBLE);
            //button_astaxanthin.setVisibility(View.GONE);
            //button_graphseed.setVisibility(View.VISIBLE);
            //button_hachimijiogan.setVisibility(View.GONE);
            Intent intent2=new Intent(Intent.ACTION_VIEW, Uri.parse("https://smartstore.naver.com/healspet/products/5781000505?NaPm=ct%3Dlmu5qkyw%7Cci%3Daff3ec863a636a6229bc4dbecf6546936a01933f%7Ctr%3Dsls%7Csn%3D4071618%7Chk%3De2cb34e07511f192b7136071bd125162bfc81e47"));
            startActivity(intent2);
        }else if(v.getId()==R.id.cardview_list_3) {
            //button_omega3.setVisibility(View.VISIBLE);
            //button_astaxanthin.setVisibility(View.GONE);
            //button_graphseed.setVisibility(View.VISIBLE);
            //button_hachimijiogan.setVisibility(View.GONE);
            Intent intent3=new Intent(Intent.ACTION_VIEW, Uri.parse("https://smartstore.naver.com/wild1intl/products/4269681827?NaPm=ct%3Dlmu5s6u8%7Cci%3D6078c16ba290e9b22637598ee2ec7a893c4fa3c8%7Ctr%3Dsls%7Csn%3D751585%7Chk%3D3ba7cf42c263ebfa62026bd851fe89d9a14942cc"));
            startActivity(intent3);
        }
    }
}