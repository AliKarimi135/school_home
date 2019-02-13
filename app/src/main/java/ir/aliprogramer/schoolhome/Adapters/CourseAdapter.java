package ir.aliprogramer.schoolhome.Adapters;

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
import ir.aliprogramer.schoolhome.Fragments.StudentFragment;
import ir.aliprogramer.schoolhome.R;
import ir.aliprogramer.schoolhome.TeachingModel.Course;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder>{
    Context context;

    int studentId,type;
    List<Course>CourseList;
    FragmentManager manager;
    public CourseAdapter(Context context, List<Course> courseList, int studentId, int type, FragmentManager manager) {
       this.context=context;
        this.type = type;
        this.studentId = studentId;
        CourseList = courseList;
        this.manager=manager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        if(type==1) {
            view = LayoutInflater.from(context).inflate(R.layout.course_recycle_view, viewGroup, false);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.course_recycle_view2, viewGroup, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bookName.setText(CourseList.get(i).getBookName());
        if(type==1)
            viewHolder.className.setText(CourseList.get(i).getClassName());
    }

    @Override
    public int getItemCount() {
        return CourseList.size();
    }

    public void updateAdapterData(List<Course> data) {
        this.CourseList = data;
        this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView bookName;
        TextView className;
        AppCompatImageView navImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookName=itemView.findViewById(R.id.book_name);
            navImg=itemView.findViewById(R.id.nav_img);
            if(type==1) {
                className=itemView.findViewById(R.id.class_name);
                className.setVisibility(View.VISIBLE);
            }
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //if user is teacher get student list else student mark
            if(type==1){
                //sent   class_id and Group_id and bookId to find students
                int classId=CourseList.get(getAdapterPosition()).getClassId();
                int groupId=CourseList.get(getAdapterPosition()).getGroupId();
                int bookId=CourseList.get(getAdapterPosition()).getBookId();
                StudentFragment studentFragment=new StudentFragment();
                studentFragment.setData(classId,groupId,bookId,manager,CourseList.get(getAdapterPosition()).getClassName(),CourseList.get(getAdapterPosition()).getBookName());
                FragmentTransaction transaction=manager.beginTransaction();
                transaction.replace(R.id.frame_layout,studentFragment,"studentFragment");
                transaction.addToBackStack("addstudentFragment");
                transaction.commit();

            }else {
                //send studentId and bookId to find mark students
                MarkFragment markFragment=new MarkFragment();
                markFragment.setData(studentId,CourseList.get(getAdapterPosition()).getBookId()," ",CourseList.get(getAdapterPosition()).getBookName(),"");
                FragmentTransaction transaction=manager.beginTransaction();
                transaction.replace(R.id.frame_layout,markFragment,"markFragment");
                transaction.addToBackStack("addmarkFragment");
                transaction.commit();

            }
        }
    }
}
