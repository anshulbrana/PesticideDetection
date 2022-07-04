package kias.pesticide.detection.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import kias.pesticide.detection.Activites.FinalResult;
import kias.pesticide.detection.Model.Sample;
import kias.pesticide.detection.R;

/**
 * Created by reddevil on 3/16/18.
 */

public class SampleResultAdapter extends BaseAdapter {
    private Context context;
    private Sample currentSample;
    ArrayList<Integer> firstColor;
    ArrayList<Integer> secondColor;
    String imageUrl;

    public SampleResultAdapter(Context context, Sample currentSample, ArrayList<Integer> firstColor, ArrayList<Integer> secondColor,String imageUrl) {
        this.context = context;
        this.currentSample = currentSample;
        this.firstColor=firstColor;
        this.secondColor=secondColor;
        this.imageUrl = imageUrl;
    }

    @Override
    public int getCount() {
        return currentSample.getFirstColors().size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            rowView = LayoutInflater.from(context).inflate(
                    R.layout.item_sample_result, parent, false);
        }

        LinearLayout sampleBackground=(LinearLayout)rowView.findViewById(R.id.sampleBacgroundLL);
        TextView index=(TextView)rowView.findViewById(R.id.sampleIndex);
        TextView colorOne=(TextView)rowView.findViewById(R.id.colorOne);
        TextView colorTwo=(TextView)rowView.findViewById(R.id.colorTwo);
        Button btnViewResult=(Button)rowView.findViewById(R.id.viewResultButton);


        colorOne.setText(currentSample.getFirstColors().get(position)+"");
        colorTwo.setText(currentSample.getSecondColors().get(position)+"");

        switch (position){
            case 0:{
                sampleBackground.setBackgroundColor(context.getResources().getColor(R.color.materialBlueGrey));
                btnViewResult.setTextColor(context.getResources().getColor(R.color.materialBlueGrey));
                index.setText("alpha");
                break;
            }
            case 1:{
                sampleBackground.setBackgroundColor(context.getResources().getColor(R.color.materialRed));
                btnViewResult.setTextColor(context.getResources().getColor(R.color.materialRed));
                index.setText("REd");
                break;

            }
            case 2:{
                sampleBackground.setBackgroundColor(context.getResources().getColor(R.color.materialGreen));
                btnViewResult.setTextColor(context.getResources().getColor(R.color.materialGreen));
                index.setText("green");
                break;

            }
            case 3:{
                sampleBackground.setBackgroundColor(context.getResources().getColor(R.color.materialBlue));
                btnViewResult.setTextColor(context.getResources().getColor(R.color.materialBlue));
                index.setText("blue");
                break;

            }
        }

        btnViewResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
//                builder1.setMessage("Write your message here.");
//                builder1.setCancelable(true);
//                builder1.show();

                Intent intent=new Intent(context, FinalResult.class);
                intent.putExtra("imageUrl", imageUrl);

                switch (position){
                    case 0:
                        intent.putExtra("first",firstColor.get(0));
                        intent.putExtra("second",secondColor.get(0));
                        break;
                    case 1:
                        intent.putExtra("first",firstColor.get(1));
                        intent.putExtra("second",secondColor.get(1));
                        break;
                    case 2:
                        intent.putExtra("first",firstColor.get(2));
                        intent.putExtra("second",secondColor.get(2));
                        break;
                    case 3:
                        intent.putExtra("first",firstColor.get(3));
                        intent.putExtra("second",secondColor.get(3));
                        break;
                }
                context.startActivity(intent);
            }
        });

        return rowView;
    }
}
