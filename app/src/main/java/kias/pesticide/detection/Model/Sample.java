package kias.pesticide.detection.Model;

import java.util.ArrayList;

/**
 * Created by reddevil on 3/15/18.
 */

public class Sample {

    ArrayList<Integer> firstColors;
    ArrayList<Integer> secondColors;

    public Sample(ArrayList<Integer> firstColors, ArrayList<Integer> secondColors) {
        this.firstColors = firstColors;
        this.secondColors = secondColors;
    }

    public ArrayList<Integer> getFirstColors() {
        return firstColors;
    }

    public void setFirstColors(ArrayList<Integer> firstColors) {
        this.firstColors = firstColors;
    }

    public ArrayList<Integer> getSecondColors() {
        return secondColors;
    }

    public void setSecondColors(ArrayList<Integer> secondColors) {
        this.secondColors = secondColors;
    }
}
