package ir.aliprogramer.schoolhome.UserModel;

import com.google.gson.annotations.SerializedName;

public class UserResponse {
    @SerializedName("id")
    int id;
    @SerializedName("name")
    String name;
    @SerializedName("type")
    int type;

    @SerializedName("classId")
    int classId;
    @SerializedName("token")
    String token;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public int getClassId() {
        return classId;
    }

    public String getToken() {
        return token;
    }
}
