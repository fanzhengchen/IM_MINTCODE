//package com.mintcode.launchr.app.place;
//
//
//import android.content.Context;
//import android.content.Intent;
//import android.location.Location;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.View.OnKeyListener;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.AutoCompleteTextView;
//import android.widget.TextView;
//
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.common.api.PendingResult;
//import com.google.android.gms.common.api.ResultCallback;
//import com.google.android.gms.location.LocationListener;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.location.places.Place;
//import com.google.android.gms.location.places.PlaceBuffer;
//import com.google.android.gms.location.places.Places;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.LatLngBounds;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.mintcode.launchr.base.BaseActivity;
//
//
//
//
//public class AddGooglePlacesActivity extends BaseActivity implements OnItemClickListener ,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,ResultCallback<PlaceBuffer>,LocationListener,OnMapReadyCallback,OnKeyListener{
//
//	public static final String STR_PLACE = "str_place";
//
//	public static final String LATLING = "latling";
//
//	/** 输入框 */
//	private AutoCompleteTextView mAutoCompleteTextView;
//
//	/** 清除按钮 */
//	private TextView mTvClear;
//
//	/** 返回按钮 */
//	private TextView mTvBack;
//
//	private TextView mTvSave;
//
//	/** 搜索结果适配器 */
//	private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
//
//	/** GoogleApiClient */
//	private  GoogleApiClient mGoogleApiClient;
//
//	private  GoogleApiClient mGoogleApiPlaces;
//
//
//	/** 地点 */
//	private Place mPlace;
//
//	private Location mLastLocation;
//
//	private String mDescription = "";
//
//	private LatLng mLatLng;
//	//TODO 改成动态获取
////	private static final LatLngBounds BOUNDS_GREATER_HANGZHOU = new LatLngBounds(
////            new LatLng(29.293736,120.169316), new LatLng(30.293736,121.169316));
//	private static final LatLngBounds BOUNDS_GREATER_HANGZHOU = new LatLngBounds(
//            new LatLng(34.821234,138.169316), new LatLng(36.241234,139.812343));
////  private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));
//
//	private LatLngBounds mBoundsGetLocation;
//
//	private static final LocationRequest REQUEST = LocationRequest.create()
//            .setInterval(5000)         // 5 seconds
//            .setFastestInterval(16)    // 16ms = 60fps
//            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		try {
//			setContentView(R.layout.activity_google_map);
//			initData();
//			initView();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	/**
//	 * 实例化view
//	 */
//	private void initView(){
//		mTvClear = (TextView) findViewById(R.id.tv_clear);
//		mAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.et_google_search);
//
//		mTvClear.setOnClickListener(this);
//		mAutoCompleteTextView.setOnItemClickListener(this);
//		mTvBack = (TextView) findViewById(R.id.tv_back);
//		mTvSave = (TextView) findViewById(R.id.tv_save);
//
//		mTvSave.setOnClickListener(this);
//		mTvBack.setOnClickListener(this);
//
//		mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, android.R.layout.simple_list_item_1, mGoogleApiClient, BOUNDS_GREATER_HANGZHOU, null);
//		mAutoCompleteTextView.setAdapter(mPlaceAutocompleteAdapter);
//
//		//
//		SupportMapFragment mapFragment =(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mp);
//        mapFragment.getMapAsync(this);
////        mapFragment.set
//
//        mAutoCompleteTextView.setOnKeyListener(this);
//
//
//	}
//
//	// 08-23 18:29:26.959: I/infos(10109): ====搜索出的结果=====39.90549===116.397632
//
//	/**
//	 * 实例化数据
//	 */
//	private void initData(){
////		mGoogleApiClient = new GoogleApiClient.Builder(this)
////					       .enableAutoManage(this, 0 /* clientId */, this)
////					       .addApi(Places.GEO_DATA_API)
////					       .build();
//
//		mGoogleApiPlaces = new GoogleApiClient.Builder(this)
//           .enableAutoManage(this, 0 /* clientId */, this)
//	       .addApi(Places.GEO_DATA_API)
//	       .build();
//
//		 mGoogleApiClient = new GoogleApiClient.Builder(this)
//         .addApi(LocationServices.API)
//         .addConnectionCallbacks(this)
//         .addOnConnectionFailedListener(this)
//         .build();
//
////		 FusedLocationProviderApi.setm
//
//	}
//
//	@Override
//    protected void onResume() {
//        super.onResume();
//        if (mGoogleApiClient != null) {
//        	mGoogleApiClient.connect();
//		}
//        if (mGoogleApiPlaces != null) {
//        	mGoogleApiPlaces.connect();
//		}
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
////        startLocationUpdates();
//        if (mGoogleApiClient != null) {
//        	mGoogleApiClient.disconnect();
//        }
//        if (mGoogleApiPlaces != null) {
//        	mGoogleApiPlaces.disconnect();
//        }
//    }
//
//
//	@Override
//	public void onClick(View v) {
//		if (v == mTvClear) {
//			mAutoCompleteTextView.setText("");
//			mLatLng = null;
//			mDescription = "";
//		}else if (v == mTvBack) {
//			onBackPressed();
//		}else if (v == mTvSave) {
//			Intent in = new Intent();
//			if (mLatLng != null) {
//				in.putExtra(LATLING, mLatLng);
//			}
//			if (!mDescription.equals("")) {
//				in.putExtra(STR_PLACE, mDescription);
//			}else{
//				String str = mAutoCompleteTextView.getText().toString().trim();
//				in.putExtra(STR_PLACE, str);
//			}
//			setResult(RESULT_OK, in);
//			finish();
//		}
//	}
//
//
//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
//		 // Retrieve the place ID of the selected item from the Adapter.
//	     // The adapter stores each Place suggestion in a PlaceAutocomplete object from which we
//	     // read the place ID.
//		final PlaceAutocompleteAdapter.PlaceAutocomplete item = mPlaceAutocompleteAdapter.getItem(position);
//		String placeId = String.valueOf(item.placeId);
//
//		// Issue a request to the Places Geo Data API to retrieve a Place object with additional
//        // details about the place.
//		PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiPlaces, placeId);
//		placeResult.setResultCallback(this);
//
//		mDescription = item.description + "";
//		Log.i("infos", "==" + item.description);
//
//	}
//
//
//	@Override
//	public void onConnectionFailed(ConnectionResult result) {
//
//
//	}
//
//	@Override
//	public void onResult(PlaceBuffer places) {
//		// 判断状态是否成功
//		if (!places.getStatus().isSuccess()) {
//			places.release();
//			return;
//		}
//
//		// Get the Place object from the buffer.
//		final Place place = places.get(0);
//		mPlace = place;
//		// 操作
//		updateShowPlace(place);
//
//		places.release();
//
//
//	}
//
//	GoogleMap map;
//	@Override
//	public void onMapReady(GoogleMap googleMap) {
//		map = googleMap;
//		googleMap.setMyLocationEnabled(true);
//		Log.i("infos", "====houhou=====");
////		LatLng l = new LatLng(25.52, 110.17);
////        LatLng l = new LatLng(40.360314920604836, 116.52917683124542);//116°23′17〃，北纬：39°54′27〃
//		LatLng l = new LatLng(39.90549, 116.397632);
//        //08-23 18:20:29.234: I/infos(9268): 40.360314920604836==116.52917683124542
//        //08-23 18:21:14.300: I/infos(9268): 39.90389244105055==116.40978734940289
//
//
//		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(l, 12f));
//		addPoint(googleMap, l);
//	}
//
//	@Override
//	public void onLocationChanged(Location location) {
//		double lat = location.getLatitude();
//		double lon = location.getLongitude();
//		Log.i("infos", lat + "===" + lon);
//		if (mBoundsGetLocation == null) {
//			Log.i("infos", lat + "=wa da xi==" + lon);
//			LatLng southwest = new LatLng(lat - 1, lon);
//			LatLng northeast = new LatLng(lat + 1, lon);
//			mBoundsGetLocation = new LatLngBounds(southwest, northeast);
//			mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, android.R.layout.simple_list_item_1, mGoogleApiPlaces, mBoundsGetLocation, null);
//			mAutoCompleteTextView.setAdapter(mPlaceAutocompleteAdapter);
//		}
//	}
//
//	/**
//	 * 显示更新的地点
//	 */
//	private void updateShowPlace(Place place){
//		LatLng l = place.getLatLng();
//		Log.i("infos", place.getName() + "--" +place.getAddress()+"====搜索出的结果=====" + l.latitude + "===" + l.longitude);
//		mLatLng = l;
//		addPoint(map, l);
//	}
//
//	@Override
//	public void onConnected(Bundle connectionHint) {
//		Log.i("infos", "====false=====");
//		if (mGoogleApiClient.isConnected()) {
////			mLastLocation = LocationServices.FusedLocationApi.getLastLocation( mGoogleApiClient);
//			Log.i("infos", "====true=====");
////			LocationServices.FusedLocationApi.get
//			startLocationUpdates();
//		}
//
//
//	}
//
//	@Override
//	public void onConnectionSuspended(int cause) {
//		// TODO Auto-generated method stub
//
//	}
//
//	protected void startLocationUpdates() {
//	    LocationServices.FusedLocationApi.requestLocationUpdates( mGoogleApiClient, REQUEST, this);
//
//
//	}
//
//	protected void stopLocationUpdates() {
//	    LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
//	}
//
//	private void addPoint(GoogleMap map,LatLng l ){
//		map.moveCamera(CameraUpdateFactory.newLatLngZoom(l, 12f));
//		map.addMarker(new MarkerOptions()
//         .position(l)
//         .title("Sydney")
//         .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
//	}
//
//	@Override
//	public boolean onKey(View v, int keyCode, KeyEvent event) {
//		if (keyCode==KeyEvent.KEYCODE_ENTER) {
//			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//			imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//			return true;
//		}
//		return false;
//	}
//
//}