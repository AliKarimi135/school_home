package ir.aliprogramer.schoolhome.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ir.aliprogramer.schoolhome.Activity.HomeActivity;
import ir.aliprogramer.schoolhome.Activity.LoginActivity;
import ir.aliprogramer.schoolhome.AppPreferenceTools;
import ir.aliprogramer.schoolhome.Model.MarkModel.Mark;
import ir.aliprogramer.schoolhome.R;
import ir.aliprogramer.schoolhome.webService.APIClientProvider;
import ir.aliprogramer.schoolhome.webService.APIInterface;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarkAdapter extends RecyclerView.Adapter<MarkAdapter.ViewHolder> {
    Context context;
    List<Mark>markList;
    AppPreferenceTools appPreferenceTools;
    String StMonth[]={" ","فروردین","اردیبیهشت","خرداد","تیر","مرداد","شهریور","مهر","آبان","آذر","دی","بهمن","اسفند"};
    public MarkAdapter(Context context, List<Mark> markList) {
        this.context = context;
        this.markList = markList;
        appPreferenceTools=new AppPreferenceTools(context);
    }
    public void updateAdapterData(List<Mark> data) {
        this.markList = data;
        this.notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.mark_recycle_view,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.mark1.setText(markList.get(i).getMark()+" ");
        viewHolder.description.setText(markList.get(i).getDescription());
        viewHolder.day.setText(markList.get(i).getDay()+" ");
        viewHolder.month.setText(StMonth[markList.get(i).getMonth()]);
    }

    @Override
    public int getItemCount() {
        return markList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mark1,description,day,month;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mark1=itemView.findViewById(R.id.mark1);
            description=itemView.findViewById(R.id.description);
            day=itemView.findViewById(R.id.day);
            month=itemView.findViewById(R.id.month);
            if(appPreferenceTools.getType()==1) {
                itemView.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View view) {
            MarkDialog markDialog=new MarkDialog(context,markList.get(getAdapterPosition()).getId(),markList.get(getAdapterPosition()).getMark(),markList.get(getAdapterPosition()).getDescription(),getAdapterPosition());
            markDialog.show();
            Window window = markDialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

    }
    public class MarkDialog extends Dialog implements View.OnClickListener{
        private Context context;
        TextInputLayout markL,descriptionL;
        TextInputEditText markInput,description;
        AppCompatButton edith;
        AppCompatButton delete;
        AppCompatButton no;
        int id,mark,Itemposition;
        String desc;//description
        public MarkDialog(@NonNull Context context,int id,int mark,String desc,int Itemposition) {
            super(context);
            this.context=context;
            this.id=id;
            this.mark=mark;
            this.desc=desc;
            this.Itemposition=Itemposition;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.customize_mark_dialog);

            markL=findViewById(R.id.mark_layout);
            descriptionL=findViewById(R.id.description_layout);
            markInput=findViewById(R.id.mark2);
            description=findViewById(R.id.description_dialog);
            edith=findViewById(R.id.edith);
            delete=findViewById(R.id.delete);
            markInput.setText(mark+"");
            description.setText(desc);
            no=findViewById(R.id.no);
            edith.setOnClickListener(this);
            delete.setOnClickListener(this);
            no.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //APIInterface apiInterface= APIClient.getClient(appPreferenceTools.getAccessToken()).create(APIInterface.class);
            APIClientProvider clientProvider=new APIClientProvider();
            APIInterface apiInterface=clientProvider.getService();
            if(view.getId()==R.id.no){
                dismiss();
                return;
            }else if(view.getId()==R.id.delete) {
                deleteMark(apiInterface,id,Itemposition);
                dismiss();
            }else{
                String mark=markInput.getText().toString().trim();
                String descriptionSt=description.getText().toString().trim();
                if(mark.isEmpty())
                    markL.setError("لطفا نمره را وارد کنید.");
                if(descriptionSt.isEmpty())
                    descriptionL.setError("لطفا توضیحاتی در مورد نمره وارد کنید.");
                if(mark.isEmpty() || descriptionSt.isEmpty())
                    return;
                if(mark.equals(markList.get(Itemposition).getMark()+"") && descriptionSt.equals(markList.get(Itemposition).getDescription())){
                    dismiss();
                    return;
                }
                updateMark(apiInterface,id,Integer.parseInt(mark),descriptionSt,Itemposition);
                dismiss();
                }
            }

        }

    private void updateMark(APIInterface apiInterface, int id, final int mark, final String descriptionSt, final int Itemposition) {
        Call<ResponseBody>bodyCall=apiInterface.updateMark(id,mark,descriptionSt);
        bodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code()==405 || response.code() == 401){
                    Toast.makeText(context,"لطفا مجدد وارد برنامه شوید.",Toast.LENGTH_LONG).show();
                    (context).startActivity(new Intent(context,LoginActivity.class));
                    ((Activity)context).finish();
                    return;
                }
              if(response.isSuccessful()){
                    Mark m=markList.get(Itemposition);
                    m.setMark(mark);
                    m.setDescription(descriptionSt);
                    markList.remove(Itemposition);
                    markList.add(Itemposition,m);
                    updateAdapterData(markList);
                    Toast.makeText(context,"ویرایش نمره با موفقیت انجام شد.",Toast.LENGTH_LONG).show();
                }else {
                  Toast.makeText(context,"ویرایش نمره انجام نشد.",Toast.LENGTH_LONG).show();
              }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context,"اتصال اینترنت را بررسی کنید.",Toast.LENGTH_LONG).show();
                return;
            }
        });

    }

    private void deleteMark(APIInterface apiInterface, int id,final int Itemposition) {
       Call<ResponseBody> bodyCall=apiInterface.destroyMark(id);
        bodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code()==405 || response.code() == 401){
                    Toast.makeText(context,"لطفا مجدد وارد برنامه شوید.",Toast.LENGTH_LONG).show();
                    (context).startActivity(new Intent(context,LoginActivity.class));
                    ((Activity)context).finish();
                    return;
                }
                if(response.isSuccessful()){
                    markList.remove(Itemposition);
                    updateAdapterData(markList);
                    Toast.makeText(context,"نمره با موفقیت حذف شد",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(context,"نمره حذف نشد",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context,"اتصال اینترنت را بررسی کنید.",Toast.LENGTH_LONG).show();
                return;
            }
        });
    }


}
