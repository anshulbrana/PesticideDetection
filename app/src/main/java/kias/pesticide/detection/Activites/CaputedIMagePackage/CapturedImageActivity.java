package kias.pesticide.detection.Activites.CaputedIMagePackage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import kias.pesticide.detection.Activites.CameraActivity;
import kias.pesticide.detection.Activites.EditPointerActivity;
import kias.pesticide.detection.Activites.ResultActivity;
import kias.pesticide.detection.R;

public class CapturedImageActivity extends AppCompatActivity implements CapturedImageInterface, View.OnClickListener {
    String imageUrl;

    ImageView firstHalfImage, secondHalfImage, firstHalfPointer, secondHalfPointer;
    FrameLayout firstHalfImageFrame, secondHalfImageFrame;

    ArrayList<Integer> firstColors = new ArrayList<>();
    ArrayList<Integer> secondColors = new ArrayList<>();

    LinearLayout doneLL;
    LinearLayout editLL;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captured_image);

        imageUrl = getIntent().getStringExtra("imageUrl");
        modifyUI();
        bindActivity();

        File imgFile = new File(imageUrl);
        if (imgFile.exists()) {


            Bitmap fullImageBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            cropImageIntoTwoHalf(fullImageBitmap);

        }
    }


    private void bindActivity() {
        //Dialog box
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Calculating...");

        doneLL = (LinearLayout) findViewById(R.id.doneLL);
        doneLL.setOnClickListener(this);

        editLL = (LinearLayout) findViewById(R.id.editLL);
        editLL.setOnClickListener(this);

        firstHalfImage = (ImageView) findViewById(R.id.firstHalfImage);
        firstHalfPointer = (ImageView) findViewById(R.id.firstHalfPointer);
        firstHalfImageFrame = (FrameLayout) findViewById(R.id.firstHalfImageFrame);

        secondHalfImage = (ImageView) findViewById(R.id.secondHalfImage);
        secondHalfPointer = (ImageView) findViewById(R.id.secondHalfPOinter);
        secondHalfImageFrame = (FrameLayout) findViewById(R.id.secondHalfImageFrame);

    }


    private ArrayList<Integer> getColorCode(ImageView imageView) {

        ArrayList<Integer> colors = new ArrayList<>();

        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        int pixelColor = bitmap.getPixel(imageView.getWidth() + imageView.getWidth()  / 2, imageView.getHeight() + imageView.getHeight() / 2);

        int A = Color.alpha(pixelColor);      //alpha
        int R = Color.red(pixelColor);        //red
        int G = Color.green(pixelColor);      //greeen
        int B = Color.blue(pixelColor);       //blue

        colors.add(A);
        colors.add(R);
        colors.add(G);
        colors.add(B);
        return colors;
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.doneLL: {
                dialog.show();
                Intent i = new Intent(CapturedImageActivity.this, ResultActivity.class);
                Toast.makeText(CapturedImageActivity.this, "Processing...", Toast.LENGTH_LONG).show();
                firstColors = getColorCode(firstHalfImage);
                secondColors = getColorCode(secondHalfImage);

                i.putIntegerArrayListExtra("firstColors", firstColors);
                i.putIntegerArrayListExtra("secondColors", secondColors);

                dialog.dismiss();
                CapturedImageActivity.this.finish();
                startActivity(i);
                break;
            }

            case R.id.editLL:
                Intent i2 = new Intent(CapturedImageActivity.this, EditPointerActivity.class);
                File picFile = new File(imageUrl);
                i2.putExtra("imageUrl",picFile.toString());
                startActivity(i2);



        }
    }

    @Override
    public void cropImageIntoTwoHalf(Bitmap bmp) {
        Bitmap half1 = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth() / 2, bmp.getHeight());
        firstHalfImage.setImageBitmap(half1);
        firstColors = getColorCode(firstHalfImage);

        Bitmap half2 = Bitmap.createBitmap(bmp, bmp.getWidth() / 2, 0, bmp.getWidth() / 2, bmp.getHeight());
        secondHalfImage.setImageBitmap(half2);
        getColorCode(secondHalfImage);
        secondColors = getColorCode(secondHalfImage);

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
}
