package com.ko.statussaver;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

public interface OnShowCaseListener {
    void performShowCaseView(Context context, RecyclerView.ViewHolder viewHolder, int position);
}
