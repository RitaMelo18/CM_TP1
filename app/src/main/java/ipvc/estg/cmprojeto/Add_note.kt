package ipvc.estg.cmprojeto

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText


class Add_note : AppCompatActivity() {

    private lateinit var titleText: EditText
    private lateinit var descripText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        titleText = findViewById(R.id.insertTitle)
        descripText = findViewById(R.id.insertDescription)

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(titleText.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                replyIntent.putExtra(EXTRA_REPLY_TITLE, titleText.text.toString())
                replyIntent.putExtra(EXTRA_REPLY_DESC, descripText.text.toString())
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY_TITLE = "com.example.android.city"
        const val EXTRA_REPLY_DESC = "com.example.android.country"
    }
}