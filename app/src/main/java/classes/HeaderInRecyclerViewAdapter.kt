package classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.involio.R

class HeaderAdapter: RecyclerView.Adapter<HeaderAdapter.HeaderViewHolder>() {
    private var portfoliosIncom: Double = 12.3
    private var sp500Incom: Double = 15.4
    private var usersIncom: Double = 5.4

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view){
        private val portfoliosIncomTextView: TextView = itemView.findViewById(R.id.my_incom_data)
        private val sp500IncomTextView: TextView = itemView.findViewById(R.id.sp500_incom_data)
        private val usersIncomTextView: TextView = itemView.findViewById(R.id.users_incom_data)

        fun bind(portfoliosIncom: Double, sp500Incom: Double, usersIncom: Double) {
            portfoliosIncomTextView.text = portfoliosIncom.toString()
            sp500IncomTextView.text = sp500Incom.toString()
            usersIncomTextView.text = usersIncom.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.portfolios_header, parent, false)
        return HeaderViewHolder(view)
    }

    /* Binds number of flowers to the header. */
    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        holder.bind(portfoliosIncom, sp500Incom, usersIncom)
    }

    /* Returns number of items, since there is only one item in the header return one  */
    override fun getItemCount(): Int {
        return 1
    }

    /* Updates header to display number of flowers when a flower is added or subtracted. */
    fun updateFlowerCount(updatePortfoliosIncom: Double, updateSp500Incom: Double, updateUsersIncom: Double) {
        portfoliosIncom = updatePortfoliosIncom
        sp500Incom = updateSp500Incom
        usersIncom = updateUsersIncom
        notifyDataSetChanged()
    }
}