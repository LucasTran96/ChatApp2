package com.huytranse.chatapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.huytranse.model.ChatItem;
import com.huytranse.viewholder.ChatRecyclerviewHolder;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button btnGui;
    private EditText edtSent;
    private RecyclerView listChat;
    private DatabaseReference mdatabase;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter<ChatItem,ChatRecyclerviewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addcontrol();
        mdatabase = FirebaseDatabase.getInstance().getReference();
        btnGui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitChatContent();
                edtSent.setText("");
            }
        });
        //cài đặt layoutmanager
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setStackFromEnd(false);
        listChat.setLayoutManager(linearLayoutManager);
        listChat.setItemAnimator(new DefaultItemAnimator());
        // get data chat frome database
        Query queryChat= mdatabase.child("chats");
        adapter= new FirebaseRecyclerAdapter<ChatItem, ChatRecyclerviewHolder>(ChatItem.class,R.layout.item_chat,ChatRecyclerviewHolder.class,queryChat) {
            @Override
            protected void populateViewHolder(ChatRecyclerviewHolder viewHolder, ChatItem model, int position) {
                viewHolder.txtAuthorChat.setText(model.chatAuthor);
                viewHolder.txtContentChat.setText(model.chatContent);
            }
        };
        listChat.setAdapter(adapter);
    }

    private void addcontrol() {
        btnGui= (Button) findViewById(R.id.btnSent);
        edtSent= (EditText) findViewById(R.id.edtText);
        listChat= (RecyclerView) findViewById(R.id.list_chat);
    }
    private void  submitChatContent(){
        String chatContent= edtSent.getText().toString().trim();
        if(TextUtils.isEmpty(chatContent)){
            edtSent.setError("Yêu cầu nhập văn bản");
            return;
        }
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String userID= user.getUid();
        String userEmail= user.getEmail();
        ChatItem chatItem= new ChatItem(userID,userEmail,chatContent);
        Map<String,Object> chatValue= chatItem.toMap();
        String key = mdatabase.child("chats").push().getKey();
        Map<String,Object> childUpdate= new HashMap<>();
        childUpdate.put("/chats/"+key,chatValue);
        childUpdate.put("/user-chats/"+ userID+"/"+key,chatValue);
        mdatabase.updateChildren(childUpdate);
    }
}
