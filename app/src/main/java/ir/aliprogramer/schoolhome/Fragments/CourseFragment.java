package ir.aliprogramer.schoolhome.Fragments;

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

import ir.aliprogramer.schoolhome.AppPreferenceTools;
import ir.aliprogramer.schoolhome.Activity.HomeActivity;
import ir.aliprogramer.schoolhome.R;
import ir.aliprogramer.schoolhome.Adapters.CourseAdapter;
import ir.aliprogramer.schoolhome.Model.TeachingModel.Course;
import ir.aliprogramer.schoolhome.webService.APIClientProvider;
import ir.aliprogramer.schoolhome.webService.APIInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseFragment extends Fragment {
    RecyclerView recyclerView;
    List<Course> courseList;
    int userId,classId,type;
    FragmentManager manager;
    AppPreferenceTools appPreferenceTools;
    CourseAdapter cursorAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.course_fragment,container,false);
        recyclerView=view.findViewById(R.id.course_recycle);
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
        appPreferenceTools=new AppPreferenceTools(getContext());

        ((HomeActivity)getActivity()).changeToolBarText("آقای "+appPreferenceTools.getUserName());
        if(courseList==null) {
            userId=appPreferenceTools.getUserId();
            classId=appPreferenceTools.getClassId();
            type=appPreferenceTools.getType();

            APIClientProvider clientProvider=new APIClientProvider();
            APIInterface apiInterface2=clientProvider.getService();
            Call<List<Course>> listCall;

                if (type == 1) {
                    listCall = apiInterface2.getCourse(userId, 1);
                } else {
                    listCall = apiInterface2.getCourse(classId, 0);
                }

                ((HomeActivity) getActivity()).showProgressDialog();
                listCall.enqueue(new Callback<List<Course>>() {
                    @Override
                    public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                        Log.d("courseStatus", response.code() + "");
                        Log.d("courseErr", response.errorBody() + "");

                        ((HomeActivity) getActivity()).hideProgressDialog();
                        if (response.code() == 405) {
                            Toast.makeText(getContext(), "عدم دسترسی", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (response.isSuccessful()) {
                            courseList = response.body();
                            cursorAdapter = new CourseAdapter(getActivity(), courseList, userId, type, manager);
                            recyclerView.setAdapter(cursorAdapter);
                        }

                    }

                    @Override
                    public void onFailure(Call<List<Course>> call, Throwable t) {
                        ((HomeActivity) getActivity()).hideProgressDialog();
                        Toast.makeText(getContext(), "اتصال اینترنت را بررسی کنید.", Toast.LENGTH_LONG).show();
                        return;
                    }
                });
        }else{
            //cursorAdapter = new CourseAdapter(getActivity(), courseList, userId, type, manager);
            recyclerView.setAdapter(cursorAdapter);
        }
       //APIInterface apiInterface= APIClient.getClient(appPreferenceTools.getAccessToken()).create(APIInterface.class);

    }


    public void setData(FragmentManager manager){
        this.manager=manager;
    }

}
