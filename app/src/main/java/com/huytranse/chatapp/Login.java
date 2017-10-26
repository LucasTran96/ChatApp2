package com.huytranse.chatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.huytranse.model.User;

public class Login extends AppCompatActivity {
   // Button btnSubrice;
    EditText txtEmail,txtPass;
    Button btnDangNhap,btnDangKy;
    TextView txtEmailDisplay,txtUserDisplay;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth =FirebaseAuth.getInstance();
        // cài đặt sự kiện lắng nghe đăng nhập
        authStateListener=  new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user= firebaseAuth.getCurrentUser();
                if(user !=null)
                {
                    // đang nhap thanh cong
                    txtUserDisplay.setText(user.getUid());
                    txtEmailDisplay.setText(user.getEmail());
                    txtEmailDisplay.setVisibility(View.INVISIBLE);
                    txtUserDisplay.setVisibility(View.INVISIBLE);
                    txtEmail.setVisibility(View.INVISIBLE);
                    txtPass.setVisibility(View.INVISIBLE);
                    btnDangKy.setVisibility(View.INVISIBLE);
                    btnDangNhap.setText("Đăng xuất");
                      //gotoChat();
                }
                else {
                    // dang nhap that bai
                    txtEmail.setVisibility(View.VISIBLE);
                    txtPass.setVisibility(View.VISIBLE);
                    btnDangKy.setVisibility(View.VISIBLE);
                    btnDangNhap.setText("Đăng nhập");
                    txtEmailDisplay.setVisibility(View.INVISIBLE);
                    txtUserDisplay.setVisibility(View.INVISIBLE);
                }
            }
        };
        //addEvencotrol();
        txtEmail= (EditText) findViewById(R.id.txtEmail);
        txtPass= (EditText) findViewById(R.id.txtPassword);
        btnDangNhap= (Button) findViewById(R.id.btnDangNhap);
        btnDangKy= (Button) findViewById(R.id.btnDangKy);
        txtEmailDisplay= (TextView) findViewById(R.id.tv_display_email);
        txtUserDisplay= (TextView) findViewById(R.id.tv_display_user_id);

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user= firebaseAuth.getCurrentUser();
                if(user !=null)
                {
                    firebaseAuth.signOut();

                }
                else {
                    String email= txtEmail.getText().toString();
                    String password= txtPass.getText().toString();
                    boolean isValid= validateFrom(email,password);
                    if (isValid==true)
                    {
                        dangNhap(email,password);

                    }else
                    {
                        Toast.makeText(getApplicationContext()," Bạn đã đăng nhập không thành công",Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email= txtEmail.getText().toString();
                String password= txtPass.getText().toString();
                boolean isValid= validateFrom(email,password);

                if (isValid == true)
                {
                    dangKy(email,password);
                }else
                {
                    Toast.makeText(getApplicationContext()," Bạn đã đăng nhập không thành công",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
    public void dangKy(String email,String password)
    {
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Đăng Ký thành công"
                            ,Toast.LENGTH_SHORT).show();
                    writeUser();
                }
                else {
                    Toast.makeText(getApplicationContext()," Đăng Ký không thành công"
                            ,Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    public void dangNhap(String email, String password)
    {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),"Đăng nhập thành công"
                                    ,Toast.LENGTH_SHORT).show();
                            gotoChat();

                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Đăng nhập thất bại"
                                    ,Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
    // kích hoạt AuthStateListener
    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener !=null)
        {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
    // kiem tra dang nhap dung hay sai
    private  boolean validateFrom(String email, String password){
        if(!email.isEmpty() && !password.isEmpty())
        {
            return true;
        }
        else {
            return false;
        }
    }
    private void gotoChat(){
        Intent intent= new Intent(Login.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void writeUser()
    {
        FirebaseUser currenUser= firebaseAuth.getCurrentUser();
        DatabaseReference mdatabase= FirebaseDatabase.getInstance().getReference();
        User user= new User(currenUser.getEmail());
        mdatabase.child("users").child(currenUser.getUid()).setValue(user);
        gotoChat();

    }

}
