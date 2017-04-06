package pearsistent.knutreasurehunt;

/**
 * Created by Zzeulki on 2017. 3. 28..
 */

public class Item {
    private int image_i;
    private String text;
    //String location;


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
}
