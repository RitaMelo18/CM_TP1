package ipvc.estg.cmprojeto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.cmprojeto.adapters.NotasAdapter


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun notespage(view: View) {
        var intent = Intent(this, NotesActivity::class.java)
        startActivity(intent)
    }

    fun map(view: View) {
        var intent = Intent(this, map::class.java)
        startActivity(intent)
    }

}
