package com.zaaach.citypicker.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zaaach.citypicker.R;
import com.zaaach.citypicker.adapter.decoration.GridItemDecoration;
import com.zaaach.citypicker.model.City;

import java.util.List;

/**
 * @Author: Bro0cL
 * @Date: 2018/2/5 12:06
 */
public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.BaseViewHolder> {
    private static final int VIEW_TYPE_CURRENT = 10;
    private static final int VIEW_TYPE_HOT     = 11;

    private Context mContext;
    private List<City> mData;
    private List<City> mHots;
    private InnerDismissListener mDismissListener;

    public CityListAdapter(Context context, List<City> data, List<City> hots) {
        this.mData = data;
        this.mContext = context;
        this.mHots = hots;
    }

    public void updateData(List<City> cities){
        this.mData = cities;
        notifyDataSetChanged();
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case VIEW_TYPE_CURRENT:
                view = LayoutInflater.from(mContext).inflate(R.layout.cp_list_item_current_layout, parent, false);
                return new CurrentViewHolder(view);
            case VIEW_TYPE_HOT:
                view = LayoutInflater.from(mContext).inflate(R.layout.cp_list_item_hot_layout, parent, false);
                return new HotViewHolder(view);
            default:
                view = LayoutInflater.from(mContext).inflate(R.layout.cp_list_item_default_layout, parent, false);
                return new DefaultViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (holder == null) return;
        if (holder instanceof DefaultViewHolder){
            final int pos = holder.getAdapterPosition();
            final City data = mData.get(pos);
            if (data == null) return;
            ((DefaultViewHolder)holder).name.setText(data.getName());
            ((DefaultViewHolder) holder).name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDismissListener != null){
                        mDismissListener.dismiss(pos, data.getName());
                    }
                }
            });
        }
        //定位城市
        if (holder instanceof CurrentViewHolder){
            final int pos = holder.getAdapterPosition();
            final City data = mData.get(pos);
            if (data == null) return;
            //设置宽高
            DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
            int screenWidth = dm.widthPixels;
            TypedValue typedValue = new TypedValue();
            mContext.getTheme().resolveAttribute(R.attr.cpGridItemSpace, typedValue, true);
            int space = mContext.getResources().getDimensionPixelSize(R.dimen.cp_grid_item_space);
            int padding = mContext.getResources().getDimensionPixelSize(R.dimen.cp_default_padding);
            int indexBarWidth = mContext.getResources().getDimensionPixelSize(R.dimen.cp_index_bar_width);
            int itemWidth = (screenWidth - padding - space * (GridListAdapter.SPAN_COUNT - 1) - indexBarWidth) / GridListAdapter.SPAN_COUNT;
            ViewGroup.LayoutParams lp = ((CurrentViewHolder) holder).container.getLayoutParams();
            lp.width = itemWidth;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            ((CurrentViewHolder) holder).container.setLayoutParams(lp);

            ((CurrentViewHolder) holder).current.setText(data.getName());
            ((CurrentViewHolder) holder).container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDismissListener != null){
                        mDismissListener.dismiss(pos, data.getName());
                    }
                }
            });
        }
        //热门城市
        if (holder instanceof HotViewHolder){
            final int pos = holder.getAdapterPosition();
            final City data = mData.get(pos);
            if (data == null) return;
            GridListAdapter mAdapter = new GridListAdapter(mContext, mHots);
            mAdapter.setInnerDismissListener(mDismissListener);
            ((HotViewHolder) holder).mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && TextUtils.equals("定", mData.get(position).getSection().substring(0, 1)))
            return VIEW_TYPE_CURRENT;
        if (position == 1 && TextUtils.equals("热", mData.get(position).getSection().substring(0, 1)))
            return VIEW_TYPE_HOT;
        return super.getItemViewType(position);
    }

    public void setInnerDismissListener(InnerDismissListener listener){
        this.mDismissListener = listener;
    }

    static class BaseViewHolder extends RecyclerView.ViewHolder{
        BaseViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class DefaultViewHolder extends BaseViewHolder{
        TextView name;

        DefaultViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cp_list_item_name);
        }
    }

    public static class HotViewHolder extends BaseViewHolder {
        RecyclerView mRecyclerView;

        HotViewHolder(View itemView) {
            super(itemView);
            mRecyclerView = itemView.findViewById(R.id.cp_hot_list);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new GridLayoutManager(itemView.getContext(),
                    GridListAdapter.SPAN_COUNT, LinearLayoutManager.VERTICAL, false));
            int space = itemView.getContext().getResources().getDimensionPixelSize(R.dimen.cp_grid_item_space);
            mRecyclerView.addItemDecoration(new GridItemDecoration(GridListAdapter.SPAN_COUNT,
                    space));
        }
    }

    public static class CurrentViewHolder extends BaseViewHolder {
        FrameLayout container;
        TextView current;

        CurrentViewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.cp_list_item_current_layout);
            current = itemView.findViewById(R.id.cp_list_item_current);
        }
    }
}
