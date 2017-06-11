package com.yl.yuanlu.whatsnext.Model;

import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * Created by LUYUAN on 6/5/2017.
 */

public class Next {

    public String id;

    public int alarmID;

    public String name;

    public String description;

    public Boolean done;

    public Date remindDate;

    public Next() {
        this.done = false;
        this.id = UUID.randomUUID().toString();
    }

    public Next(@NonNull String name, @NonNull Date remindDate, String description) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.remindDate = remindDate;
        this.description = description;
    }

}
