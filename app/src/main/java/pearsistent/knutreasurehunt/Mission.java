package pearsistent.knutreasurehunt;

import android.widget.CheckBox;

/**
 * Created by Zzeulki on 2017. 4. 21..
 */

public class Mission {

    private CheckBox checkBox;
    private String objectName;

    //String location;

    public Mission() {

    }

    public Mission(String objectName, CheckBox checkBox) {
        this.objectName = objectName;
        this.checkBox = checkBox;
    }


    public String getObjectName() {
        return objectName;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }
}

