package com.aipawtners.peteye;

import static org.tensorflow.lite.support.image.ops.ResizeOp.ResizeMethod.NEAREST_NEIGHBOR;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Pair;

import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.Tensor;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.label.TensorLabel;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.Map;

public class ClassifierWithSupport {
    private static final String MODEL_NAME="aipawtners_model.tflite"; //모델 파일
    private static final String LABEL_FILE="labels.txt"; //추론 결과와 인덱스 매핑 위한 파일

    Context context;
    Interpreter interpreter;
    TensorImage inputImage;
    TensorBuffer outputBuffer;
    int modelInputWidth, modelInputHeight, modelInputChannel;
    private List<String> labels;

    public ClassifierWithSupport(Context context) {
        this.context = context;
    }

    public void init() throws IOException{
        //텐서플로 라이트 서포트 라이브러리를 활용한 모델 로드
        ByteBuffer model=FileUtil.loadMappedFile(context,MODEL_NAME);
        model.order(ByteOrder.nativeOrder());
        interpreter=new Interpreter(model);

        initModelShape();
        labels=FileUtil.loadLabels(context,LABEL_FILE); //labels.txt 파일을 List<String> 형태로 불러옴
    }

    private void initModelShape(){
        //입력 이미지 전처리 과정
        Tensor inputTensor=interpreter.getInputTensor(0);
        int[] shape=inputTensor.shape();
        modelInputChannel=shape[0];
        modelInputWidth=shape[1];
        modelInputHeight=shape[2];

        //모델과 동일한 데이터 타입의 TensorImage 생성
        inputImage=new TensorImage(inputTensor.dataType());

        //TensorBuffer 클래스를 통해 출력 값을 받아 옴
        Tensor outputTensor=interpreter.getOutputTensor(0);
        outputBuffer= TensorBuffer.createFixedSize(outputTensor.shape(),outputTensor.dataType());
        //createFixedSize() 함수는 매개변수로 텐서의 형태와 데이터 타입을 전달 받아 메모리 공간을 계산하여 할당
    }

    private Bitmap convertBitmapToARGB888(Bitmap bitmap){
        return bitmap.copy(Bitmap.Config.ARGB_8888,true);
    }

    //비트맵 이미지를 전처리하고 TensorImage 형태로 반환하는 함수
    private TensorImage loadImage(final Bitmap bitmap){
        //TensorImage는 ARGB_8888 비트맵만 받음
        if(bitmap.getConfig()!=Bitmap.Config.ARGB_8888){
            inputImage.load(convertBitmapToARGB888(bitmap));
        }else{
            inputImage.load(bitmap);
        }

        //입력 받은 이미지를 ImageProcessor가 전처리
        ImageProcessor imageProcessor=
                new ImageProcessor.Builder()
                        //ResizeOp는 이미지 크기를 변경하는 연산,최근적 보간법 이용하여 모델 입력 크기의 가로세로와 동일하게 변경
                        .add(new ResizeOp(modelInputWidth,modelInputHeight, NEAREST_NEIGHBOR))
                        //NormalizeOp는 이미지를 정규화하는 연산,평균과 표준편차를 매개변수로 받아 이미지의 원본 픽셀 값에서 평균을 뺀 값을 표준편차로 나눔
                        .add(new NormalizeOp(0.0f,255.0f))
                        .build();
        return imageProcessor.process(inputImage);
    }

    public Pair<String,Float> classify(Bitmap image){
        inputImage=loadImage(image);
        //추론 수행
        //입력 이미지로 Tensorimage의 ByteBuffer를 전달하고, 출력 값은 TensorBuffor의 ByteBuffer를 되감기하여 전달
        //rewind() 호출하면 ByteBuffer가 할당받은 메모리 크기는 유지된 채 내부의 위치 정보와 마크 정보가 초기화 됨
        interpreter.run(inputImage.getBuffer(),outputBuffer.getBuffer().rewind());

        //추론이 끝난 뒤 모델의 출력 값에 매핑(TensorLabel 클래스 이용)
        Map<String,Float>output=
                new TensorLabel(labels,outputBuffer).getMapWithFloatValue();

        return argmax(output);
    }

    //Map에서 속할 확률이 가장 높은 클래스명과 확률 쌍 얻는 함수
    private Pair<String,Float>argmax(Map<String,Float>map){
        String maxKey="";
        float maxVal=-1;

        for(Map.Entry<String,Float>entry:map.entrySet()){
            float f=entry.getValue();
            if(f>maxVal){
                maxKey= entry.getKey();
                maxVal=f;
            }
        }

        return new Pair<>(maxKey,maxVal);
    }
}
