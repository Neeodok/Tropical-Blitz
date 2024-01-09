package potic.troltic.blikz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import potic.troltic.blikz.databinding.CandyScreenBinding

class CandyActivity : AppCompatActivity() {

    private lateinit var binding: CandyScreenBinding
    private lateinit var gameManager: CandyGameManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CandyScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gameManager = CandyGameManager(binding, this, lifecycleScope)
        gameManager.initializeGame()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        gameManager.onBackPressed()
    }
}
