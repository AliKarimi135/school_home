package ir.aliprogramer.schoolhome.MarkModel;

import com.google.gson.annotations.SerializedName;

public class Mark {
    @SerializedName("id")
    int id;
    @SerializedName("mark")
    int mark;
    @SerializedName("description")
    String description;
    @SerializedName("day")
    int day;
    @SerializedName("month")
    int month;

    public int getId() {
        return id;
    }

    public int getMark() {
        return mark;
    }

    public String getDescription() {
        return description;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
