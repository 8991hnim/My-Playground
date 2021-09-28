package m.tech.tree_view

import m.tech.tree_view.model.NodeState
import m.tech.tree_view.model.NodeViewData

data class SampleModel(
    override val nodeId: String,
    override val parentNodeIds: List<String>,
    val id: String,
    val name: String,
    override var isExpanded: Boolean,
    override var nodeState: NodeState? = null,
    override var nodeLevel: Int = 0,
    override var isLeaf: Boolean = false,
    override var isSelected: Boolean = false,
) : NodeViewData() {

    override fun areItemsTheSame(item: NodeViewData): Boolean {
        return if (item !is SampleModel) false
        else id == item.id
    }

    override fun areContentsTheSame(item: NodeViewData): Boolean {
        return if (item !is SampleModel) false
        else name == item.name
    }

    override fun shallowCopy(): NodeViewData {
        return copy()
    }

}