package com.KonDuckJoa.kuleumbridge.Taste;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.KonDuckJoa.kuleumbridge.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/*
    맛집 리스트 중 한 개 클릭 시, 나타나는 세부 화면에서
    지도를 구성하는 클래스
*/
public class TastePlaceInfoMap extends Fragment implements OnMapReadyCallback {
    View rootView;
    MapView mapView = null;
    Double latitude = 0.0;
    Double longitude = 0.0;
    String address = "";
    String name = "";

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) { }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.taste_info_item, container, false);
        mapView = rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        TextView textViewName = rootView.findViewById(R.id.info_name);
        TextView textViewAddress = rootView.findViewById(R.id.info_address);

        //TastePlaceInfo(Activity)의 값을 받아오는 과정
        Bundle bundle = getArguments();

        name = bundle.getString("name");
        address = bundle.getString("address");
        latitude = bundle.getDouble("latitude");
        longitude = bundle.getDouble("longitude");

        textViewName.setText(name);
        textViewAddress.setText(address);

        mapView.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        MapsInitializer.initialize(this.getActivity());

        //해당 맛집 위도, 경도 마커로 표시하는 과정
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 16);

        googleMap.animateCamera(cameraUpdate);

        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(name));
    }
}



