package pearsistent.knutreasurehunt;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Zzeulki on 2017. 3. 28..
 */

public class Item implements Parcelable {
    private int image_i;
    private String text;
    private String name;
    //String location;

    public Item() {

    }

    public Item(String n, String t) {
        this.name = n;
        this.text = t;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(text);
        dest.writeString(name);
    }

    public static final Parcelable.Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return null;
        }

        @Override
        public Object[] newArray(int size) {
            return new Object[0];
        }
    };

}
