package co.netguru.blueprint.services.store.domain

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Order(@SerializedName("id") var id: Long?,
                 @SerializedName("petId") var petId: Long?,
                 @SerializedName("quantity") var quantity: Int?,
                 @SerializedName("shipDate") var shipDate: String?,
                 @SerializedName("status") var status: OrderStatus?,
                 @SerializedName("complete") var complete: Boolean?) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readLong(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readValue(OrderStatus::class.java.classLoader) as OrderStatus,
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id!!)
        parcel.writeLong(petId!!)
        parcel.writeInt(quantity!!)
        parcel.writeString(shipDate)
        parcel.writeValue(status)
        parcel.writeValue(complete)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Order> {
        override fun createFromParcel(parcel: Parcel): Order {
            return Order(parcel)
        }

        override fun newArray(size: Int): Array<Order?> {
            return arrayOfNulls(size)
        }
    }
}