package com.example.manu.gpsmaps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private String qr1 = "QRpunto1";
    private String qr2 = "QRpunto2";
    private String qr3 = "QRpunto3";
    public static final int LOCATION_REQUEST_CODE = 1;
    private GoogleApiClient apiCliente;
    private Location objetoLocalizacion;
    private CircleOptions statsCirculo;
    private Circle circulo;
    private LatLng marcador;
    private Marker marca;
    Location marcaUbicacion =new Location("premio");
    private float distanciaPremio;
    View instruccionesOb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        marcador = new LatLng(42.237804, -8.716776);
        marcaUbicacion.setLatitude(marcador.latitude);
        marcaUbicacion.setLongitude(marcador.longitude);
        if (apiCliente == null) {
            // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
            // See https://g.co/AppIndexing/AndroidStudio for more information.
            apiCliente = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(AppIndex.API).build();
        }
        instruccionesOb = findViewById(R.id.instrucciones);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Controles UI
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Mostrar diálogo explicativo
            } else {
                // Solicitar permiso
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_REQUEST_CODE);
            }
        }
        mMap.setOnMapClickListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMapLongClickListener(this);
        crearCirculo(new LatLng(42.238061, -8.716973));
        marca = mMap.addMarker(new MarkerOptions().position(marcador));
        marca.setVisible(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            // ¿Permisos asignados?
            if (permissions.length > 0 &&
                    permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);
            } else {
                Toast.makeText(this, "Error de permisos", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        apiCliente.connect();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(apiCliente, getIndexApiAction());
    }

    @Override
    protected void onStop() {
        super.onStop();// ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(apiCliente, getIndexApiAction());
        apiCliente.disconnect();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        objetoLocalizacion = LocationServices.FusedLocationApi.getLastLocation(apiCliente);

        distanciaPremio =marcaUbicacion.distanceTo(objetoLocalizacion);
        if (((int)(distanciaPremio)) < 25){

            marca.setVisible(true);
        }
        Toast.makeText(this, "Distancia: "+((int)(distanciaPremio))+" metros", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void ocultarFragment (View view){
        instruccionesOb.setVisibility(View.INVISIBLE);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Maps Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Intent intent = new Intent(getBaseContext(), ScannerActivity.class);
        int code = 4545; // Esto puede ser cualquier código.
        startActivityForResult(intent, code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 4545){
            if (resultCode == RESULT_OK){
                String valorQR = data.getStringExtra("valorQR");
                elegirQr(valorQR);
            }
        }
    }

    /**
     * Cambia las zonas circulares en el mapa.
     * @param puntoCirculo recibe una latitud y longitud en este orden.
     */
    public void crearCirculo(LatLng puntoCirculo){
        statsCirculo = new CircleOptions()
                .center(puntoCirculo)
                .radius(100)
                .strokeColor(Color.parseColor("#6FB1E4"))
                .strokeWidth(4)
                .fillColor(Color.argb(32, 33, 150, 243));
        circulo = mMap.addCircle(statsCirculo);
        circulo.setVisible(true);
    }
    /**
     * Recibe un codigo QR y lo compara para ver si es uno de los QR que estan almacenados en variables. Tambien borra las marcas y circulos descubierta.
     * @param qrComparar recibe un String que es un codigo QR
     */
    public void elegirQr (String qrComparar) {
        if (qrComparar.equals(qr1)) {
            Toast.makeText(this, "Bien has encontrado la primera pista!!!", Toast.LENGTH_LONG).show();
            circulo.remove();
            marca.remove();
            marcador = new LatLng(42.236682, -8.711511);
            marcaUbicacion.setLatitude(marcador.latitude);
            marcaUbicacion.setLongitude(marcador.longitude);
            crearCirculo(new LatLng(42.236776, -8.712169));
        } else if (qrComparar.equals(qr2)) {
            Toast.makeText(this, "Bien has encontrado la segunda pista!!!", Toast.LENGTH_LONG).show();
            circulo.remove();
            marca.remove();
            marcador = new LatLng(42.236151, -8.714749);
            marcaUbicacion.setLatitude(marcador.latitude);
            marcaUbicacion.setLongitude(marcador.longitude);
            crearCirculo(new LatLng(42.236815, -8.714780));
        }
        else if (qrComparar.equals(qr3)){
            Toast.makeText(this,"Bien has encontrado todas las pistas!!!",Toast.LENGTH_LONG).show();
            circulo.remove();
        }
        else {
            Toast.makeText(this,"Codigo QR incorrecto",Toast.LENGTH_LONG).show();
        }
    }
}

