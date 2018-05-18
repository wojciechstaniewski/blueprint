package co.netguru.blueprint.services.user.domain

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class User(@SerializedName("id") var id: Long?,
                @SerializedName("username") var username: String?,
                @SerializedName("firstName") var firstName: String?,
                @SerializedName("lastName") var lastName: String?,
                @SerializedName("email") var email: String?,
                @SerializedName("password") var password: String?,
                @SerializedName("phone") var phone: String?,
                @SerializedName("userStatus") var userStatus: Int?) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id!!)
        parcel.writeString(username)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(email)
        parcel.writeString(password)
        parcel.writeString(phone)
        parcel.writeInt(userStatus!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}