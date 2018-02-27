package com.zaaach.citypicker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zaaach.citypicker.adapter.CityListAdapter;
import com.zaaach.citypicker.adapter.InnerDismissListener;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.adapter.decoration.DividerItemDecoration;
import com.zaaach.citypicker.adapter.decoration.SectionItemDecoration;
import com.zaaach.citypicker.db.DBManager;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.view.SideIndexBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: Bro0cL
 * @Date: 2018/2/6 20:50
 */
public class CityPickerDialogFragment extends AppCompatDialogFragment implements TextWatcher,
        View.OnClickListener, SideIndexBar.OnIndexTouchedChangedListener, InnerDismissListener {
    private View mContentView;
    private RecyclerView mRecyclerView;
    private View mEmptyView;
    private TextView mOverlayTextView;
    private SideIndexBar mIndexBar;
    private EditText mSearchBox;
    private TextView mCancelBtn;
    private ImageView mClearAllBtn;

    private LinearLayoutManager mLayoutManager;
    private CityListAdapter mAdapter;
    private List<City> mAllCities;
    private List<City> mHotCities;
    private List<City> mResults;

    private DBManager dbManager;

    private int mAnimStyle;
    private String mCurrentCity;
    private List<String> mHots;
    private OnPickListener mOnPickListener;

    private static final String[] DEFAULT_HOT_CITIES = {"北京", "上海", "广州", "深圳", "杭州", "南京", "武汉", "福州", "厦门"};

    /**
     * 获取实例
     * @param animStyle 动画效果
     * @param current 当前定位城市
     * @param list 热门城市
     * @return
     */
    public static CityPickerDialogFragment newInstance(@StyleRes int animStyle, String current, ArrayList<String> list){
        final CityPickerDialogFragment fragment = new CityPickerDialogFragment();
        Bundle args = new Bundle();
        args.putInt("cp_anim_style", animStyle);
        args.putString("cp_current", current);
        args.putStringArrayList("cp_hots", list);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.CityPickerStyle);

        Bundle args = getArguments();
        if (args != null) {
            mAnimStyle = args.getInt("cp_anim_style");
            mCurrentCity = args.getString("cp_current");
            mHots = args.getStringArrayList("cp_hots");
        }
        if (mAnimStyle <= 0)
            mAnimStyle = R.style.DefaultCityPickerAnimation;
        if (TextUtils.isEmpty(mCurrentCity))
            mCurrentCity = getString(R.string.cp_locate_failed);
        if (mHots == null || mHots.isEmpty())
            mHots = Arrays.asList(DEFAULT_HOT_CITIES);
        initHotCities(mHots);

        dbManager = new DBManager(getContext());
        mAllCities = dbManager.getAllCities();
        mAllCities.add(0, new City(mCurrentCity, "定位城市"));
        mAllCities.add(1, new City(mHots.get(0), "热门城市"));
        mResults = mAllCities;
    }

    private void initHotCities(List<String> list) {
        List<City> hots = new ArrayList<>();
        for (String name : list) {
            City city = new City(name, "热门城市");
            hots.add(city);
        }
        mHotCities = new ArrayList<>();
        mHotCities.addAll(hots);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.cp_dialog_city_picker, container, false);

        mRecyclerView = mContentView.findViewById(R.id.cp_city_recyclerview);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new SectionItemDecoration(getActivity(), mAllCities), 0);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()), 1);
        mAdapter = new CityListAdapter(getActivity(), mAllCities, mHotCities);
        mAdapter.setInnerDismissListener(this);
        mRecyclerView.setAdapter(mAdapter);

        mEmptyView = mContentView.findViewById(R.id.cp_empty_view);
        mOverlayTextView = mContentView.findViewById(R.id.cp_overlay);

        mIndexBar = mContentView.findViewById(R.id.cp_side_index_bar);
        mIndexBar.setOverlayTextView(mOverlayTextView)
                .setOnIndexChangedListener(this);

        mSearchBox = mContentView.findViewById(R.id.cp_search_box);
        mSearchBox.addTextChangedListener(this);

        mCancelBtn = mContentView.findViewById(R.id.cp_cancel);
        mClearAllBtn = mContentView.findViewById(R.id.cp_clear_all);
        mCancelBtn.setOnClickListener(this);
        mClearAllBtn.setOnClickListener(this);

        Window window = getDialog().getWindow();
        if(window != null) {
            window.getDecorView().setPadding(0, 0, 0, 0);
            window.setBackgroundDrawableResource(android.R.color.transparent);
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            window.setWindowAnimations(mAnimStyle);
        }

        return mContentView;
    }

    /** 搜索框监听 */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        String keyword = s.toString();
        if (TextUtils.isEmpty(keyword)){
            mClearAllBtn.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.GONE);
            mResults = mAllCities;
            ((SectionItemDecoration)(mRecyclerView.getItemDecorationAt(0))).setData(mResults);
            mAdapter.updateData(mResults);
        }else {
            mClearAllBtn.setVisibility(View.VISIBLE);
            //开始数据库查找
            mResults = dbManager.searchCity(keyword);
            ((SectionItemDecoration)(mRecyclerView.getItemDecorationAt(0))).setData(mResults);
            if (mResults == null || mResults.isEmpty()){
                mEmptyView.setVisibility(View.VISIBLE);
            }else {
                mEmptyView.setVisibility(View.GONE);
                mAdapter.updateData(mResults);
            }
        }
        mRecyclerView.scrollToPosition(0);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.cp_cancel) {
            dismiss();
        }else if(id == R.id.cp_clear_all){
            mSearchBox.setText("");
        }
    }

    @Override
    public void onIndexChanged(String index, int position) {
        //滚动RecyclerView到索引位置
        if (mResults == null || mResults.isEmpty()) return;
        if (TextUtils.isEmpty(index)) return;
        int size = mResults.size();
        for (int i = 0; i < size; i++) {
            if (TextUtils.equals(index.substring(0, 1), mResults.get(i).getSection().substring(0, 1))){
                if (mLayoutManager != null){
                    mLayoutManager.scrollToPositionWithOffset(i, 0);
                    return;
                }
            }
        }
    }

    @Override
    public void dismiss(int position, String data) {
        dismiss();
        if (mOnPickListener != null){
            mOnPickListener.onPick(position, data);
        }
    }

    public void setOnPickListener(OnPickListener listener){
        this.mOnPickListener = listener;
    }
}
