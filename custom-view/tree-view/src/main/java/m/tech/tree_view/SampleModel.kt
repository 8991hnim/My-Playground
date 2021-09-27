package m.tech.tree_view

import m.tech.tree_view.model.NodeViewData

data class SampleModel(
    override val nodeId: String,
    override val parentNodeIds: List<String>?,
    val id: String,
    val name: String,
    var isChecked: Boolean,
    override var isExpanded: Boolean = false,
) : NodeViewData() {

    override fun areItemsTheSame(item: NodeViewData): Boolean {
        return if (item !is SampleModel) false
        else id == item.id
    }

    override fun areContentsTheSame(item: NodeViewData): Boolean {
        return if (item !is SampleModel) false
        else name == item.name && isChecked == item.isChecked
    }

    override fun shallowCopy(): NodeViewData {
        return copy()
    }

}