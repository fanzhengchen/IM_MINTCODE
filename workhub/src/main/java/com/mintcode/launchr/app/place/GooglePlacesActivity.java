//package com.mintcode.launchr.app.place;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Typeface;
//import android.location.Address;
//import android.location.Geocoder;
//import android.location.Location;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.ActivityCompat;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.text.style.CharacterStyle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.BaseAdapter;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.mintcode.launchr.R;
//import com.mintcode.launchr.base.BaseActivity;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Locale;
//import java.util.concurrent.TimeUnit;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import butterknife.OnItemClick;
//
///**
// * Created by JulyYu on 2016/5/13.
// */
//public class GooglePlacesActivity extends BaseActivity implements AdapterView.OnItemClickListener,
//        ActivityCompat.OnRequestPermissionsResultCallback,
////        GoogleApiClient.OnConnectionFailedListener,
////        GoogleApiClient.ConnectionCallbacks
//{
//
//    private LayoutInflater mInflater;
//    /** 搜索结果列表*/
//    @Bind(R.id.lv_baidu_map)
//     ListView mLvResult;
//    /** 输入框*/
//    @Bind(R.id.et_search)
//     EditText mEditInput;
//
//
//    private GoogleApiClient mGoogleApiClient;
////    private List<SuggestionResult.SuggestionInfo> mResultData;
//    private ResultAdapter mAdapter;
//    private AutocompletePredictionBuffer mResultData;
//    public static final String STR_PLACE = "str_place";
//    public static final String  LATITUDE= "latitude";
//    public static final String LONGITUDE= "longitude";
//    public static final String ADDRESS = "address";
//
//    private double mCurrentLongitude;
//    private double mCurrentLatitude;
//    private String mCurrentAddress;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_add_google_place);
//            ButterKnife.bind(this);
//            initData();
//    }
//
//
//    /** 实例化数据*/
//    private void initData(){
//        mInflater = LayoutInflater.from(GooglePlacesActivity.this);
//        mEditInput.addTextChangedListener(watcher);
////        mResultData = new ArrayList<SuggestionResult.SuggestionInfo>();
//        mAdapter = new ResultAdapter();
//        mLvResult.setAdapter(mAdapter);
//        if (mGoogleApiClient == null) {
//            mGoogleApiClient = new GoogleApiClient.Builder(this)
//                    .addConnectionCallbacks(this)
//                    .addOnConnectionFailedListener(this)
//                    .addApi(Places.GEO_DATA_API)
//                    .addApi(LocationServices.API)
//                    .build();
//        }
//
//    }
//
//    private TextWatcher watcher = new TextWatcher() {
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            String word = mEditInput.getText().toString();
//            if(word!=null && !word.equals("")){
//                // 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
//                Log.i("Log.i- onTextChanged", mEditInput.getText().toString());
////                mGoogleApiClient.connect();
//                 mGoogleApiClient.reconnect();
//            }else{
////                mResultData = new ArrayList<>();
//            }
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//
//        }
//    };
//
//    @OnClick(R.id.tv_clear)
//    void clearAddress(){
//        mEditInput.setText("");
//    }
//    @OnClick(R.id.tv_back)
//    void back(){
//        onBackPressed();
//    }
//    @OnClick(R.id.tv_save)
//    void save(){
//        Intent in = new Intent();
//        String keyWOrd = mEditInput.getText().toString();
//
//        if (!keyWOrd.equals("")) {
//            in.putExtra(STR_PLACE, keyWOrd);
//        }else{
//            toast(getString(R.string.calendar_place_toast));
//            return;
//        }
//        setResult(RESULT_OK, in);
//        finish();
//    }
//    @OnClick(R.id.tv_current_place)
//    void currenPlace(){
//        Intent intent = new Intent();
//        intent.putExtra(STR_PLACE, mCurrentAddress);
//        intent.putExtra(LATITUDE, mCurrentLatitude + "");
//        intent.putExtra(LONGITUDE, mCurrentLongitude + "");
//        setResult(RESULT_OK, intent);
//        Log.i("google-currenPlace",mCurrentAddress +  "x:" + mCurrentLatitude + "y:" + mCurrentLongitude);
//        finish();
//    }
//
//    @Override
//    @OnItemClick(R.id.lv_baidu_map)
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        AutocompletePrediction prediction = (AutocompletePrediction) mAdapter.getItem(position);
//    }
//
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            Log.w("Log.i-enableMyLocation", "getMyLocation    waram");
//            return;
//        }
//        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
//                mGoogleApiClient);
//        if (mLastLocation != null) {
//            mCurrentLatitude = mLastLocation.getLatitude();
//            mCurrentLongitude = mLastLocation.getLongitude();
//            mCurrentAddress = "";
//            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//            List<Address> addresses = null;
//            try {
//                addresses = geocoder.getFromLocation(mCurrentLatitude, mCurrentLongitude, 1);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            if(addresses != null){
//                String cityName = addresses.get(0).getAddressLine(0);
//                String stateName = addresses.get(0).getAddressLine(1);
//                String countryName = addresses.get(0).getAddressLine(2);
//                mCurrentAddress = countryName + " " + cityName + " " + stateName;
//            }
//            Log.i("Log.i-enableMyLocation", "getMyLocation    " + mCurrentLatitude + "-" + mCurrentLongitude + "-" );
//
//        }
//        if(!TextUtils.isEmpty(mEditInput.getText().toString())){
//            Log.i("Log.i- onConnected", mEditInput.getText().toString());
//            PendingResult<AutocompletePredictionBuffer> result = Places.GeoDataApi.getAutocompletePredictions(mGoogleApiClient,mEditInput.getText().toString(),null,null);
//            if(result != null) {
//                result.setResultCallback(new ResultCallback<AutocompletePredictionBuffer>() {
//                    @Override
//                    public void onResult(@NonNull AutocompletePredictionBuffer autocompletePredictions) {
//                        if(autocompletePredictions != null){
//                            mResultData = autocompletePredictions;
//                            mAdapter.notifyDataSetChanged();
//                        }else{
//                            mResultData = null;
//                            mAdapter.notifyDataSetChanged();
//                        }
//                    }
//                });
//            }
//        }
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        if(mGoogleApiClient != null){
//            mGoogleApiClient.connect();
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if(mGoogleApiClient != null){
//            mGoogleApiClient.disconnect();
//        }
//    }
//    private class ResultAdapter extends BaseAdapter {
//        @Override
//        public int getCount() {
//            return mResultData == null?0:mResultData.getCount();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return mResultData == null?null:mResultData.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolder holder;
//            if(convertView == null){
//                holder = new ViewHolder();
//                convertView = mInflater.inflate(R.layout.item_map_search_result, null);
//                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
//                holder.tvAddress = (TextView) convertView.findViewById(R.id.tv_address);
//                convertView.setTag(holder);
//            }else{
//                holder = (ViewHolder) convertView.getTag();
//            }
//             AutocompletePrediction prediction = mResultData.get(position);
//            prediction.getPlaceId();
////            SuggestionResult.SuggestionInfo info = mResultData.get(position);
//            holder.tvName.setText(prediction.getDescription());
////            holder.tvAddress.setText(info.city + info.district);
////            holder.tvName.setText(prediction.getPrimaryText(CharacterStyle));
////            holder.tvAddress.setText(prediction.getSecondaryText(JaSTYLE_BOLD));
//            return convertView;
//        }
//    }
//
//    private class ViewHolder{
//        TextView tvName;
//        TextView tvAddress;
//    }
//}
