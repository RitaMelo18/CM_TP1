package ipvc.estg.cmprojeto

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
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
import ipvc.estg.cmprojeto.api.*
import kotlinx.android.synthetic.main.activity_markerwindow.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Url
import java.io.*
import java.net.URI
import kotlin.properties.Delegates
import java.net.URL
import java.util.*

val REQUEST_CODE = 1
private lateinit var image: ImageView



class AdicionarOcorrencias : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_ocorrencias)

        //Dados vindos da MapsActivity
        var intent = intent
        val id_user = intent.getStringExtra("id_user")

        //Imagem
        image = findViewById(R.id.imagemRecebida)

        //Spinner Ocorrencias
        val spinner = findViewById<Spinner>(R.id.ocorrenciaRecebida)
        val adapter = ArrayAdapter.createFromResource(this, R.array.tipos, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        val button = findViewById<Button>(R.id.addOcorrencia)

        //Quando o botão é carregado
        button.setOnClickListener {

            //Localização - as últimas coordenas recebidas no MapsActivity são enviadas para esta atividade assim q a atividade é aberta
            //Latitude
            val lat = intent.getParcelableExtra<LatLng>("localizacao")!!.latitude

            //Longitude
            val long = intent.getParcelableExtra<LatLng>("localizacao")!!.longitude

            //Título inserido
            val titulo = findViewById<EditText>(R.id.tituloRecebido)

            //Descrição inserida
            val descricao = findViewById<EditText>(R.id.descricaoRecebida)

            //Tipo de ocorrência selecionado
            val tipo = spinner.selectedItemId

            //Nome da imagem inserido
            val nomeImagem = findViewById<EditText>(R.id.nomeImagem)


            //Validações
            if(titulo.text.isNullOrEmpty() || descricao.text.isNullOrEmpty() ||tipo.toString().equals("0") || nomeImagem.text.isNullOrEmpty()){
                if(titulo.text.isNullOrEmpty()){
                    titulo.error = getString(R.string.titlebox)
                }
                if(descricao.text.isNullOrEmpty()){
                    descricao.error = getString(R.string.descriptionbox)
                    Log.d("***AQUI", tipo.toString())

                }
                if (tipo.toString().equals("0")){
                    Toast.makeText(this@AdicionarOcorrencias, R.string.selectType, Toast.LENGTH_SHORT).show()
                }
                if (nomeImagem.text.isNullOrEmpty()){
                    nomeImagem.error = getString(R.string.Insertimagename)
                }

            } else {

                //URL da imagem (webhost)
                var imageUrl = "https://cm22059.000webhostapp.com/myslim/api/imagens/" + nomeImagem.text.toString() + ".JPG"

                //Codificar imagem
                val bos = ByteArrayOutputStream()
                val imagem: Bitmap = (image.drawable as BitmapDrawable).bitmap
                imagem.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                val encodedImage: String = Base64.getEncoder().encodeToString(bos.toByteArray())


                val request = ServiceBuilder.buildService(EndPoints::class.java)
                val call = request.adicionarOcorrencia(lat.toString(), long.toString(), titulo.text.toString(), descricao.text.toString(), imageUrl.toString(),id_user.toInt(),tipo.toInt(),encodedImage,nomeImagem.text.toString())
                var intent = Intent(this, MapsActivity::class.java)


                Log.d("***AQUI",encodedImage )


                call.enqueue(object : Callback<Pontos_adicionar> {
                    override fun onResponse(call: Call<Pontos_adicionar>, response: Response<Pontos_adicionar>) {
                        if (response.isSuccessful){

                            Toast.makeText(this@AdicionarOcorrencias, R.string.savedsucessfull, Toast.LENGTH_SHORT).show()
                            startActivity(intent)


                        } else {
                            Toast.makeText(this@AdicionarOcorrencias, R.string.errorSave, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Pontos_adicionar>, t: Throwable) {
                        //Toast.makeText(this@Editar_eliminarPontos, "${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })

            }



        }


    }
    //Escolher a imagem
    fun chooseImage(view: View) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    //Atribuir a imagem selecionada à Image View
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