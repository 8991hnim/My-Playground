package m.tech.tree_view.treeview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import m.tech.tree_view.TreeViewAdapter
import m.tech.tree_view.model.NodeState
import m.tech.tree_view.model.NodeViewData
import kotlin.math.max
import kotlin.math.min

/**
 * @author 89hnim
 * @since 27/09/2021
 */
class TreeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {

    private val nodes = mutableListOf<NodeViewData>()
    private val nodesShowOnUI = mutableListOf<NodeViewData>()

    private var treeViewAdapter: TreeViewAdapter? = null

    private var onNodeSelected: ((node: NodeViewData, child: List<NodeViewData>, isSelected: Boolean) -> Unit)? =
        null

    fun initialize(
        @LayoutRes itemLayoutRes: Int,
        nodes: List<NodeData<*>>,
        showAllNodes: Boolean,
        isSupportMargin: Boolean,
        onBind: (view: View, position: Int, item: NodeViewData) -> Unit,
        onNodeSelected: ((node: NodeViewData, child: List<NodeViewData>, isSelected: Boolean) -> Unit)
    ) {
        this.onNodeSelected = onNodeSelected

        //prepare data
//        nodes.forEach {
//            it.nodeLevel = findNodeLevel(it)
//            it.isLeaf = isLeaf(nodes, it)
//            it.isExpanded = showAllNodes
//        }
//        nodes.forEach {
//            Log.d("icdd", "initialize: ${it.toReadableString()}")
//        }
//
//        this@TreeView.nodes.clear()
//        this@TreeView.nodes.addAll(nodes)
//        this@TreeView.nodesShowOnUI.clear()
//        this@TreeView.nodesShowOnUI.addAll(
//            nodes.filter { it.nodeLevel == 0 }
//        )
//
//        //prepare UI
//        layoutManager = LinearLayoutManager(context)
//        adapter = TreeViewAdapter(itemLayoutRes, isSupportMargin, onBind).also {
//            this@TreeView.treeViewAdapter = it
//        }
//
//        //update UI
//        requestUpdateTree()

        val result = mutableListOf<NodeViewDataV2>()
        result.addAll(recursiveGetDepartmentChild(emptyList(), nodes).orEmpty())
        result.forEach {
            Log.d("icdddddd", "initialize: $it")
        }
    }

    private fun recursiveGetDepartmentChild(
        parentIds: List<String>,
        listChild: List<NodeData<*>>?
    ): List<NodeViewDataV2>? {
        if (listChild == null || listChild.isEmpty()) return null
        val result = ArrayList<NodeViewDataV2>()
        listChild.forEach {
            val internalParentIds = arrayListOf<String>()
            internalParentIds.addAll(parentIds)
            internalParentIds.add(it.nodeViewId)
            result.add(
                NodeViewDataV2(
                    nodeId = it.nodeViewId,
                    parentNodeIds = parentIds.toMutableList(),
                    isExpanded = false,
                    nodeState = null,
                    nodeLevel = 0,
                    isLeaf = false,
                    isSelected = false
                )
            )
            result.addAll(
                recursiveGetDepartmentChild(
                    internalParentIds.toMutableList(),
                    it.getNodeChild()
                ).orEmpty()
            )
        }
        return result
    }


    fun expandNode(nodeId: String) {
        val parentNodeIndex = nodesShowOnUI.indexOfFirst { it.nodeId == nodeId }
        val parentNode = nodes.find { it.nodeId == nodeId }
        if (parentNodeIndex == -1 || parentNode == null) return
        parentNode.isExpanded = true

        //add lại parent node
        nodesShowOnUI.removeAt(parentNodeIndex)
        nodesShowOnUI.add(max(0, parentNodeIndex), parentNode.shallowCopy())
        //add item của parent node
        nodesShowOnUI.addAll(
            min(nodesShowOnUI.size - 1, parentNodeIndex + 1),
            nodes.filter { it.parentNodeIds.contains(nodeId) && it.nodeLevel == parentNode.nodeLevel + 1 }
        )
        requestUpdateTree()
    }

    fun collapseNode(nodeId: String) {
        val parentNodeIndex = nodesShowOnUI.indexOfFirst { it.nodeId == nodeId }
        val parentNode = nodes.find { it.nodeId == nodeId }
        if (parentNodeIndex == -1 || parentNode == null) return
        parentNode.isExpanded = false

        //add lại node, cập nhật isExpanded
        nodesShowOnUI.removeAt(parentNodeIndex)
        nodesShowOnUI.add(max(0, parentNodeIndex), parentNode.shallowCopy())
        //ẩn item của parent node
        nodes.forEach {
            if (it.parentNodeIds.contains(nodeId)) {
                it.isExpanded = false
            }
        }
        nodesShowOnUI.removeAll { it.parentNodeIds.contains(nodeId) }
        requestUpdateTree()
    }

    fun setNodesState(nodeIds: List<String>, nodeState: NodeState) {

    }

    fun clearNodesState(nodeIds: List<String>) {

    }

    fun getNodesByState(nodeState: NodeState): List<NodeViewData> {
        return nodes.filter { it.nodeState == nodeState }
    }

    fun getSelectedNodes(): List<NodeViewData> {
        return nodes.filter { it.isSelected }
    }

    fun selectNode(nodeId: String, isSelected: Boolean) {
        val node = nodes.find { it.nodeId == nodeId } ?: return
        val child = nodes.filter { it.parentNodeIds.contains(nodeId) }
        onNodeSelected?.invoke(node, child, isSelected)
    }

    fun setSelectedNode(nodes: List<NodeViewData>, isSelected: Boolean) {
        nodes.forEach { it.isSelected = isSelected }
        nodes.forEach { updatedNote ->
            this.nodes.forEach {
                if (updatedNote.nodeId == it.nodeId) it.isSelected = isSelected
            }
            this.nodesShowOnUI.forEach {
                if (updatedNote.nodeId == it.nodeId) it.isSelected = isSelected
            }
        }
    }

    fun requestUpdateTree() {
        treeViewAdapter?.submitList(nodesShowOnUI.map { it.shallowCopy() })
    }

    private fun findNodeLevel(node: NodeViewData): Int {
        return node.parentNodeIds.size
    }

    private fun isLeaf(nodes: List<NodeViewData>, node: NodeViewData): Boolean {
        nodes.forEach {
            if (it.parentNodeIds.contains(node.nodeId)) {
                return false
            }
        }
        return true
    }

}