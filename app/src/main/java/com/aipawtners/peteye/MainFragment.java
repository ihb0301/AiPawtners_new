package com.aipawtners.peteye;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements View.OnClickListener{

    private ClassifierWithSupport cls;
    public static final int GALLERY_IMAGE_REQUEST_CODE=1;
    TextView textview;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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
        View root=inflater.inflate(R.layout.fragment_main,container,false);

        cls=new ClassifierWithSupport(getActivity());
        try{
            cls.init();
        }catch(IOException io){
            io.printStackTrace();
        }

        CardView cardview1=root.findViewById(R.id.main_imageload);
        cardview1.setOnClickListener(this);
        textview=root.findViewById(R.id.textview_main);
        // Inflate the layout for this fragment
        return root;

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.main_imageload){
            Intent intent=new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
            startActivityForResult(intent,GALLERY_IMAGE_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(resultCode== Activity.RESULT_OK && requestCode==GALLERY_IMAGE_REQUEST_CODE){
            if(data==null){
                return;
            }

            Uri selectedImage=data.getData();
            Bitmap bitmap=null;

            try{
                if(Build.VERSION.SDK_INT>=29){
                    ImageDecoder.Source src=ImageDecoder.createSource(getActivity().getContentResolver(),selectedImage);
                    bitmap=ImageDecoder.decodeBitmap(src);
                }else{
                    bitmap= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),selectedImage);
                }
            }catch(IOException io){
                io.printStackTrace();
            }

            if(bitmap!=null){
                Pair<String,Float> output=cls.classify(bitmap);
                String result= output.first;
                if ((result.equals("결막염")||result.equals("백내장")) && output.second*100<=99){
                    //result="증상 없음";
                }
                String resultStr=String.format("%.2f%%의 확률로 %s으로 판단됩니다",output.second*100,result);

                textview.setText(resultStr);
            }
        }
    }
}