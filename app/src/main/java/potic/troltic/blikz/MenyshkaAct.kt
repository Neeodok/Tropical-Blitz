package potic.troltic.blikz

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import potic.troltic.blikz.databinding.MenyskaScrBinding


class MenyshkaAct :AppCompatActivity(){
    private val binding by lazy { MenyskaScrBinding.inflate(layoutInflater) }
    private val btnGame by lazy {binding.btnStart  }
    private val btnBack by lazy {binding.exit  }
    private val btnRules by lazy {binding.rules  }
    private val btnPoli by lazy {binding.btnPoli  }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        btnGame.setOnClickListener {
            animClickView(it,this)
            val intent = Intent(this@MenyshkaAct, CandyActivity::class.java)
            startActivity(intent)
        }

        btnRules.setOnClickListener {
            animClickView(it,this)
            val intent = Intent(this@MenyshkaAct, AboutAct::class.java)
            startActivity(intent)

        }



        btnBack.setOnClickListener {
            showExitDialog(this@MenyshkaAct)
        }
        btnPoli.setOnClickListener {
            openWebPage("https://sites.google.com/view/tropicalblitz/%D0%B3%D0%BE%D0%BB%D0%BE%D0%B2%D0%BD%D0%B0-%D1%81%D1%82%D0%BE%D1%80%D1%96%D0%BD%D0%BA%D0%B0")
        }

    }

    private fun openWebPage(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "No web browser found", Toast.LENGTH_SHORT).show()
        }
    }
}
