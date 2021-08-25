package Adapter

import Models.UserModel
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vohidov.contactlistdb.R
import kotlinx.android.synthetic.main.item_rv.view.*

class RvAdapter(var context: Context, var list: ArrayList<UserModel>, var rvClick: RvClick) : RecyclerView.Adapter<RvAdapter.MyViewHolder>() {

    inner class MyViewHolder(var itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(model: UserModel, position: Int) {

            itemView.txt_name.text = model.name
            itemView.txt_number.text = model.number

            itemView.setOnClickListener {
                rvClick.call(model, position, list)
            }
            itemView.btn_delete.setOnClickListener {
                rvClick.deleteItem(model)
            }
            itemView.btn_edit.setOnClickListener {
                rvClick.editUser(model)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_rv, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

interface RvClick {
    fun deleteItem(userModel: UserModel)
    fun editUser(userModel: UserModel)
    fun call(userModel: UserModel, position: Int, list: ArrayList<UserModel>)
}