package uz.shokirov.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import uz.shokirov.cash.Til
import uz.shokirov.models.Valute
import uz.shokirov.sumdollar.R
import uz.shokirov.sumdollar.databinding.ItemRvBinding

class RvAdapter(var context: Context, var list: List<Valute>, var rvClick: RvClick) :
        RecyclerView.Adapter<RvAdapter.Vh>() {
    inner class Vh(var itemrv: ItemRvBinding) : RecyclerView.ViewHolder(itemrv.root) {
        fun onBind(valute: Valute, position: Int) {
            Til.init(context)

                    when (Til.color) {
                        1 -> {
                            itemrv.tvValuteType.text = valute.CcyNm_EN
                            itemrv.tvValueValute.text = valute.Rate
                        }
                        2 -> {
                            itemrv.tvValuteType.text = valute.CcyNm_RU
                            itemrv.tvValueValute.text = valute.Rate
                        }
                        3 -> {
                            itemrv.tvValuteType.text = valute.CcyNm_UZ
                            itemrv.tvValueValute.text = valute.Rate
                        }
                    }



            itemrv.root.setOnClickListener {
                rvClick.click(list[position])
            }
            itemrv.root.setOnLongClickListener {
                val unicode = 0x1F60A
                var emoji = getEmojiByUnicode(unicode)
                var text: String = "UZ"
                true
            }
            val anim = AnimationUtils.loadAnimation(context, R.anim.left_to_right)
            itemrv.root.startAnimation(anim)
        }

        fun getEmojiByUnicode(unicode: Int): String? {
            return String(Character.toChars(unicode))
        }

        private fun getEmojiByUnicode(reactionCode: String): String {
            val code = reactionCode.substring(4).toInt(16)
            return String(Character.toChars(code))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

interface RvClick {
    fun click(valute: Valute)
}