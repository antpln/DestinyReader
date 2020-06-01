package com.plin.destinyreader

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.github.kittinunf.fuel.Fuel
import com.google.android.material.snackbar.Snackbar
import com.plin.destinyreader.api.DestinyAPI
import com.plin.destinyreader.api.ManifestResponse
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream


class MainActivity : AppCompatActivity() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    val manifestUrl = MutableLiveData<String>("")
    var lastDatabaseVersion: String = ""
    private val progressBar = lazy {
        findViewById<ProgressBar>(R.id.progressBar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)

    }

    override fun onStart() {
        super.onStart()
        val isDatabaseOnDevice: Boolean =
            (File::exists)(File(applicationInfo.dataDir + "/databases/destiny_manifest.db"))

        if (isDatabaseOnDevice) {
            goToMainDisplay()
        } else {
            Thread({ getDownloadUrl(); downloadManifest() }).start()
        }

    }

    fun goToMainDisplay() {
        val controller = Navigation.findNavController(this, R.id.nav_host_fragment)
        if (controller.currentDestination?.id == R.id.startFragment) {
            controller.navigate(R.id.action_startFragment_to_mainFragment)
        }
        checkForUpdates()
        Log.i("destinyreader", "PATH " + applicationInfo.dataDir)
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onSupportNavigateUp()
        onBackPressed()
        return true
    }

    fun getDownloadUrl() {
        //Do some Network Request
        val manifestResponse = DestinyAPI.retrofitService.getManifest().execute()
        if (manifestResponse.isSuccessful) {
            Log.i("destinyreader", "Request Success")
            lastDatabaseVersion = requireNotNull(manifestResponse.body()?.response?.version)
            Log.i("destinyreader", "Last database version : " + lastDatabaseVersion)
            if (isDownloadNeeded(lastDatabaseVersion)) {
                Log.i("destinyreader", "DOWNLOAD NEEDED")
                //TODO : Internationalise

                val paths =
                    requireNotNull(manifestResponse.body()?.response?.mobileWorldContentPaths)
                val Url = when (Locale.getDefault().language) {
                    "ja" -> paths.ja
                    "de" -> paths.de
                    "ko" -> paths.ko
                    "it" -> paths.it
                    "de" -> paths.de
                    "es" -> paths.es
                    "es-mx" -> paths.es_mx
                    "fr" -> paths.fr
                    else -> paths.en
                }
                manifestUrl.postValue(Url)
            }
        } else {
            Log.i("destinyreader", "Request Failure : " + manifestResponse.errorBody().toString())
        }
    }

    fun checkForUpdates() {

        val manifestResponse = DestinyAPI.retrofitService.getManifest().enqueue(
            object : Callback<ManifestResponse> {
                override fun onFailure(call: Call<ManifestResponse>, t: Throwable) {
                    Log.i("destinyreader", "Request Failure : " + t.message)
                }

                override fun onResponse(
                    call: Call<ManifestResponse>,
                    response: Response<ManifestResponse>
                ) {
                    Log.i("destinyreader", "Request Success")
                    lastDatabaseVersion = requireNotNull(response.body()?.response?.version)
                    Log.i("destinyreader", "Last database version : '" + lastDatabaseVersion + "'")
                    if (isDownloadNeeded(lastDatabaseVersion)) {
                        Log.i("destinyreader", "DOWNLOAD NEEDED")
                        //TODO : Internationalise

                        val paths =
                            requireNotNull(response.body()?.response?.mobileWorldContentPaths)
                        manifestUrl.value = when (Locale.getDefault().language) {
                            "ja" -> paths.ja
                            "de" -> paths.de
                            "ko" -> paths.ko
                            "it" -> paths.it
                            "de" -> paths.de
                            "es" -> paths.es
                            "es-mx" -> paths.es_mx
                            "fr" -> paths.fr
                            else -> paths.en
                        }
                        val mySnackbar = Snackbar.make(
                            findViewById(R.id.nav_host_fragment),
                            getString(R.string.update_required),
                            Snackbar.LENGTH_LONG
                        )
                        mySnackbar.setAction(
                            getString(R.string.update_now),
                            UpdateSnackbarListener { updateManifest() }
                        )
                        mySnackbar.show()

                    }
                }
            }
        )

    }

    fun updateManifest() {
        Log.i("destinyreader", "Update called")
        Log.i("destinyreader", "Update Url " + "https://www.bungie.net" + manifestUrl.value)
        progressBar.value.visibility = View.VISIBLE
        Fuel.download("https://www.bungie.net${manifestUrl.value}")
            .fileDestination { response, url ->
                File(cacheDir, "temp_database.zip")
            }
            .progress { readBytes, totalBytes ->
                progressBar.value.progress = readBytes.toInt(); progressBar.value.max =
                totalBytes.toInt()
            }
            .response { request, response, result ->
                progressBar.value.visibility = View.GONE
                Log.i("destinyreader", response.statusCode.toString())
                if (response.statusCode != 200) {
                    Snackbar.make(
                        findViewById(R.id.nav_host_fragment),
                        "Une erreur est survenue lors de la mise à jour",
                        Snackbar.LENGTH_LONG
                    ).show()
                } else {
                    uiScope.launch {
                        unzip(applicationInfo.dataDir + "/cache/temp_database.zip")
                        getSharedPreferences("DestinyReaderPrefs", Context.MODE_PRIVATE).edit()
                            .putString("database_version", lastDatabaseVersion)
                            .putBoolean("update", true)
                            .apply()

                        Snackbar.make(
                            findViewById(R.id.nav_host_fragment),
                            getString(R.string.update_succes),
                            Snackbar.LENGTH_INDEFINITE
                        )
                            .setAction(
                                getString(R.string.restart),
                                UpdateSnackbarListener { finishAffinity() }).show()
                    }
                }
            }
    }

    fun downloadManifest() {
        getDownloadUrl()
        Log.i("destinyreader", "Download called")
        val Url = manifestUrl.value

        Log.i("destinyreader", "Download Url " + "https://www.bungie.net" + Url)
        uiScope.launch { progressBar.value.visibility = View.VISIBLE }
        Fuel.download("https://www.bungie.net$Url")
            .fileDestination { response, url ->
                File(cacheDir, "temp_database.zip")
            }
            .progress { readBytes, totalBytes ->
                progressBar.value.progress = readBytes.toInt(); progressBar.value.max =
                totalBytes.toInt()
            }
            .response { request, response, result ->
                progressBar.value.visibility = View.GONE
                Log.i("destinyreader", "DOWNLOADED")
                Log.i("destinyreader", response.statusCode.toString())
                if (response.statusCode != 200) {
                    Snackbar.make(
                        findViewById(R.id.nav_host_fragment),
                        "Une erreur est survenue lors du téléchargement de la base de données",
                        Snackbar.LENGTH_LONG
                    ).show()
                } else {
                    uiScope.launch {
                        unzip(applicationInfo.dataDir + "/cache/temp_database.zip")
                        Log.i(
                            "destinyreader",
                            "Database File Unzipped : " + File(applicationInfo.dataDir + "/databases/destiny_manifest.db").exists()
                                .toString()
                        )
                        getSharedPreferences("DestinyReaderPrefs", Context.MODE_PRIVATE).edit()
                            .putString("database_version", lastDatabaseVersion)
                            .apply()
                        Log.i("destinyreader", "Database OK")
                        goToMainDisplay()
                    }
                }
            }
    }


    fun getLocaleCode(): String {
        val availableLocales: List<String> = listOf(
            "fr",
            "en",
            "es",
            "de",
            "it",
            "ja",
            "ru",
            "pl",
            "ko",
            "es-mx",
            "pt-br",
            "zh-cht",
            "zh-chs"
        )
        val locale: Locale
        locale = if (Build.VERSION.SDK_INT >= 24) {
            resources.configuration.locales.get(0)

        } else {
            resources.configuration.locale
        }

        return when (locale.language) {
            //TODO : Ajouter langages
            "fra" -> "fr"
            "spa" -> "es"

            else -> "en"
        }

    }

    fun isDownloadNeeded(version: String): Boolean {
        /*Check if database is ready and up-to-date
        * Args :
        *   version : String -> Newest version of database
        * Returns :
        *   Boolean : Is a download necessary?
        **/
        val currentVersion: String = getSharedPreferences(
            "DestinyReaderPrefs",
            Context.MODE_PRIVATE
        ).getString("database_version", "") ?: ""
        Log.i("destinyreader", "Current version " + currentVersion)
        Log.i("destinyreader", "New version " + version)
        return (currentVersion != version)

    }

    private suspend fun unzip(filePath: String) {
        withContext(Dispatchers.IO) {
            Log.i("destinyreader", "ZIPFILE : " + filePath)
            val fis = FileInputStream(filePath)
            val zis = ZipInputStream(BufferedInputStream(fis))

            val zipfile = ZipFile(filePath)
            val entry = zis.nextEntry
            val file = FileOutputStream(
                applicationInfo.dataDir + "/databases/destiny_manifest.db",
                false
            )
            var a = 0
            val buffer = ByteArray(1024)
            var count = 0
            while (zis.read(buffer).also { count = it } > 0) {
                file.write(buffer, 0, count)
            }
            file.close()
            zis.closeEntry()
            zipfile.close()
        }
    }
}

class UpdateSnackbarListener(private val fct: () -> Unit) : View.OnClickListener {
    override fun onClick(v: View) {
        fct()
    }
}