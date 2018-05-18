package co.netguru.blueprint.services.authentication

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ApiAuth(@SerializedName("access_token") var accessToken: String? = null,
                   @SerializedName("expires_in") var expiresIn: Int? = null,
                   @SerializedName("refresh_expires_in") var refreshExpires: Int? = null,
                   @SerializedName("refresh_token") var refreshToken: String? = null,
                   @SerializedName("token_type") var tokenType: String? = null,
                   @SerializedName("id_token") var idToken: String? = null,
                   @SerializedName("not-before-policy") var notBeforePolicy: String? = null,
                   @SerializedName("session_state") var sessionState: String? = null) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(accessToken)
        parcel.writeInt(expiresIn!!)
        parcel.writeInt(refreshExpires!!)
        parcel.writeString(refreshToken)
        parcel.writeString(tokenType)
        parcel.writeString(idToken)
        parcel.writeString(notBeforePolicy)
        parcel.writeString(sessionState)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR = object : Parcelable.Creator<ApiAuth> {
            override fun createFromParcel(parcel: Parcel): ApiAuth {
                return ApiAuth(parcel)
            }

            override fun newArray(size: Int): Array<ApiAuth?> {
                return arrayOfNulls(size)
            }
        }
    }
}

