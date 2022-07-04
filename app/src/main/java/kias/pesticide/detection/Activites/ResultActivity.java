package kias.pesticide.detection.Activites;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import kias.pesticide.detection.Adapters.SampleResultAdapter;
import kias.pesticide.detection.Model.Sample;
import kias.pesticide.detection.R;


public class ResultActivity extends AppCompatActivity {

    ArrayList<Integer> firstColors = new ArrayList<>();
    ArrayList<Integer> secondColors = new ArrayList<>();
    int redLeft,blueLeft,greenLeft,redRight,blueRight,greenRight;

    ImageView samepleOneImageView,sampleTwoImageView;
    String imageUrl;

    Sample sample;
    ListView listView;
    SampleResultAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        bindActivity();
        modifyUI();

        firstColors=getIntent().getExtras().getIntegerArrayList("firstColors");
        secondColors=getIntent().getExtras().getIntegerArrayList("secondColors");
        imageUrl = getIntent().getStringExtra("imageUrl");

        sample=new Sample(firstColors,secondColors);

        adapter=new SampleResultAdapter(this,sample,firstColors,secondColors,imageUrl);
        listView.setAdapter(adapter);

        samepleOneImageView.setBackgroundColor(Color.rgb(firstColors.get(1),firstColors.get(2),firstColors.get(3)));
        sampleTwoImageView.setBackgroundColor(Color.rgb(secondColors.get(1),secondColors.get(2),secondColors.get(3)));
         redLeft = firstColors.get(1);
         redRight= secondColors.get(1);
         blueLeft = firstColors.get(2);
         blueRight = firstColors.get(2);
         greenLeft = firstColors.get(3);
         greenRight = firstColors.get(3);



    }

    private void bindActivity() {
        samepleOneImageView=(ImageView)findViewById(R.id.sampleOneImageView) ;
        sampleTwoImageView=(ImageView)findViewById(R.id.sampleTwoImageView) ;

        listView=(ListView)findViewById(R.id.listView) ;
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
