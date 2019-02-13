package ir.aliprogramer.schoolhome.webService;

import java.util.List;

import ir.aliprogramer.schoolhome.Model.MarkModel.Mark;
import ir.aliprogramer.schoolhome.Model.MarkModel.MarkResponse;
import ir.aliprogramer.schoolhome.Model.ResponseModel;
import ir.aliprogramer.schoolhome.Model.StudentModel.StudentResponse;
import ir.aliprogramer.schoolhome.Model.TeachingModel.Course;
import ir.aliprogramer.schoolhome.UserModel.UserResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {


    @FormUrlEncoded
    @POST("register")
    Call<ResponseModel>registerUser(@Field("user_code") String codeNumber, @Field("password") String password, @Field("type") int type);

    @FormUrlEncoded
    @POST("users/login")
    Call<UserResponse>loginUser(@Field("user_code") String codeNumber, @Field("password") String password);

    @GET("course")
    Call<List<Course>>getCourse(@Query("user_id") int userId, @Query("type") int type);

    @GET("student")
    Call<List<StudentResponse>>getStudents(@Query("class_id") int classId,@Query("book_id") int bookId, @Query("group_id") int groupId);


    @GET("get/mark")
    Call<List<Mark>>getMarks(@Query("book_id") int bookId, @Query("student_id") int studentId);

    @FormUrlEncoded
    @POST("set/mark")
    Call<MarkResponse>setMark(@Field("book_id") int bookId, @Field("student_id") int studentId, @Field("mark") int mark, @Field("description") String description);

    @FormUrlEncoded
    @PUT("update/mark/{id}")
    Call<ResponseBody>updateMark(@Path("id") int id,@Field("mark") int mark,@Field("description") String description);


    @DELETE("delete/mark/{id}")
    Call<ResponseBody>destroyMark(@Path("id") int markId);

 /*@FormUrlEncoded
    @POST("course")
    Call<List<Course>>getCourse(@Field("user_id") int userId, @Field("type") int type);
    @FormUrlEncoded
    @POST("student")
    Call<List<StudentResponse>>getStudents(@Field("class_id") int classId,@Field("book_id") int bookId, @Field("group_id") int groupId);
     @FormUrlEncoded
    @POST("get/mark")
    Call<List<Mark>>getMarks(@Field("book_id") int bookId, @Field("student_id") int studentId);

    @FormUrlEncoded
    @POST("update/mark")
    Call<ResponseBody>updateMark(@Field("id") int id,@Field("mark") int mark,@Field("description") String description);

    @FormUrlEncoded
    @POST("delete/mark")
    Call<ResponseBody>destroyMark(@Field("id") int markId);
    */
}
