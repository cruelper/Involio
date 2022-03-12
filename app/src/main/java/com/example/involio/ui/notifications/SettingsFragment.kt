package com.example.involio.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.involio.R

class SettingsFragment : Fragment() {

    var root: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        root = inflater.inflate(R.layout.fragment_settings, container, false)

        val toolbar: Toolbar = root!!.findViewById(R.id.toolbar)
        val curActivity = (requireActivity() as AppCompatActivity)
        curActivity.setSupportActionBar(toolbar)
        curActivity.supportActionBar!!.setDisplayShowHomeEnabled(false)
        curActivity.supportActionBar!!.title = "Настройки"


        return root
    }
}