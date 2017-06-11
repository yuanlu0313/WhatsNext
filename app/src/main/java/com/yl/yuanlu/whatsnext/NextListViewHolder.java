package com.yl.yuanlu.whatsnext;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LUYUAN on 6/4/2017.
 */

public class NextListViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.recycler_view_item_checkbox) CheckBox nextItemCheckBox;
    @BindView(R.id.recycler_view_item_name) TextView nextItemName;
    @BindView(R.id.recycler_view_item_layout) LinearLayout nextItemLayout;

    public NextListViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
