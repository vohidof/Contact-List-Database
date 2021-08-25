package com.vohidov.contactlistdb

import Adapter.RvAdapter
import Adapter.RvClick
import Models.UserModel
import Utils.MyDbHelper
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.edt_name
import kotlinx.android.synthetic.main.activity_main.edt_number
import kotlinx.android.synthetic.main.item_rv.*

class MainActivity : AppCompatActivity() {

    lateinit var rvAdapter: RvAdapter
    lateinit var userList: ArrayList<UserModel>
    lateinit var myDbHelper: MyDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myDbHelper = MyDbHelper(this)

        btn_save.setOnClickListener {
            val name = edt_name.text.toString()
            val number = edt_number.text.toString()

            val user = UserModel(name, number)
            myDbHelper.addContact(user)
            Toast.makeText(this, "Save $name $number", Toast.LENGTH_SHORT).show()
            onResume()
        }
    }

    override fun onResume() {
        super.onResume()

        userList = ArrayList()
        userList.addAll(myDbHelper.getAllContact())
        rvAdapter = RvAdapter(this, userList, object : RvClick {
            override fun deleteItem(userModel: UserModel) {

                val dialog = AlertDialog.Builder(this@MainActivity)

                dialog.setMessage("You want delete your contact?")

                dialog.setPositiveButton("No", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {

                    }
                })

                dialog.setNegativeButton("Yes", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        myDbHelper.deleteContact(userModel)
                        onResume()
                        Toast.makeText(this@MainActivity, "Deleted", Toast.LENGTH_SHORT).show()
                    }
                })
                dialog.show()

            }

            @SuppressLint("InflateParams")
            override fun editUser(userModel: UserModel) {
                val dialog = BottomSheetDialog(this@MainActivity)

                dialog.setContentView(
                    layoutInflater.inflate(
                        R.layout.dialog_layout,
                        null,
                        false
                    )
                )
                dialog.edt_name2.setText(userModel.name)
                dialog.edt_number2.setText(userModel.number)

                dialog.btn_edit_Save.setOnClickListener {
                    userModel.name = dialog.edt_name2.text.toString()
                    userModel.number = dialog.edt_number2.text.toString()

                    myDbHelper.updateContact(userModel)
                    onResume()
                    Toast.makeText(this@MainActivity, "Update!", Toast.LENGTH_SHORT).show()
                    dialog.cancel()
                }

                dialog.show()
            }

            override fun call(userModel: UserModel, position: Int, list: ArrayList<UserModel>) {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${list[position].number}")
                startActivity(intent)
            }

        })

        rv.adapter = rvAdapter
    }
}