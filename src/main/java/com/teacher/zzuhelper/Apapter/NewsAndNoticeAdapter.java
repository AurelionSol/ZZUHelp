package com.teacher.zzuhelper.Apapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teacher.zzuhelper.Activity.SeatActivity;
import com.teacher.zzuhelper.Mould.Course;
import com.teacher.zzuhelper.Mould.NewsAndNotice;
import com.teacher.zzuhelper.R;
import com.teacher.zzuhelper.Util.MyApplication;

import java.util.List;

/**
 * Created by Administrator on 2016/10/17.
 */
public class NewsAndNoticeAdapter extends RecyclerView.Adapter<NewsAndNoticeAdapter.NewsAndNoticeViewHolder> implements View.OnClickListener{
    private List<NewsAndNotice> newsAndNoticeList;
    private  OnRecyclerViewItemClickListener onRecyclerViewItemClickListener = null;
    private Context context;
    public NewsAndNoticeAdapter(List<NewsAndNotice> newsAndNoticeList, Context context){
        this.newsAndNoticeList=newsAndNoticeList;
        this.context=context;
    }
    public static class NewsAndNoticeViewHolder extends RecyclerView.ViewHolder  {
        CardView newsandnotice_cardview;
        TextView title,time;
        public NewsAndNoticeViewHolder(final View itemView){
            super(itemView);
            newsandnotice_cardview = (CardView)itemView.findViewById(R.id.newsandnotice_cardview);
            title = (TextView)itemView.findViewById(R.id.newsandnotice_title);
            time = (TextView)itemView.findViewById(R.id.newsandnotice_time);
        }


    }

    @Override
    public NewsAndNoticeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item_newsandnotice,parent,false);
        NewsAndNoticeViewHolder cvh =new NewsAndNoticeViewHolder(v);
        v.setOnClickListener(this);
        return cvh;
    }

    @Override
    public void onBindViewHolder(NewsAndNoticeViewHolder holder, int position) {
        holder.title.setText(newsAndNoticeList.get(position).getTitle());
        holder.time.setText(newsAndNoticeList.get(position).getTime());
        holder.itemView.setTag(newsAndNoticeList.get(position));
    }

    @Override
    public int getItemCount() {
        return newsAndNoticeList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , NewsAndNotice newsAndNotice);
    }

    @Override
    public void onClick(View v) {
        if (onRecyclerViewItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            onRecyclerViewItemClickListener.onItemClick(v,(NewsAndNotice)v.getTag());
        }
    }
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.onRecyclerViewItemClickListener = listener;
    }
}
