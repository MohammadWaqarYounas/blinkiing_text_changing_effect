package com.tagify.typewritereffect

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import com.tagify.typewritereffect.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // List of text variations
    private val textVariations = listOf(
        "Find a friend...", "Find a colleague...", "Find a Stranger...", "Connect with them..."
    )

    private val handler = Handler()
    private val initialDelay = 2000L // Stay on screen for 2 seconds (in milliseconds)
    private val fadeInDuration = 1500L // Fade in duration (in milliseconds)
    private val fadeOutDuration = 1500L // Fade out duration (in milliseconds)
    private var currentVariationIndex = 0
    private var isAnimating = false
    private var isFirstAnimation = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val label="Lava G kia killa thukao gy kia!"
        val textView = findViewById<TextView>(R.id.quote)
        val mainHandler = Handler(Looper.getMainLooper())


//        for (iteration in 0 until 5) {
//
//            Thread {
//                val stringBuilder = StringBuilder()
//
//                for (letter in label) {
//                    stringBuilder.append(letter)
//                    Thread.sleep(100)
//
//                    mainHandler.post {
//                        textView.text = stringBuilder.toString()
//                    }
//                }
//
//                // Clear the textView after completing the sentence
//                mainHandler.post {
//                    textView.text = ""
//                }
//
//                // Sleep for a while before starting to write the next sentence
//                Thread.sleep(100) // Adjust the duration as needed
//            }.start()
//        }
        startTextAnimation()
//

    }

    private fun startTextAnimation() {
        if (isAnimating) return

        val currentText = textVariations[currentVariationIndex]
        val nextVariationIndex = (currentVariationIndex + 1) % textVariations.size
        val nextText = textVariations[nextVariationIndex]

        // Animate the text change
        animateTextChange(currentText, nextText)

        currentVariationIndex = nextVariationIndex

        if (isFirstAnimation) {
            // Set the flag to false to start waiting for 2 seconds
            isFirstAnimation = false

            // Wait for 2 seconds (initialDelay) before starting the next animation
            handler.postDelayed({
                startTextAnimation()
            }, initialDelay)
        }
    }

    private fun animateTextChange(currentText: String, nextText: String) {
        if (isAnimating) return
        isAnimating = true

        // Animate the text change and alpha (fade)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), 0, 255)
        colorAnimation.duration =
            fadeInDuration + fadeOutDuration // Total duration (in milliseconds)

        colorAnimation.addUpdateListener { animator ->
            val alpha = animator.animatedValue as Int
            binding!!.quote.alpha = alpha / 255f

            // Update the text with the current alpha value
            updateTextWithAlpha(binding!!.quote, currentText, nextText, alpha)
        }

        colorAnimation.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {

            }

            override fun onAnimationEnd(p0: Animator) {
                // Update the text with the final alpha value
                updateTextWithAlpha(binding!!.quote, currentText, nextText, 255)

                // Wait for 1 second (fadeOutDuration) before starting the next animation
                handler.postDelayed({
                    isAnimating = false
                    startTextAnimation()
                }, fadeOutDuration)

            }

            override fun onAnimationCancel(p0: Animator) {
                TODO("Not yet implemented")
            }

            override fun onAnimationRepeat(p0: Animator) {
                TODO("Not yet implemented")
            }
        })

        colorAnimation.start()
    }

    private fun updateTextWithAlpha(
        textView: TextView, currentText: String, nextText: String, alpha: Int
    ) {
        val interpolatedText = if (alpha == 0) nextText else currentText
        textView.text = interpolatedText
    }
}