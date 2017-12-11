package edu.unc.thomasshealy.foodlinkfinal;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.firebase.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VolunteerMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    List<Pin> pinList = new ArrayList<Pin>();
    String phoneNumber;
    String description;
    double latitudeDir;
    double longitudeDir;
    double zoomLat;
    double zoomLong;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("pins");

        // myRef.setValue("Hello, World!");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               pinList.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    // String value = dataSnapshot.getValue(String.class);
                    //System.out.println("Value is " + value);
                    System.out.println(data.getKey());
                    latitudeDir = 7500;
                    longitudeDir = 7500;
                    String latString = data.child("latitude").getValue().toString();
                    String longString = data.child("longitude").getValue().toString();
                    String description = data.child("description").getValue().toString();
                    String phoneNumber = data.child("phoneNumber").getValue().toString();
                    String type = data.child("type").getValue().toString();
                    String username = data.child("username").getValue().toString();
                    double latitude = Double.parseDouble(latString);
                    double longitude = Double.parseDouble(longString);
                    if(latitude != 90 && longitude != 0){
                        zoomLat = latitude;
                        zoomLong = longitude;
                    }
                    int pinType = Integer.parseInt(type);

                    Pin toAdd = new Pin(latitude, longitude, pinType, username, description,
                            phoneNumber);

                    pinList.add(toAdd);



                    System.out.println("latitude: " + latString);
                    System.out.println("longitude: " + longString);
                }
                onMapReady(mMap);
                // Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                System.out.println("failure");
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });



        Button addButton = (Button) this.findViewById(R.id.addLocations);

        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VolunteerMap.this, AddData.class);
                startActivity(intent);
            }


        });

        Button navButton = (Button) this.findViewById(R.id.navigateBtn);


        navButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               /* Uri uri = Uri.parse("smsto:0800000123");
                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                it.putExtra("sms_body", "The SMS text");
                startActivity(it);
                */
                // String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=%f,%f&daddr=%f,%f", 35.933819, -79.054549);
                if(latitudeDir != 7500 && longitudeDir != 7500) {
                    String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", latitudeDir, longitudeDir, "Selected Location");
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);
                }
                else{
                    Toast.makeText(VolunteerMap.this, "Select a pin first",
                            Toast.LENGTH_SHORT).show();
                }
            }


        });

        Button deliverButton = (Button) this.findViewById(R.id.sendMsg);

        deliverButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(phoneNumber != null && description != null){
                Uri uri = Uri.parse("smsto:"+phoneNumber);
                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                it.putExtra("sms_body", "Hello, I am delivering/picking up at your location: "+description);
                startActivity(it);
                }
                else{
                    Toast.makeText(VolunteerMap.this, "Select a pin first",
                            Toast.LENGTH_SHORT).show();
                }

               // String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=%f,%f&daddr=%f,%f", 35.933819, -79.054549);
               /* if(latitudeDir != 7500 && longitudeDir != 7500) {
                    String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", latitudeDir, longitudeDir, "Selected Location");
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);
                }
                else{
                    Toast.makeText(VolunteerMap.this, "Select a pin first",
                            Toast.LENGTH_SHORT).show();
                }*/
            }


        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        System.out.println("enters map ready");
        for(Pin pin : pinList){
        System.out.println("enters loop");
            if(pin.type == 0){
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(pin.latitude
                                , pin.longitude))
                        .title(pin.description)
                        .snippet(pin.phoneNumber)
                );
            }
            else{

                mMap.addMarker( new MarkerOptions() .position(new LatLng( pin.latitude,pin.longitude))
                        .title(pin.description)
                        .snippet(pin.phoneNumber)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

            }

        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(Marker arg0) {
                arg0.showInfoWindow();
                System.out.println("retrieved marker title: " + arg0.getTitle());
                System.out.println("retrieved marker phone number: " + arg0.getSnippet());
                latitudeDir = arg0.getPosition().latitude;
                longitudeDir = arg0.getPosition().longitude;
                description = arg0.getTitle();
                phoneNumber = arg0.getSnippet();

                return true;
            }
        });

        LatLng townHouse = new LatLng(zoomLat, zoomLong);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(townHouse));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
        mMap.animateCamera(zoom);
    }
}
