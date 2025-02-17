import android.os.Parcelable
import com.tele.crm.data.network.model.callLogs.Campaign
import com.tele.crm.domain.entites.MetaItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class Lead(
    val __v: Int,
    val _id: String,
    val addedBy: String,
    val addedById: String,
    val address: String,
    val alternate_mobile: String,
    val campaigns: List<Campaign>,
    val college_name: String,
    val createdAt: String,
    val email_id: String,
    val interested_in: String,
    val mobile: String,
    val name: String,
    val rating: Int,
    val status: String,
    val stream: String,
    val updatedAt: String,
    val year: String
) : Parcelable
