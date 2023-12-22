package com.example.groupproject

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RestaurantDetailActivity : AppCompatActivity() {

    private lateinit var tvName: TextView
    private lateinit var tvDescription: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var btnReserve: Button
    private lateinit var btnFavorite: Button
    private lateinit var btnAllFavorites: Button
    private lateinit var etDate: EditText
    private lateinit var etTime: EditText
    private lateinit var restaurantModel: RestaurantModel
    private lateinit var preferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_detail)

        restaurantModel = RestaurantModel()

        tvName = findViewById(R.id.tvName)
        tvDescription = findViewById(R.id.tvDescription)
        ratingBar = findViewById(R.id.ratingBar)
        btnReserve = findViewById(R.id.btnReserve)
        btnFavorite = findViewById(R.id.btnFavorite)
        btnAllFavorites = findViewById(R.id.btnAllFavorites)

        preferences = this.getSharedPreferences( this.packageName + "_preferences",
            Context.MODE_PRIVATE )

        val restaurantName = intent.getStringExtra("RESTAURANT_NAME")
        if (restaurantName == null) {
            return
        }

        val restaurantPrice = restaurantModel.findRestaurantPrice(restaurantName)
        var fullTvName = restaurantName + " ("
        for (i in 1..restaurantPrice) {
            fullTvName += "$"
        }
        fullTvName += ")"

        updateFavoriteButton(restaurantName)

        val restaurantDescription = intent.getStringExtra("RESTAURANT_DESCRIPTION")
        val restaurantRating = preferences.getFloat(restaurantName + "_rating", 0f)

        tvName.text = fullTvName
        tvDescription.text = restaurantDescription
        ratingBar.rating = restaurantRating

        etDate = findViewById(R.id.etDate)
        etTime = findViewById(R.id.etTime)

        btnReserve.setOnClickListener {
            val date = etDate.text.toString()
            val time = etTime.text.toString()

            if (date.isEmpty() || time.isEmpty()) {
                Toast.makeText(this, "Please enter both date and time.", Toast.LENGTH_SHORT).show()
            }
            else {
                val emailIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "message/rfc822"
                    putExtra(Intent.EXTRA_EMAIL, arrayOf("hfrances@umd.edu"))
                    putExtra(Intent.EXTRA_SUBJECT, "Restaurant Reservation")
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "I would like to make a reservation at ${tvName.text} for $date at $time."
                    )
                }

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send email using..."))
                } catch (ex: android.content.ActivityNotFoundException) {
                    Toast.makeText(this, "No email clients installed.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnFavorite.setOnClickListener {
            val isFavorite = preferences.getBoolean(restaurantName + "_favorite", false)
            val newFavoriteStatus = !isFavorite
            preferences.edit().putBoolean(restaurantName + "_favorite", newFavoriteStatus).apply()

            if (newFavoriteStatus) {
                btnFavorite.text = "Unfavorite"
            } else {
                btnFavorite.text = "Favorite"
            }
        }

        btnAllFavorites.setOnClickListener {
            val intent = Intent(this, RestaurantFavoriteActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in_and_scale, 0 )
        }

        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            preferences.edit().putFloat(restaurantName + "_rating", rating).apply()
        }
    }

    private fun updateFavoriteButton(restaurantName : String) {
        val isFavorite = preferences.getBoolean(restaurantName + "_favorite", false)
        btnFavorite.text = if (isFavorite) "Unfavorite" else "Favorite"
    }
}
