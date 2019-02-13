package ir.aliprogramer.schoolhome.Activity;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import ir.aliprogramer.schoolhome.Model.ResponseModel;
import ir.aliprogramer.schoolhome.R;
import ir.aliprogramer.schoolhome.webService.APIClient;
import ir.aliprogramer.schoolhome.webService.APIInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    AppCompatImageView imgBoard;
    TextInputLayout nationalCodeLayout;
    TextInputLayout passwordLayout;
    TextInputLayout confirmPasswordLayout;
    TextInputEditText password;
    TextInputEditText nationalCode;
    TextInputEditText confirmPassword;
    AppCompatButton btnCancel;
    AppCompatButton btnRegister;
    RadioGroup radioGroup;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        imgBoard=findViewById(R.id.board);
        nationalCodeLayout=findViewById(R.id.nationalCodeLayout);
        passwordLayout=findViewById(R.id.passwordLayout);
        confirmPasswordLayout=findViewById(R.id.confirmPasswordLayout);
        nationalCode=findViewById(R.id.nationalCode);
        password=findViewById(R.id.password);
        confirmPassword=findViewById(R.id.confirmPassword);
        btnCancel=findViewById(R.id.cancel);
        btnRegister=findViewById(R.id.register);
        radioGroup=findViewById(R.id.radio_group);


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int type=0;
                String codeNumber,StPassword,StConfirmPassword;
                codeNumber=nationalCode.getText().toString().trim();
                StPassword=password.getText().toString().trim();
                StConfirmPassword=confirmPassword.getText().toString().trim();

                if(!checkInput(codeNumber,StPassword,StConfirmPassword))
                    return;

                APIInterface apiInterface= APIClient.getClient().create(APIInterface.class);
                //APIClientProvider clientProvider=new APIClientProvider();
                //APIInterface apiInterface=clientProvider.getService();
                if(radioGroup.getCheckedRadioButtonId()==R.id.teacher){
                    type=1;
                }else{
                    type=0;
                 }
                RegisterUser(apiInterface,type);
            }
        });
    }
    boolean checkInput(String codeNumber,String StPass,String StConfirmPass){
        if(codeNumber.isEmpty() && radioGroup.getCheckedRadioButtonId()==R.id.teacher){
            nationalCodeLayout.setError("لطفا کد پرسنلی خود را وارد کنید.");
        }
        if(codeNumber.isEmpty() && radioGroup.getCheckedRadioButtonId()==R.id.parent){
            nationalCodeLayout.setError("لطفا کد ملی دانش آموز خود را وارد کنید. ");
        }
        if(StPass.isEmpty()){
            passwordLayout.setError("لطفا رمز عبور خود را وارد کنید.");
        }
        boolean result=StConfirmPass.equals(StPass);
        if(!result){
            confirmPasswordLayout.setError("رمز عبور با تکرار رمز عبور تطابق ندارد.");
        }
        int length=StPass.length();
        if(length<7){
            passwordLayout.setError("طول رمزعبور باید حداقل هشت باشد.");
        }
        if(codeNumber.isEmpty() || StPass.isEmpty() || !result || length<7)
            return false;

        return true;
    }
    private void RegisterUser(APIInterface apiInterface, int type) {
        Call<ResponseModel>apiCall=apiInterface.registerUser(nationalCode.getText().toString().trim(),password.getText().toString().trim(),type);

        apiCall.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    if(response.body().getStatus()==200){
                        Toast.makeText(RegisterActivity.this,"ثبت نام شما با موفقیت انجام شد.",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                        finish();
                    }else if(response.body().getStatus()==404){
                        String responseRecieved=response.body().getMessage();
                        if(responseRecieved.equals("1")){
                            Toast.makeText(RegisterActivity.this,"کد پرسنلی شما در سامانه ثبت نشده یا قبلا ثبت نام کرده اید.",Toast.LENGTH_LONG).show();
                            nationalCodeLayout.setError("کد پرسنلی شما در سامانه ثبت نشده یا قبلا ثبت نام کرده اید.");
                            return;
                        }else if(responseRecieved.equals("2")){
                            Toast.makeText(RegisterActivity.this,"کد ملی شما در سامانه ثبت نشده یا قبلا ثبت نام کرده اید.",Toast.LENGTH_LONG).show();
                            nationalCodeLayout.setError("کد ملی شما در سامانه ثبت نشده یا قبلا ثبت نام کرده اید.");
                            return;
                        }
                    }


            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(RegisterActivity.this,"اتصال اینترنت را بررسی کنید.",Toast.LENGTH_LONG).show();
                return;
            }
        });
    }



}
