package ipvc.estg.cmprojeto

import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng

private lateinit var lastLocation: Location
private lateinit var fusedLocationClient: FusedLocationProviderClient
private lateinit var  locationCallback: LocationCallback
private lateinit var locationRequest: LocationRequest

class AdicionarOcorrencias : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_ocorrencias)

        //Receber dados da MapsActivity
        var intent = intent
        val idUtilizadorLogado = intent.getStringExtra("id_user")

        Log.d("***AQUI", idUtilizadorLogado)


        //inicializar fusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback(){
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                lastLocation = p0.lastLocation
                var loc = LatLng(lastLocation.latitude, lastLocation.longitude)

                Log.d("***AQUI*", "nova localização - " + loc.latitude + "-" + loc.longitude)
            }
        }

        createLocationRequest()
    }

    private fun startLocationUpdates() {
        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1)
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null )
    }

    private fun createLocationRequest(){
        locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    override fun onPause(){
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
        Log.d("***AQUI*", "onPause - removeLocationUpdates")
    }

    public override fun onResume(){
        super.onResume()
        startLocationUpdates()
        Log.d("***AQUI*", "onResume - startLocationUpdates")
    }
}
