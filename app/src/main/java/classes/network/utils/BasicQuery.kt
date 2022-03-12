package classes.network.utils

import android.content.Context


fun getJWT(appContext: Context): String{
    val pref = appContext.getSharedPreferences("MyPref", 0)
    return "Bearer " + pref.getString("JWT", null)!!
}
