package potic.troltic.blikz

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.lifecycle.LifecycleCoroutineScope

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import potic.troltic.blikz.databinding.CandyScreenBinding


class CandyGameManager(
    private val binding: CandyScreenBinding,
    private val context: Context,
    private val lifecycleScope: LifecycleCoroutineScope
) {

    private var isGameRunning = true
    private var scoreGame = 0

    private val ELEMENT_SIZE = 150
    private val GAME_TIME = 60

    private var isFallingStopped = false
    private val INITIAL_GAME_TIME = 60

    private val candyImages = listOf(
        R.drawable.symbol_1, R.drawable.symbol_2, R.drawable.symbol_3,
        R.drawable.symbol_4, R.drawable.symbol_5, R.drawable.symbol_6,
        R.drawable.mega_candy, R.drawable.ice_bomb,R.drawable.bomb,R.drawable.time_bomb,
    )

    private val explosionEffects = listOf(
        R.drawable.boom_1, R.drawable.boom_2,
        R.drawable.boom_3, R.drawable.boom_4,
        R.drawable.boom_5,R.drawable.boom_6
    )

    fun initializeGame() {
        scoreGame = 0
        binding.textScoreGame.text = context.getString(R.string.initial_score, scoreGame)
        binding.textTime.text = GAME_TIME.toString()

        binding.imgEndGame.visibility = View.INVISIBLE
        binding.restartgame.visibility = View.INVISIBLE

        binding.restartgame.setOnClickListener {
            restartGame()
        }

        startGame()
    }

    private fun startGame() {
        lifecycleScope.launch {
            var time = INITIAL_GAME_TIME
            delay(1000)
            generateElements()
            while (time > 0 && isGameRunning) {
                delay(999)
                time--
                binding.textTime.text = time.toString()
            }
            isGameRunning = false
            binding.imgEndGame.visibility = View.VISIBLE
            binding.restartgame.visibility = View.VISIBLE
        }
    }

    private fun restartGame() {
        scoreGame = 0
        binding.textScoreGame.text = context.getString(R.string.initial_score, scoreGame)
        binding.textTime.text = GAME_TIME.toString()
        binding.imgEndGame.visibility = View.INVISIBLE
        binding.restartgame.visibility = View.INVISIBLE
        isGameRunning = true
        startGame()
    }

    private fun generateElements() {
        lifecycleScope.launch {
            while (isGameRunning) {
                if (isFallingStopped) {
                    delay(300)
                    continue
                }
                val randomizing = (0 until candyImages.size).random()
                val randomElement = candyImages[randomizing]
                val imgElem = ImageView(context)
                imgElem.setImageResource(randomElement)
                imgElem.layoutParams = ViewGroup.LayoutParams(ELEMENT_SIZE, ELEMENT_SIZE)
                imgElem.x = (0 until binding.root.width - ELEMENT_SIZE).random().toFloat()
                imgElem.y = -ELEMENT_SIZE.toFloat()
                when (randomizing) {
                    6 -> imgElem.tag = "60" // супер конфета
                    7 -> imgElem.tag = "70" // Льодяна бомба
                    8 -> imgElem.tag = "80" // Звичайна бомба
                    9 -> imgElem.tag = "90" // bomb_time
                    else -> imgElem.tag = (randomizing * 10).toString()
                }
                binding.root.addView(imgElem)
                putDown(imgElem)
                delay(300)
            }
        }
    }
    fun onBackPressed() {
        val intent = Intent(context, MenyshkaAct::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
    }

    private fun putDown(imgToDown: ImageView) {
        lifecycleScope.launch {
            while (imgToDown.y <= binding.root.height && isGameRunning) {
                imgToDown.y += 20
                imgToDown.setOnClickListener {
                    clickOnElement(imgToDown)
                }
                delay(45)
            }
            binding.root.removeView(imgToDown)
        }
    }

    private fun clickOnElement(element: ImageView) {
        lifecycleScope.launch {
            if (element.tag.toString().toInt() == 70) {
                if (element.tag == "70") {
                    binding.frzEff.visibility = View.VISIBLE
                    isFallingStopped = true

                    for (i in 0 until binding.root.childCount) {
                        val child = binding.root.getChildAt(i)
                        if (child is ImageView && child.tag.toString().toInt() !in setOf(70)) {
                            child.visibility = View.INVISIBLE
                        }
                    }

                    delay(3000)

                    binding.frzEff.visibility = View.GONE
                    isFallingStopped = false

                    for (i in 0 until binding.root.childCount) {
                        val child = binding.root.getChildAt(i)
                        if (child is ImageView && child.tag.toString().toInt() !in setOf(70)) {
                            child.visibility = View.VISIBLE
                        }
                    }

                }
            } else if (element.tag.toString().toInt() == 80) {
                val explosion = AnimationUtils.loadAnimation(context, R.anim.explode)
                val boom = ImageView(context)
                boom.setImageResource(R.drawable.boom_text)
                boom.layoutParams = ViewGroup.LayoutParams(200, 200)
                boom.x = element.x
                boom.y = element.y
                binding.root.addView(boom)
                boom.startAnimation(explosion)
                delay(100)
                binding.root.removeView(boom)
                scoreGame -= 100
                binding.textScoreGame.text = scoreGame.toString()
            } else if (element.tag.toString().toInt() == 90) {
                val newTime = (binding.textTime.text.toString().toInt() - 5).coerceAtLeast(0)
                binding.textTime.text = newTime.toString()
            } else if (element.tag.toString().toInt() == 60) {
                val explosion = AnimationUtils.loadAnimation(context, R.anim.explode)
                val candy = ImageView(context)
                candy.setImageResource(R.drawable.blink)
                candy.layoutParams = ViewGroup.LayoutParams(200, 200)
                candy.x = element.x
                candy.y = element.y
                binding.root.addView(candy)
                candy.startAnimation(explosion)
                delay(100)
                binding.root.removeView(candy)
                scoreGame += 250
                binding.textScoreGame.text = scoreGame.toString()
            } else {
                explosionEffects.forEach {
                    element.setImageResource(it)
                    delay(45)
                }
                scoreGame += 50
                binding.textScoreGame.text = scoreGame.toString()
            }
            element.y = binding.root.height + 10f
        }
    }
}
