package com.example.groupproject

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

class RestaurantListActivity : AppCompatActivity() {

    private lateinit var tableLayout: LinearLayout
    private lateinit var priceSeekBar: SeekBar
    private lateinit var ratingSeekBar: SeekBar
    private val restaurantModel = RestaurantModel()
    private var allRestaurants: List<RestaurantModel.Restaurant> = listOf()
    private lateinit var preferences : SharedPreferences

    private lateinit var adView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_list)

        tableLayout = findViewById(R.id.llTable)
        priceSeekBar = findViewById(R.id.priceSeekBar)
        ratingSeekBar = findViewById(R.id.rateSeekBar)

        allRestaurants = restaurantModel.getAllRestaurants()

        preferences = this.getSharedPreferences( this.packageName + "_preferences",
            Context.MODE_PRIVATE )

        populateTable(allRestaurants)

        priceSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val ratingProgress = ratingSeekBar.progress
                val filteredRestaurantsByPrice : List<RestaurantModel.Restaurant> = restaurantModel.filterRestaurants(progress)
                val filteredRestaurants : List<RestaurantModel.Restaurant> = restaurantModel.filterRestaurantsByRating(preferences, filteredRestaurantsByPrice, ratingProgress)
                populateTable(filteredRestaurants)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        ratingSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val pricingProgress = priceSeekBar.progress
                val filteredRestaurantsByPrice : List<RestaurantModel.Restaurant> = restaurantModel.filterRestaurants(pricingProgress)
                val filteredRestaurants : List<RestaurantModel.Restaurant> = restaurantModel.filterRestaurantsByRating(preferences, filteredRestaurantsByPrice, progress)
                populateTable(filteredRestaurants)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        createAd( )
    }


    override fun onResume() {
        super.onResume()
        populateTable(allRestaurants)
    }

    private fun populateTable(restaurants: List<RestaurantModel.Restaurant>) {
        tableLayout.removeAllViews()
        restaurants.forEach { restaurant ->
            val rowView = LayoutInflater.from(this).inflate(R.layout.table_row, tableLayout, false)
            val tvName = rowView.findViewById<TextView>(R.id.tvName)
            val tvRating = rowView.findViewById<TextView>(R.id.tvRating)
            val tvPrice = rowView.findViewById<TextView>(R.id.tvPrice)

            tvName.text = restaurant.name
            tvPrice.text = "${restaurant.price}"
            val rating = preferences.getFloat(restaurant.name + "_rating", 0f)
            tvRating.text = if (rating > 0) rating.toString() else "N/A"

            rowView.setOnClickListener {
                val intent = Intent(this, RestaurantDetailActivity::class.java).apply {
                    putExtra("RESTAURANT_NAME", restaurant.name)
                    putExtra("RESTAURANT_DESCRIPTION", restaurant.description)
                }
                startActivity(intent)
                overridePendingTransition(R.anim.slide_left, 0 )
            }

            tableLayout.addView(rowView)
        }
    }

    fun createAd( ) {
        adView = AdView( this )
        val adSize = AdSize.LARGE_BANNER
        adView.setAdSize( adSize )

        var adUnitId : String = "ca-app-pub-3940256099942544/6300978111"
        adView.adUnitId = adUnitId

        var builder : AdRequest.Builder = AdRequest.Builder( )
        builder.addKeyword( "fitness" ).addKeyword( "workout" )
        var request : AdRequest = builder.build()

        var layout : LinearLayout = findViewById( R.id.ad_view )
        layout.addView( adView )

        adView.loadAd( request )
    }

}
