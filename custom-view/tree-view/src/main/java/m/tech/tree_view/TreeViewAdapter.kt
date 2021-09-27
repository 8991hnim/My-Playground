package m.tech.tree_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import m.tech.tree_view.model.NodeViewData

/**
 * @author minhta
 * @since 27/09/2021
 */
class TreeViewAdapter(
    private val layoutRes: Int,
    private val onBind: (view: View, position: Int, item: NodeViewData) -> Unit,
    private val interaction: Interaction? = null
) : ListAdapter<NodeViewData, RecyclerView.ViewHolder>(NodeViewData.DiffCallback) {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return NodeHolder(view, onBind, interaction)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).hashCode.toLong()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NodeHolder -> {
                holder.bind(getItem(position))
            }
        }
    }

    private class NodeHolder(
        itemView: View,
        private val onBind: (view: View, position: Int, item: NodeViewData) -> Unit,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: NodeViewData) =
            with(itemView) {
                onBind(this, adapterPosition, item)
            }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: NodeViewData)
    }

}
