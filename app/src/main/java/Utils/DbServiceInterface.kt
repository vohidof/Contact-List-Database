package Utils

import Models.UserModel

interface DbServiceInterface {

    fun addContact(userModel: UserModel)
    fun deleteContact(userModel: UserModel)
    fun updateContact(userModel: UserModel):Int
    fun getAllContact() : List<UserModel>
    
}