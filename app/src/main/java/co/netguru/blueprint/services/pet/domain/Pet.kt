package co.netguru.blueprint.services.pet.domain

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class Pet(@SerializedName("id") var id: Long?,
               @SerializedName("category") var category: Category?,
               @SerializedName("name") var name: String?,
               @SerializedName("photoUrls") var photoUrls: List<String>?,
               @SerializedName("tags") var tags: List<Tag>?,
               @SerializedName("status") var status: Status) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readValue(Category::class.java.classLoader) as Category,
            parcel.readString(),
            parcel.createStringArrayList(),
            listOf<Tag>().apply { parcel.readTypedList(this, Tag) },
            parcel.readValue(Status::class.java.classLoader) as Status)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id!!)
        parcel.writeValue(category)
        parcel.writeString(name)
        parcel.writeList(photoUrls)
        parcel.writeList(tags)
        parcel.writeValue(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Pet> {
        override fun createFromParcel(parcel: Parcel): Pet {
            return Pet(parcel)
        }

        override fun newArray(size: Int): Array<Pet?> {
            return arrayOfNulls(size)
        }
    }
}