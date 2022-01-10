package com.example.involio.ui.tabbs

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.involio.R

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2,
    R.string.tab_text_3,
    R.string.tab_text_4
    )

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> FirstTabFragment()
            1 -> SecondTabFragment()
            2 -> ThirdTabFragment()
            3 -> FourthTabFragment()
            else -> throw IllegalStateException ("Bad tab position in tabbed stock content")
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return TAB_TITLES.size
    }
}