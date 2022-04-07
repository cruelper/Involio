package com.example.involio.ui.extendedPortfolioInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import classes.network.dto.ExtendedPortfolioInfoDto
import com.example.involio.R

class TaxFragment : Fragment() {
    private lateinit var root: View
    private lateinit var extendedInfo: ExtendedPortfolioInfoDto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        extendedInfo = (requireActivity() as ExtendedInfoActivity).extendedInfo
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        root = inflater.inflate(R.layout.fragment_tax, container, false)

        root.findViewById<TextView>(R.id.tax).text = extendedInfo.tax

        return root
    }
}