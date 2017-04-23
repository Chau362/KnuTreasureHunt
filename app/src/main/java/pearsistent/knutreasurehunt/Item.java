package pearsistent.knutreasurehunt;


/**
 * Created by Zzeulki on 2017. 3. 28..
 */

public class Item {
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

//    public Item(String t, String n, int temp) {
//        this.text = t;
//        this.name = n;
//        this.image_i = temp;
//    }

    public Item(String t, int i){
        this.text = t;
        this.image_i = i;
    }

    public int getImage_i() {
        return this.image_i;
    }

    public String getText() {
        return this.text;
    }

    public String getName() {
        return this.name;
    }
}
