package ir.aliprogramer.schoolhome.RecycleAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ir.aliprogramer.schoolhome.Fragments.MarkFragment;
import ir.aliprogramer.schoolhome.R;
import ir.aliprogramer.schoolhome.StudentModel.StudentResponse;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    Context context;
    List<StudentResponse>studentList;
    int bookId;
    String className,bookName;
    FragmentManager manager;

    public StudentAdapter(Context context, List<StudentResponse> studentList, int bookId, FragmentManager manager,String className,String bookName) {
        this.context = context;
        this.studentList = studentList;
        this.bookId = bookId;
        this.manager=manager;
        this.className=className;
        this.bookName=bookName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.student_recycle_view,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
            viewHolder.name.setText(studentList.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        AppCompatImageView navImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.student_name);
            navImg=itemView.findViewById(R.id.nav_img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            MarkFragment markFragment=new MarkFragment();
            markFragment.setData(studentList.get(getAdapterPosition()).getStudentId(),bookId,className,bookName,studentList.get(getAdapterPosition()).getName());
            FragmentTransaction transaction=manager.beginTransaction();
            transaction.replace(R.id.frame_layout,markFragment,"markFragment");
            transaction.addToBackStack("addmarkFragment");
            transaction.commit();
        }
    }
}
