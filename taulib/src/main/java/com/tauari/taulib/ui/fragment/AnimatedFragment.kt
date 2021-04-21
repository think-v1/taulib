package com.tauari.taulib.ui.fragment

import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import com.tauari.taulib.R

open class AnimatedFragment: Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enablebAnimation()
    }
    private fun enablebAnimation() {
        val infl = TransitionInflater.from(requireContext())
        enterTransition = infl.inflateTransition(R.transition.slide_right)
        exitTransition = infl.inflateTransition(R.transition.fade)
    }
}