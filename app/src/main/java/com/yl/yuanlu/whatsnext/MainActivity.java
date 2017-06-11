package com.yl.yuanlu.whatsnext;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.yl.yuanlu.whatsnext.Model.Next;
import com.yl.yuanlu.whatsnext.Utils.ModelUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view_main) RecyclerView recyclerView;
    @BindView(R.id.fab) FloatingActionButton fab;

    private static final String SP_DATA = "sp_data";
    private static final String KEY_NEXTLIST = "next_list";
    public static final String KEY_NOTIFICATION = "notification";
    public static final String KEY_NOTIFICATION_NEXT = "notification_next";
    public static final int REQ_CODE_EDIT_NEXT = 100;

    private NextListAdaptor adaptor;
    private List<Next> nextList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if(getIntent().getBooleanExtra(KEY_NOTIFICATION, false)) {
            Log.i("Yuan DBG : ", "Back from Notification");
            Next next = ModelUtils.toObject(getIntent().getStringExtra(KEY_NOTIFICATION_NEXT), new TypeToken<Next>(){});
            ((NotificationManager) getSystemService(this.NOTIFICATION_SERVICE)).cancel(next.alarmID);
            Intent intent = new Intent(MainActivity.this, EditNextActivity.class);
            intent.putExtra(EditNextActivity.KEY_EDIT_NEXT, getIntent().getStringExtra(KEY_NOTIFICATION_NEXT));
            startActivityForResult(intent, MainActivity.REQ_CODE_EDIT_NEXT);
        }

        loadData();
        setupUI();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQ_CODE_EDIT_NEXT && resultCode == Activity.RESULT_OK) {
            boolean newItem = data.getBooleanExtra(EditNextActivity.KEY_EDIT_NEXT_NEW_ITEM, true);
            boolean toDelete = data.getBooleanExtra(EditNextActivity.KEY_EDIT_NEXT_DELETE, false);
            Next next = ModelUtils.toObject(data.getStringExtra(EditNextActivity.KEY_EDIT_NEXT), new TypeToken<Next>(){});
            updateNextList(next, newItem, toDelete);
        }

    }

    private void loadData() {
        nextList = ModelUtils.toObject(ModelUtils.load_from_sp(SP_DATA, MainActivity.this, KEY_NEXTLIST), new TypeToken<List<Next>>(){});
        if(nextList == null) nextList = new ArrayList<>();
    }

    private void setupUI() {

        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        adaptor = new NextListAdaptor(nextList, this, MainActivity.this);
        recyclerView.setAdapter(adaptor);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditNextActivity.class);
                startActivityForResult(intent, REQ_CODE_EDIT_NEXT);
            }
        });

    }

    private void updateNextList(Next next, boolean newItem, boolean toDelete) {
        if(newItem) {
            adaptor.prepend(next);
        }
        else {
            int position=-1;
            for(int i=0; i<nextList.size(); i++) {
                if(nextList.get(i).id.equals(next.id)) {
                    position = i;
                    break;
                }
            }
            if(toDelete) {
                adaptor.remove(position);
            }
            else {
                adaptor.update(next, position);
            }
        }
        saveData();
    }

    private void saveData() {
        ModelUtils.save_to_sp(SP_DATA, MainActivity.this, ModelUtils.toString(nextList, new TypeToken<List<Next>>(){}), KEY_NEXTLIST);
    }

    public void updateNextChecked(int position, boolean isChecked) {
        nextList.get(position).done = isChecked;
        saveData();
    }

}
