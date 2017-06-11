package com.yl.yuanlu.whatsnext.Utils;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by LUYUAN on 6/7/2017.
 */

public class UIUtils {

    public static void setTextViewStrikeThrough(@NonNull TextView tv, boolean strikeThrough) {
        if (strikeThrough) {
            // strike through effect on the text
            tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            // no strike through effect
            tv.setPaintFlags(tv.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    public static void setEditTextStrikeThrough(@NonNull EditText et, boolean strikeThrough) {
        if(strikeThrough) {
            et.setPaintFlags(et.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            et.setTextColor(Color.GRAY);
        }
        else {
            et.setPaintFlags(et.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
            et.setTextColor(Color.WHITE);
        }
    }

}
