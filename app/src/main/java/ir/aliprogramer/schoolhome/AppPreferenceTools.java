package ir.aliprogramer.schoolhome;

import android.content.Context;
import android.content.SharedPreferences;

import ir.aliprogramer.schoolhome.Model.UserModel.UserResponse;

public class AppPreferenceTools {
    private Context mContext;
    private SharedPreferences mPreferences;

    public AppPreferenceTools(Context context) {
        this.mContext = context;
        this.mPreferences = this.mContext.getSharedPreferences("app_preference",Context.MODE_PRIVATE);
    }
    public void saveUserAuthenticationInfo(UserResponse userResponse){
        mPreferences.edit()
                .putInt("id",userResponse.getId())
                .putString("name",userResponse.getName())
                .putInt("type",userResponse.getType())
                .putInt("classId",userResponse.getClassId())
                .putString("token",userResponse.getToken())
                .apply();
    }
    public String getAccessToken(){
        return mPreferences.getString("token","ali");
    }
    public boolean isAccess(){
        return !(getAccessToken().equals("ali"));
    }
    public int getUserId(){
        return mPreferences.getInt("id",0);
    }
    public String getUserName(){
        return mPreferences.getString("name","name");
    }
    public int getType(){
        return mPreferences.getInt("type",3);
    }
    public int getClassId(){
        return mPreferences.getInt("classId",0);
    }
    /**
     * remove all prefs in logout
     */
    public void removeAllPrefs() {
        mPreferences.edit().clear().apply();
    }
}
