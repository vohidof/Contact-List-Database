package Utils

import Models.UserModel
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.content.contentValuesOf

class MyDbHelper(context: Context) : SQLiteOpenHelper(context, Constant.DB_NAME, null, Constant.TABLE_VERSION), DbServiceInterface {
    override fun onCreate(db: SQLiteDatabase?) {
        val query = "create table ${Constant.TABLE_NAME} (${Constant.ID} integer not null primary key autoincrement unique, ${Constant.NAME} text not null,${Constant.NUMBER} text not null)"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("drop table if exists ${Constant.TABLE_NAME}")
        onCreate(db)
    }

    override fun addContact(userModel: UserModel) {
        val database = this.writableDatabase
        val contentValue = ContentValues()
        contentValue.put(Constant.NAME, userModel.name)
        contentValue.put(Constant.NUMBER, userModel.number)
        database.insert(Constant.TABLE_NAME, null, contentValue)
        database.close()
    }

    override fun deleteContact(userModel: UserModel) {
        val database = this.writableDatabase
        database.delete(Constant.TABLE_NAME, "${Constant.ID} = ?", arrayOf("${userModel.id}"))
        database.close()
    }

    override fun updateContact(userModel: UserModel): Int {
        val database = this.writableDatabase
        val contentValue = ContentValues()
        contentValue.put(Constant.ID, userModel.id)
        contentValue.put(Constant.NAME, userModel.name)
        contentValue.put(Constant.NUMBER, userModel.number)
        return database.update(Constant.TABLE_NAME, contentValue, "${Constant.ID} = ?", arrayOf("${userModel.id}"))
    }

    override fun getAllContact(): List<UserModel> {
        val list = ArrayList<UserModel>()
        val query = "select * from ${Constant.TABLE_NAME}"
        val database = this.readableDatabase
        val cursor = database.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val name = cursor.getString(1)
                val number = cursor.getString(2)

                val user = UserModel(id, name, number)
                list.add(user)

            } while (cursor.moveToNext())

        }
        return list
    }
}