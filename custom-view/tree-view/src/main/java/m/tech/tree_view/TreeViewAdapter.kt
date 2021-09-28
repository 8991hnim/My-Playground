package m.tech.tree_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginStart
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import m.tech.tree_view.model.NodeViewData

/**
 * @author minhta
 * @since 27/09/2021
 */
class TreeViewAdapter(
    private val layoutRes: Int,
    private val isSupportMargin: Boolean,
    private val onBind: (view: View, position: Int, item: NodeViewData) -> Unit,
) : ListAdapter<NodeViewData, RecyclerView.ViewHolder>(NodeViewData.DiffCallback) {

//    init {
//        setHasStableIds(true)
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return NodeHolder(view, isSupportMargin, onBind)
    }

//    override fun getItemId(position: Int): Long {
//        return getItem(position).hashCode.toLong()
//    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NodeHolder -> {
                holder.bind(getItem(position))
            }
        }
    }

    private class NodeHolder(
        itemView: View,
        private val isSupportMargin: Boolean,
        private val onBind: (view: View, position: Int, item: NodeViewData) -> Unit,
    ) : RecyclerView.ViewHolder(itemView) {

        private val space = itemView.context.resources.getDimension(R.dimen.space)

        fun bind(item: NodeViewData) =
            with(itemView) {
                onBind(this, adapterPosition, item)
                if (isSupportMargin) {
                    val margin = space * item.nodeLevel
                    itemView.setPadding(margin.toInt(), 0, 0, 0)
                }
            }
    }

}
