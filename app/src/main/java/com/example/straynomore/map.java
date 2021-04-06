/* Project: Stray No More
   Group: Faheem Ahmed,Edward Philip, Abdirahman Ali, Muhammed Yilmaz
   Description: This activity is the display of the map. It will show the animals location and
    the location of nearby animal shelters as well as a button that links to the wildlife info*/
package com.example.straynomore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class map extends AppCompatActivity {

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static MapView mapView;
    private static final String TAG = "MAP";
    private FirebaseDatabase root;
    private DatabaseReference dbRef, refControl, refShelter;
    private FirebaseAuth mAuth;
    private Marker marker, marker1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mapView= findViewById(R.id.map_animal);
        Button wildlife = findViewById(R.id.btn_wildlife);

        mAuth = FirebaseAuth.getInstance();
        root = FirebaseDatabase.getInstance();
        dbRef = root.getReference("messages");
        refControl = root.getReference("animalservices/animalcontrol");
        refShelter = root.getReference("animalservices/animalshelter");


        wildlife.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(),wildlife_info.class));
            finish();
        });

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this::onMapReady);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.map);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.home:
                                startActivity(new Intent(getApplicationContext(), Home.class));
                                finish();
                                return true;
                            case R.id.forum:
                                startActivity(new Intent(getApplicationContext(), forum.class));
                                finish();
                                return true;
                            case R.id.create_post:
                                startActivity(new Intent(getApplicationContext(), create_post.class));
                                finish();
                                return true;
                            case R.id.map:
                                return true;
                        }
                        return false;
                    }
                });
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    public void onMapReady(GoogleMap googleMap) {

        googleMap.getUiSettings().setAllGesturesEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(true);

        GoogleMap googleMap1;


        LatLng toronto= new LatLng(43.67626463077383, -79.41110820189814);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(toronto,14));

        refControl.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Control control;
                control = snapshot.getValue(Control.class);
                Log.d(TAG, String.valueOf(control));
                String address = control.getlocation();
                googleMap.addMarker(new MarkerOptions()
                        .position(getLocationFromAddress(getApplicationContext(), address))
                        .title(control.getName())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
                        .showInfoWindow();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        refShelter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Shelter shelter;
                shelter = snapshot.getValue(Shelter.class);
                Log.d(TAG, String.valueOf(shelter));
                String address = shelter.getLocation();
                googleMap.addMarker(new MarkerOptions()
                        .position(getLocationFromAddress(getApplicationContext(), address))
                        .title(shelter.getName())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)))
                .showInfoWindow();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ForumHelper forumHelper;
                    forumHelper = ds.getValue(ForumHelper.class);
                    String address = ds.child("address").getValue(String.class);
 
                    assert address != null;
                    if(!address.equals("null"))
                    {
                        marker = googleMap.addMarker(new MarkerOptions()
                                .position(getLocationFromAddress(getApplicationContext(), address))
                                .title(ds.child("title").getValue(String.class)));
                        marker.setTag(forumHelper);
                    }
                }

                marker.showInfoWindow();

                googleMap.setOnMarkerClickListener(marker -> {
                    ForumHelper forumHelper;
                    forumHelper = (ForumHelper) marker.getTag();

                    if(marker.getTag() != null)
                    {
                        Intent intent = new Intent(getApplicationContext(), chat.class);
                        intent.putExtra("TITLE", forumHelper.getTitle());
                        intent.putExtra("MESSAGE", forumHelper.getMessage());
                        intent.putExtra("IMAGE", forumHelper.getImage());
                        intent.putExtra("UID", forumHelper.getUid());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    else
                    {
                        String name = marker.getTitle();

                        if(name.equals("Toronto Humane Society"))
                        {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.torontohumanesociety.com/"));
                            startActivity(intent);
                        }
                        else if (name.equals("AAA Professional Wildlife Control"))
                        {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.wildlifecontrolservice.ca/"));
                            startActivity(intent);
                        }

                    }
                    return false;
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.d(TAG, "Failed to connect to database");
            }
        });

//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public LatLng getLocationFromAddress(Context context, String strAddress)
    {
        Geocoder coder= new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try
        {
            address = coder.getFromLocationName(strAddress, 5);
            if(address==null)
            {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return p1;
    }
}