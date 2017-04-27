package pearsistent.knutreasurehunt;


import android.widget.CheckBox;

/**
 * Created by Zzeulki on 2017. 3. 28..
 */

public class Item {

    private CheckBox checkBox;
    private boolean choice;
    private int image_i;
    private String description;
    private String name;
    private int points;

    public Item() {

    }

    public Item(String n, CheckBox c) {
        this.name = n;
        this.checkBox = c;
    }

    public Item(String n, String t) {

        this.name = n;
        this.description = t;
    }

    public Item(String n, String t, int p) {

        this.name = n;
        this.description = t;
        this.choice = false;
        this.points = p;
    }

//    public Item(String t, String n, int temp) {
//        this.text = t;
//        this.name = n;
//        this.image_i = temp;
//    }


    public int getPoints() {
        return points;
    }

    public int getImage_i() {
        return this.image_i;
    }

    public boolean getChoice() {
        return this.choice;
    }

    public CheckBox getCheckBox() {
        return this.checkBox;
    }

    public void setCheckBox(CheckBox c) {
        this.checkBox = c;
    }

    public String getText() {
        return this.description;
    }

    public String getName() {
        return this.name;
    }
}
