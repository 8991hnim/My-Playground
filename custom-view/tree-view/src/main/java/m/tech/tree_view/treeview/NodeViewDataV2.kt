package m.tech.tree_view.treeview

import m.tech.tree_view.model.NodeState

data class NodeViewDataV2(
    val nodeId: String,
    val parentNodeIds: List<String>,
    var isExpanded: Boolean,
    var nodeState: NodeState?,
    var nodeLevel: Int,
    var isLeaf: Boolean,
    var isSelected: Boolean
) {
}