import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.nantouparking.databinding.ItemParkingInfoBinding
import com.jotangi.nantouparking.model.ParkStatus

interface ParkingInfoClickListener {
    fun onParkingInfoItemClick()
}

class ParkingInfoAdapter(
    private var data: List<ParkStatus>,
    val context: Context,
    private val listener: ParkingInfoClickListener?
) : RecyclerView.Adapter<ParkingInfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParkingInfoViewHolder {
        val binding = ItemParkingInfoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ParkingInfoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ParkingInfoViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)

        // Set click listener for the map button
        holder.binding.btnMap.setOnClickListener {
            val gmmIntentUri = Uri.parse("geo:0,0?q=${Uri.encode(item.address)}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            context.startActivity(mapIntent)
        }
    }

    override fun getItemCount(): Int = data.size

    fun updateDataSource(dataSource: List<ParkStatus>) {
        this.data = dataSource
        notifyDataSetChanged()
    }
}

class ParkingInfoViewHolder(val binding: ItemParkingInfoBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ParkStatus) {
        binding.itemParkingInfoTitleTextView.text = item.road
        binding.itemParkingInfoCountTextView.text = "剩餘 ${item.emptyCount} 停車格"
        binding.address.text = item.address
    }
}
