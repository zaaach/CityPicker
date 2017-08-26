package com.zaaach.citypicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.zaaach.citypicker.adapter.CityListAdapter;
import com.zaaach.citypicker.adapter.ResultListAdapter;
import com.zaaach.citypicker.db.DBManager;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.view.SideLetterBar;

import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * 功能：//外部使用fragment就行了
 * 作者：oliver
 * 日期：2017/8/8
 * 邮箱：qiwei0727@163.com
 */
public class CityPickerFragment extends Fragment implements View.OnClickListener {

    public static final String KEY_PICKED_CITY = "picked_city";

    //城市选择
    public static final int REQUEST_CODE_PICK_CITY = 2;

    private ListView mListView;

    private ListView mResultListView;

    private SideLetterBar mLetterBar;

    private EditText searchBox;

    private ImageView clearBtn;
    //    private ImageView backBtn;
    private ViewGroup emptyView;

    private ResultListAdapter mResultAdapter;

    private List<City> mAllCities;

    private CityListAdapter mCityAdapter;

    private DBManager dbManager;

    View mRootView;
    /**
     * 更新定位状态
     *
     * @param state
     * @param city
     */
    public void updateLocateState(int state, String city) {
        mCityAdapter.updateLocateState(state, city);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.cp_fragment_city_list, container, false);

        initData();
        initView();
        return mRootView;
    }

    private void initData() {
        dbManager = new DBManager(getActivity());
        dbManager.copyDBFile();
        mAllCities = dbManager.getAllCities();
        mCityAdapter = new CityListAdapter(getActivity(), mAllCities);
        mCityAdapter.setOnCityClickListener(new CityListAdapter.OnCityClickListener() {
            @Override
            public void onCityClick(String name) {
                back(name);
            }

            @Override
            public void onLocateClick() {
                mCityAdapter.updateLocateState(LocateState.LOCATING, null);
//                mLocationClient.startLocation();
            }
        });

        mResultAdapter = new ResultListAdapter(getActivity(), null);
    }

    private void initView() {
        mListView = (ListView) mRootView.findViewById(R.id.listview_all_city);
        mListView.setAdapter(mCityAdapter);

        TextView overlay = (TextView) mRootView.findViewById(R.id.tv_letter_overlay);
        mLetterBar = (SideLetterBar) mRootView.findViewById(R.id.side_letter_bar);
        mLetterBar.setOverlay(overlay);
        mLetterBar.setOnLetterChangedListener(new SideLetterBar.OnLetterChangedListener() {
            @Override
            public void onLetterChanged(String letter) {
                int position = mCityAdapter.getLetterPosition(letter);
                mListView.setSelection(position);
            }
        });

        searchBox = (EditText) mRootView.findViewById(R.id.et_search);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString();
                if (TextUtils.isEmpty(keyword)) {
                    clearBtn.setVisibility(View.GONE);
                    emptyView.setVisibility(View.GONE);
                    mResultListView.setVisibility(View.GONE);
                } else {
                    clearBtn.setVisibility(View.VISIBLE);
                    mResultListView.setVisibility(View.VISIBLE);
                    List<City> result = dbManager.searchCity(keyword);
                    if (result == null || result.size() == 0) {
                        emptyView.setVisibility(View.VISIBLE);
                    } else {
                        emptyView.setVisibility(View.GONE);
                        mResultAdapter.changeData(result);
                    }
                }
            }
        });

        emptyView = (ViewGroup) mRootView.findViewById(R.id.empty_view);
        mResultListView = (ListView) mRootView.findViewById(R.id.listview_search_result);
        mResultListView.setAdapter(mResultAdapter);
        mResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                back(mResultAdapter.getItem(position).getName());
            }
        });

        clearBtn = (ImageView) mRootView.findViewById(R.id.iv_search_clear);

        clearBtn.setOnClickListener(this);

    }

    private void back(String city) {
        Intent data = new Intent();
        data.putExtra(KEY_PICKED_CITY, city);
        getActivity().setResult(RESULT_OK, data);
        getActivity().finish();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.iv_search_clear) {
            searchBox.setText("");
            clearBtn.setVisibility(View.GONE);
            emptyView.setVisibility(View.GONE);
            mResultListView.setVisibility(View.GONE);
        }
    }


}
