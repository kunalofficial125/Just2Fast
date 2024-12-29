package com.just2fast.ushop;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.Manifest;

import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectLocation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectLocation extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int LOCATION_PERMISSION_REQUEST_CODE =1 ;
    private FusedLocationProviderClient fusedLocationClient;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SelectLocation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectLocation.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectLocation newInstance(String param1, String param2) {
        SelectLocation fragment = new SelectLocation();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    FirebaseDatabase firebaseDatabase ;
    ArrayList<String> locations = new ArrayList<>();
    AutoCompleteTextView autoCompleteTextView;
    ImageView autoDetect;
    View.OnClickListener listener;
    List<Geofence> geofencesToAdd;
    String street;
    String city;
    Activity parent;
    TextView testerDialogButton;
    Button confirm;
    FirebaseUser firebaseUser;
    private GeofencingClient geofencingClient;
    LottieAnimationView lottieAnimationView;
    Dialog TesterDialog ;
    TextView alertText;
    private LocationCallback locationCallback;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parent = getActivity();
        View v = inflater.inflate(R.layout.fragment_select_location, container, false);

        confirm = v.findViewById(R.id.confirm);
        lottieAnimationView = v.findViewById(R.id.lottieAnimationView);
        geofencingClient = LocationServices.getGeofencingClient(parent);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        alertText = v.findViewById(R.id.alertText);
        testerDialogButton = v.findViewById(R.id.testerDialogButton);
        TesterDialog = new Dialog(parent);
        TesterDialog.setContentView(R.layout.beta_dialog);

        EditText testerCodeBox = TesterDialog.findViewById(R.id.testerCodeBox);
        Button done = TesterDialog.findViewById(R.id.done);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = testerCodeBox.getText().toString();
                if(code.equals("1001")){



                    SharedPreferences sp23 = parent.getSharedPreferences("loginCheck",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp23.edit();
                    editor.putBoolean("locationCheck",true);
                    editor.putString("Location","Earth,MilkyWay");
                    editor.putString("City","Agra");
                    editor.apply();
                    Bundle bundle = new Bundle();
                    bundle.putString("Location","Earth,MilkyWay");
                    TesterDialog.dismiss();
                    ((MainActivity) parent).runFragment(bundle,new home(),"Select Location");


                }
            }
        });


        testerDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TesterDialog.show();
            }
        });


        progressBar = v.findViewById(R.id.progressBar);

        firebaseDatabase = FirebaseDatabase.getInstance();

        if(parent instanceof MainActivity){
            ((MainActivity) parent).bnv.setVisibility(View.INVISIBLE);
        }

        List<DeliveryAreaGeofence> deliveryAreas = new ArrayList<>();
        DeliveryAreaGeofence d1 = new DeliveryAreaGeofence();
        d1.setCenter(new LatLng(33.2778,75.3412));
        d1.setRadiusInMeters(15000);
        d1.setRequestId("d1");
        deliveryAreas.add(d1);



         geofencesToAdd = createGeofences(deliveryAreas);


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(parent);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirm.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                if (ContextCompat.checkSelfPermission(parent, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(parent, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            LOCATION_PERMISSION_REQUEST_CODE);

                }
                else{
                    if(((MainActivity)parent).isGPSEnabled()){

                        progressBar.setVisibility(View.VISIBLE);
                        getLastLocation();
                        geofencingClient.addGeofences(new GeofencingRequest.Builder()
                                        .addGeofences(geofencesToAdd)
                                        .build(), null)
                                .addOnSuccessListener(parent, new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("Geofences", "Geofences added successfully");
                                    }
                                })
                                .addOnFailureListener(parent, new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e("Geofences", "Error adding geofences: " + e.getMessage());
                                    }
                                });
                    }
                    else {
                        ((MainActivity)parent).checkLocationSettings();
                    }
                }

            }
        });

//        autoDetect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//            }
//        });

        if (ContextCompat.checkSelfPermission(parent, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(parent, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);

        } else {
            progressBar.setVisibility(View.VISIBLE);

            getLastLocation();
            geofencingClient.addGeofences(new GeofencingRequest.Builder()
                            .addGeofences(geofencesToAdd)
                            .build(), null)
                    .addOnSuccessListener(parent, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d("Geofences", "Geofences added successfully");
                        }
                    })
                    .addOnFailureListener(parent, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Geofences", "Error adding geofences: " + e.getMessage());
                        }
                    });


            //LocationRequest locationRequest = new LocationRequest.Builder(10000).build().setFastestInterval(5000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

//            fusedLocationClient.getCurrentLocation(locationRequest,geofenceLocationCallback,null);



        }


