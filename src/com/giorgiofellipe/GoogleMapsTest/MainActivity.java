package com.giorgiofellipe.GoogleMapsTest;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.*;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends SherlockActivity {
    static final LatLng LUGAR = new LatLng(-27.235885, -49.639594);
    static final LatLng ZOOM = new LatLng(-27.235885, -49.639594);
    private GoogleMap map;
    private HashMap<Marker, CustomMarker> markersHashMap;
    private ArrayList<CustomMarker> myMarkersArray = new ArrayList<CustomMarker>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        try {
            // Loading map
            initilizeMap();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initilizeMap() {
        if (map == null) {
            map = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            // check if map is created successfully or not
            if (map == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            } else {
                configMap();

                addMarkers();

                placeMarkers(myMarkersArray);

                positionTheCamera();
            }
        }
    }

    private void configMap() {
        //Here we'll tell that we want to do something when the marker is clicked
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker) {
                marker.showInfoWindow();
                return true;
            }
        });
        //Shows your current location (defaults to false)
//                map.setMyLocationEnabled(true);

        //Enable/Disable zoom buttons (defaults do enabled)
//                map.getUiSettings().setZoomControlsEnabled(false);

        //Disable zooming gesture functionality
//                map.getUiSettings().setZoomGesturesEnabled(false);

        //Enable/Disable compass
//                map.getUiSettings().setCompassEnabled(true);

        //EnableDisable MyLocationButton
//                map.getUiSettings().setMyLocationButtonEnabled(true);

        //Enable/Disable RotateGesture
//                map.getUiSettings().setRotateGesturesEnabled(true);

        //Changing MAP Type
//                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    private void addMarkers() {
        // Initialize the HashMap for Markers and CustomMarker object
        markersHashMap = new HashMap<Marker, CustomMarker>();

        myMarkersArray.add(new CustomMarker("Brasil", "icon1", Double.parseDouble("-27.235885"), Double.parseDouble("-49.639594")));
        myMarkersArray.add(new CustomMarker("United States", "icon2", Double.parseDouble("33.7266622"), Double.parseDouble("-87.1469829")));
        myMarkersArray.add(new CustomMarker("Canada", "icon3", Double.parseDouble("51.8917773"), Double.parseDouble("-86.0922954")));
        myMarkersArray.add(new CustomMarker("England", "icon4", Double.parseDouble("52.4435047"), Double.parseDouble("-3.4199249")));
    }

    private void placeMarkers(ArrayList<CustomMarker> markers) {
        if(markers.size() > 0) {
            for (CustomMarker myMarker : markers) {

                // Create user marker with custom icon and other options
                MarkerOptions markerOption = new MarkerOptions().position(new LatLng(myMarker.getmLatitude(), myMarker.getmLongitude()));
                markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.currentlocation_icon));

                Marker currentMarker = map.addMarker(markerOption);
                markersHashMap.put(currentMarker, myMarker);

                map.setInfoWindowAdapter(new MarkerInfoWindowAdapter());
            }
        }
    }

    private void positionTheCamera() {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(ZOOM).zoom(4).build();

        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    private int manageMarkerIcon(String markerIcon)
    {
        if (markerIcon.equals("icon1"))
            return R.drawable.icon1;
        else if(markerIcon.equals("icon2"))
            return R.drawable.icon2;
        else if(markerIcon.equals("icon3"))
            return R.drawable.icon3;
        else if(markerIcon.equals("icon4"))
            return R.drawable.icon4;
        else
            return R.drawable.icondefault;
    }

    public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        public MarkerInfoWindowAdapter() {
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            View v  = getLayoutInflater().inflate(R.layout.infowindow, null);

            CustomMarker myMarker = markersHashMap.get(marker);

            ImageView markerIcon = (ImageView) v.findViewById(R.id.marker_icon);

            TextView markerLabel = (TextView)v.findViewById(R.id.marker_label);

            markerIcon.setImageResource(manageMarkerIcon(myMarker.getmIcon()));
            markerLabel.setText(myMarker.getmLabel());

            TextView anotherLabel = (TextView)v.findViewById(R.id.another_label);
            anotherLabel.setText("A custom text");

            return v;
        }
    }
}