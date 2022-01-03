package uz.shokirov.sumdollar

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.about_dialog.view.*
import kotlinx.android.synthetic.main.about_dialog.view.tvTitle
import kotlinx.android.synthetic.main.internet_connect_dioalog.view.*
import kotlinx.android.synthetic.main.language_dialog.view.*
import uz.shokirov.cash.Til
import uz.shokirov.sumdollar.databinding.FragmentSettingsBinding
import uz.shokirov.utils.NetworkHelper


class SettingsFragment : Fragment() {
    lateinit var binding: FragmentSettingsBinding
    lateinit var handler: Handler
    lateinit var networkHelper: NetworkHelper
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        Til.init(context)


        binding.btnDasturHaqida.setOnClickListener {
            val alertDialog = AlertDialog.Builder(context)
            val dialog = alertDialog.create()

            val dialogView =
                layoutInflater.inflate(R.layout.about_dialog, null, false)
            dialog.setView(dialogView)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)


            when (Til.color) {
                1 -> {
                    dialogView.tvTitle.text = "About us"
                    dialogView.description.text =
                        "You can learn about exchange rates through this app"
                    dialogView.tvVersion.text = "Version 1.0"
                    dialogView.tvPhone.text = "Phone : +998 91 666 11 80"
                    dialogView.tvManbaa.text = "Information taken from the site https://cbu.uz/oz/"
                    dialogView.tvFeedback.text = "If you have any feedbacks, please contact us."
                }
                2 -> {
                    dialogView.tvTitle.text = "О нас"
                    dialogView.description.text =
                        "Вы можете узнать об обменных курсах через это приложение"
                    dialogView.tvVersion.text = "Версия 1.0"
                    dialogView.tvPhone.text = "Номер телефона : +998 91 666 11 80"
                    dialogView.tvManbaa.text = "Информация взята с сайта https://cbu.uz/oz/"
                    dialogView.tvFeedback.text = "Если у вас есть отзывы, свяжитесь с нами."
                }
                3 -> {
                    dialogView.tvTitle.text = "Biz haqimizda"
                    dialogView.description.text =
                        "valyuta kurslari haqida ushbu ilova orqali bilib olishingiz mumkin"
                    dialogView.tvVersion.text = "Versiya 1.0"
                    dialogView.tvPhone.text = "Telefon raqam : +998 91 666 11 80"
                    dialogView.tvManbaa.text = "Ma'lumotlar https://cbu.uz/oz/ saytidan olindi"
                    dialogView.tvFeedback.text =
                        "Agar sizda biron bir fikr-mulohaza bo'lsa, biz bilan bog'laning."
                }
            }

            dialogView.contactTelegram.setOnClickListener {
                linkIntent("https://t.me/It_blog_adm1n")
            }
            dialogView.contactInstagram.setOnClickListener {
                linkIntent("https://www.instagram.com/shokirov_ollohberdi/")
            }

            dialog.show()
        }

        binding.btnDasturTili.setOnClickListener {
            val alertDialog = AlertDialog.Builder(context)
            val dialog = alertDialog.create()

            val dialogView =
                layoutInflater.inflate(R.layout.language_dialog, null, false)
            dialog.setView(dialogView)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            when (Til.color) {
                1 -> {
                    dialogView.radioEnglish.isChecked = true
                }
                2 -> {
                    dialogView.radioRussian.isChecked = true
                }
                3 -> {
                    dialogView.radioUzbek.isChecked = true
                }
            }

            dialogView.btnCancel.setOnClickListener {
                dialog.cancel()
            }
            dialogView.btnCheck.setOnClickListener {
                if (dialogView.radioEnglish.isChecked) {
                    Til.color = 1
                } else if (dialogView.radioRussian.isChecked) {
                    Til.color = 2
                } else if (dialogView.radioUzbek.isChecked) {
                    Til.color = 3
                }
                dialog.cancel()
            }

//
            dialog.show()

        }

        val runnable = object : Runnable {
            override fun run() {
                Til.init(context)

                when (Til.color) {
                    1 -> {
                        binding.tvDasturHaqida.text = "About application"
                        binding.tvDasturTili.text = "Language application"
                    }
                    2 -> {
                        binding.tvDasturHaqida.text = "О приложении"
                        binding.tvDasturTili.text = "Языковое приложение"
                    }
                    3 -> {
                        binding.tvDasturHaqida.text = "Dastur haqida"
                        binding.tvDasturTili.text = "Dastur tili"
                    }
                }
                handler.postDelayed(this, 100)
            }
        }
        handler = Handler()
        handler.postDelayed(runnable, 100)




        return binding.root
    }

    private fun linkIntent(link: String) {
        val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(myIntent)
    }


}