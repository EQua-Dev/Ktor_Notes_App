package com.androidstrike.androidstrike.ktornotesapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

/**
 * Created by Richard Uzor  on 04/07/2022
 */

/**
 * Util File to check the internet connectivity (state) of the device
 */

fun isNetworkConnected(context: Context): Boolean{

    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetwork
    val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)

    //returns true if the network capability is not null and is internet network capability
    return networkCapabilities != null &&
            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

}
