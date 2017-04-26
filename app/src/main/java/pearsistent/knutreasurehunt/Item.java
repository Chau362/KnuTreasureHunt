package pearsistent.knutreasurehunt;


import android.widget.CheckBox;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Zzeulki on 2017. 3. 28..
 */

public class Item {

    private CheckBox checkBox;
    private boolean choice;
    private int image_i;
    private String text;
    private String name;
    //String location;

    public Item() {

    }

    public Item(String n, CheckBox c){
        this.name = n;
        this.checkBox = c;
    }
    public Item(String n, String t) {

        this.name = n;
        this.text = t;
        this.choice = false;
    }

//    public Item(String t, String n, int temp) {
//        this.text = t;
//        this.name = n;
//        this.image_i = temp;
//    }

    public Item(String n, int i){
        this.name = n;
        this.image_i = i;
    }

    public int getImage_i() {
        return this.image_i;
    }

    public void setImage_i(int image){
        this.image_i = image;
    }

    public boolean getChoice(){ return this.choice; }

    public void setChoice(boolean value){
        this.choice = value;
    }

    public CheckBox getCheckBox(){ return this.checkBox;}

    public void setCheckBox(CheckBox c){
        this.checkBox = c;
        //this.checkBox.setChecked(true);
    }

    public String getText() {
        return this.text;
    }
    public String getName() {
        return this.name;
    }

    public Map<String, Object> toMap(){

        HashMap<String,Object> item = new HashMap<>();
        item.put("choice",choice);
        item.put("image_i",image_i);
        item.put("name",name);
        item.put("text",text);

        return item;
    }
}
