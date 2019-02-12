package ir.aliprogramer.schoolhome.webService;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import ir.aliprogramer.schoolhome.AppPreferenceTools;
import ir.aliprogramer.schoolhome.HomeActivity;
import ir.aliprogramer.schoolhome.TApplication;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;

import retrofit2.converter.gson.GsonConverterFactory;

public class APIClientProvider {
    public static final String BASE_URL="http://alideveloper.ir/app/v1/";
    private   Retrofit retrofit;
    private APIInterface apiInterface;
    private AppPreferenceTools appPreferenceTools;

    public APIClientProvider() {
        if(retrofit==null){
            this.appPreferenceTools=new AppPreferenceTools((HomeActivity.getContext()));
            OkHttpClient.Builder httpClient=new OkHttpClient.Builder();
            File file=new File((HomeActivity.getContext()).getCacheDir(),"httpCache");
            Cache cache=new Cache(file,10*1000*1000);
            httpClient.readTimeout(30, TimeUnit.SECONDS)
                       .connectTimeout(30,TimeUnit.SECONDS)
                        .writeTimeout(30,TimeUnit.SECONDS)
                        .cache(cache).build();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request orginal=chain.request();

                    Request.Builder requestBulder=orginal.newBuilder();
                    requestBulder.addHeader("Accept","Application/json");
                    requestBulder.addHeader("Content-Type", "Application/json;charset=UTF-8");
                    if(appPreferenceTools.isAccess()) {
                        requestBulder.addHeader("Authorization", "bearer " + appPreferenceTools.getAccessToken());
                       // orginal = orginal.newBuilder().header("Cache-Control", "public, max-age=" + 60).build();
                    }
                    requestBulder.method(orginal.method(),orginal.body());
                    Request request=requestBulder.build();
                    return chain.proceed(request);
                }
            });
            retrofit=new Retrofit.Builder().baseUrl(BASE_URL).client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create()).build();
            apiInterface=retrofit.create(APIInterface.class);
        }
    }
    public APIInterface getService(){
        return  apiInterface;
    }
}