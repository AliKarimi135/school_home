package ir.aliprogramer.schoolhome.webService;




import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


import ir.aliprogramer.schoolhome.TApplication;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;

import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    public static final String BASE_URL="http://alideveloper.ir/app/v1/";
    private static  Retrofit retrofit=null;

    public  static Retrofit getClient(){
        if (retrofit==null){
            //Gson gson = new GsonBuilder().setLenient().create();
         /*   OkHttpClient client=new OkHttpClient.Builder()
                    .readTimeout(20, TimeUnit.SECONDS)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .build();
           retrofit=new Retrofit.Builder().baseUrl(BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create(gson)).build();*/
           // mAppPreferenceTools=new AppPreferenceTools(TApplication.applicationContext);

            final OkHttpClient.Builder httpClient=new OkHttpClient.Builder();

            httpClient.readTimeout(30,TimeUnit.SECONDS);
            httpClient.connectTimeout(30,TimeUnit.SECONDS);
            httpClient.writeTimeout(30,TimeUnit.SECONDS);
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request orginal=chain.request();

                    Request.Builder requestBulder=orginal.newBuilder();
                    requestBulder.addHeader("Accept","Application/json");
                    requestBulder.addHeader("Content-Type", "Application/json;charset=UTF-8");
                    requestBulder.method(orginal.method(),orginal.body());
                    Request request=requestBulder.build();
                    return chain.proceed(request);
                }
            });
           retrofit=new Retrofit.Builder().baseUrl(BASE_URL).client(httpClient.build())
                   .addConverterFactory(GsonConverterFactory.create()).build();
        }

       return retrofit;
    }


}
