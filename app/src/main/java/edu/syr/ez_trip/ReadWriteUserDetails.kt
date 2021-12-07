package edu.syr.ez_trip

class ReadWriteUserDetails(fullName : String, email : String, phoneNum : String) {

    var fullName: String
    var email: String
    var phoneNum: String

    init {
        this.fullName = fullName
        this.email = email
        this.phoneNum = phoneNum
    }

}