package com.convertreal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.convertreal.adapter.TabAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentAdapter = TabAdapter(supportFragmentManager)
        viewpager_main.adapter = fragmentAdapter

        tabs_main.setupWithViewPager(viewpager_main)

        tabs_main.getTabAt(0)?.setIcon(R.drawable.ic_dollar)
        tabs_main.getTabAt(1)?.setIcon(R.drawable.ic_calculator)
        tabs_main.getTabAt(2)?.setIcon(R.drawable.ic_euro)

        tabs_main.getTabAt(1)?.select()
    }

}