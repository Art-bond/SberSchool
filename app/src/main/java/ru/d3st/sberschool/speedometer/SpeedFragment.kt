package ru.d3st.sberschool.speedometer

import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import ru.d3st.sberschool.R
import ru.d3st.sberschool.databinding.FragmentSpeedBinding


class SpeedFragment : Fragment() {

    private var _binding: FragmentSpeedBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSpeedBinding.inflate(inflater, container, false)

        val arrowAnim = animArrow()
        val colorAnim = animColor()
        val textAnim = animTextSize()
        AnimatorSet().apply {
            play(arrowAnim).with(colorAnim).with(textAnim)
            start()
        }

        return binding.root
    }

    private fun animColor(): ValueAnimator {
        val colorAnimation = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ValueAnimator.ofObject(
                ArgbEvaluator(),
                resources.getColor(R.color.green, null),
                resources.getColor(R.color.red, null)
            )
        } else {
            ValueAnimator.ofObject(
                ArgbEvaluator(),
                resources.getColor(R.color.green),
                resources.getColor(R.color.red)
            )
        }
        colorAnimation.duration = DURATION
        colorAnimation.interpolator = FastOutSlowInInterpolator()
        colorAnimation.addUpdateListener { animation ->
            val color = animation.animatedValue as Int
            binding.speedView.setTextColor(color)
        }
        return colorAnimation
    }

    private fun animArrow(): ValueAnimator {
        val anim = ValueAnimator.ofInt(0, 100)
        anim.duration = DURATION
        anim.interpolator = FastOutSlowInInterpolator()
        anim.addUpdateListener { animation ->

            val animProgress = animation.animatedValue as Int
            binding.speedView.setProgress(animProgress)

        }
        return anim

    }

    private fun animTextSize(): ValueAnimator {
        val anim = ValueAnimator.ofFloat(36F, 42F)
        anim.duration = DURATION
        anim.interpolator = FastOutSlowInInterpolator()
        anim.addUpdateListener { animation ->

            val animProgress = animation.animatedValue as Float
            binding.speedView.setTextSize(animProgress)
        }
        return anim

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val DURATION = 10000L
    }
}