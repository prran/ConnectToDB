package com.example.analysetableinmysql

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.analysetableinmysql.page.LoginPage
import kotlinx.android.synthetic.main.fragment_login.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private var transaction : FragmentTransaction? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        pageChange(LoginPage())
    }

    fun pageChange(fragment : Fragment)
    {
        var fragmentManager : FragmentManager = supportFragmentManager
        transaction = fragmentManager.beginTransaction()
        transaction?.replace(R.id.fragment_layout, fragment)!!.commitAllowingStateLoss()
    }
}
