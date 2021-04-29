package ipvc.estg.cmprojeto

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.graphics.drawable.toBitmap
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import ipvc.estg.cmprojeto.api.EndPoints
import ipvc.estg.cmprojeto.api.ServiceBuilder
import kotlinx.android.synthetic.main.activity_markerwindow.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*
import kotlin.properties.Delegates

private lateinit var lastLocation: Location
private lateinit var fusedLocationClient: FusedLocationProviderClient
private lateinit var  locationCallback: LocationCallback
private lateinit var locationRequest: LocationRequest
val REQUEST_CODE = 1
private lateinit var image: ImageView
private var imageUri: Uri? = null


class AdicionarOcorrencias : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_ocorrencias)

        var intent = intent
        //Latitude
        val latitude = intent.getParcelableExtra<LatLng>("localizacao")!!.latitude
        //Longitude
        val longitude = intent.getParcelableExtra<LatLng>("localizacao")!!.longitude

        val id_user = intent.getStringExtra("id_user")

        Log.d("***AQUI", latitude.toString())

        image = findViewById(R.id.imagemRecebida)

        val spinner = findViewById<Spinner>(R.id.ocorrenciaRecebida)
        val adapter = ArrayAdapter.createFromResource(this, R.array.tipos, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        val button = findViewById<Button>(R.id.addOcorrencia)

        button.setOnClickListener {
            //imagem
            val imgBitmap: Bitmap = findViewById<ImageView>(R.id.imagemRecebida).drawable.toBitmap()
            val imageFile: File = convertBitmapToFile("file", imgBitmap)
            val imgFileRequest: RequestBody = RequestBody.create(MediaType.parse("image/*"), imageFile)
            val foto: MultipartBody.Part = MultipartBody.Part.createFormData("foto", imageFile.name, imgFileRequest)

            val titulo = findViewById<EditText>(R.id.tituloRecebido).text.toString()
            val tituloEnviar: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), titulo)

            val descricao = findViewById<EditText>(R.id.descricaoRecebida).text.toString()
            val descricaoEnviar: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), descricao)

            val lat = intent.getParcelableExtra<LatLng>("localizacao")!!.latitude
            val latEnviar: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), lat.toString())

            val long = intent.getParcelableExtra<LatLng>("localizacao")!!.longitude
            val longEnviar: RequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), long.toString())
            val request = ServiceBuilder.buildService(EndPoints::class.java)

            val sharedPref: SharedPreferences = getSharedPreferences(
                getString(R.string.preference_login), Context.MODE_PRIVATE
            )
            //val user: Int = sharedPref.getInt(R.string.Id_LoginUser.toString(),)
            //val userEnviar = RequestBody.create(MediaType.parse("multipart/form-data"), user.toString())

            val tipo = spinner.selectedItemId
            val tipoEnviar = RequestBody.create(MediaType.parse("multipart/form-data"), tipo.toString())



            Log.d("***AQUI",titulo )
            Log.d("***AQUI",descricao )
            Log.d("***AQUI",lat.toString() )
            Log.d("***AQUI",long.toString() )
            Log.d("***AQUI",id_user.toString() )
            Log.d("***AQUI",tipo.toString() )


        }


    }
    private fun convertBitmapToFile(fileName: String, bitmap: Bitmap): File {
        //create a file to write bitmap data
        val file = File(this@AdicionarOcorrencias.cacheDir, fileName)
        file.createNewFile()

        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, bos)
        val bitMapData = bos.toByteArray()

        //write the bytes in file
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            fos?.write(bitMapData)
            fos?.flush()
            fos?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }
    fun chooseImage(view: View) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            if (data != null){
                image.setImageURI(data.data)
            }
            //imageView.setImageURI(imageUri)
            Log.d("IMAGEM", "image " + image.toString() )
        }
    }


}
