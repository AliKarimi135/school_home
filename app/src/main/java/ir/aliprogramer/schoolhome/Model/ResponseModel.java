package ir.aliprogramer.schoolhome.Model;

import com.google.gson.annotations.SerializedName;

public class ResponseModel {
    @SerializedName("status")
    int status;
    @SerializedName("message")
    String message;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
