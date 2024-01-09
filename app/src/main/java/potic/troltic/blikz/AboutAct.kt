package potic.troltic.blikz

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import potic.troltic.blikz.databinding.AboutScreenBinding

class AboutAct:AppCompatActivity() {
    private val bin by lazy { AboutScreenBinding.inflate(layoutInflater) }
    private val btnMenu by lazy { bin.menu }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bin.root)

        btnMenu.setOnClickListener {
            val intent = Intent(this@AboutAct, MenyshkaAct::class.java).addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        or Intent.FLAG_ACTIVITY_CLEAR_TASK
            )
            startActivity(intent)
        }
    }
}
