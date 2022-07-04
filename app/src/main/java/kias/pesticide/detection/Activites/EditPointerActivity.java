package kias.pesticide.detection.Activites;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;

import kias.pesticide.detection.Activites.CaputedIMagePackage.CapturedImageInterface;
import kias.pesticide.detection.R;

public class EditPointerActivity extends AppCompatActivity implements CapturedImageInterface, View.OnClickListener {
    String imageUrl;

    //    ImageView firstHalfPointer, secondHalfPointer;
    CropImageView firstHalfImage, secondHalfImage;


    LinearLayout dividedLL;
    LinearLayout doneLL;

    ArrayList<Integer> firstColors = new ArrayList<>();
    ArrayList<Integer> secondColors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pointer);

        imageUrl = getIntent().getStringExtra("imageUrl");

        File imgFile = new File(imageUrl);


        bindActivity();
        modifyUI();

        if (imgFile.exists()) {

            Bitmap fullImageBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            cropImageIntoTwoHalf(fullImageBitmap);

        }

    }


    private void bindActivity() {
        dividedLL = (LinearLayout) findViewById(R.id.dividedLL);
        doneLL = (LinearLayout) findViewById(R.id.doneLL);
        doneLL.setOnClickListener(this);

        firstHalfImage = (CropImageView) findViewById(R.id.firstHalfImage);

        secondHalfImage = (CropImageView) findViewById(R.id.secondHalfImage);

    }


    private ArrayList<Integer> getColorFromXY(Bitmap bitmap) {
        ArrayList<Integer> colorsArrayList = new ArrayList<>();


        Integer count = 0;
        int totalRed = 0, totalBlue = 0, totalGreen = 0;
        int avgRed = 0, avgBlue = 0, avgGreen = 0, avgAlpha;
        int width, height;


        for (width = 0; width < bitmap.getWidth(); width++) {
            for (height = 0; height < bitmap.getHeight(); height++) {
                int pixelColor = bitmap.getPixel(width, height);
                int R = Color.red(pixelColor);
                int G = Color.green(pixelColor);
                int B = Color.blue(pixelColor);

                count++;
                totalRed = totalRed + R;
                totalGreen = totalGreen + G;
                totalBlue = totalBlue + B;

            }
        }

        avgAlpha = 255;
        avgRed = totalRed / count;
        avgGreen = totalGreen / count;
        avgBlue = totalBlue / count;
        colorsArrayList.add(avgAlpha);
        colorsArrayList.add(avgRed);
        colorsArrayList.add(avgGreen);
        colorsArrayList.add(avgBlue);
        return colorsArrayList;

    }

    @Override
    public void cropImageIntoTwoHalf(Bitmap bmp) {
        Bitmap half1 = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth()/2, bmp.getHeight());
        firstHalfImage.setImageBitmap(half1);
        firstHalfImage.setScaleX((float)1.5);
        firstHalfImage.setScaleY((float)1.5);

        Bitmap half2 = Bitmap.createBitmap(bmp, bmp.getWidth()/2, 0, bmp.getWidth()/2, bmp.getHeight());
        secondHalfImage.setImageBitmap(half2);
        secondHalfImage.setScaleX((float)1.5);
        secondHalfImage.setScaleY((float)1.5);

    }

    private void modifyUI() {

        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id.doneLL:

                Bitmap firstHalfImageCroppedImage = firstHalfImage.getCroppedImage();
                Bitmap secondHalfCroppedImage = secondHalfImage.getCroppedImage();

                firstColors = getColorFromXY(firstHalfImageCroppedImage);
                secondColors = getColorFromXY(secondHalfCroppedImage);

                Intent i = new Intent(EditPointerActivity.this, ResultActivity.class);
                Toast.makeText(EditPointerActivity.this, "Processing...", Toast.LENGTH_LONG).show();

                i.putIntegerArrayListExtra("firstColors", firstColors);
                i.putIntegerArrayListExtra("secondColors", secondColors);
                i.putExtra("imageUrl", imageUrl);
                startActivity(i);
                EditPointerActivity.this.finish();

                break;
        }

    }


}
