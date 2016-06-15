package com.mintcode.launchr.app.place;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.mintcode.launchr.R;
import com.mintcode.launchr.base.BaseActivity;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.util.Const;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

//import com.baidu.location.BDLocation;
//import com.baidu.location.BDLocationListener;
//import com.baidu.location.LocationClient;
//import com.baidu.location.LocationClientOption;
//import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
//import com.baidu.mapapi.search.sug.SuggestionResult;
//import com.baidu.mapapi.search.sug.SuggestionSearch;
//import com.baidu.mapapi.search.sug.SuggestionSearchOption;

public class AddPlacesActivity extends BaseActivity implements
        OnItemClickListener,
        OnKeyListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {


    private LayoutInflater mInflater;
    /**
     * 输入框
     */
    @Bind(R.id.et_search)
    protected EditText mEditInput;
    /**
     * 搜索结果列表
     */
    @Bind(R.id.lv_map_address_info)
    protected ListView mLvResult;
    private ResultAdapter mAdapter;

    //百度相关
//	private LocationClient mBaiduLocalClient;
//	/** 查询地址类*/
//	private SuggestionSearch mSuggestionSearch;
    //Google相关
    private GoogleApiClient mGoogleApiClient;

    /**
     * 当前坐标long
     */
    private double mCurrentLongitude;
    /**
     * 当前坐标lat
     */
    private double mCurrentLatitude;
    /**
     * 当前地址
     */
    private String mCurrentAddress;
    /**
     * 地点
     */
    private String mAddress;
    /**
     * 当前城市
     */
    private String mCurrentCity;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_places);
        ButterKnife.bind(this);
        initData();
    }

    /**
     * 实例化数据
     */
    private void initData() {
        mInflater = LayoutInflater.from(AddPlacesActivity.this);
        mAdapter = new ResultAdapter();
        mLvResult.setAdapter(mAdapter);
        mLvResult.setOnItemClickListener(this);
        mAddress = getIntent().getStringExtra(Const.ADDRESS);
        if (mAddress != null) {
            mEditInput.setText(mAddress);
        }
        mEditInput.addTextChangedListener(watcher);
        initMapData();
    }

    private void initMapData() {
        if (LauchrConst.DEV_CODE == LauchrConst.MT) {
            initBaiduMapData();
        } else if (LauchrConst.DEV_CODE == LauchrConst.JP) {
            initGoogleMapData();
        }
    }

    /**
     * Google服务初始化
     */
    private void initGoogleMapData() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .build();
        }
    }

    /**
     * 百度服务初始化
     */
    private void initBaiduMapData() {
//		mSuggestionSearch = SuggestionSearch.newInstance();
//		mSuggestionSearch.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {
//			@Override
//			public void onGetSuggestionResult(SuggestionResult res) {
//				if (res == null || res.getAllSuggestions() == null) {
//					return;
//					//未找到相关结果
//				}
//				String keyWord = mEditInput.getText().toString();
//				//获取在线建议检索结果
//				List<SuggestionResult.SuggestionInfo> mResultData;
//				if(res.getAllSuggestions() != null && keyWord!=null && !keyWord.equals("")){
//					mResultData = res.getAllSuggestions();
//					for(int i=0; i<mResultData.size(); i++){
//						SuggestionResult.SuggestionInfo info = mResultData.get(i);
//						if(info==null || info.city==null || info.district==null || info.city.equals("") || info.district.equals("")){
//							mResultData.remove(i);
//							i -= 1;
//						}
//					}
//					mAdapter.setBaiduResult(mResultData);
//				}else{
//					mAdapter.setBaiduResult(null);
//				}
//			}
//		});
//		initLocation();
    }

    private TextWatcher watcher = new TextWatcher() {

        private String str = "";
        Runnable r = new Runnable() {

            @Override
            public void run() {
                Log.i("keyWord", str + "");
                if (LauchrConst.DEV_CODE == LauchrConst.MT) {
                    baiduSearch(str);
                } else if (LauchrConst.DEV_CODE == LauchrConst.JP) {
                    googleSearch(str);
                }
            }
        };

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String word = mEditInput.getText().toString();
            if (word != null && !word.equals("")) {
                str = s.toString().trim();
                mHandler.removeCallbacks(r);
                mHandler.postDelayed(r, 500);
            } else {
                mAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void googleSearch(String word) {
        PendingResult<AutocompletePredictionBuffer> result = Places.GeoDataApi.getAutocompletePredictions(mGoogleApiClient, word, null, null);
        if (result != null) {
            result.setResultCallback(new ResultCallback<AutocompletePredictionBuffer>() {
                @Override
                public void onResult(@NonNull AutocompletePredictionBuffer autocompletePredictions) {
                    if (autocompletePredictions.getStatus().isSuccess()) {
                        AutocompletePredictionBuffer mAutocompleteList = autocompletePredictions;
                        Iterator<AutocompletePrediction> iterator = autocompletePredictions.iterator();
                        while (iterator.hasNext()) {
                            AutocompletePrediction prediction = iterator.next();
                            // Get the details of this prediction and copy it into a new PlaceAutocomplete object.
                            Log.i("Log.i-Prediction Result", prediction.getPlaceId() + "---" + prediction.getDescription()
                                    + "--------\n"
                                    + prediction.getFullText(null) + "\n"
                                    + prediction.getPrimaryText(null) + "\n"
                                    + prediction.getSecondaryText(null) + "\n"
                            );
                        }
                        mAdapter.setGoogleResult(mAutocompleteList);
                        autocompletePredictions.release();
                    }
                }
            });
        }
    }

    private void baiduSearch(String word) {
//		if(mCurrentCity != null){
//			mSuggestionSearch.requestSuggestion((new SuggestionSearchOption()).keyword(word).city(mCurrentCity));
//		}else{
//			mSuggestionSearch.requestSuggestion((new SuggestionSearchOption()).keyword(word).city(""));
//		}

    }

    @OnClick(R.id.tv_clear)
    protected void clearText() {
        mEditInput.setText("");
    }

    @OnClick(R.id.tv_back)
    protected void Back() {
        onBackPressed();
    }

    @OnClick(R.id.tv_save)
    protected void Save() {
        Intent in = new Intent();
        String keyWOrd = mEditInput.getText().toString();

        if (!keyWOrd.equals("")) {
            in.putExtra(Const.STR_PLACE, keyWOrd);
        } else {
            toast(getString(R.string.calendar_place_toast));
            return;
        }
        setResult(RESULT_OK, in);
        finish();
    }

    /**
     * 返回当前位置
     */
    @OnClick(R.id.tv_current_place)
    protected void chooseCurrentPlace() {
        Intent intent = new Intent();
        intent.putExtra(Const.STR_PLACE, mCurrentAddress);
        intent.putExtra(Const.LATITUDE, mCurrentLatitude + "");
        intent.putExtra(Const.LONGITUDE, mCurrentLongitude + "");
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent == mLvResult) {
            if (LauchrConst.DEV_CODE == LauchrConst.MT) {
//				SuggestionResult.SuggestionInfo info = (SuggestionResult.SuggestionInfo)mAdapter.getItem(position);
//				Intent intent = new Intent();
//				intent.putExtra(Const.STR_PLACE, info.key);
//				intent.putExtra(Const.LATITUDE, info.pt.latitude + "");
//				intent.putExtra(Const.LONGITUDE, info.pt.longitude + "");
//				setResult(RESULT_OK, intent);
//				finish();
            } else if (LauchrConst.DEV_CODE == LauchrConst.JP) {
                final AutocompletePrediction info = (AutocompletePrediction) mAdapter.getItem(position);
                Places.GeoDataApi.getPlaceById(mGoogleApiClient, info.getPlaceId())
                        .setResultCallback(new ResultCallback<PlaceBuffer>() {
                            @Override
                            public void onResult(@NonNull PlaceBuffer places) {
                                Place p = places.get(0);
                                LatLng pl = p.getLatLng();
                                Intent intent = new Intent();
                                intent.putExtra(Const.STR_PLACE, info.getFullText(null));
                                intent.putExtra(Const.LATITUDE, pl.latitude + "");
                                intent.putExtra(Const.LONGITUDE, pl.longitude + "");
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        });
            }

        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            return true;
        }
        return false;
    }


    private class ResultAdapter extends BaseAdapter {

        //		private  List<SuggestionResult.SuggestionInfo> mBaiduResult;
        private AutocompletePredictionBuffer mGoogleResult;

        //		public void setBaiduResult(List<SuggestionResult.SuggestionInfo> result){
//			mBaiduResult = result;
//			notifyDataSetChanged();
//		}
        public void setGoogleResult(AutocompletePredictionBuffer result) {
            mGoogleResult = result;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (LauchrConst.DEV_CODE == LauchrConst.MT) {
//				return mBaiduResult == null?0:mBaiduResult.size();
                return 0;
            } else if (LauchrConst.DEV_CODE == LauchrConst.JP) {
                return mGoogleResult == null ? 0 : mGoogleResult.getCount();
//				return 0;
            } else {
                return 0;
            }

        }

        @Override
        public Object getItem(int position) {
            if (LauchrConst.DEV_CODE == LauchrConst.MT) {
//				return mBaiduResult == null ? null : mBaiduResult.get(position);
                return 0;
            } else if (LauchrConst.DEV_CODE == LauchrConst.JP) {
                return mGoogleResult == null ? null : mGoogleResult.get(position);
//				return 0;
            } else {
                return 0;
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.item_map_search_result, null);
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tvAddress = (TextView) convertView.findViewById(R.id.tv_address);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (LauchrConst.DEV_CODE == LauchrConst.MT) {
                baiduResultItem(holder, position);

            } else if (LauchrConst.DEV_CODE == LauchrConst.JP) {
                googleResultItem(holder, position);
            }

            return convertView;
        }

        /**
         * 加载google数据
         */
        private void googleResultItem(ViewHolder holder, int position) {
            AutocompletePrediction prediction = mGoogleResult.get(position);
            holder.tvName.setText(prediction.getPrimaryText(null));
            holder.tvAddress.setText(prediction.getSecondaryText(null));
        }

        /**
         * 加载百度数据
         */
        private void baiduResultItem(ViewHolder holder, int position) {
//			SuggestionResult.SuggestionInfo info = mBaiduResult.get(position);
//			holder.tvName.setText(info.key);
//			holder.tvAddress.setText(info.city + info.district);
        }

        private class ViewHolder {
            TextView tvName;
            TextView tvAddress;
        }
    }


    private void initLocation() {
        // 定位初始化
//		mBaiduLocalClient = new LocationClient(getApplicationContext());
//		mBaiduLocalClient.registerLocationListener(new BDLocationListener() {
//			@Override
//			public void onReceiveLocation(BDLocation location) {
//				if (location == null ) {
//					return;
//				}
//				mCurrentCity = location.getCity();
//				mCurrentLongitude = location.getLongitude();
//				mCurrentLatitude = location.getLatitude();
//				mCurrentAddress = location.getAddrStr();
//			}
//		});
//		LocationClientOption option = new LocationClientOption();
//		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
//		);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
//		option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
//		int span=1000;
//		option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
//		option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
////		option.setOpenGps(true);//可选，默认false,设置是否使用gps
//		option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
//		option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
//		option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
////		option.setIgnoreKillProcess(false);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
//		option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
//		option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
//		mBaiduLocalClient.setLocOption(option);
//		mBaiduLocalClient.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (LauchrConst.DEV_CODE == LauchrConst.JP) {
            if (mGoogleApiClient != null) {
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (LauchrConst.DEV_CODE == LauchrConst.JP) {
            if (mGoogleApiClient != null) {
                mGoogleApiClient.disconnect();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (LauchrConst.DEV_CODE == LauchrConst.MT) {
            // 退出时销毁定位
//			mBaiduLocalClient.stop();
            //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
//			mSuggestionSearch.destroy();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            mCurrentLatitude = mLastLocation.getLatitude();
            mCurrentLongitude = mLastLocation.getLongitude();
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(mCurrentLatitude, mCurrentLongitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses != null) {
                String cityName = addresses.get(0).getAddressLine(0);
                String stateName = addresses.get(0).getAddressLine(1);
                String countryName = addresses.get(0).getAddressLine(2);
                mCurrentAddress = cityName + stateName + countryName;
                Log.i("Log.i-enableMyLocation", "getMyLocation    " + cityName + "-" + stateName + "-" + countryName);
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
}