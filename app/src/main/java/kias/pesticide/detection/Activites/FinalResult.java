package kias.pesticide.detection.Activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import kias.pesticide.detection.R;
import pl.droidsonroids.gif.GifImageView;

public class FinalResult extends AppCompatActivity implements View.OnClickListener{

    TextView textView,result;
    GifImageView imageView;

    Integer firstColor, secondColor;
    String edible,imageUrl;
    double percent,total,difference,a;
    double greenLeft, greenRight, totalPercent;
    Button buttonAddItem;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_final_result);


        textView = (TextView) findViewById(R.id.textView);
        imageView = (GifImageView) findViewById(R.id.imageView);
        result = (TextView) findViewById(R.id.txtResult);
        buttonAddItem = (Button)findViewById(R.id.btn_add_item);
        buttonAddItem.setOnClickListener(this);


        firstColor = getIntent().getIntExtra("first", 0);
        secondColor = getIntent().getIntExtra("second", 0);
        imageUrl = getIntent().getStringExtra("imageUrl");



        if (firstColor >= secondColor) {
            edible = "Edible";
            total = ((firstColor / 15.00) * 100);
            difference = Math.abs(secondColor - firstColor);
            percent = ((difference / total) * 100);



        } else {

            total = ((firstColor / 15.00) * 100);
            difference = (secondColor - firstColor);
            percent = ((difference / total) * 100);

            if (percent > 0 && percent <= 15) {
                edible = "Edible";
            } else if (percent > 15 && percent <= 25) {
                edible = "washEdible";
            } else if (percent > 25 && percent <= 40) {
                edible = "waitWashEdible";

            } else {
                edible = "notEdible";
            }

        }

        greenLeft = firstColor;
        greenRight = secondColor;
        totalPercent = percent;





        updateUI(edible);


    }
    private void   addItemToSheet() {

        final ProgressDialog loading = ProgressDialog.show(this,"Adding Item","Please wait");
//        NumberFormat nf1 = DecimalFormat.getInstance();
//        nf1.setMaximumFractionDigits(0);
//        final String GreenLeft = nf1.format(greenLeft);
        final String GreenLeft=String.valueOf(greenLeft);
//        final String ImageName=imageUrl;


//        final String GreenLeft = greenLeft;
//        NumberFormat nf2 = DecimalFormat.getInstance();
//        nf2.setMaximumFractionDigits(0);
//        final String GreenRight = nf2.format(greenRight);
        final String GreenRight=String.valueOf(greenRight);
//        final double GreenRight= greenRight;
//        NumberFormat nf3 = DecimalFormat.getInstance();
//        nf3.setMaximumFractionDigits(0);
//        final String Percentage = nf2.format(percent);
        final String Percentage=String.valueOf(percent);





        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbzb1tmcixLH3UADnRO8DPNQ4YtVEzILZKhI_OB84CfZxM8zXs8/exec",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        loading.dismiss();
                        Toast.makeText(FinalResult.this,response,Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(),CameraActivity.class);
                        startActivity(intent);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parmas = new HashMap<>();

                //here we pass params
                parmas.put("action","addItem");
//                parmas.put("imageName",ImageName);
                parmas.put("greenLeft",GreenLeft);
                parmas.put("greenRight",GreenRight);
                parmas.put("percentage",Percentage);


                return parmas;
            }
        };
        int socketTimeOut = 30000;// u can change this .. here it is 50 seconds

        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue queue = Volley.newRequestQueue(this);

        queue.add(stringRequest);


    }
    @Override
    public void onClick(View v) {

        if(v==buttonAddItem){
            addItemToSheet();

            //Define what to do when button is clicked
        }
    }

    private void updateUI(String edible) {
        String str = String.format("%.02f", percent);

        result.setText(String.valueOf(str));

        switch (edible) {
            case "Edible":
                textView.setText( +
                        R.string.Edible);
                imageView.setImageResource(R.drawable.eat);
                break;
            case "washEdible":
                textView.setText(R.string.washEdible);
                imageView.setImageResource(R.drawable.wash_eat);
                break;
            case "waitWashEdible":
                textView.setText(R.string.waitWashEdible);
                imageView.setImageResource(R.drawable.wait);
                break;
            case "notEdible":
                textView.setText(R.string.notEdible);
                imageView.setImageResource(R.drawable.dont_eat);
                break;
        }


    }
}
