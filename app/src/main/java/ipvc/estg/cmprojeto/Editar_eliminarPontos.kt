package ipvc.estg.cmprojeto

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.squareup.picasso.Picasso

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

}







