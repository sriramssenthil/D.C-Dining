package com.example.groupproject

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class RestaurantModel : AppCompatActivity() {

    inner class Restaurant {
        val name: String
        val description: String
        var price: Int

        constructor(name: String, description: String, price: Int) {
            this.name = name
            this.description = description
            this.price = price
        }
    }

    private val restaurants = mutableListOf<Restaurant>()

    init {
        restaurants.add(Restaurant("Convivial", "Serving French-American dishes in a convivial atmosphere, this restaurant is a place to celebrate the joy of sharing good food.", 3))
        restaurants.add(Restaurant("Rose's Luxury", "An upscale eatery providing a diverse menu that changes frequently, known for its exceptional service and cozy atmosphere.", 5))
        restaurants.add(Restaurant("Zaytinya", "Offering a modern take on traditional Mediterranean dishes, Zaytinya brings flavors from Turkey, Greece, and Lebanon to the heart of DC.", 4))
        restaurants.add(Restaurant("Le Diplomate", "A Parisian-style brasserie known for its French cuisine and lively ambiance, reminiscent of dining in the streets of Paris.", 4))
        restaurants.add(Restaurant("Busboys and Poets", "A community gathering place with a menu as diverse as the city itself, featuring American fare with a twist.", 2))
        restaurants.add(Restaurant("The Dabney", "Nestled in the historic Blagden Alley, The Dabney offers a locally-sourced menu cooked over a wood fire.", 4))
        restaurants.add(Restaurant("Oyamel Cocina Mexicana", "A vibrant setting serving refined Mexican classics and renowned for its extensive tequila selection.", 3))
        restaurants.add(Restaurant("Komi", "An intimate dining experience, Komi serves a multi-course tasting menu with Mediterranean influences.", 5))
        restaurants.add(Restaurant("Rasika", "Featuring modern Indian cuisine, Rasika is known for its innovative dishes and stylish decor.", 4))
        restaurants.add(Restaurant("Joe's Seafood, Prime Steak & Stone Crab", "A classic steakhouse offering a wide selection of meats and fresh seafood, including its famous stone crab claws.", 5))
        restaurants.add(Restaurant("Kinship", "Kinship celebrates the rich diversity of American culture through its creative and contemporary dishes.", 4))
        restaurants.add(Restaurant("Little Serow", "A hidden gem offering a set menu of Northern Thai dishes in a simple, intimate setting.", 4))
        restaurants.add(Restaurant("Blue Duck Tavern", "A Michelin-starred restaurant serving up American classics made from the freshest ingredients in an open kitchen.", 5))
        restaurants.add(Restaurant("Fiola Mare", "Located on the waterfront, Fiola Mare specializes in exquisite seafood dishes with Italian flair.", 5))
        restaurants.add(Restaurant("Bad Saint", "A small, lively spot offering a unique take on Filipino cuisine, known for its bold flavors and communal dining.", 3))
        restaurants.add(Restaurant("Farmers", "A farmer-owned restaurant offering fresh, American-inspired dishes with ingredients sourced from farms across the country.", 3))
    }

    fun getAllRestaurants(): List<Restaurant> {
        return restaurants
    }

    fun filterRestaurants(maxPrice: Int): List<Restaurant> {
        val filteredRestaurants = mutableListOf<Restaurant>()
        for (restaurant in restaurants) {
            if (restaurant.price <= maxPrice) {
                filteredRestaurants.add(restaurant)
            }
        }
        return filteredRestaurants
    }

    fun filterRestaurantsByRating(preferences: SharedPreferences, currRestaurants: List<Restaurant>, maxRating: Int): List<Restaurant> {
        val filteredRestaurants = mutableListOf<Restaurant>()

        for (restaurant in currRestaurants) {
            val ratingKey = restaurant.name + "_rating"
            val rating = preferences.getFloat(ratingKey, 0.0f)

            if (rating <= maxRating) {
                filteredRestaurants.add(restaurant)
            }
        }

        return filteredRestaurants
    }

    fun getFavoriteRestaraunts(preferences : SharedPreferences) : List<Restaurant> {
        val favoriteRestaurants = mutableListOf<RestaurantModel.Restaurant>()

        for (restaurant in restaurants) {
            val isFavorite = preferences.getBoolean(restaurant.name + "_favorite", false)
            if (isFavorite) {
                favoriteRestaurants.add(restaurant)
            }
        }

        return favoriteRestaurants
    }

    fun findRestaurantPrice(rName : String) : Int {
        for (restaurant in restaurants) {
            if (restaurant.name == rName) {
                return restaurant.price
            }
        }
        return -1
    }
}
