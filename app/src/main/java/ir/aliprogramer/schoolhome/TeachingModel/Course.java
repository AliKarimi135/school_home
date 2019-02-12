package ir.aliprogramer.schoolhome.TeachingModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Course {
    @SerializedName("book_name")
    String bookName;
    @SerializedName("class_name")
    String className;
    @SerializedName("class_id")
    int classId;
    @SerializedName("book_id")
    int bookId;
    @SerializedName("team")
    int groupId;

    public String getBookName() {
        return bookName;
    }

    public String getClassName() {
        return className;
    }

    public int getClassId() {
        return classId;
    }

    public int getBookId() {
        return bookId;
    }

    public int getGroupId() {
        return groupId;
    }
}
