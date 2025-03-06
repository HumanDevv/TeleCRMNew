import android.os.Parcelable
import com.tele.crm.data.network.model.callLogs.Campaign
import kotlinx.parcelize.Parcelize

@Parcelize
data class Lead(
    val __v: Int? = 0,
    val _id: String? = "",
    val addedBy: String? = "",
    val addedById: String? = "",
    val address: String? = "",
    val alternate_mobile: String? = "",
    val campaigns: List<Campaign>? = emptyList(), // Ensuring empty list instead of null
    val college_name: String? = "",
    val createdAt: String? = "",
    val email_id: String? = "",
    val interested_in: String? = "",
    val mobile: String? = "",
    val name: String? = "",
    val rating: String? = "",
    val status: String? = "",
    val stream: String? = "",
    val updatedAt: String? = "",
    val year: String? = ""
) : Parcelable
