package com.teacher.zzuhelper.View;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teacher.zzuhelper.R;

/**
 * Created by Administrator on 2016/10/6.
 */
public class MyFragment extends Fragment {
    private TextView tv;
    private String name;

    public static MyFragment newInstance(String name){
        MyFragment fragment = new MyFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name",name);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_score,container,false);
        tv = (TextView) view.findViewById(R.id.fragment_tv);
        tv.setText(name);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText("我变了-" + name);
            }
        });
        return view;
    }
}
