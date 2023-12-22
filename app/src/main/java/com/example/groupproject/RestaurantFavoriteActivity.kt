package com.example.groupproject

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RestaurantFavoriteActivity : AppCompatActivity() {

    private lateinit var tableLayout: LinearLayout
    private lateinit var preferences: SharedPreferences
    private val restaurantModel = RestaurantModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_favorite)

        tableLayout = findViewById(R.id.tableLayoutFavorites)
        preferences = this.getSharedPreferences(this.packageName + "_preferences", Context.MODE_PRIVATE)

        displayFavoriteRestaurants()
    }

    private fun displayFavoriteRestaurants() {
        val favoriteRestaurants = restaurantModel.getFavoriteRestaraunts(preferences)
        populateTable(favoriteRestaurants)
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
            }

            tableLayout.addView(rowView)
        }
    }
}
