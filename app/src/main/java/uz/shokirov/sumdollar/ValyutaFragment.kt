package uz.shokirov.sumdollar

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.ClipboardManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.android.volley.*
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.about_dialog.view.*
import kotlinx.android.synthetic.main.dialog_value.view.*
import kotlinx.android.synthetic.main.fragment_valyuta.*
import kotlinx.android.synthetic.main.internet_connect_dioalog.view.*
import kotlinx.android.synthetic.main.internet_connect_dioalog.view.tvTitle
import uz.shokirov.adapter.RvAdapter
import uz.shokirov.adapter.RvClick
import uz.shokirov.adapter.SpinnerAdapter
import uz.shokirov.cash.Til
import uz.shokirov.models.Valute
import uz.shokirov.sumdollar.databinding.FragmentValyutaBinding
import uz.shokirov.utils.NetworkHelper


class ValyutaFragment : Fragment() {
    lateinit var binding: FragmentValyutaBinding
    lateinit var requestQueue: RequestQueue
    lateinit var rvAdapter: RvAdapter
    lateinit var spinnerAdapter: SpinnerAdapter
    lateinit var handler: Handler
    lateinit var networkHelper: NetworkHelper
    var url = "http://cbu.uz/uzc/arkhiv-kursov-valyut/json/"

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentValyutaBinding.inflate(LayoutInflater.from(context))

        Til.init(context)

        networkHelper = NetworkHelper(context!!)

        if (networkHelper.isNetworkConnected()) {

        } else {
            val alertDialog = AlertDialog.Builder(context)
            val dialog = alertDialog.create()

            val dialogView =
                layoutInflater.inflate(R.layout.internet_connect_dioalog, null, false)
            dialog.setView(dialogView)

            when (Til.color) {
                1 -> {
                    dialogView.tvNoconnection.text = "No Connection"
                    dialogView.tvTitle.text =
                        "No internet connection. Check your connection or try again"
                    dialogView.cancel.text = "Cancel"
                    dialogView.retry.text = "Retry"
                }
                2 -> {
                    dialogView.tvNoconnection.text = "Нет соединения"
                    dialogView.tvTitle.text =
                        "Нет соединения с интернетом. Проверьте ваше соединение или попробуйте еще раз"
                    dialogView.cancel.text = "Отмена"
                    dialogView.retry.text = "Повторить"
                }
                3 -> {
                    dialogView.tvNoconnection.text = "Ulanish yo'q"
                    dialogView.tvTitle.text =
                        "Internet aloqasi yo'q. Ulanishingizni tekshiring yoki qaytadan urining"
                    dialogView.cancel.text = "Bekor qilish"
                    dialogView.retry.text = "Qayta urinish"
                }
            }

            dialogView.cancel.setOnClickListener {
                activity?.finish()
            }
            dialogView.retry.setOnClickListener {
                dialog.cancel()
                if (networkHelper.isNetworkConnected()) {
                    binding.rv.adapter?.notifyDataSetChanged()
                } else {
                    dialog.show()
                }
            }
            dialog.setCancelable(false)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.show()
        }

        requestQueue = Volley.newRequestQueue(context)
        VolleyLog.DEBUG = true //qanday ma'lumot kelayotganini Logda ko'rsatib turadi

        val jsonArrayRequest = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->
                val type = object : TypeToken<List<Valute>>() {}.type
                val list = Gson().fromJson<ArrayList<Valute>>(response.toString(), type)
                var listSpinner = ArrayList<String>()

                list.add(
                    Valute(
                        "UZS", "UZS", "Узс", "UZS", "12", "32",
                        "", " ", "1", "1", 1
                    )
                )


                for (element in list) {
                    listSpinner.add(element.Ccy)
                }
                var rvList = ArrayList<Valute>()

                for (valuate in 0 until list.size - 1) {
                    rvList.add(list[valuate])
                }

                spinnerAdapter = SpinnerAdapter(listSpinner)

                binding.fromSpinner.adapter = spinnerAdapter
                binding.toSpinner.adapter = spinnerAdapter
                var fromPosition: Int = 0
                var toPosition: Int = 0
                var fromRate: Double = 0.0
                var toRate: Double = 0.0
                var input: Double = 0.0
                binding.edtFromValute.addTextChangedListener {
                    if (binding.edtFromValute.text.isNotEmpty()) {
                        fromPosition = binding.fromSpinner.selectedItemPosition
                        toPosition = binding.toSpinner.selectedItemPosition
                        fromRate = list[fromPosition].Rate.toString().toDouble()
                        toRate = list[toPosition].Rate.toString().toDouble()
                        input = edtFromValute.text.toString().toDouble()
                        binding.edtToValute.setText("${(fromRate * input) / toRate}")
                    } else {
                        binding.edtToValute.text.clear()
                    }

                }

