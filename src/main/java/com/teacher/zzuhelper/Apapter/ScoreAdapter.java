package com.teacher.zzuhelper.Apapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teacher.zzuhelper.Mould.Course;
import com.teacher.zzuhelper.R;

import java.util.List;

/**
 * Created by Administrator on 2016/10/6.
 */
public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.CourseViewHolder> {
    private List<Course> courseList;
    private Context context;
    public ScoreAdapter(List<Course> courseList, Context context){
        this.courseList=courseList;
        this.context=context;
    }
    static class CourseViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView name,type,credit,score,gpa;
        public CourseViewHolder(final View itemView){
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.cCardView);
            name = (TextView)itemView.findViewById(R.id.cName);
            type = (TextView)itemView.findViewById(R.id.cType);
            credit = (TextView)itemView.findViewById(R.id.cCredit);
            score = (TextView)itemView.findViewById(R.id.cScore);
            gpa = (TextView)itemView.findViewById(R.id.cGpa);
        }
    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item_score,parent,false);
        CourseViewHolder cvh =new CourseViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(CourseViewHolder holder, int position) {
        holder.name.setText(courseList.get(position).getcName());
        holder.type.setText("修习类别："+courseList.get(position).getcType());
        holder.credit.setText("学分："+courseList.get(position).getcCredit());
        holder.score.setText("成绩："+courseList.get(position).getcScore());
        holder.gpa.setText("绩点："+courseList.get(position).getcGpa());
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }
}
