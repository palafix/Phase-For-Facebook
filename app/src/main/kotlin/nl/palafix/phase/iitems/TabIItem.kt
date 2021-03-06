package nl.palafix.phase.iitems

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import ca.allanwang.kau.iitems.KauIItem
import ca.allanwang.kau.utils.*
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IItem
import com.mikepenz.fastadapter_extensions.drag.IDraggable
import nl.palafix.phase.R
import nl.palafix.phase.facebook.FbItem
import nl.palafix.phase.utils.Prefs

/**
 * Created by Allan Wang on 26/11/17.
 **/
class TabIItem(val item: FbItem) : KauIItem<TabIItem, TabIItem.ViewHolder>(
        R.layout.iitem_tab_preview,
        { ViewHolder(it) }
), IDraggable<TabIItem, IItem<*, *>> {

    override fun withIsDraggable(draggable: Boolean): TabIItem = this

    override fun isDraggable() = true

    class ViewHolder(itemView: View) : FastAdapter.ViewHolder<TabIItem>(itemView) {

        val image: ImageView by bindView(R.id.image)
        val text: TextView by bindView(R.id.text)

        override fun bindView(item: TabIItem, payloads: MutableList<Any>) {
            val isInToolbar = adapterPosition < 5
            val color = if (isInToolbar) Prefs.iconColor else Prefs.textColor
            image.setIcon(item.item.icon, 20, color)
            if (isInToolbar)
                text.invisible()
            else {
                text.visible().setText(item.item.titleId)
                text.setTextColor(color.withAlpha(200))
            }
        }

        override fun unbindView(item: TabIItem) {
            image.setImageDrawable(null)
            text.visible().text = null
        }

    }
}