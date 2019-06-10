package ir.aliprogramer.schoolhome.Activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;



import ir.aliprogramer.schoolhome.AppPreferenceTools;
import ir.aliprogramer.schoolhome.R;
import ir.aliprogramer.schoolhome.UserModel.UserResponse;
import ir.aliprogramer.schoolhome.webService.APIClient;
import ir.aliprogramer.schoolhome.webService.APIInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    AppCompatImageView imgBoard;
    TextInputLayout userNameLayout;
    TextInputLayout passwordLayout;
    TextInputEditText username;
    TextInputEditText password;
    AppCompatButton btnLogin;
    AppCompatButton btnRegister;

    AppPreferenceTools appPreferenceTools;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        imgBoard=findViewById(R.id.board);
        userNameLayout=findViewById(R.id.userNameLayout);
        passwordLayout=findViewById(R.id.passwordLayout);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        btnLogin=findViewById(R.id.login);


        btnRegister=findViewById(R.id.register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
        appPreferenceTools=new AppPreferenceTools(this);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkInput(username.getText().toString().trim(),password.getText().toString().trim()))
                    return;
                APIInterface apiInterface= APIClient.getClient().create(APIInterface.class);
                //APIClientProvider clientProvider=new APIClientProvider();
                //APIInterface apiInterface=clientProvider.getService();
                checkUser(apiInterface,username.getText().toString().trim(),password.getText().toString().trim());
            }
        });
    }

    private void checkUser(APIInterface apiInterface, String user, String pass) {
        Call<UserResponse> userResponseCall=apiInterface.loginUser(user,pass);


        userResponseCall.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if(response.code()==401){
                        Toast.makeText(LoginActivity.this,"نام کاربری یا رمز عبور شما اشتباه است.",Toast.LENGTH_LONG).show();
                        return;
                }else if(response.code()==200){
                    appPreferenceTools.saveUserAuthenticationInfo(response.body());
                    startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                    finish();
                }else{
                    Log.d("errorRoute",response.code()+""+response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this,"اتصال اینترنت را بررسی کنید.",Toast.LENGTH_LONG).show();
                return;
            }
        });
    }

    boolean checkInput(String StUsername,String StPassword){
        if(StUsername.isEmpty()){
            userNameLayout.setError("لطفا نام کاربری خود را وارد کنید.");
        }
        if(StPassword.isEmpty()){
            passwordLayout.setError("لطفا رمز عبور خود را وارد کنید.");
        }

        if(StUsername.isEmpty() || StPassword.isEmpty())
            return false;

        return true;
    }
}
