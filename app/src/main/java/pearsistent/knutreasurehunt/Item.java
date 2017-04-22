package pearsistent.knutreasurehunt;

import java.util.ArrayList;

/**
 * Created by Zzeulki on 2017. 3. 28..
 */

public class Item extends ArrayList<Item> {
    private int image_i;
    private String text;
    private String name;
    //String location;

    public Item() {

    }

    public Item(String t, String n) {
        this.text = t;
        this.name = n;
    }

    public Item(String t, int i){
        this.text = t;
        this.image_i = i;
    }

    public int getImage_i() {
        return image_i;
    }

    public String getText() {
        return text;
    }

    public String getName() {
        return name;
    }
}
