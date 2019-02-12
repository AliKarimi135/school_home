package ir.aliprogramer.schoolhome.TeacherModel;



import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TeacherResponse {
    @SerializedName("teachers")
    private List<Teacher>teachers;

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }
}
