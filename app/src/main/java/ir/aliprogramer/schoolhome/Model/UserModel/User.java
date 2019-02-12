package ir.aliprogramer.schoolhome.Model.UserModel;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    int id;
    @SerializedName("national_code")
    String 	code;
    @SerializedName("password")
    String password;
    @SerializedName("type")
    boolean type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }
}
