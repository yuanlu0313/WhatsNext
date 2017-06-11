package com.yl.yuanlu.whatsnext;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.google.gson.reflect.TypeToken;
import com.yl.yuanlu.whatsnext.Model.Next;
import com.yl.yuanlu.whatsnext.Utils.ModelUtils;
import com.yl.yuanlu.whatsnext.Utils.UIUtils;

import java.util.List;

/**
 * Created by LUYUAN on 6/4/2017.
 */

public class NextListAdaptor extends RecyclerView.Adapter {

    private List<Next> nextList;
    private Context context;
    private MainActivity activity;
    AlarmBroadcast alarmBroadcast;

    public NextListAdaptor(@NonNull List<Next> nextList, MainActivity activity, Context context) {
        this.nextList = nextList;
        this.activity = activity;
        this.context = context;
        alarmBroadcast = new AlarmBroadcast();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);

        NextListViewHolder viewHolder = new NextListViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Next nextItem = nextList.get(position);
        final NextListViewHolder viewHolder = (NextListViewHolder) holder;
        viewHolder.nextItemName.setText(nextItem.name);
        viewHolder.nextItemCheckBox.setChecked(nextItem.done);
        UIUtils.setTextViewStrikeThrough(viewHolder.nextItemName, nextItem.done);

        viewHolder.nextItemCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                nextItem.done = isChecked;
                UIUtils.setTextViewStrikeThrough(viewHolder.nextItemName, isChecked);
                activity.updateNextChecked(viewHolder.getAdapterPosition(), isChecked);
                if(isChecked) {
                    alarmBroadcast.cancelAlarm(context, nextItem);
                }
                else {
                    alarmBroadcast.setAlarm(context, nextItem);
                }
            }
        });

        viewHolder.nextItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditNextActivity.class);
                intent.putExtra(EditNextActivity.KEY_EDIT_NEXT, ModelUtils.toString(nextItem, new TypeToken<Next>(){}));
                activity.startActivityForResult(intent, MainActivity.REQ_CODE_EDIT_NEXT);
            }
        });

    }

    @Override
    public int getItemCount() {
        return nextList.size();
    }


    public void prepend(@NonNull Next next) {
        nextList.add(0, next);
        notifyDataSetChanged();
    }

    public void update(@NonNull Next next, int position) {
        if(position!=-1) {
            nextList.set(position, next);
            notifyDataSetChanged();
        }
    }

    public void remove(int position) {
        if(position!=-1) {
            nextList.remove(position);
            notifyDataSetChanged();
        }
    }


}
