package edu.unc.thomasshealy.foodlinkfinal;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Random;

import java.io.IOException;
import java.util.List;

public class AddData extends AppCompatActivity {

   // public DatabaseReference ref;
    public DatabaseReference myRef;
    public FirebaseDatabase database;
    public PlaceAutocompleteFragment autocompleteFragment;
    public LatLng retrievedCoordinates;
    private FirebaseAuth mAuth;
    public String username;
    public EditText phoneNumber;
    public EditText description;
    public DataSnapshot snapshot;
    public Random rand;
    public boolean addPin = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("pins");
        final LatLng test = getLocationFromAddress(getApplicationContext(), "425 Hillsborough Street, Chapel Hill, NC");
        System.out.println(test);
        rand = new Random();
        //System.out.println("latitude from geolocater =  " + test.latitude);
        Button addPickup = (Button) this.findViewById(R.id.addPickup);
        Button addBank = (Button) this.findViewById(R.id.addFoodBank);
        Button delete = (Button) this.findViewById(R.id.deleteLocation);
        description = (EditText) this.findViewById(R.id.description);
        phoneNumber = (EditText) this.findViewById(R.id.phonenumber);
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.search_bar);
        mAuth = FirebaseAuth.getInstance();
        username = mAuth.getCurrentUser().getEmail();
        //System.out.println("username = " + username);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            snapshot = dataSnapshot;
                // Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                System.out.println("failure");
                // Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                //System.out.println("Place: " + place.getName());
                retrievedCoordinates = place.getLatLng();
                System.out.println("coordinates of place: " + place.getLatLng());
               // Log.i(TAG, "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                System.out.println("An error occurred: " + status);
                //Log.i(TAG, "An error occurred: " + status);
            }
        });

        addPickup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(retrievedCoordinates != null) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        double latitude = Double.parseDouble(data.child("latitude").getValue().toString());
                        double longitude = Double.parseDouble(data.child("longitude").getValue().toString());
                        String user = data.child("username").getValue().toString();

                        if (latitude == retrievedCoordinates.latitude &&
                                longitude == retrievedCoordinates.longitude &&
                                username.equals(user)) {
                            myRef.child(data.getKey()).removeValue();
                         //   Toast.makeText(AddData.this, "Successfully updated location",
                          //          Toast.LENGTH_SHORT).show();
                            //retrievedCoordinates = null;
                            addPin = true;
                            break;
                        }
                        else if(latitude == retrievedCoordinates.latitude &&
                                longitude == retrievedCoordinates.longitude &&
                                !username.equals(user)){
                            Toast.makeText(AddData.this, "Location already exists, please try another",
                                              Toast.LENGTH_SHORT).show();
                            addPin = false;
                            break;
                        }

                        else{
                            addPin = true;
                        }


                    }
                }



                System.out.println("enters button clicked method");
                if(retrievedCoordinates != null && description.getText().length() > 6 && PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber.getText().toString())
                        && addPin) {
                    Pin testPin = new Pin(retrievedCoordinates.latitude, retrievedCoordinates.longitude, 0, username,
                            description.getText().toString(), phoneNumber.getText().toString());
                    int num = rand.nextInt(659099999);
                    myRef.child("Pickup"+num).setValue(testPin);
                    Toast.makeText(AddData.this, "Successfully added location",
                            Toast.LENGTH_SHORT).show();
                  // retrievedCoordinates = null;
                }
                else{
                    Toast.makeText(AddData.this, "Failed to add location, try again.  To update location, delete and re enter",
                            Toast.LENGTH_SHORT).show();
                }

            }


        });

        addBank.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(retrievedCoordinates != null) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        double latitude = Double.parseDouble(data.child("latitude").getValue().toString());
                        double longitude = Double.parseDouble(data.child("longitude").getValue().toString());
                        String user = data.child("username").getValue().toString();

                        if (latitude == retrievedCoordinates.latitude &&
                                longitude == retrievedCoordinates.longitude &&
                                username.equals(user)) {
                            myRef.child(data.getKey()).removeValue();
                            //   Toast.makeText(AddData.this, "Successfully updated location",
                            //          Toast.LENGTH_SHORT).show();
                            //retrievedCoordinates = null;
                            addPin = true;
                            break;
                        }
                        else if(latitude == retrievedCoordinates.latitude &&
                                longitude == retrievedCoordinates.longitude &&
                                !username.equals(user)){
                            Toast.makeText(AddData.this, "Location already exists, please try another",
                                    Toast.LENGTH_SHORT).show();
                            addPin = false;
                            break;
                        }

                        else{
                            addPin = true;
                        }


                    }
                }



                System.out.println("enters button clicked method");
                if(retrievedCoordinates != null && description.getText().length() > 6 && PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber.getText().toString())
                        && addPin) {
                    Pin testPin = new Pin(retrievedCoordinates.latitude, retrievedCoordinates.longitude, 0, username,
                            description.getText().toString(), phoneNumber.getText().toString());
                    int num = rand.nextInt(659099999);
                    myRef.child("Pickup"+num).setValue(testPin);
                    Toast.makeText(AddData.this, "Successfully added location",
                            Toast.LENGTH_SHORT).show();
                    // retrievedCoordinates = null;
                }
                else{
                    Toast.makeText(AddData.this, "Failed to add location, try again.  To update location, delete and re enter",
                            Toast.LENGTH_SHORT).show();
                }

            }


        });

        addBank.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(retrievedCoordinates != null) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        double latitude = Double.parseDouble(data.child("latitude").getValue().toString());
                        double longitude = Double.parseDouble(data.child("longitude").getValue().toString());
                        String user = data.child("username").getValue().toString();

                        if (latitude == retrievedCoordinates.latitude &&
                                longitude == retrievedCoordinates.longitude &&
                                username.equals(user)) {
                            myRef.child(data.getKey()).removeValue();
                            //   Toast.makeText(AddData.this, "Successfully updated location",
                            //          Toast.LENGTH_SHORT).show();
                            //retrievedCoordinates = null;
                            addPin = true;
                            break;
                        }
                        else if(latitude == retrievedCoordinates.latitude &&
                                longitude == retrievedCoordinates.longitude &&
                                !username.equals(user)){
                            Toast.makeText(AddData.this, "Location already exists, please try another",
                                    Toast.LENGTH_SHORT).show();
                            addPin = false;
                            break;
                        }

                        else{
                            addPin = true;
                        }


                    }
                }



                System.out.println("enters button clicked method");
                if(retrievedCoordinates != null && description.getText().length() > 6 && PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber.getText().toString())
                        && addPin) {
                    Pin testPin = new Pin(retrievedCoordinates.latitude, retrievedCoordinates.longitude, 1, username,
                            description.getText().toString(), phoneNumber.getText().toString());
                    int num = rand.nextInt(659099999);
                    myRef.child("Foodbank"+num).setValue(testPin);
                    Toast.makeText(AddData.this, "Successfully added location",
                            Toast.LENGTH_SHORT).show();
                    // retrievedCoordinates = null;
                }
                else{
                    Toast.makeText(AddData.this, "Failed to add location, try again.  To update location, delete and re enter",
                            Toast.LENGTH_SHORT).show();
                }


               /* System.out.println("enters button clicked method");
                Pin testPin = new Pin(6.435, 10.615, 1, "test_username100", "test description90", "(336)384-3287");
                myRef.child("Test100").setValue(testPin);
                */

            }


        });

        delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(retrievedCoordinates != null) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        double latitude = Double.parseDouble(data.child("latitude").getValue().toString());
                        double longitude = Double.parseDouble(data.child("longitude").getValue().toString());
                        String user = data.child("username").getValue().toString();

                        if (latitude == retrievedCoordinates.latitude &&
                                longitude == retrievedCoordinates.longitude &&
                                username.equals(user)) {
                            myRef.child(data.getKey()).removeValue();
                            Toast.makeText(AddData.this, "Successfully deleted location",
                                    Toast.LENGTH_SHORT).show();
                            //retrievedCoordinates = null;
                            break;
                        }


                    }
                }

                else{
                    Toast.makeText(AddData.this, "enter address of the location you wish to delete",
                            Toast.LENGTH_SHORT).show();
                }

               // System.out.println("enters button clicked method");
                //Pin testPin = new Pin(6.435, 10.615, 1, "test_username100", "test description90", "(336)384-3287");
                //myRef.child("Test100").removeValue();

            }


        });
    }



    public LatLng getLocationFromAddress(Context context,String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        List <Address> validate;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                System.out.println("sets that skank to null");
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            validate = coder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            System.out.println("validated Address line = " + validate.get(0).getAddressLine(0));
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {
            System.out.println("enters exception caught");
            ex.printStackTrace();
        }

        return p1;
    }
}
