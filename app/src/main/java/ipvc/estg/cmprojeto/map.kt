package ipvc.estg.cmprojeto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.cmprojeto.adapters.UserAdapter
import ipvc.estg.cmprojeto.api.EndPoints
import ipvc.estg.cmprojeto.api.OutputPost
import ipvc.estg.cmprojeto.api.ServiceBuilder
import ipvc.estg.cmprojeto.api.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class map : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewmap)
        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.getUsers()

        call.enqueue(object : Callback<List<User>>{
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful){
                    recyclerView.apply {
                        setHasFixedSize(true)
                        layoutManager = LinearLayoutManager(this@map)
                        adapter = UserAdapter(response.body()!!)
                    }
                }
            }
            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Toast.makeText(this@map, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /*fun post(view: View) {

        val request = ServiceBuilder.buildService(EndPoints::class.java)
        val call = request.postTest("atirana24@hotmail.com")

        call.enqueue(object : Callback<OutputPost>{
            override fun onResponse(call: Call<OutputPost>, response: Response<OutputPost>) {
                if (response.isSuccessful){
                    val c: OutputPost = response.body()!!
                    Toast.makeText(this@map, c.email.toString() + "-" + c.password, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<OutputPost>, t: Throwable) {
                Toast.makeText(this@map, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }*/
}
