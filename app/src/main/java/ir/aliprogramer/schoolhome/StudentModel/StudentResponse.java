package ir.aliprogramer.schoolhome.StudentModel;

import com.google.gson.annotations.SerializedName;

public class StudentResponse {
    @SerializedName("id")
    int studentId;
    @SerializedName("name")
    String name;

    public int getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }


}
