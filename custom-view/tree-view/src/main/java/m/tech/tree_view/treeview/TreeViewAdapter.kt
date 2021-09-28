package m.tech.tree_view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import m.tech.tree_view.treeview.NodeViewDataV2
import m.tech.tree_view.treeview.TreeViewManager

/**
 * @author minhta
 * @since 27/09/2021
 */
internal class TreeViewAdapter<T>(
    private val layoutRes: Int,
    private val isSupportMargin: Boolean,
//    private val onBind: (holder: View, position: Int, item: NodeViewDataV2<T>, data: T) -> Unit,
    private val listener: TreeViewManager.Listener<T>
) : ListAdapter<NodeViewDataV2<T>, TreeViewAdapter.NodeHolder<T>>(NodeViewDataV2.DiffCallback()) {

//    init {
//        setHasStableIds(true)
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NodeHolder<T> {
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return NodeHolder(view, isSupportMargin, listener)
    }

//    override fun getItemId(position: Int): Long {
//        return getItem(position).hashCode.toLong()
//    }

    override fun onBindViewHolder(holder: NodeHolder<T>, position: Int) {
        holder.bind(getItem(position))
    }

    internal class NodeHolder<T>(
        itemView: View,
        private val isSupportMargin: Boolean,
        private  val listener: TreeViewManager.Listener<T>,
    ) : RecyclerView.ViewHolder(itemView) {

        private val space = itemView.context.resources.getDimension(R.dimen.space)

        fun bind(item: NodeViewDataV2<T>) =
            with(itemView) {
                listener.onBind(this, adapterPosition, item, item.data as T)
                if (isSupportMargin) {
                    val margin = space * item.nodeLevel
                    itemView.setPadding(margin.toInt(), 0, 0, 0)
                }
            }
    }
}
