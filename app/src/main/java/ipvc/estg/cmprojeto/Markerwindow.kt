package ipvc.estg.cmprojeto

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.squareup.picasso.Picasso

class Markerwindow(context: Context) : GoogleMap.InfoWindowAdapter {
    var mContext = context
    var mWindow = (context as Activity).layoutInflater.inflate(R.layout.activity_markerwindow, null)

    private fun rendowWindowText(marker: Marker, view: View){

        val nome = view.findViewById<TextView>(R.id.titulo_info)
        val descricao = view.findViewById<TextView>(R.id.descricao)
        val foto = view.findViewById<ImageView>(R.id.imagem)
        val botBar = view.findViewById<LinearLayout>(R.id.bottomBar)
        val id_user = view.findViewById<TextView>(R.id.utilizador)




        val strs = marker.snippet.split("+").toTypedArray() // dividir a string que contem a descricao, o url e o id do utl

        nome.text = marker.title         // aplicar o TITULO

        descricao.text = strs[0]            // aplicar a descricao

        Picasso.get().load(strs[1]).into(foto); // definir a imagem com o url


        foto.getLayoutParams().height = 450; // ajudtar tamanho da iamgem
        foto.getLayoutParams().width = 600;
        foto.requestLayout();

        if(strs[2].toInt() == strs[3].toInt()){     // se o utilizador que reportou a anomalia for o mesmo que tem login iniciado
            id_user.visibility = (View.VISIBLE)
            botBar.visibility = (View.VISIBLE)
        }else{                                      // caso nao seja
            id_user.visibility = (View.GONE)
            botBar.visibility = (View.GONE)
        }

    }

    override fun getInfoWindow(marker: Marker): View {
        rendowWindowText(marker, mWindow)
        return mWindow
    }

    override fun getInfoContents(marker: Marker): View? {
        rendowWindowText(marker, mWindow)
        return mWindow
    }


}
