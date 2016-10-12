package com.meowapp.testweather;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.meowapp.testweather.bean.ForecastBean;
import com.meowapp.testweather.utils.FontManager;

import java.util.List;

/**
 * Created by Maggie on 10/11/2016.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {

    List<ForecastBean.UsefulInfo> list;

    public RVAdapter(List<ForecastBean.UsefulInfo> usefulInfos) {
        list = usefulInfos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ForecastBean.UsefulInfo info = list.get(position);
        holder.tv_desc.setText(info.description);
        holder.tv_temp.setText(info.temp);
        holder.tv_day.setText(info.dayOfWeek);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_day;
        public TextView tv_desc;
        public TextView tv_temp;

        public ViewHolder(View view) {
            super(view);
            tv_day = (TextView) view.findViewById(R.id.tv_day);
            tv_desc = (TextView) view.findViewById(R.id.tv_desc);
            tv_temp = (TextView) view.findViewById(R.id.tv_temp);

            // customize font
            FontManager.TYPEFACE.setNanoSansTypeface(tv_day);
            FontManager.TYPEFACE.setNanoSansTypeface(tv_temp);
            FontManager.TYPEFACE.setNanoSansTypeface(tv_desc);
        }
    }
}
