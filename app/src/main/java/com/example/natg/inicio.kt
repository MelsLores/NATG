package com.example.natg

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.example.natg.io.ApiService
import com.example.natg.io.response.getEm
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Url
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/*
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
*/
class inicio : AppCompatActivity(), OnMapReadyCallback {
/*
    lateinit var locationRequest:LocationRequest
    lateinit var locationCallback:LocationCallback
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onDestroy() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        super.onDestroy()
    }
*/

    private lateinit var map: GoogleMap
    lateinit var mFromLatLng: LatLng
    lateinit var mToLatLng: LatLng

    private var markerTo: Marker? = null

    private var markerFrom: Marker? = null


    var lat = 0.0
    var long = 0.0
    lateinit var fusedLocationClient: FusedLocationProviderClient
    var modo:String="driving"
    companion object {
        const val REQUEST_CODE_LOCATION = 0
        const val AUTOCOMPLETE_REQUEST_CODE_TO = 1
        const val AUTOCOMPLETE_REQUEST_CODE_FROM = 2

        const val TAG = "inicio"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)


        createMapFragment()

        Places.initialize(applicationContext, getString(R.string.api_key))

        var btnTo = findViewById<Button>(R.id.btnBuscarD)
        btnTo.setOnClickListener {
            autocompleteTo()
        }

