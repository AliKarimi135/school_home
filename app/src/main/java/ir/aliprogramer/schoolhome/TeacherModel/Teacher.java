package ir.aliprogramer.schoolhome.TeacherModel;


import com.google.gson.annotations.SerializedName;

public class Teacher {
    @SerializedName("id")
    int id;
    @SerializedName("personal_code")
    String personalCode;
   @SerializedName("name")
    String name;
    @SerializedName("expertise")
    String expertise;
   @SerializedName("degree")
    int degree;
   @SerializedName("active")
    boolean active;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPersonalCode() {
        return personalCode;
    }

    public void setPersonalCode(String personalCode) {
        this.personalCode = personalCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }
}