//        firebaseDatabase.getReference().child("Category").child("Location").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot childrenSnapshot : snapshot.getChildren()){
//                    locations.add(childrenSnapshot.getValue(String.class));
//                    Log.d("locations", childrenSnapshot.getValue(String.class) + "");
//                }
//
//                //List<String> addresses =  fetchStreetAddresses(geofencesToAdd.get(0),10);
//
//                autoCompleteTextView.setAdapter(new ArrayAdapter<String>(parent, android.R.layout.simple_list_item_1, locations));
//                autoCompleteTextView.setThreshold(2);
//                autoCompleteTextView.setOnFocusChangeListener((v, hasFocus) -> {
//                    if (hasFocus) {
//                        if (autoCompleteTextView.getText().toString().isEmpty()) {
//                            autoCompleteTextView.setHint("");
//                        }
//                    } else {
//                        if (autoCompleteTextView.getText().toString().isEmpty()) {
//                            autoCompleteTextView.setHint("Select Area, Street, City...");
//                        }
//                    }
//                });
//
//
//                autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                        confirm.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                String selectedLocation = autoCompleteTextView.getText().toString();
//                                Toast.makeText(parent, selectedLocation, Toast.LENGTH_SHORT).show();
//                                SharedPreferences sp = parent.getSharedPreferences("loginCheck", Context.MODE_PRIVATE);
//                                SharedPreferences.Editor editor = sp.edit();
//                                editor.putBoolean("locationCheck",true);
//                                editor.apply();
//
//                                ((MainActivity) parent).bnv.setVisibility(View.VISIBLE);
//
//
//                                String []a = selectedLocation.split(",");
//
//                                firebaseDatabase.getReference().child("Category").child("UsersData").child(firebaseUser.getEmail().replace(".","")).child("City").setValue(a[1]).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void unused) {
//                                        firebaseDatabase.getReference().child("Category").child("UsersData").child(firebaseUser.getEmail().replace(".","")).child("Area").setValue(a[0]);
//                                    }
//                                });
//
//
//                                ((MainActivity) parent).runFragment(new Bundle(),new home(),"Select Location");
//
//
//                            }
//                        });
//                    }
//                });
//
//
//
//                confirm.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Toast.makeText(parent, "Choose Location First !", Toast.LENGTH_SHORT).show();
//                    }
//                });
//               // autoCompleteTextView.getText();
//
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        return v;
    }

    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(parent, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnCompleteListener( parent, new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                Location location = task.getResult();

                                SharedPreferences userLatLong = parent.getSharedPreferences("userLatLong",Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor125 = userLatLong.edit();
                                editor125.putString("lat", ""+ location.getLatitude());
                                editor125.putString("long", ""+location.getLongitude());
                                editor125.apply();


                                String locationText = getAddressFromLocation(location);
                                if (isWithinGeofence(location)) {
                                    SharedPreferences sp23 = parent.getSharedPreferences("loginCheck",Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp23.edit();
                                    editor.putBoolean("locationCheck",true);
                                    editor.putString("Location",locationText);
                                    editor.apply();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("Location",locationText);
                                    ((MainActivity) parent).runFragment(bundle,new home(),"Select Location");

                                } else {
                                    lottieAnimationView.setAnimation(R.raw.not_available_place);
                                    confirm.setVisibility(View.INVISIBLE);
                                    alertText.setVisibility(View.VISIBLE);
                                    lottieAnimationView.setPadding(40,40,40,40);
                                    lottieAnimationView.playAnimation();
                                    Bundle bundle = new Bundle();
                                    SharedPreferences sp23 = parent.getSharedPreferences("loginCheck",Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp23.edit();
                                    editor.putBoolean("locationCheck",true);
                                    editor.putString("Location","Earth,MilkyWay");
                                    editor.putString("City","Agra");
                                    editor.apply();
                                    bundle.putString("Location",locationText);
                                    ((MainActivity) parent).runFragment(bundle,new home(),"Select Location");
                                }
                            } else {
                                requestLocationUpdates();
                            }
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
        }
    }


    private String getAddressFromLocation(Location location) {
        progressBar.setVisibility(View.VISIBLE);
        Geocoder geocoder = new Geocoder(parent, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                 street = address.getThoroughfare();
                 city = address.getLocality();

                SharedPreferences sp23 = parent.getSharedPreferences("loginCheck",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp23.edit();
                editor.putString("City",city);
                editor.apply();


                String state = address.getAdminArea();
                String country = address.getCountryName();
                String postalCode = address.getPostalCode();
                String fullAddress = address.getAddressLine(0); // Full address

                progressBar.setVisibility(View.INVISIBLE);

                // Display the full address
                return street+", "+city;

                // Alternatively, display individual address components
                // Toast.makeText(MainActivity.this, "Street: " + street + "\nCity: " + city + "\nState: " + state + "\nCountry: " + country + "\nPostal Code: " + postalCode, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(parent, "No address found for the location", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(parent, "Geocoder service failed", Toast.LENGTH_LONG).show();
        }
        return "null";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(parent, "Permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private List<Geofence> createGeofences(List<DeliveryAreaGeofence> deliveryAreas) {
        List<Geofence> geofences = new ArrayList<>();
        for (DeliveryAreaGeofence area : deliveryAreas) {
            Geofence geofence = new Geofence.Builder()
                    .setRequestId(area.getRequestId())
                    .setCircularRegion(area.getCenter().latitude, area.getCenter().longitude, (float) area.getRadiusInMeters())
                    .setExpirationDuration(Geofence.NEVER_EXPIRE) // Set expiration as needed
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT) // Monitor both entering and exiting
                    .build();
            geofences.add(geofence);
        }
        return geofences;
    }

//    private LocationCallback geofenceLocationCallback = new LocationCallback() {
//        @Override
//        public void onLocationResult(@NonNull LocationResult locationResult) {
//           super.onLocationResult(locationResult);
//            for (GeofencingEvent event : locationResult.getGeofencingEvents()) {
//                int transitionType = event.getTransitionType();
//                if (transitionType == Geofence.GEOFENCE_TRANSITION_EXIT) {
//                    // User exited a delivery area, show toast
//                    Toast.makeText(parent, "Delivery service unavailable in your area. Please check our website for coverage details.", Toast.LENGTH_LONG).show();
//                    break;
//                }
//            }
//       }
//    };


    private boolean isWithinGeofence(Location location) {
        // Iterate through your geofences (replace with your data structure)
        for (Geofence geofence : geofencesToAdd) {
            double latitude = geofence.getLatitude();
            double longitude = geofence.getLongitude();
            float radius = geofence.getRadius();

            Location geofenceCenter = new Location("geofenceCenter");  // Optional (for readability)
            geofenceCenter.setLatitude(latitude);
            geofenceCenter.setLongitude(longitude);


            // Calculate distance between user location and geofence center
            float distance = distanceBetween(location,geofenceCenter);

            // Check if distance is less than or equal to geofence radius
            if (distance <= radius) {
                return true; // User is within this geofence
            }
        }

        return false; // User is not within any geofence
    }


    private void requestLocationUpdates() {
        progressBar.setVisibility(View.VISIBLE);
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)
                .setFastestInterval(5000);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult != null) {
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        handleNewLocation(location);
                        fusedLocationClient.removeLocationUpdates(locationCallback);  // Stop updates after getting a fix
                    }
                }
            }
        };

        if (ContextCompat.checkSelfPermission(parent, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void handleNewLocation(Location location) {
        if (location != null) {
            // Handle the new location
            //Toast.makeText(parent, "Location: " + location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_LONG).show();
            // You can also process the location (e.g., get address, check within geofence, etc.)

            SharedPreferences userLatLong = parent.getSharedPreferences("userLatLong",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor125 = userLatLong.edit();
            editor125.putString("lat", ""+ location.getLatitude());
            editor125.putString("long", ""+location.getLongitude());
            editor125.apply();

            String locationText = getAddressFromLocation(location);
            if (isWithinGeofence(location)) {
                SharedPreferences sp23 = parent.getSharedPreferences("loginCheck",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp23.edit();
                editor.putBoolean("locationCheck",true);
                editor.putString("Location",locationText);
                editor.apply();
                Bundle bundle = new Bundle();
                bundle.putString("Location",locationText);
                ((MainActivity) parent).runFragment(bundle,new home(),"Select Location");
            } else {
                Toast.makeText(parent, "Delivery service unavailable in your area. Please check our website for coverage details.", Toast.LENGTH_LONG).show();
                Bundle bundle = new Bundle();
                bundle.putString("Location",locationText);
                SharedPreferences sp23 = parent.getSharedPreferences("loginCheck",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp23.edit();
                editor.putBoolean("locationCheck",true);
                editor.putString("Location","Earth,MilkyWay");
                editor.putString("City","Agra");
                editor.apply();
                ((MainActivity) parent).runFragment(bundle,new home(),"Select Location");
            }
        }
    }


    private float distanceBetween(Location location1, Location location2) {
        // No need for separate latitude/longitude arguments
        return location1.distanceTo(location2);
    }

    /*private List<String> fetchStreetAddresses(Geofence geofence, int gridSize) {
        List<String> addresses = new ArrayList<>();
        double centerLat = geofence.getLatitude();
        double centerLng = geofence.getLongitude();
        float radius = geofence.getRadius();

        // Calculate offsets for grid squares based on grid size and radius
        double offsetLat = gridSize / (2.0 * 111132); // Meters to degrees conversion
        double offsetLng = offsetLat / Math.cos(Math.toRadians(centerLat));

        // Loop through grid squares
        for (int i = 0; i < Math.ceil(radius / gridSize); i++) {
            for (int j = 0; j < Math.ceil(radius / gridSize); j++) {
                double squareLat = centerLat + (i * offsetLat);
                double squareLng = centerLng + (j * offsetLng);

                // Generate a sample point slightly offset from the center
                double sampleLat = squareLat + (Math.random() - 0.5) * offsetLat / 5;
                double sampleLng = squareLng + (Math.random() - 0.5) * offsetLng / 5;

                // Perform reverse geocoding on the sample point
                Geocoder geocoder = new Geocoder(parent, Locale.getDefault());
                try {
                    List<Address> results = geocoder.getFromLocation(sampleLat, sampleLng, 1);
                    if (results != null && !results.isEmpty()) {
                        Address address = results.get(0);
                        String street = address.getThoroughfare();
                        if (!addresses.contains(street)) {
                            addresses.add(street);
                        }
                    }
                } catch (IOException e) {
                    // Handle geocoding exceptions
                }
            }
        }

        return addresses;
    }*/

}