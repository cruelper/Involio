package classes

import android.widget.TextView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.app.Activity
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.graphics.drawable.toDrawable
import com.example.involio.R


class PiecesAdapter (
    private val context: Activity,
    private val names: Array<String>,
    private val parts: Array<String>,
    private val colors: Array<Int>
) :
    ArrayAdapter<String>(context, R.layout.piece, names) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView: View = inflater.inflate(R.layout.piece, null, true)

        val nameText = rowView.findViewById(R.id.name) as TextView
        val colorBlock = rowView.findViewById(R.id.color) as TextView
        val partText = rowView.findViewById(R.id.part) as TextView
        nameText.text = names[position]
        colorBlock.background = colors[position].toDrawable()
        partText.text = parts[position]
        return rowView
    }


}