package com.architecture.cleanmvvm.core.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.architecture.cleanmvvm.R

class UnsafeFragment : Fragment() {
    private val exitButton: AppCompatButton by lazy {
        view!!.findViewById<AppCompatButton>(R.id.btnExit)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getSetUpView(), container, false)
    }

    private fun getSetUpView(): Int {
        return R.layout.fragment_unsafe
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        exitButton.setOnClickListener { activity?.finish() }
    }
}