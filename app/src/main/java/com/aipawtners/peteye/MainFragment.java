package com.aipawtners.peteye;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
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
    public static final int CAMERA_IMAGE_REQUEST_CODE=2;
    private static final String KEY_SELECTED_URI = "KEY_SELECTED_URI";
    TextView textview;
    Uri selectedImageUri;

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

        if(savedInstanceState!=null){
            Uri uri=savedInstanceState.getParcelable(KEY_SELECTED_URI);
            if(uri!=null)
                selectedImageUri=uri;
        }

        CardView cardview1=root.findViewById(R.id.main_imageload);
        CardView cardview2=root.findViewById(R.id.main_camera);
        cardview1.setOnClickListener(this);
        cardview2.setOnClickListener(this);
        textview=root.findViewById(R.id.textview_main);
        // Inflate the layout for this fragment
        return root;

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.main_imageload){
            Intent intent=new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
            startActivityForResult(intent,GALLERY_IMAGE_REQUEST_CODE);
        }else if(v.getId()==R.id.main_camera){
            getImageFromCamera();
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
        }else if(resultCode== Activity.RESULT_OK && requestCode==CAMERA_IMAGE_REQUEST_CODE){
            Bitmap bitmap=null;

            try{
                if(Build.VERSION.SDK_INT>=29){
                    ImageDecoder.Source src=ImageDecoder.createSource(getActivity().getContentResolver(),selectedImageUri);
                    bitmap=ImageDecoder.decodeBitmap(src);
                }else{
                    bitmap= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),selectedImageUri);
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState){
        super.onSaveInstanceState(outState);

        outState.putParcelable(KEY_SELECTED_URI,selectedImageUri);
    }

    private void getImageFromCamera(){
        File file=new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),"pitute.jpg");
        if(file.exists()) file.delete();
        selectedImageUri= FileProvider.getUriForFile(getActivity(),getActivity().getPackageName(),file);

        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,selectedImageUri);
        startActivityForResult(intent,CAMERA_IMAGE_REQUEST_CODE);
    }
}