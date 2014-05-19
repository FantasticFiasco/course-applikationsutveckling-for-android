package com.kindborg.mattias.gasstations;

import java.io.*;
import java.util.*;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import android.os.*;
import android.support.v4.app.*;

public class MainActivity extends FragmentActivity {

    private static final LatLngBounds SWEDEN = new LatLngBounds(new LatLng(54, 9), new LatLng(70, 25));

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

        // Read file with petrol stations
        new PetrolStationReader().execute();
    }

    private void addPetrolStations(List<PetrolStation> petrolStations) {
        for (PetrolStation petrolStation : petrolStations) {
            MarkerOptions markerOptions = new MarkerOptions()
                .position(petrolStation.getLatLng())
                .title(String.format("%s (%s)", petrolStation.getName(), petrolStation.getCity()));

            map.addMarker(markerOptions);
        }

        map.moveCamera(CameraUpdateFactory.newLatLngBounds(SWEDEN, 0));
    }

    private class PetrolStationReader extends AsyncTask<Void, Void, List<PetrolStation>> {

        @Override
        protected List<PetrolStation> doInBackground(Void... params) {
            InputStream inputStream = MainActivity.this.getResources().openRawResource(R.raw.petrol_stations);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            List<PetrolStation> petrolStations = new ArrayList<PetrolStation>();
            String line = null;

            try {
                while ((line = reader.readLine()) != null) {
                    // Ignore comments
                    if (line.startsWith(";")) {
                        continue;
                    }

                    String[] values = line.split(",");
                    petrolStations.add(new PetrolStation(
                        Double.parseDouble(values[0]),
                        Double.parseDouble(values[1]),
                        values[2],
                        values[3]));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return petrolStations;
        }

        @Override
        protected void onPostExecute(List<PetrolStation> result) {
            addPetrolStations(result);
        }
    }
}