package ir.aliprogramer.schoolhome.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import ir.aliprogramer.schoolhome.Activity.LoginActivity;
import ir.aliprogramer.schoolhome.AppPreferenceTools;
import ir.aliprogramer.schoolhome.Activity.HomeActivity;
import ir.aliprogramer.schoolhome.Model.MarkModel.Mark;
import ir.aliprogramer.schoolhome.Model.MarkModel.MarkResponse;
import ir.aliprogramer.schoolhome.R;
import ir.aliprogramer.schoolhome.Adapters.MarkAdapter;
import ir.aliprogramer.schoolhome.webService.APIClientProvider;
import ir.aliprogramer.schoolhome.webService.APIInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarkFragment extends Fragment {
    int studentId;
    int bookId;
    String className,bookName,studentName="";
    List<Mark>markList;
    RecyclerView recyclerView;
    FloatingActionButton btnAddMark;
    AppPreferenceTools appPreferenceTools;
    MarkAdapter markAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.mark_fragment,container,false);
        recyclerView=view.findViewById(R.id.mark_recycle);
        btnAddMark=view.findViewById(R.id.add_mark);
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

    @SuppressLint("RestrictedApi")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null){
            studentId=savedInstanceState.getInt("studentId");
            bookId=savedInstanceState.getInt("bookId");
            className=savedInstanceState.getString("className");
            bookName=savedInstanceState.getString("bookName");
            studentName=savedInstanceState.getString("studentName");
        }

        appPreferenceTools=new AppPreferenceTools(getContext());

        if(appPreferenceTools.getType()==0){
            btnAddMark.setVisibility(View.INVISIBLE);
        }

        String title="";
        title+=bookName;
        if(!className.equals(" ")) {
            title += " کلاس ";
            title += className;
        }
        if(!studentName.equals("")) {
            title+="\n";
            title += " آقای ";
            title += studentName;
        }else{
            title="نمرات کتاب ";
            title+=bookName;
        }
        ((HomeActivity)getActivity()).changeToolBarText(title);
        if(markList==null) {
            APIClientProvider clientProvider=new APIClientProvider();
            APIInterface apiInterface=clientProvider.getService();
            Call<List<Mark>>listMark=apiInterface.getMarks(bookId,studentId);
            ((HomeActivity) getActivity()).showProgressDialog();
            listMark.enqueue(new Callback<List<Mark>>() {

                @Override
                public void onResponse(Call<List<Mark>> call, Response<List<Mark>> response) {
                    ((HomeActivity) getActivity()).hideProgressDialog();
                    if (response.code() == 405 || response.code() == 401) {
                        Toast.makeText(getContext(), "لطفا مجدد وارد برنامه شوید.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (response.isSuccessful()) {

                        btnAddMark.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MarkDialog markDialog = new MarkDialog(getActivity());
                                markDialog.show();
                                Window window = markDialog.getWindow();
                                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            }
                        });

                        markList = response.body();
                        Collections.reverse(markList);
                        markAdapter = new MarkAdapter(getActivity(), markList);
                        recyclerView.setAdapter(markAdapter);
                    }
                }

                @Override
                public void onFailure(Call<List<Mark>> call, Throwable t) {
                    ((HomeActivity) getActivity()).hideProgressDialog();
                    Toast.makeText(getContext(), "اتصال اینترنت را بررسی کنید.", Toast.LENGTH_LONG).show();
                    return;
                }
            });
        }else{
            recyclerView.setAdapter(markAdapter);
        }
    }
public class MarkDialog extends Dialog implements View.OnClickListener{
    private Context context;
    TextInputLayout markL,descriptionL;
    TextInputEditText markInput,description;
    AppCompatButton ok;
    AppCompatButton no;

    public MarkDialog(@NonNull Context context) {
        super(context);
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.mark_dialog);

        markL=findViewById(R.id.mark_layout);
        descriptionL=findViewById(R.id.description_layout);
        markInput=findViewById(R.id.mark_alert);
        description=findViewById(R.id.description_dialog);
        ok=findViewById(R.id.ok);
        no=findViewById(R.id.no);
        ok.setOnClickListener(this);
        no.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.no){
            dismiss();
            return;
        }else{
            String mark=markInput.getText().toString().trim();
            String descriptionSt=description.getText().toString().trim();
            if(mark.isEmpty())
                markL.setError("لطفا نمره را وارد کنید.");
            if(descriptionSt.isEmpty())
                descriptionL.setError("لطفا توضیحاتی در مورد نمره وارد کنید.");
            if(mark.isEmpty() || descriptionSt.isEmpty())
                return;
            saveMark(Integer.parseInt(mark),descriptionSt,bookId,studentId);
            dismiss();
        }

    }
}

    private void saveMark(final int mark, final String descriptionSt, int bookId, int studentId) {
            //APIInterface apiInterface=APIClient.getClient(appPreferenceTools.getAccessToken()).create(APIInterface.class);
                APIClientProvider clientProvider=new APIClientProvider();
                APIInterface apiInterface=clientProvider.getService();
            Call<MarkResponse>listCall=apiInterface.setMark(bookId,studentId,mark,descriptionSt);
            listCall.enqueue(new Callback<MarkResponse>() {
                @Override
                public void onResponse(Call<MarkResponse> call, Response<MarkResponse> response) {
                    if(response.code()==405){
                        Toast.makeText(getContext(),"لطفا مجدد وارد برنامه شوید.",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getContext(),LoginActivity.class));
                        (getActivity()).finish();
                        return;
                    }
                    if(response.isSuccessful()){
                        Mark m=new Mark();
                        m.setId(response.body().getId());
                        m.setMark(mark);
                        m.setDescription(descriptionSt);
                        m.setMonth(response.body().getMonth());
                        m.setDay(response.body().getDay());
                        markList.add(0,m);
                       // Collections.reverse(markList);
                        markAdapter.updateAdapterData(markList);
                        Toast.makeText(getContext(),"نمره با موفقیت ثبت شد",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getContext(),"نمره ثبت نشد",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<MarkResponse> call, Throwable t) {
                    Toast.makeText(getContext(),"اتصال اینترنت را بررسی کنید.",Toast.LENGTH_LONG).show();
                    return;
                }
            });

    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("studentId",studentId);
        outState.putInt("bookId",bookId);
        outState.putString("className",className);
        outState.putString("bookName",bookName);
        outState.putString("studentName",studentName);
    }

    public void setData(int studentId, int bookId,String className,String bookName,String studentName){
        this.studentId=studentId;
        this.bookId=bookId;
        this.className=className;
        this.bookName=bookName;
        this.studentName=studentName;
    }
}
