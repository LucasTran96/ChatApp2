package com.huytranse.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.huytranse.chatapp.R;

/**
 * Created by HuyTranSE on 10/17/2017.
 */

public class ChatRecyclerviewHolder extends RecyclerView.ViewHolder {
    public TextView txtAuthorChat;
    public TextView txtContentChat;
    public ChatRecyclerviewHolder(View itemView) {
        super(itemView);
        txtAuthorChat= (TextView) itemView.findViewById(R.id.txtUsername);
        txtContentChat= (TextView) itemView.findViewById(R.id.txtNoiDung);

    }
}
