package com.android.example.destinyreader

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.example.destinyreader.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)



    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun setActionBarTitle(string : String) {
        supportActionBar?.setTitle(string)
    }



}
