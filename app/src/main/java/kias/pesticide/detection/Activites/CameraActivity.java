package kias.pesticide.detection.Activites;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import kias.pesticide.detection.Activites.CaputedIMagePackage.CapturedImageActivity;
import kias.pesticide.detection.R;


public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback, View.OnClickListener {
    FloatingActionButton captureButton;
    FloatingActionButton galleryButton;
    private static final int PICK_IMAGE = 100;
    Uri imageUrl;

    Camera camera;
    SurfaceView cameraSurfaceView;

    SurfaceHolder surfaceHolder;
    Camera.PictureCallback jpegCallback;
    Camera.ShutterCallback shutterCallback;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        bindActivity();
        modifyUI();


        defineJPEGCallback();
        bringOutCamera();

    }

    private void bringOutCamera() {
        surfaceHolder = cameraSurfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUrl = data.getData();

            Uri selectedImageURI = data.getData();


            Intent i = new Intent(CameraActivity.this, CapturedImageActivity.class);
            i.putExtra("imageUrl", getRealPathFromURI(selectedImageURI));
            startActivity(i);
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.captureFAB:
                cameraImage();
                break;
            case R.id.toGallery:
                openGallery();
                break;
        }

    }

    private void bindActivity() {
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("Saving Image...");

        captureButton = (FloatingActionButton) findViewById(R.id.captureFAB);
        captureButton.setOnClickListener(this);
        cameraSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        galleryButton = (FloatingActionButton) findViewById(R.id.toGallery);
        galleryButton.setOnClickListener(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera = Camera.open();
        } catch (RuntimeException e) {

        }

        Camera.Parameters parameters;

        parameters = camera.getParameters();
        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }

        parameters.setJpegQuality(100);

//        camera.setParameters(parameters);

        parameters = camera.getParameters();

        List<Camera.Size> sizes = parameters.getSupportedPictureSizes();
        Camera.Size size = sizes.get(0);
//Camera.Size size1 = sizes.get(0);
        for (int i = 0; i < sizes.size(); i++) {

            if (sizes.get(i).width > size.width)
                size = sizes.get(i);


        }
        parameters.setPictureSize(size.width, size.height);

        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        parameters.set("orientation", "landscape");

        camera.setParameters(parameters);

        try {

            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();

        } catch (Exception e) {

        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        refreshCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    private void defineJPEGCallback() {
        jpegCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                dialog.show();
                FileOutputStream outputStream = null;
                File fileImage = getDir();
                if (!fileImage.exists() && !fileImage.mkdir()) {
                    Toast.makeText(CameraActivity.this, "Can't Create Directory to Save Image...", Toast.LENGTH_SHORT).show();
                    return;
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmddhhmmss");
                String date = simpleDateFormat.format(new Date());
                String photoFile = "Sample_Image" + date + ".jpg";
                String fileName = fileImage.getAbsolutePath() + "/" + photoFile;

                File picFile = new File(fileName);
                try {

                    outputStream = new FileOutputStream(picFile);
                    outputStream.write(data);
                    outputStream.close();


                    Intent i = new Intent(CameraActivity.this, CapturedImageActivity.class);
                    i.putExtra("imageUrl", picFile.toString());
                    dialog.dismiss();
                    startActivity(i);


                } catch (IOException e) {

                } finally {

                }

                Toast.makeText(CameraActivity.this, "Image Saved...", Toast.LENGTH_SHORT).show();
                refreshCamera();
                refreshGallery(picFile);
            }
        };
    }

    private File getDir() {
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(file, "Sample");
    }

    private void cameraImage() {

        camera.takePicture(null, null, jpegCallback);
    }

    public void refreshGallery(File file) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);

    }

    public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            return;
        }
        try {
            camera.startPreview();
        } catch (Exception e) {

        }

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
        }
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
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
