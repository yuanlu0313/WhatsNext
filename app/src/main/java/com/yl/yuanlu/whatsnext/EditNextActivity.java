package com.yl.yuanlu.whatsnext;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.reflect.TypeToken;
import com.yl.yuanlu.whatsnext.Model.Next;
import com.yl.yuanlu.whatsnext.Utils.DateUtils;
import com.yl.yuanlu.whatsnext.Utils.ModelUtils;
import com.yl.yuanlu.whatsnext.Utils.UIUtils;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LUYUAN on 6/6/2017.
 */

public class EditNextActivity extends AppCompatActivity {

    @BindView(R.id.edit_next_name) EditText editNextName;
    @BindView(R.id.edit_next_check_box) CheckBox editNextCheckBox;
    @BindView(R.id.edit_next_check_complete_layout) LinearLayout editNextComplete;
    @BindView(R.id.edit_next_date_select) TextView editNextDateSel;
    @BindView(R.id.edit_next_time_select) TextView editNextTimeSel;
    @BindView(R.id.edit_next_fab_save) FloatingActionButton editNextFab;
    @BindView(R.id.edit_next_delete_btn) TextView editNextDeleteBtn;

    public static final String KEY_EDIT_NEXT = "edit_next";
    public static final String KEY_EDIT_NEXT_DELETE = "edit_next_delete";
    public static final String KEY_EDIT_NEXT_NEW_ITEM = "edit_next_new_item";


    private Next next;
    private Boolean newItem;
    AlarmBroadcast alarmBroadcast = new AlarmBroadcast();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_edit);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        setTitle(null);

        next = ModelUtils.toObject(getIntent().getStringExtra(KEY_EDIT_NEXT), new TypeToken<Next>(){});

        setupUI();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setupUI() {

        if(next == null) {
            editNextDeleteBtn.setVisibility(View.GONE);
            next = new Next();
            next.alarmID = next.hashCode();
            newItem = true;
        }
        else {
            editNextDeleteBtn.setVisibility(View.VISIBLE);
            newItem = false;
        }

        if(next.name != null) editNextName.setText(next.name);

        editNextCheckBox.setChecked(next.done);
        UIUtils.setEditTextStrikeThrough(editNextName, next.done);

        editNextCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                next.done = isChecked;
                UIUtils.setEditTextStrikeThrough(editNextName, isChecked);
            }
        });

        editNextComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editNextCheckBox.setChecked(!editNextCheckBox.isChecked());
            }
        });

        setupDate();

        editNextFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAndExit(false);
            }
        });

        editNextDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAndExit(true);
            }
        });

    }

    private void setupDate() {

        final Calendar myCalendar = Calendar.getInstance();
        if (next.remindDate != null) {
            myCalendar.setTime(next.remindDate);
            editNextDateSel.setText(DateUtils.dateToString(next.remindDate));
            editNextTimeSel.setText(DateUtils.timeToString(next.remindDate));
        } else {
            editNextDateSel.setText("Set date");
            editNextTimeSel.setText("Set time");
        }

        final DatePickerDialog.OnDateSetListener remindDateListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                editNextDateSel.setText(DateUtils.dateToString(myCalendar.getTime()));
                next.remindDate = myCalendar.getTime();
            }
        };

        editNextDateSel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(EditNextActivity.this, remindDateListener, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        final TimePickerDialog.OnTimeSetListener remindTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                editNextTimeSel.setText(DateUtils.timeToString(myCalendar.getTime()));
                next.remindDate = myCalendar.getTime();
            }
        };

        editNextTimeSel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(EditNextActivity.this, remindTimeListener, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true).show();
            }
        });

    }


    private void saveAndExit(boolean toDelete) {
        next.name = editNextName.getText().toString();
        if(toDelete) {
            alarmBroadcast.cancelAlarm(this, next);
        }
        else {
            alarmBroadcast.setAlarm(this, next);
        }
        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_EDIT_NEXT_NEW_ITEM, newItem);
        resultIntent.putExtra(KEY_EDIT_NEXT_DELETE, toDelete);
        resultIntent.putExtra(KEY_EDIT_NEXT, ModelUtils.toString(next, new TypeToken<Next>(){}));
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

}
