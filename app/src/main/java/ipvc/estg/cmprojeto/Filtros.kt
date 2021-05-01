package ipvc.estg.cmprojeto

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ipvc.estg.cmprojeto.api.EndPoints
import ipvc.estg.cmprojeto.api.Pontos
import ipvc.estg.cmprojeto.api.ServiceBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Filtros : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.filtros)
    }

    fun redefinir(view: View){
        var intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }

    fun aplicarfiltros(view: View){
        var intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)

        }


    }

