package com.android.example.destinyreader

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.android.example.destinyreader.api.DestinyAPI
import com.android.example.destinyreader.api.ManifestResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getManifest()
        setContentView(R.layout.main_activity)



    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun getManifest(){
        DestinyAPI.retrofitService.getManifest().enqueue(
            object : Callback<ManifestResponse> {
                override fun onFailure(call: Call<ManifestResponse>, t: Throwable) {
                    Log.i("destinyreader", "Request Failure : " + t.message)
                }

                override fun onResponse(
                    call: Call<ManifestResponse>,
                    response: Response<ManifestResponse>
                ) {
                    Log.i("destinyreader", "Request Success")
                    Log.i("destinyreader", response.body()?.version ?: "null")
                }
            }
        )
    }



}
