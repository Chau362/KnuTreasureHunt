package pearsistent.knutreasurehunt;


import android.widget.CheckBox;

import com.google.firebase.database.Exclude;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Zzeulki on 2017. 3. 28..
 */

public class Item {

    private CheckBox checkBox;
    private boolean choice;
    private StorageReference imageReference;    //To use FirebaseUI, I need StorageReference
    private String text;
    private String name;
    private int points;
    //String location;

    public Item() {
        this.name = null;
        this.text = null;
        this.points = 0;
        this.choice = false;

    }

    public Item(String n, CheckBox c){
        this.name = n;
        this.checkBox = c;
    }
    public Item(String n, String t, int point) {

        this.name = n;
        this.text = t;
        this.points = point;
        this.choice = false;
    }

//    public Item(String t, String n, int temp) {
//        this.text = t;
//        this.name = n;
//        this.image_i = temp;
//    }

    public int getPoints() {
        return points;
    }

    @Exclude
    public StorageReference getImageReference() {
        return this.imageReference;
    }

    @Exclude
    public void setImageReference(StorageReference imageReference) {
        this.imageReference = imageReference;
    }

    public boolean getChoice(){ return this.choice; }

    public void setChoice(boolean value){
        this.choice = value;
    }

    @Exclude
    public CheckBox getCheckBox(){ return this.checkBox;}

    @Exclude
    public void setCheckBox(CheckBox c){
        this.checkBox = c;
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
        item.put("name",name);
        item.put("text",text);

        return item;
    }


}
