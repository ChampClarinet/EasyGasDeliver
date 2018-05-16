package th.co.easygas.admin.easygasdeliver;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import th.co.easygas.admin.easygasdeliver.Core.GPSTracker;
import th.co.easygas.admin.easygasdeliver.Model.Tasks;

import static th.co.easygas.admin.easygasdeliver.Core.Utils.dismissLoadDialog;

public class DestinationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    private GoogleMap gmap;
    private GPSTracker tracker;
    private Tasks tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);

        ButterKnife.bind(this);

        dismissLoadDialog();

        tasks = (Tasks) getIntent().getSerializableExtra(getString(R.string.model_name_taks));
        if (tasks != null) setTitle(tasks.getStoreName());

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        tracker = new GPSTracker(getApplicationContext());

        arrivedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arriveLayout.setVisibility(View.GONE);
                detailsLayout.setVisibility(View.VISIBLE);
            }
        });
        backDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        sendDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        getBackIcon();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        gmap.setMinZoomPreference(12);
        if (tasks != null) {
            LatLng l = new LatLng(tasks.getLatitude(), tasks.getLongitude());
            gmap.moveCamera(CameraUpdateFactory.newLatLng(l));
            showDistance(googleMap);
        }
    }

    private void showDistance(final GoogleMap googleMap) {
        GoogleDirection.withServerKey("AIzaSyAVy_lR1owgidmHn6LvglXOm12BCwizjHc")
                .from(new LatLng(tracker.getLatitude(), tracker.getLongitude()))
                .to(new LatLng(tasks.getLatitude(),tasks.getLongitude()))
                .avoid(AvoidType.FERRIES)
                .avoid(AvoidType.HIGHWAYS)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if(direction.isOK()) {
                            // Do something
                            Route route = direction.getRouteList().get(0);
                            googleMap.addMarker(new MarkerOptions().position(new LatLng(tracker.getLatitude(), tracker.getLongitude())));
                            googleMap.addMarker(new MarkerOptions().position(new LatLng(tasks.getLatitude(), tasks.getLongitude())));
                            ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
                            googleMap.addPolyline(DirectionConverter.createPolyline(
                                    DestinationActivity.this, directionPositionList, 5, getResources().getColor(R.color.route)));
                            setCameraWithCoordinationBounds(route);
                            int distance = Integer.parseInt(route.getLegList().get(0).getDistance().getValue());
                            String distanceString = String.format(Locale.getDefault(), "%.2f", distance / 1000.0);
                            distanceTextView.setText(getString(R.string.distance_label, distanceString));
                            googleMapsButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String url = "http://maps.google.com/maps?daddr="+tasks.getLatitude()+","+tasks.getLongitude();
                                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                            Uri.parse(url));
                                    startActivity(intent);
                                }
                            });
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        // Do something
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void setCameraWithCoordinationBounds(Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        gmap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }

    private void getBackIcon() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @BindView(R.id.map)
    MapView mapView;
    @BindView(R.id.distance)
    TextView distanceTextView;
    @BindView(R.id.button_arrived)
    Button arrivedButton;
    @BindView(R.id.button_back)
    Button backDetailsButton;
    @BindView(R.id.button_send)
    Button sendDetailsButton;
    @BindView(R.id.group_arrive)
    ConstraintLayout arriveLayout;
    @BindView(R.id.group_options)
    LinearLayout detailsLayout;
    @BindView(R.id.button_google_maps)
    ImageButton googleMapsButton;

}
