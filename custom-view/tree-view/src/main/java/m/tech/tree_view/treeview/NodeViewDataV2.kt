package m.tech.tree_view.treeview

import androidx.recyclerview.widget.DiffUtil

data class NodeViewDataV2<T>(
    val data: NodeData<T>,
    val nodeId: String,
    val parentNodeIds: List<String>,
    var isExpanded: Boolean,
    var nodeState: NodeState?,
    var nodeLevel: Int,
    var isLeaf: Boolean,
    var isSelected: Boolean
) {
    constructor(data: NodeData<T>, nodeId: String, parentNodeIds: List<String>) : this(
        data = data,
        nodeId = nodeId,
        parentNodeIds = parentNodeIds,
        isExpanded = false,
        nodeState = null,
        nodeLevel = 0,
        isLeaf = false,
        isSelected = false
    )

    private fun internalAreItemsTheSame(item: NodeViewDataV2<T>): Boolean {
        return nodeId == item.nodeId && data.areItemsTheSame(item.data)
    }

    private fun internalAreContentsTheSame(item: NodeViewDataV2<T>): Boolean {
        return isExpanded == item.isExpanded
            && nodeState == item.nodeState
            && nodeLevel == item.nodeLevel
            && isSelected == item.isSelected
            && isLeaf == item.isLeaf
            && data.areContentsTheSame(item.data)
    }

    /**
     * compare [NodeViewDataV2] changes && [T] changes
     */
    internal class DiffCallback<T> : DiffUtil.ItemCallback<NodeViewDataV2<T>>() {
        override fun areItemsTheSame(
            oldItem: NodeViewDataV2<T>,
            newItem: NodeViewDataV2<T>
        ): Boolean {
            return oldItem.internalAreItemsTheSame(newItem)
        }

        override fun areContentsTheSame(
            oldItem: NodeViewDataV2<T>,
            newItem: NodeViewDataV2<T>
        ): Boolean {
            return oldItem.internalAreContentsTheSame(newItem)
        }
    }
}