                rvAdapter = RvAdapter(context!!, rvList, object : RvClick {
                    @SuppressLint("ObsoleteSdkInt")
                    override fun click(valute: Valute) {
                        val alertDialog = AlertDialog.Builder(context)
                        val dialog = alertDialog.create()

                        val dialogView =
                            layoutInflater.inflate(R.layout.dialog_value, null, false)
                        dialog.setView(dialogView)


                        val runnable = object : Runnable {
                            override fun run() {
                                when (Til.color) {
                                    1 -> {
                                        dialogView.dialogValuteValue?.text =
                                            valute.Rate + " sum"
                                        dialogView.dialogValuteType?.text = valute.CcyNm_EN
                                    }
                                    2 -> {
                                        dialogView.dialogValuteValue?.text =
                                            valute.Rate + " сум"
                                        dialogView.dialogValuteType?.text = valute.CcyNm_RU
                                    }
                                    3 -> {
                                        dialogView.dialogValuteValue?.text =
                                            valute.Rate + " so'm"
                                        dialogView.dialogValuteType?.text = valute.CcyNm_UZ
                                    }
                                }
                            }
                        }
                        handler = Handler()
                        handler.postDelayed(runnable, 100)

                        dialogView.dialogTvDate?.text = "  ${valute.Date}"
                        dialogView.btnOkDialog.setOnClickListener {
                            dialog.cancel()
                        }
                        dialogView.btnShare.setOnClickListener {
                            val sharingIntent = Intent(Intent.ACTION_SEND)
                            sharingIntent.type = "text/plain"
                            var share = " "
                            Log.d("Til", "${Til.color} til")

                            val runnable = object : Runnable {
                                override fun run() {
                                    when (Til.color) {
                                        1 -> {
                                            // share = "1 ${valute.CcyNm_EN} equals to ${valute.Rate} sum"
                                            sharingIntent.putExtra(
                                                Intent.EXTRA_TEXT,
                                                "1 ${valute.CcyNm_EN} equals to ${valute.Rate} sum"
                                            )
                                            Log.d("Til", "english")
                                        }
                                        2 -> {
                                            sharingIntent.putExtra(
                                                Intent.EXTRA_TEXT,
                                                "1 ${valute.CcyNm_RU} равен ${valute.Rate} сум"
                                            )
                                            Log.d("Til", "russian")
                                        }
                                        3 -> {
                                            sharingIntent.putExtra(
                                                Intent.EXTRA_TEXT,
                                                "1 ${valute.CcyNm_UZ} ${valute.Rate} so'mga teng"
                                            )
                                            Log.d("Til", "Uzbek")
                                        }
                                    }
                                    startActivity(Intent.createChooser(sharingIntent, " "))
                                }
                            }
                            handler = Handler()
                            handler.postDelayed(runnable, 100)

                            startActivity(Intent.createChooser(sharingIntent, " "))
                        }
                        dialogView.btnCopy.setOnClickListener {
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                                val clipboard =
                                    context!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager


                                val runnable = object : Runnable {
                                    override fun run() {
                                        when (Til.color) {
                                            1 -> {
                                                clipboard.text =
                                                    "1 ${valute.CcyNm_EN} equals to ${valute.Rate} sum"
                                                dialog.cancel()
                                                Toast.makeText(
                                                    context,
                                                    "Copied!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                            2 -> {
                                                clipboard.text =
                                                    "1 ${valute.CcyNm_RU} равен ${valute.Rate} сум"
                                                dialog.cancel()
                                                Toast.makeText(
                                                    context,
                                                    "Скопировано!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                            3 -> {
                                                clipboard.text =
                                                    "1 ${valute.CcyNm_UZ} ${valute.Rate} so'mga teng"
                                                dialog.cancel()
                                                Toast.makeText(
                                                    context,
                                                    "Nusxalandi!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    }
                                }
                                handler = Handler()
                                handler.postDelayed(runnable, 100)


                            } else {
                                val clipboard =
                                    context!!.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                                var copy = " "
                                var label = " "
                                val runnable = object : Runnable {
                                    override fun run() {
                                        when (Til.color) {
                                            1 -> {
                                                copy =
                                                    "1 ${valute.CcyNm_EN} equals to ${valute.Rate} sum"
                                                label = "Copied!"
                                            }
                                            2 -> {
                                                copy =
                                                    "1 ${valute.CcyNm_RU} равен ${valute.Rate} сум"
                                                label = "Скопировано"
                                            }
                                            3 -> {
                                                copy =
                                                    "1 ${valute.CcyNm_UZ} ${valute.Rate} so'mga teng"
                                                label = "Nusxalandi"
                                            }

                                        }
                                        val clip = ClipData.newPlainText(label, copy)
                                        clipboard.setPrimaryClip(clip)
                                        dialog.cancel()
                                        Toast.makeText(context, label, Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }
                                handler = Handler()
                                handler.postDelayed(runnable, 100)

                            }
                        }

                        dialog.setCancelable(false)
                        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                        dialog.show()
                    }

                })
                binding.rv.adapter = rvAdapter


                Log.d("TAG", "onResponse : ${response.toString()}")
            }) { }

        jsonArrayRequest.tag = "tag1" //tag berilyapti
        requestQueue.add(jsonArrayRequest)



        return binding.root
    }

}