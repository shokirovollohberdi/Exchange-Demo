package uz.shokirov.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.spinner_item.view.*
import uz.shokirov.sumdollar.R

class SpinnerAdapter(var list: ArrayList<String>):BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int,convertView: View?, parent: ViewGroup?): View {
        var itemView:View
        if (convertView == null){
            itemView = LayoutInflater.from(parent?.context).inflate(R.layout.spinner_item,parent, false)
        }else{
            itemView = convertView
        }
        itemView.tvValuteType.text = list[position]

        return itemView
    }
}