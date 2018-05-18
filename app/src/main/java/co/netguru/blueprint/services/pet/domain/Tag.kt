package co.netguru.blueprint.services.pet.domain

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class Tag(@SerializedName("id") var id: Int?,
               @SerializedName("name") var name: String?) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.readInt(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Tag> {
        override fun createFromParcel(parcel: Parcel): Tag {
            return Tag(parcel)
        }

        override fun newArray(size: Int): Array<Tag?> {
            return arrayOfNulls(size)
        }
    }
}