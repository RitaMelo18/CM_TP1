package ipvc.estg.cmprojeto

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ipvc.estg.cmprojeto.adapters.NotasAdapter
import ipvc.estg.cmprojeto.entidade.Notas
import ipvc.estg.cmprojeto.viewModel.NotasViewModel

const val  PARAM_1= "PARAM1_NAME"

class NotesActivity : AppCompatActivity() {


    private lateinit var notasViewModel: NotasViewModel
    private val newWordActivityRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        // recycler view
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = NotasAdapter(this, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // view model
        notasViewModel = ViewModelProvider(this).get(NotasViewModel::class.java)
        notasViewModel.allNotas.observe(this, Observer { notas ->
            // Update the cached copy of the words in the adapter.
            notas?.let { adapter.setNotas(it) }
        })

        //Fab
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@NotesActivity, Add_note::class.java)
            startActivityForResult(intent, newWordActivityRequestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            val ptitle = data?.getStringExtra(Add_note.EXTRA_REPLY_TITLE)
            val pdesc = data?.getStringExtra(Add_note.EXTRA_REPLY_DESC)

            if (ptitle!= null && pdesc != null) {
                val city = Notas(titulo = ptitle, descricao = pdesc)
                notasViewModel.insert(city)
            }

        } else {
           // Toast.makeText(
             //   applicationContext,
               // R.string.boxempty,
                //Toast.LENGTH_LONG).show()
        }
    }

    fun delete(id : Int?){
      notasViewModel.deleteNoteById(id)


    }
}
