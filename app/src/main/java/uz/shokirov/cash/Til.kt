package uz.shokirov.cash

import android.content.Context
import android.content.SharedPreferences

object Til {
    private const val NAME = "ApplicationLanguage"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    fun init(context: Context?) {
        preferences = context!!.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var color: Int
        get() = preferences.getInt("language", 1)!!
        set(value) = preferences.edit {
            if (value != null) {
                it.putInt("language", value)
            }
        }
}