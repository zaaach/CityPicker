package com.zaaach.citypicker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.zaaach.citypicker.adapter.CityListAdapter;
import com.zaaach.citypicker.adapter.ResultListAdapter;
import com.zaaach.citypicker.db.DBManager;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.utils.ExtractLocationUtils;
import com.zaaach.citypicker.view.SideLetterBar;

import java.util.List;

/**
 * author zaaach on 2016/1/26.
 */
public class CityPickerFragment extends Fragment {
    public static final int REQUEST_CODE_PICK_CITY = 2333;
    public static final String KEY_PICKED_CITY = "picked_city";

    private ListView mListView;
    private ListView mResultListView;
    private SideLetterBar mLetterBar;
    private EditText searchBox;
    private ImageView clearBtn;
    private ImageView backBtn;
    private ViewGroup emptyView;

    private CityListAdapter mCityAdapter;
    private ResultListAdapter mResultAdapter;
    private List<City> mAllCities;
    private DBManager dbManager;
    protected View mRootView;
    private AMapLocationClient mLocationClient;
    private OnCityChoseListener onCityChoseListener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_city_list, container, false);
        initData();
        initView();
        initLocation();
        return mRootView;
    }

    private void initLocation() {
        mLocationClient = new AMapLocationClient(getContext().getApplicationContext());
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        option.setOnceLocation(true);
        mLocationClient.setLocationOption(option);
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
                        String city = aMapLocation.getCity();
                        String district = aMapLocation.getDistrict();
                        Log.e("onLocationChanged", "city: " + city);
                        Log.e("onLocationChanged", "district: " + district);
                        String location = ExtractLocationUtils.extractLocation(city, district);
                        mCityAdapter.updateLocateState(LocateState.SUCCESS, location);
                    } else {
                        //定位失败
                        mCityAdapter.updateLocateState(LocateState.FAILED, null);
                        Log.e("onLocationChanged: ", "errorCode: " + aMapLocation.getErrorCode());
                    }
                }
            }

        });
        mLocationClient.startLocation();
    }

    private void initData() {
        dbManager = new DBManager(getContext());
        dbManager.copyDBFile();
        mAllCities = dbManager.getAllCities();
        mCityAdapter = new CityListAdapter(getContext(), mAllCities);
        mCityAdapter.setOnCityClickListener(new CityListAdapter.OnCityClickListener() {
            @Override
            public void onCityClick(String name) {
                onCityChoseListener.Onclick(name);

            }

            @Override
            public void onLocateClick() {
                Log.e("onLocateClick", "重新定位...");
                mCityAdapter.updateLocateState(LocateState.LOCATING, null);
                mLocationClient.startLocation();
            }
        });

        mResultAdapter = new ResultListAdapter(getContext(), null);
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
                onCityChoseListener.Onclick(mResultAdapter.getItem(position).getName());
            }
        });

        clearBtn = (ImageView) mRootView.findViewById(R.id.iv_search_clear);
        backBtn = (ImageView) mRootView.findViewById(R.id.back);

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchBox.setText("");
                clearBtn.setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);
                mResultListView.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stopLocation();
    }

    public void setOnCityChoseListener(OnCityChoseListener onCityChoseListener) {
        this.onCityChoseListener = onCityChoseListener;
    }

    public interface OnCityChoseListener {
        void Onclick(String city);
    }

}
