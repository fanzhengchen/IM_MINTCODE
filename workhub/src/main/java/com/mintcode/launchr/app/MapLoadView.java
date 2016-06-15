package com.mintcode.launchr.app;


import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ZoomControls;

//import com.baidu.mapapi.map.BaiduMap;
//import com.baidu.mapapi.map.BitmapDescriptor;
//import com.baidu.mapapi.map.BitmapDescriptorFactory;
//import com.baidu.mapapi.map.MapStatusUpdate;
//import com.baidu.mapapi.map.MapStatusUpdateFactory;
//import com.baidu.mapapi.map.MapView;
//import com.baidu.mapapi.map.MarkerOptions;
//import com.baidu.mapapi.map.OverlayOptions;
//import com.baidu.mapapi.map.UiSettings;
//import com.baidu.mapapi.model.LatLng;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.mintcode.launchr.R;
import com.mintcode.launchr.consts.LauchrConst;

/**
 * Created by JulyYu on 2016/5/24.
 */
public class MapLoadView extends LinearLayout
        implements OnMapReadyCallback
                {

    private Context mContext;
    private double mDoubleLngx;
    private double mDoubleLaty;

//    private MapView mMapView;

    private GoogleMap mMap;
    private com.google.android.gms.maps.MapView mGoogleMapView = null;

    public MapLoadView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public MapLoadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public MapLoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init(){
        this.setBackgroundColor(Color.WHITE);
        this.setOrientation(HORIZONTAL);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,150);
        this.setLayoutParams(params);
    }
    public void getMap(double lngx, double laty){
        mDoubleLngx = lngx;
        mDoubleLaty = laty;
        if(LauchrConst.DEV_CODE == LauchrConst.MT){
                setBaiduMap();
        }else if(LauchrConst.DEV_CODE == LauchrConst.JP){
                setGoogleMap();
        }

    }
    public void onDestroyMap(){
        if(LauchrConst.DEV_CODE == LauchrConst.MT){
            destoryBaiduMap();
        }else if(LauchrConst.DEV_CODE == LauchrConst.JP){
            setGoogleMap();
        }
    }

    public void destoryBaiduMap() {
//        if(mMapView != null){
//            mMapView.onDestroy();
//        }
    }
    public void onResumeMap(){
//        if(mMapView != null){
//            mMapView.onResume();
//        }
    }

    public void onPauseMap() {
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
//        if(mMapView != null){
//            mMapView.onPause();
//        }
    }
    private void setGoogleMap() {
        mGoogleMapView = new com.google.android.gms.maps.MapView(mContext);
        mGoogleMapView.onCreate(null);
        mGoogleMapView.getMapAsync(this);
        this.removeAllViews();
        this.addView(mGoogleMapView);
    }

    private void setBaiduMap() {
//         mMapView = new MapView(mContext);
//        LatLng point = new LatLng(mDoubleLaty, mDoubleLngx);
//        BaiduMap mBaiduMap = mMapView.getMap();
//        if(point!=null && mBaiduMap!=null) {
//            //定位到当前位置
//            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(point);
//            mBaiduMap.animateMapStatus(u);
//
//            //添加标记
//            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
//            OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
//            mBaiduMap.addOverlay(option);
//
//            // 设置显示比例，例如当前为200米
//            MapStatusUpdate msu = MapStatusUpdateFactory.newLatLngZoom(point, 19);
//            mBaiduMap.setMapStatus(msu);
//
//            //关闭一切手势操作
//            UiSettings settings = mBaiduMap.getUiSettings();
//            settings.setAllGesturesEnabled(false);
//
//            // 隐藏放大缩小
//            int childCount = mMapView.getChildCount();
//            View zoom = null;
//            for (int i = 0; i < childCount; i++) {
//                View child = mMapView.getChildAt(i);
//                if (child instanceof ZoomControls) {
//                    zoom = child;
//                    break;
//                }
//            }
//            zoom.setVisibility(View.GONE);
//        }
//        this.removeAllViews();
//        this.addView(mMapView);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(mContext);
        mMap = googleMap;
        com.google.android.gms.maps.model.LatLng coordinate = new com.google.android.gms.maps.model.LatLng(mDoubleLaty,mDoubleLngx);
        Log.i("coordinate",mDoubleLaty + "----" + mDoubleLngx);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 13f));
        mMap.addMarker(new com.google.android.gms.maps.model.MarkerOptions().position(coordinate));
        // Set the map type back to normal.
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }
}
