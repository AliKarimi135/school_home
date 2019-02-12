package ir.aliprogramer.schoolhome.MarkModel;

import com.google.gson.annotations.SerializedName;

public class MarkResponse {
    @SerializedName("id")
    int id;
    @SerializedName("month")
    int month;
    @SerializedName("day")
    int day;

    public int getId() {
        return id;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }
}
