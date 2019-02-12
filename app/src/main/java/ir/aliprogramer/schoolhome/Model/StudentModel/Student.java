package ir.aliprogramer.schoolhome.Model.StudentModel;

import com.google.gson.annotations.SerializedName;

public class Student {
    @SerializedName("id")
    int id;
    @SerializedName("national_code")
    String nationalCode;
    @SerializedName("name")
    String name;
    @SerializedName("class_id")
    int classId;
    @SerializedName("active")
    boolean active;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNationalCode() {
        return nationalCode;
    }

    public void setNationalCode(String nationalCode) {
        this.nationalCode = nationalCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
