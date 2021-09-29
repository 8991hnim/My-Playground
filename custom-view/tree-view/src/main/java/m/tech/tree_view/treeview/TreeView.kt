package m.tech.tree_view.treeview

import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.max
import kotlin.math.min

/**
 * @author 89hnim
 * @since 27/09/2021
 */
class TreeView<T> {

    private val nodes = mutableListOf<NodeViewDataV2<T>>()
    private val nodesShowOnUI = mutableListOf<NodeViewDataV2<T>>()

    private var treeViewAdapter: TreeViewAdapter<T>? = null

    private var listener: Listener<T>? = null

    interface Listener<T> {
        fun onBind(holder: View, position: Int, item: NodeViewDataV2<T>, data: T)

        fun onNodeSelected(
            node: NodeViewDataV2<T>,
            child: List<NodeViewDataV2<T>>,
            isSelected: Boolean
        )
    }

    fun initialize(
        recyclerView: RecyclerView,
        @LayoutRes itemLayoutRes: Int,
        nodes: List<NodeData<T>>,
        showAllNodes: Boolean,
        isSupportMargin: Boolean,
        listener: Listener<T>,
        vararg adapters: RecyclerView.Adapter<*> = emptyArray()
    ) {
        this.listener = listener

        //prepare data
        val result = mutableListOf<NodeViewDataV2<T>>()
        result.addAll(recursiveGetDepartmentChild(emptyList(), nodes).orEmpty())
        result.forEach {
            it.nodeLevel = findNodeLevel(it)
            it.isLeaf = isLeaf(result, it)
            it.isExpanded = showAllNodes
        }

        //add data to root list
        this@TreeView.nodes.clear()
        this@TreeView.nodes.addAll(result)

        //add data to ui list
        this@TreeView.nodesShowOnUI.clear()
        this@TreeView.nodesShowOnUI.addAll(
            result.filter { it.nodeLevel == 0 }
        )

        //prepare UI
        with(recyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = ConcatAdapter(
                listOf(
                    *adapters,
                    TreeViewAdapter(itemLayoutRes, isSupportMargin, listener).also {
                        this@TreeView.treeViewAdapter = it
                    }
                )
            )
        }

        //update UI
        requestUpdateTree()
    }

    private fun recursiveGetDepartmentChild(
        parentIds: List<String>,
        listChild: List<NodeData<T>>?
    ): List<NodeViewDataV2<T>>? {
        if (listChild == null || listChild.isEmpty()) return null
        val result = ArrayList<NodeViewDataV2<T>>()
        listChild.forEach {
            val internalParentIds = arrayListOf<String>()
            internalParentIds.addAll(parentIds)
            internalParentIds.add(it.nodeViewId)
            result.add(
                NodeViewDataV2(
                    data = it,
                    nodeId = it.nodeViewId,
                    parentNodeIds = parentIds.toMutableList()
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
        nodesShowOnUI.add(max(0, parentNodeIndex), parentNode.copy())
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
        nodesShowOnUI.add(max(0, parentNodeIndex), parentNode.copy())
        //ẩn item của parent node
        nodes.forEach {
            if (it.parentNodeIds.contains(nodeId)) {
                it.isExpanded = false
            }
        }
        nodesShowOnUI.removeAll { it.parentNodeIds.contains(nodeId) }
        requestUpdateTree()
    }

    fun setNodesState(nodeIds: List<String>, nodeState: NodeState?) {
        nodes.forEach { updatedNote ->
            this.nodes.forEach {
                if (nodeIds.contains(it.nodeId)) it.nodeState = nodeState
            }
            this.nodesShowOnUI.forEach {
                if (nodeIds.contains(it.nodeId)) it.nodeState = nodeState
            }
        }
    }

    fun clearNodesState(nodeIds: List<String>) {
        this.nodes.forEach {
            it.nodeState = null
        }
        this.nodesShowOnUI.forEach {
            it.nodeState = null
        }
    }

    fun getNodesByState(nodeState: NodeState): List<NodeViewDataV2<T>> {
        return nodes.filter { it.nodeState == nodeState }
    }

    fun getSelectedNodes(): List<T> {
        return nodes.filter { it.isSelected }.map { it.data as T }
    }

    fun selectNode(nodeId: String, isSelected: Boolean) {
        val node = nodes.find { it.nodeId == nodeId } ?: return
        val child = nodes.filter { it.parentNodeIds.contains(nodeId) }
        listener?.onNodeSelected(node, child, isSelected)
    }

    fun setSelectedNode(nodes: List<NodeViewDataV2<T>>, isSelected: Boolean) {
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
        treeViewAdapter?.submitList(nodesShowOnUI.map { it.copy() })
    }

    private fun findNodeLevel(node: NodeViewDataV2<T>): Int {
        return node.parentNodeIds.size
    }

    private fun isLeaf(nodes: List<NodeViewDataV2<T>>, node: NodeViewDataV2<T>): Boolean {
        nodes.forEach {
            if (it.parentNodeIds.contains(node.nodeId)) {
                return false
            }
        }
        return true
    }

}