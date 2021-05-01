package ipvc.estg.cmprojeto

import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import ipvc.estg.cmprojeto.api.EndPoints
import ipvc.estg.cmprojeto.api.Pontos
import ipvc.estg.cmprojeto.api.ServiceBuilder
import ipvc.estg.cmprojeto.api.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.google.android.gms.location.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.filtros.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var pontos: List<Pontos>
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var  locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest

    private var clicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        //inicializar fusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback(){
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                lastLocation = p0.lastLocation
                var loc = LatLng(lastLocation.latitude, lastLocation.longitude)
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15.0f))
                Log.d("***AQUI", "nova localização - " + loc.latitude + "-" + loc.longitude)
            }
        }

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getPontos()
        var position: LatLng
        val sharedPref: SharedPreferences = getSharedPreferences(
            getString(R.string.preference_login), Context.MODE_PRIVATE
        )

        call.enqueue(object : Callback<List<Pontos>>{
            override fun onResponse(call: Call<List<Pontos>>, response: Response<List<Pontos>>) {
                if (response.isSuccessful){
                    pontos = response.body()!!
                    for(ponto in pontos){
                        position = LatLng(ponto.latitude.toString().toDouble(), ponto.longitude.toString().toDouble())
                        if (ponto.id_user.equals(sharedPref.all[getString(R.string.Id_LoginUser)])){

                            mMap.addMarker(MarkerOptions()
                                .position(position)
                                .title(ponto.nome)
                                .snippet(ponto.descricao + "+" + ponto.foto + "+" + ponto.id_user + "+" + sharedPref.all[getString(R.string.Id_LoginUser)].toString() + "+" + ponto.id_ocorrencia + "+" + ponto.id + "+" + ponto.latitude.toString().toDouble() + "+" +ponto.longitude.toString().toDouble() )
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))

                            )
                        }else {
                            mMap.addMarker(
                                MarkerOptions()
                                    .position(position)
                                    .title(ponto.nome)
                                    .snippet(ponto.descricao + "+" + ponto.foto + "+" + ponto.id_user + "+" + sharedPref.all[getString(R.string.Id_LoginUser)].toString() + "+" + ponto.id_ocorrencia + "+" + ponto.id + "+" + ponto.latitude.toString().toDouble() + "+" +ponto.longitude.toString().toDouble())
                            )
                        }
                    }
                }
            }
            override fun onFailure(call: Call<List<Pontos>>, t: Throwable) {
                Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

        val acidentes = findViewById<FloatingActionButton>(R.id.Acidente3)
        acidentes.setOnClickListener {
            mMap.clear()

            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.filtrosOcorrencias(1)
            var position: LatLng
            val sharedPref: SharedPreferences = getSharedPreferences(
                getString(R.string.preference_login), Context.MODE_PRIVATE
            )

            call.enqueue(object : Callback<List<Pontos>> {
                override fun onResponse(call: Call<List<Pontos>>, response: Response<List<Pontos>>) {
                    if (response.isSuccessful){
                        pontos = response.body()!!
                        for(ponto in pontos){
                            position = LatLng(ponto.latitude.toString().toDouble(), ponto.longitude.toString().toDouble())
                            if (ponto.id_user.equals(sharedPref.all[getString(R.string.Id_LoginUser)])){

                                mMap.addMarker(
                                    MarkerOptions()
                                        .position(position)
                                        .title(ponto.nome)
                                        .snippet(ponto.descricao + "+" + ponto.foto + "+" + ponto.id_user + "+" + sharedPref.all[getString(R.string.Id_LoginUser)].toString() + "+" + ponto.id_ocorrencia + "+" + ponto.id + "+" + ponto.latitude.toString().toDouble() + "+" +ponto.longitude.toString().toDouble() )
                                        .icon(
                                            BitmapDescriptorFactory.defaultMarker(
                                                BitmapDescriptorFactory.HUE_AZURE))

                                )
                            }else {
                                mMap.addMarker(
                                    MarkerOptions()
                                        .position(position)
                                        .title(ponto.nome)
                                        .snippet(ponto.descricao + "+" + ponto.foto + "+" + ponto.id_user + "+" + sharedPref.all[getString(R.string.Id_LoginUser)].toString() + "+" + ponto.id_ocorrencia + "+" + ponto.id + "+" + ponto.latitude.toString().toDouble() + "+" +ponto.longitude.toString().toDouble())
                                )
                            }
                        }
                    }
                }
                override fun onFailure(call: Call<List<Pontos>>, t: Throwable) {
                    Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
        val obras = findViewById<FloatingActionButton>(R.id.Obras3)
        obras.setOnClickListener {
            mMap.clear()

            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.filtrosOcorrencias(2)
            var position: LatLng
            val sharedPref: SharedPreferences = getSharedPreferences(
                getString(R.string.preference_login), Context.MODE_PRIVATE
            )

            call.enqueue(object : Callback<List<Pontos>> {
                override fun onResponse(call: Call<List<Pontos>>, response: Response<List<Pontos>>) {
                    if (response.isSuccessful){
                        pontos = response.body()!!
                        for(ponto in pontos){
                            position = LatLng(ponto.latitude.toString().toDouble(), ponto.longitude.toString().toDouble())
                            if (ponto.id_user.equals(sharedPref.all[getString(R.string.Id_LoginUser)])){

                                mMap.addMarker(
                                    MarkerOptions()
                                        .position(position)
                                        .title(ponto.nome)
                                        .snippet(ponto.descricao + "+" + ponto.foto + "+" + ponto.id_user + "+" + sharedPref.all[getString(R.string.Id_LoginUser)].toString() + "+" + ponto.id_ocorrencia + "+" + ponto.id + "+" + ponto.latitude.toString().toDouble() + "+" +ponto.longitude.toString().toDouble() )
                                        .icon(
                                            BitmapDescriptorFactory.defaultMarker(
                                                BitmapDescriptorFactory.HUE_AZURE))

                                )
                            }else {
                                mMap.addMarker(
                                    MarkerOptions()
                                        .position(position)
                                        .title(ponto.nome)
                                        .snippet(ponto.descricao + "+" + ponto.foto + "+" + ponto.id_user + "+" + sharedPref.all[getString(R.string.Id_LoginUser)].toString() + "+" + ponto.id_ocorrencia + "+" + ponto.id + "+" + ponto.latitude.toString().toDouble() + "+" +ponto.longitude.toString().toDouble())
                                )
                            }
                        }
                    }
                }
                override fun onFailure(call: Call<List<Pontos>>, t: Throwable) {
                    Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        //Repor dados do mapa
        val all = findViewById<FloatingActionButton>(R.id.Todos)
        all.setOnClickListener {
            mMap.clear()
            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.getPontos()
            var position: LatLng
            val sharedPref: SharedPreferences = getSharedPreferences(
                getString(R.string.preference_login), Context.MODE_PRIVATE
            )

            call.enqueue(object : Callback<List<Pontos>>{
                override fun onResponse(call: Call<List<Pontos>>, response: Response<List<Pontos>>) {
                    if (response.isSuccessful){
                        pontos = response.body()!!
                        for(ponto in pontos){
                            position = LatLng(ponto.latitude.toString().toDouble(), ponto.longitude.toString().toDouble())
                            if (ponto.id_user.equals(sharedPref.all[getString(R.string.Id_LoginUser)])){

                                mMap.addMarker(MarkerOptions()
                                    .position(position)
                                    .title(ponto.nome)
                                    .snippet(ponto.descricao + "+" + ponto.foto + "+" + ponto.id_user + "+" + sharedPref.all[getString(R.string.Id_LoginUser)].toString() + "+" + ponto.id_ocorrencia + "+" + ponto.id + "+" + ponto.latitude.toString().toDouble() + "+" +ponto.longitude.toString().toDouble() )
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))

                                )
                            }else {
                                mMap.addMarker(
                                    MarkerOptions()
                                        .position(position)
                                        .title(ponto.nome)
                                        .snippet(ponto.descricao + "+" + ponto.foto + "+" + ponto.id_user + "+" + sharedPref.all[getString(R.string.Id_LoginUser)].toString() + "+" + ponto.id_ocorrencia + "+" + ponto.id + "+" + ponto.latitude.toString().toDouble() + "+" +ponto.longitude.toString().toDouble())
                                )
                            }
                        }
                    }
                }
                override fun onFailure(call: Call<List<Pontos>>, t: Throwable) {
                    Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
        //Até 5km
        val km5 = findViewById<FloatingActionButton>(R.id.Distancia5km)
        km5.setOnClickListener {
            mMap.clear()
            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.getPontos()
            var position: LatLng
            val sharedPref: SharedPreferences = getSharedPreferences(
                getString(R.string.preference_login), Context.MODE_PRIVATE
            )

            call.enqueue(object : Callback<List<Pontos>>{
                override fun onResponse(call: Call<List<Pontos>>, response: Response<List<Pontos>>) {
                    if (response.isSuccessful){
                        pontos = response.body()!!
                        for(ponto in pontos){
                            val distancia = calculateDistance(ponto.latitude.toDouble(), ponto.longitude.toDouble(),lastLocation.latitude, lastLocation.longitude)
                            position = LatLng(ponto.latitude.toString().toDouble(), ponto.longitude.toString().toDouble())
                            if(distancia < 500){
                                if (ponto.id_user.equals(sharedPref.all[getString(R.string.Id_LoginUser)])){

                                    mMap.addMarker(MarkerOptions()
                                        .position(position)
                                        .title(ponto.nome)
                                        .snippet(ponto.descricao + "+" + ponto.foto + "+" + ponto.id_user + "+" + sharedPref.all[getString(R.string.Id_LoginUser)].toString() + "+" + ponto.id_ocorrencia + "+" + ponto.id + "+" + ponto.latitude.toString().toDouble() + "+" +ponto.longitude.toString().toDouble() )
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))

                                    )
                                }else {
                                    mMap.addMarker(
                                        MarkerOptions()
                                            .position(position)
                                            .title(ponto.nome)
                                            .snippet(ponto.descricao + "+" + ponto.foto + "+" + ponto.id_user + "+" + sharedPref.all[getString(R.string.Id_LoginUser)].toString() + "+" + ponto.id_ocorrencia + "+" + ponto.id + "+" + ponto.latitude.toString().toDouble() + "+" +ponto.longitude.toString().toDouble())
                                    )
                                }
                            }

                        }
                    }
                }
                override fun onFailure(call: Call<List<Pontos>>, t: Throwable) {
                    Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

        //Até 10km
        val km10 = findViewById<FloatingActionButton>(R.id.Distancia10km)
        km10.setOnClickListener {
            mMap.clear()
            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.getPontos()
            var position: LatLng
            val sharedPref: SharedPreferences = getSharedPreferences(
                getString(R.string.preference_login), Context.MODE_PRIVATE
            )

            call.enqueue(object : Callback<List<Pontos>>{
                override fun onResponse(call: Call<List<Pontos>>, response: Response<List<Pontos>>) {
                    if (response.isSuccessful){
                        pontos = response.body()!!
                        for(ponto in pontos){
                            val distancia = calculateDistance(ponto.latitude.toDouble(), ponto.longitude.toDouble(),lastLocation.latitude, lastLocation.longitude)
                            position = LatLng(ponto.latitude.toString().toDouble(), ponto.longitude.toString().toDouble())
                            if(distancia < 5000){
                                if (ponto.id_user.equals(sharedPref.all[getString(R.string.Id_LoginUser)])){

                                    mMap.addMarker(MarkerOptions()
                                        .position(position)
                                        .title(ponto.nome)
                                        .snippet(ponto.descricao + "+" + ponto.foto + "+" + ponto.id_user + "+" + sharedPref.all[getString(R.string.Id_LoginUser)].toString() + "+" + ponto.id_ocorrencia + "+" + ponto.id + "+" + ponto.latitude.toString().toDouble() + "+" +ponto.longitude.toString().toDouble() )
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))

                                    )
                                }else {
                                    mMap.addMarker(
                                        MarkerOptions()
                                            .position(position)
                                            .title(ponto.nome)
                                            .snippet(ponto.descricao + "+" + ponto.foto + "+" + ponto.id_user + "+" + sharedPref.all[getString(R.string.Id_LoginUser)].toString() + "+" + ponto.id_ocorrencia + "+" + ponto.id + "+" + ponto.latitude.toString().toDouble() + "+" +ponto.longitude.toString().toDouble())
                                    )
                                }
                            }

                        }
                    }
                }
                override fun onFailure(call: Call<List<Pontos>>, t: Throwable) {
                    Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
        val botao2 = findViewById<FloatingActionButton>(R.id.Acidente3)
        val botaosuperior = findViewById<FloatingActionButton>(R.id.fab3)
        botaosuperior.setOnClickListener {
            onAddButtonClicked()
        }

        createLocationRequest()

    }

    private fun onAddButtonClicked(){
        setVisibility(clicked)
        clicked = !clicked
    }

    private fun setVisibility(clicked: Boolean){
        val botao2 = findViewById<FloatingActionButton>(R.id.Acidente3)
        val botao3 = findViewById<FloatingActionButton>(R.id.Obras3)
        val distancia5 = findViewById<FloatingActionButton>(R.id.Distancia5km)
        val distancia10 = findViewById<FloatingActionButton>(R.id.Distancia10km)
        val repor = findViewById<FloatingActionButton>(R.id.Todos)
        if(!clicked){
            botao2.visibility=View.VISIBLE
            botao3.visibility=View.VISIBLE
            distancia5.visibility=View.VISIBLE
            distancia10.visibility=View.VISIBLE
            repor.visibility=View.VISIBLE
        } else {
            botao2.visibility=View.INVISIBLE
            botao3.visibility=View.INVISIBLE
            distancia5.visibility=View.INVISIBLE
            distancia10.visibility=View.INVISIBLE
            repor.visibility=View.INVISIBLE
        }
    }


    fun calculateDistance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lng1, lat2, lng2, results)
        // distance in meter
        return results[0]
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
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setUpMap()
        // Add a marker in Sydney and move the camera
        val zone = LatLng(41.6946, -8.83016)
        val zoomLevel = 15f
        var intent = Intent(this, MainActivity::class.java)

        /* mMap.moveCamera(CameraUpdateFactory.newLatLng(zone))*/
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(zone, zoomLevel))
        mMap.setInfoWindowAdapter(Markerwindow(this))

        //Passar a informação quando a InfoWindow é clicada
        mMap.setOnInfoWindowClickListener { marker ->
            val intent = Intent(this, Editar_eliminarPontos::class.java).apply{
                putExtra("Título", marker.title)
                putExtra("Spinnet", marker.snippet)
            }
            startActivity(intent)
        }


    }

    private fun setUpMap() {
        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        }else{
            mMap.isMyLocationEnabled = true

            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if(location != null){
                    lastLocation = location
                    //Toast.makeText(this@MapsActivity, lastLocation.toString(), Toast.LENGTH_SHORT).show()
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                }
            }
        }
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
        Log.d("***AQUI", "onPause - removeLocationUpdates")
    }

    public override fun onResume(){
        super.onResume()
        startLocationUpdates()
        Log.d("***AQUI", "onResume - startLocationUpdates")
    }


    fun logout(view: View) {

        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.Logout)
        builder.setMessage(R.string.LogoutMessage)
        builder.setIcon(R.drawable.ic_exit_to_app_black_24dp)
        builder.setPositiveButton(R.string.Yes) { dialog: DialogInterface?, which: Int ->
            //Fab
            val sharedPref: SharedPreferences = getSharedPreferences(
                getString(R.string.preference_login), Context.MODE_PRIVATE
            )
            with(sharedPref.edit()){
                putBoolean(getString(R.string.LoginShared), false)
                putString(getString(R.string.EmailShared), "")
                commit()
            }
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        builder.setNegativeButton(R.string.No) { dialog: DialogInterface?, which: Int ->}
        builder.show()


    }

    override fun onBackPressed() {
        //nothing
        Toast.makeText(this@MapsActivity, R.string.Back, Toast.LENGTH_SHORT).show()
    }

    fun adicionarOcorrencia(view: View) {

        //Abrir o shared preferences
        val sharedPref: SharedPreferences = getSharedPreferences(
            getString(R.string.preference_login), Context.MODE_PRIVATE
        )
        //Utilizador com sessão iniciada
        var idUtilizadorLogado = sharedPref.all[getString(R.string.Id_LoginUser)]

        val intent = Intent(this, AdicionarOcorrencias::class.java).apply {
            putExtra("id_user", idUtilizadorLogado.toString())
            putExtra("localizacao", LatLng(lastLocation.latitude, lastLocation.longitude))
        }
        startActivity(intent)


    }

    fun filtros(view: View) {
        var intent = Intent(this, Filtros::class.java)
        startActivity(intent)
    }

    fun filtrosMapa(view: View){
        val acidentes = findViewById<RadioButton>(R.id.Acidente)
        acidentes.setOnClickListener {
            mMap.clear()

            val request = ServiceBuilder.buildService(EndPoints::class.java)
            val call = request.filtrosOcorrencias(1)
            var position: LatLng
            val sharedPref: SharedPreferences = getSharedPreferences(
                getString(R.string.preference_login), Context.MODE_PRIVATE
            )

            call.enqueue(object : Callback<List<Pontos>> {
                override fun onResponse(call: Call<List<Pontos>>, response: Response<List<Pontos>>) {
                    if (response.isSuccessful){
                        pontos = response.body()!!
                        for(ponto in pontos){
                            position = LatLng(ponto.latitude.toString().toDouble(), ponto.longitude.toString().toDouble())
                            if (ponto.id_user.equals(sharedPref.all[getString(R.string.Id_LoginUser)])){

                                mMap.addMarker(
                                    MarkerOptions()
                                        .position(position)
                                        .title(ponto.nome)
                                        .snippet(ponto.descricao + "+" + ponto.foto + "+" + ponto.id_user + "+" + sharedPref.all[getString(R.string.Id_LoginUser)].toString() + "+" + ponto.id_ocorrencia + "+" + ponto.id + "+" + ponto.latitude.toString().toDouble() + "+" +ponto.longitude.toString().toDouble() )
                                        .icon(
                                            BitmapDescriptorFactory.defaultMarker(
                                                BitmapDescriptorFactory.HUE_AZURE))

                                )
                            }else {
                                mMap.addMarker(
                                    MarkerOptions()
                                        .position(position)
                                        .title(ponto.nome)
                                        .snippet(ponto.descricao + "+" + ponto.foto + "+" + ponto.id_user + "+" + sharedPref.all[getString(R.string.Id_LoginUser)].toString() + "+" + ponto.id_ocorrencia + "+" + ponto.id + "+" + ponto.latitude.toString().toDouble() + "+" +ponto.longitude.toString().toDouble())
                                )
                            }
                        }
                    }
                }
                override fun onFailure(call: Call<List<Pontos>>, t: Throwable) {
                    Toast.makeText(this@MapsActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }


    }

}
