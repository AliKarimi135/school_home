package ir.aliprogramer.schoolhome.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import ir.aliprogramer.schoolhome.Activity.LoginActivity;
import ir.aliprogramer.schoolhome.AppPreferenceTools;
import ir.aliprogramer.schoolhome.Activity.HomeActivity;
import ir.aliprogramer.schoolhome.R;
import ir.aliprogramer.schoolhome.Adapters.StudentAdapter;
import ir.aliprogramer.schoolhome.Model.StudentModel.StudentResponse;
import ir.aliprogramer.schoolhome.webService.APIClientProvider;
import ir.aliprogramer.schoolhome.webService.APIInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentFragment extends Fragment {
    int classId,groupId,bookId;
    String className,bookName;
    RecyclerView recyclerView;
    List<StudentResponse>studentList;
    FragmentManager manager;
    AppPreferenceTools appPreferenceTools;
    StudentAdapter studentAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.student_fragment,container,false);
        recyclerView=view.findViewById(R.id.student_recycle);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(recyclerView.getContext(),new LinearLayoutManager(getActivity()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null){
            classId=savedInstanceState.getInt("classId");
            groupId=savedInstanceState.getInt("groupId");
            bookId=savedInstanceState.getInt("bookId");
            className=savedInstanceState.getString("className");
            bookName=savedInstanceState.getString("bookName");
        }

        ((HomeActivity)getActivity()).changeToolBarText(bookName+"\n"+" کلاس "+ className);
        if(studentList==null) {
            APIClientProvider clientProvider = new APIClientProvider();
            APIInterface apiInterface2 = clientProvider.getService();
            Call<List<StudentResponse>> listCall = apiInterface2.getStudents(classId, bookId, groupId);

            ((HomeActivity) getActivity()).showProgressDialog();
            listCall.enqueue(new Callback<List<StudentResponse>>() {
                @Override
                public void onResponse(Call<List<StudentResponse>> call, Response<List<StudentResponse>> response) {

                    ((HomeActivity) getActivity()).hideProgressDialog();
                    if ( response.code() == 401 || response.code() == 400) {
                        Toast.makeText(getContext(), "لطفا مجدد وارد برنامه شوید.", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getContext(),LoginActivity.class));
                        (getActivity()).finish();
                        return;
                    }
                    if (response.isSuccessful()) {
                        studentList = response.body();
                        studentAdapter=new StudentAdapter(getActivity(), studentList, bookId, manager, className, bookName);
                        recyclerView.setAdapter(studentAdapter);
                    }
                }

                @Override
                public void onFailure(Call<List<StudentResponse>> call, Throwable t) {
                    ((HomeActivity) getActivity()).hideProgressDialog();
                    Toast.makeText(getContext(), "اتصال اینترنت را بررسی کنید.", Toast.LENGTH_LONG).show();
                    return;
                }
            });
        }else {
            recyclerView.setAdapter(studentAdapter);
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("classId",classId);
        outState.putInt("groupId",groupId);
        outState.putInt("bookId",bookId);
        outState.putString("className",className);
        outState.putString("bookName",bookName);
    }

    public void setData(int classId, int groupId, int bookId, FragmentManager manager,String className,String bookName){
        this.classId=classId;
        this.groupId=groupId;
        this.bookId=bookId;
        this.manager=manager;
        this.className=className;
        this.bookName=bookName;
    }
}
