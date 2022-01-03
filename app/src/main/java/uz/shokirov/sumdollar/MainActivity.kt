package uz.shokirov.sumdollar

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.internet_connect_dioalog.view.*
import uz.shokirov.cash.Til
import uz.shokirov.sumdollar.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Til.init(this)


        supportActionBar?.hide()
        if (isNetWorkConnected()) {
            onRestart()
        } else {
//            val alertDialog = AlertDialog.Builder(this)
//            val dialog = alertDialog.create()
//
//            val dialogView =
//                    layoutInflater.inflate(R.layout.internet_connect_dioalog, null, false)
//            dialog.setView(dialogView)
//
//            when (Til.color) {
//                1 -> {
//                    dialogView.tvNoconnection.text = "No Connection"
//                    dialogView.tvTitle.text = "No internet connection. Check your connection or try again"
//                    dialogView.cancel.text = "Cancel"
//                    dialogView.retry.text = "Retry"
//                }
//                2 -> {
//                    dialogView.tvNoconnection.text = "Нет соединения"
//                    dialogView.tvTitle.text = "Нет соединения с интернетом. Проверьте ваше соединение или попробуйте еще раз"
//                    dialogView.cancel.text = "Отмена"
//                    dialogView.retry.text = "Повторить"
//                }
//                3 -> {
//                    dialogView.tvNoconnection.text = "Ulanish yo'q"
//                    dialogView.tvTitle.text = "Internet aloqasi yo'q. Ulanishingizni tekshiring yoki qaytadan urining"
//                    dialogView.cancel.text = "Bekor qilish"
//                    dialogView.retry.text = "Qayta urinish"
//                }
//            }
//
//            dialogView.cancel.setOnClickListener {
//                finish()
//            }
//            dialogView.retry.setOnClickListener {
//                dialog.cancel()
//                if (isNetWorkConnected()) {
//                    finish()
//                    onRestart()
//                } else {
//                    dialog.show()
//                }
//            }
//            dialog.setCancelable(false)
//            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
//            dialog.show()
        }
    }

    @SuppressLint("MissingPermission")
    fun isNetWorkConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork
            val netwworkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)

            return netwworkCapabilities != null && netwworkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            val actveNetworkinfo = connectivityManager.activeNetworkInfo
            return actveNetworkinfo != null && actveNetworkinfo.isConnected
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.my_navigation_host).navigateUp()
    }
}