        var btnFrom = findViewById<Button>(R.id.btnBuscarH)
        btnFrom.setOnClickListener {
            autocompleteFrom()
        }


/*
        var btnCafeteria = findViewById<Button>(R.id.button5)

        var txtAPI = findViewById<TextView>(R.id.txtapi)

        btnCafeteria.setOnClickListener{
            val intent: Intent = Intent(this,cafeteria::class.java)
            startActivity(intent)
        }

        var btnComida = findViewById<Button>(R.id.button4)

        btnComida.setOnClickListener{
            val intent: Intent=Intent(this,comida::class.java)
            startActivity(intent)
        }

        var btnDiversion = findViewById<Button>(R.id.button6)

        btnDiversion.setOnClickListener{
            val intent: Intent=Intent(this,diversion::class.java)
            startActivity(intent)
        }
*/
        //http
/*
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://travel-advisor.p.rapidapi.com/locations/v2/auto-complete?query=eiffel%20tower&lang=en_US&units=km")
            .get()
            .addHeader("content-type", "application/octet-stream")
            .addHeader("X-RapidAPI-Key", "d169cac214mshcbcbe2689233d23p1406c1jsn211ac2d16cb1")
            .addHeader("X-RapidAPI-Host", "travel-advisor.p.rapidapi.com")
            .build()
            */

/*
        val response = client.newCall(request).execute()

        if (response.isSuccessful) {
            val responseBody = response.body()?.string()
            println(responseBody)
            txtAPI.setText(responseBody)
        } else {
            println("Error: ${response.code()} ${response.message()}")
        }*/


    }


    //GOOGLE MAPS


    private fun createMarker(favoritePlace: LatLng, title2: String) {
        map.addMarker(MarkerOptions().position(favoritePlace).title(title2))
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(favoritePlace, 18f),
        )
    }

    private fun createMarker1(favoritePlace: LatLng, title2: String) {
        markerFrom?.remove()
        markerFrom = map.addMarker(MarkerOptions().position(favoritePlace).title(title2))
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(favoritePlace, 18f),
        )

    }

    private fun createMarker2(favoritePlace: LatLng, title2: String) {
        markerTo?.remove()
        markerTo = map.addMarker(MarkerOptions().position(favoritePlace).title(title2))
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(favoritePlace, 18f),
        )

        var results = FloatArray(10)
        Location.distanceBetween(
            mToLatLng.latitude,
            mToLatLng.longitude,
            mFromLatLng.latitude,
            mFromLatLng.longitude,

            results
        )

        val s = String.format("%.1f", results[0] / 1000)
        var txtDistancia = findViewById<TextView>(R.id.txt_dist)
        txtDistancia.setText(s + " km")


        var b=s.toDouble()

        var txtPersona = findViewById<TextView>(R.id.txt_persona)
        var t=0.23*b
        var p=0.40*b
        txtPersona.setText("Persona: ${t} KG CO2"+"     Auto: ${p} KG CO")
        createRoute()
        search("persona")
    }

    private fun latLngToStr(latLng: LatLng) = "${latLng.latitude},${latLng.latitude}"


    private fun createMapFragment() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        enableLocation()


    }

    private fun isLocationPermissionGranted() = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    private fun enableLocation() {
        if (!::map.isInitialized) return
        if (isLocationPermissionGranted()) {
            map.isMyLocationEnabled = true

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            leerUbi()
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            Toast.makeText(this, "Vaya a ajustes y acepte los permisos.", Toast.LENGTH_SHORT).show()

        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_LOCATION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                map.isMyLocationEnabled = true


            } else {
                Toast.makeText(
                    this,
                    "Para activar la localización vaya a ajustes y acepte los permisos.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {}
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun leerUbi() {
        fusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
            var location: Location? = task.result
            if (location == null) {
                onResumeFragments()
            } else {
                lat = location.latitude
                long = location.longitude

                map.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(LatLng(lat, long), 18f)
                )

            }
        }
    }

    override fun onResumeFragments() {

        super.onResumeFragments()
        if (!::map.isInitialized) return

        if (!isLocationPermissionGranted()) {
            map.isMyLocationEnabled = false
            Toast.makeText(
                this,
                "Para activar la localización vaya a ajustes y acepte los permisos.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun autocompleteTo() {


        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        val fields =
            listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)

        // Start the autocomplete intent.
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
            .build(this)

        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE_TO)

    }

    private fun autocompleteFrom() {


        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        val fields =
            listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS)

        // Start the autocomplete intent.
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
            .build(this)

        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE_FROM)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        var txtMostrarDir = findViewById<TextView>(R.id.txt_dirSelect)
        var txtMostrarDirH = findViewById<TextView>(R.id.txt_dirSelectH)

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE_TO) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        Log.i(TAG, "Place Name & Id: ${place.name},${place.latLng}")
                        txtMostrarDir.text = "${place.name}"

                        place.latLng?.let {
                            mToLatLng = it

                        }
                        createMarker1(mToLatLng, "Desde")

                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        status.statusMessage?.let { message ->
                            Log.i(TAG, message)
                        }
                    }
                }
            }

            return
        }

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE_FROM) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        Log.i(TAG, "Place Name & Id: ${place.name},${place.id}")
                        txtMostrarDirH.text = "${place.name}"

                        place.latLng?.let {
                            mFromLatLng = it
                        }

                        createMarker2(mFromLatLng, "Hasta")


                        val url: String = getDirectionUrl(mToLatLng, mFromLatLng)


                        val downloadTask: DownloadTask = DownloadTask()
                        downloadTask.execute(url)
                    }
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        status.statusMessage?.let { message ->
                            Log.i(TAG, message)
                        }
                    }
                }
            }

            return
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    inner class DownloadTask :
        AsyncTask<String?, Void?, String>() {
        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            val parserTask = ParserTask()
            parserTask.execute(result)
        }

        override fun doInBackground(vararg url: String?): String {
            var data = ""
            try {
                data = downloadUrl(url[0].toString()).toString()
            } catch (e: java.lang.Exception) {
                Log.d("background task", e.toString())
            }

            return data
        }
    }

    @Throws(IOException::class)
    private fun downloadUrl(strUrl: String): String? {
        var data = ""
        var iStream: InputStream? = null
        var urlConnection: HttpURLConnection? = null
        try {
            val url = URL(strUrl)
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.connect()
            iStream = urlConnection!!.inputStream
            var br = BufferedReader(InputStreamReader(iStream))
            val sb = StringBuilder()
            var line:String? = ""
            //while ((br.readLine().also { line = it } != "}") ) {
            while (br.readLine().also { line = it } != null) {
                sb.append(line)
            }
            data = sb.toString()
            br.close()

        } catch (e: java.lang.Exception) {
            Log.d("Exception", e.toString())
        } finally {
            iStream!!.close()
            urlConnection!!.disconnect()
        }
        return data
    }

    inner class ParserTask :
    AsyncTask<String?,Int?,List<List<HashMap<String,String>>>?>(){
        override fun doInBackground(vararg jsonData: String?): List<List<HashMap<String, String>>>? {
            val jObject : JSONObject
            var routes: List<List<HashMap<String,String>>>?=
                null
            try{
                jObject=JSONObject(jsonData[0])
                val parser = DataParser()
                routes=parser.parse(jObject)
            }catch(e:java.lang.Exception){
                e.printStackTrace()
            }
            return routes
        }

        override fun onPostExecute(result: List<List<HashMap<String, String>>>?) {
        val points = ArrayList<LatLng?>()
        val lineOptions = PolylineOptions()
        for (i in result!!.indices) {
            val path = result[i]

            for (j in path.indices) {
                val point = path[j]
                val lat = point["lat"]!!.toDouble()
                val lng = point["lng"]!!.toDouble()
                val position = LatLng(lat, lng)
                points.add(position)
            }
            lineOptions.addAll(points)
            lineOptions.width(8f)
            lineOptions.color(Color.BLACK)
            lineOptions.geodesic(true)
        }
        if (points.size != 0) map!!.addPolyline(lineOptions)
    }
}

    private fun getDirectionUrl(origin: LatLng,dest:LatLng): String {
        val str_origin = "origin=" + origin.latitude+"%2C"+origin.longitude
        val str_dest = "destination=" + dest.latitude+"%2C"+dest.longitude
        val mode="mode="+modo
        val parameters = "$str_origin&$str_dest&$mode"
        val output ="json"

        return "https://maps.googleapis.com/maps/api/directions/$output?$parameters&key=AIzaSyBrIG_DOEDxbWmvJGw_C9M2QGFu9ZmZKhM"
    }

    /*
    private fun init(){
        locationRequest = LocationRequest()
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest.setFastestInterval(3000)
        locationRequest.setSmallestDisplacement(10f)
        locationRequest.interval=5000

        locationCallback=object : LocationCallback(){
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                val newPos = LatLng(p0!!.lastLocation.latitude,p0!!.lastLocation.longitude)
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(newPos,18f))
            }
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper())
    }
*/
/*
    fun getRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://api.openrouteservice.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }*/

    private fun getRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://my-json-server.typicode.com/jesus10tamez/API_CO2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun search(query:String){
        CoroutineScope(Dispatchers.IO).launch{
            val call=getRetrofit().create(ApiService::class.java).getH("$query")
           // val aux: getEm? = call.body()
            /*if(call.isSuccessful){

            }
            else{

            }*/
        }

    }


    fun createRoute(){
        CoroutineScope(Dispatchers.IO).launch{
            var start:String ="${mFromLatLng.longitude},${mFromLatLng.latitude}"
            var end:String ="${mToLatLng.longitude},${mToLatLng.latitude}"
            //val call = getRetrofit().create(ApiService::class.java).getRoute("5b3ce3597851110001cf624877b0306ccd41445c8e4b6054bd8c32c5",start,end)
            //val call = getRetrofit().create(ApiService::class.java).getRoute("5b3ce3597851110001cf624877b0306ccd41445c8e4b6054bd8c32c5","34.0,35.0","36.0,37.0")
            /*if(call.isSuccessful){



                //call.body()
            }else{


                }*/


        }


    }



}
