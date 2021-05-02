package ipvc.estg.cmprojeto

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.squareup.picasso.Picasso
import ipvc.estg.cmprojeto.api.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Editar_eliminarPontos : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_eliminar_pontos)

        //Receber dados da MapsActivity
        var intent = intent
        val titulo = intent.getStringExtra("Título")
        val spinnet = intent.getStringExtra("Spinnet")

        //Atribuir o título
        findViewById<EditText>(R.id.tituloMarker).setText(titulo)

        //Dividir o spinnet recebido que contem descrição, imagem e id's
        val strs = spinnet.split("+").toTypedArray()

        //Atribuir a descrição
        findViewById<EditText>(R.id.descricaoMarker).setText(strs[0])

        //Atribuir imagem
        val imagem = findViewById<ImageView>(R.id.imagemMarker)
        Picasso.get().load(strs[1]).into(imagem)
        imagem.getLayoutParams().height = 600; // ajudtar tamanho da iamgem
        imagem.getLayoutParams().width = 700;
        imagem.requestLayout();

        //Definição dos restantes dados
        val botBar = findViewById<LinearLayout>(R.id.bottomBar2)
        val id_user = findViewById<TextView>(R.id.id_user_Marker)

        if(strs[2].toInt() == strs[3].toInt()){     // se o utilizador que reportou a anomalia for o mesmo que tem login iniciado
            botBar.visibility = (View.VISIBLE)
        }else{                                      // caso nao seja
            botBar.visibility = (View.INVISIBLE)
        }


        //Atribuir a ocorrência
        val ocorrencia = findViewById<TextView>(R.id.ocorrencia)

        if(strs[4].toInt().equals(1)){
            ocorrencia.text = "Acidente"
        } else {
            ocorrencia.text = "Obras"
        }

    }

    fun editarOcorrencias(view: View) {
        var intent = intent
        val titulo = intent.getStringExtra("Título")
        val spinnet = intent.getStringExtra("Spinnet")

        //Dividir o spinnet recebido que contem descrição, imagem e id's
        val strs = spinnet.split("+").toTypedArray()

        //Atribuir id
        val id = strs[5].toInt()

        val tituloEditado = findViewById<EditText>(R.id.tituloMarker)
        val descricaoEditada = findViewById<EditText>(R.id.descricaoMarker)


        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.editarOcorrencia(id,tituloEditado.text.toString(),descricaoEditada.text.toString())
        var intent2 = Intent(this, MapsActivity::class.java)


            if (tituloEditado.text.isNullOrEmpty() || descricaoEditada.text.isNullOrEmpty()) {

                if (tituloEditado.text.isNullOrEmpty() && !descricaoEditada.text.isNullOrEmpty()) {
                    tituloEditado.error = getString(R.string.titlebox)
                }
                if (!tituloEditado.text.isNullOrEmpty() && descricaoEditada.text.isNullOrEmpty()) {
                    descricaoEditada.error = getString(R.string.descriptionbox)
                }
                if (tituloEditado.text.isNullOrEmpty() && descricaoEditada.text.isNullOrEmpty()) {
                    tituloEditado.error = getString(R.string.titlebox)
                    descricaoEditada.error = getString(R.string.descriptionbox)
                }
            } else {
                call.enqueue(object : Callback<EditarOcorrencias> {
                    override fun onResponse(call: Call<EditarOcorrencias>, response: Response<EditarOcorrencias>) {
                        if (response.isSuccessful){
                            Toast.makeText(this@Editar_eliminarPontos, R.string.updatesuccessful, Toast.LENGTH_SHORT).show()
                            startActivity(intent2)

                        }else {
                            Toast.makeText(this@Editar_eliminarPontos, R.string.ErrorupdatePoint, Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<EditarOcorrencias>, t: Throwable) {
                        //Toast.makeText(this@Editar_eliminarPontos, "${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
    }


    fun eliminarOcorrencia(view: View){
        var intent = intent
        val titulo = intent.getStringExtra("Título")
        val spinnet = intent.getStringExtra("Spinnet")

        //Dividir o spinnet recebido que contem descrição, imagem e id's
        val strs = spinnet.split("+").toTypedArray()

        //Atribuir id
        val idRecebido = strs[5].toInt()

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.eliminarOcorrencia(idRecebido)
        var intent2 = Intent(this, MapsActivity::class.java)

        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.deletepointtitle)
        builder.setMessage(R.string.DeletePoint)
        builder.setIcon(R.drawable.ic_delete_red_24dp)
        builder.setNegativeButton(R.string.No) { dialog: DialogInterface?, which: Int ->
        }
        builder.setPositiveButton(R.string.Yes) { dialog: DialogInterface?, which: Int ->
            call.enqueue(object : Callback<EliminarOcorrencias> {
                override fun onResponse(call: Call<EliminarOcorrencias>, response: Response<EliminarOcorrencias>) {
                    if (response.isSuccessful){
                            Toast.makeText(this@Editar_eliminarPontos, R.string.deletesucessful, Toast.LENGTH_SHORT).show()
                            startActivity(intent2)
                    } else {
                        Toast.makeText(this@Editar_eliminarPontos, R.string.Errordelete, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<EliminarOcorrencias>, t: Throwable) {
                    Toast.makeText(this@Editar_eliminarPontos, "${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }


        builder.show()




    }
}







