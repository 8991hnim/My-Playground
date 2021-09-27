package m.tech.tree_view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.LayoutRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import m.tech.tree_view.model.NodeState
import m.tech.tree_view.model.NodeViewData
import kotlin.math.max
import kotlin.math.min

/**
 * @author minhta
 * @since 27/09/2021
 */
class TreeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {

    private val nodes = mutableListOf<NodeViewData>()
    private val nodesShowOnUI = mutableListOf<NodeViewData>()

    private val _nodesShowOnUiLivedata = NodeViewData.LiveData()
    private val nodesShowOnUiLiveData: LiveData<List<NodeViewData>>
        get() = _nodesShowOnUiLivedata

    private var treeViewAdapter: TreeViewAdapter? = null

    private var lifecycleOwner: LifecycleOwner? = null

    suspend fun initialize(
        @LayoutRes itemLayoutRes: Int,
        nodes: List<NodeViewData>,
        showAllNodes: Boolean,
        onBind: (view: View, position: Int, item: NodeViewData) -> Unit,
    ) {
        //prepare data
        withContext(Dispatchers.Default) {
            nodes.forEach {
//                launch {
                it.nodeLevel = findNodeLevel(it)
//                }
            }

            this@TreeView.nodes.clear()
            this@TreeView.nodes.addAll(nodes)
            this@TreeView.nodes.forEach {
                Log.d("icd", "initialize" + it.toReadableString())
            }
            this@TreeView.nodesShowOnUI.clear()
            this@TreeView.nodesShowOnUI.addAll(
                nodes.onEach { node ->
                    node.isExpanded = showAllNodes
                }.filter { it.nodeLevel == 0 }
            )
        }

        //prepare UI
        layoutManager = LinearLayoutManager(context)
        adapter = TreeViewAdapter(itemLayoutRes, onBind, null).also {
            this@TreeView.treeViewAdapter = it
        }

        //update UI
        _nodesShowOnUiLivedata.setValue(this@TreeView.nodesShowOnUI)
        subscribeObserver()
    }

    private fun subscribeObserver() {
        lifecycleOwner = findViewTreeLifecycleOwner()
        lifecycleOwner?.apply {
            nodesShowOnUiLiveData.observe(this) { listNode ->
                listNode.forEach {
                    Log.d("icd", it.toReadableString())
                }
                this@TreeView.treeViewAdapter?.submitList(listNode)
            }
        }
    }

    fun expandNode(nodeId: String) {
        val parentNodeIndex = nodes.indexOfFirst { it.nodeId == nodeId }
        if (parentNodeIndex == -1) return
        val parentNode = nodes[parentNodeIndex]
        parentNode.isExpanded = true

        nodesShowOnUI.removeAll { it.parentNodeIds?.contains(nodeId) == true || it.nodeId == nodeId }
        //add lại header
        nodesShowOnUI.add(
            max(0, parentNodeIndex - 1),
            parentNode
        )
        //add item của header
        nodesShowOnUI.addAll(
            min(nodesShowOnUI.size - 1, parentNodeIndex + 1),
            nodes.filter { it.parentNodeIds?.contains(nodeId) == true && it.nodeLevel == parentNode.nodeLevel + 1 }
        )

        _nodesShowOnUiLivedata.setValue(this@TreeView.nodesShowOnUI)
    }

    fun collapseNode(nodeId: String) {
        val parentNodeIndex = nodes.indexOfFirst { it.nodeId == nodeId }
        if (parentNodeIndex == -1) return
        val parentNode = nodes[parentNodeIndex]
        parentNode.isExpanded = false

        nodesShowOnUI.removeAll { it.parentNodeIds?.contains(nodeId) == true }
        _nodesShowOnUiLivedata.setValue(this@TreeView.nodesShowOnUI)
    }

    fun setNodesState(nodeId: List<String>, nodeState: NodeState) {

    }

    private fun findNodeLevel(node: NodeViewData): Int {
//        if (node.parentNodeId == null) return 0
//        val parentNode = nodes.find { it.nodeId == node.parentNodeId } ?: return 0
//        return 1 + findNodeLevel(nodes, parentNode)
        return node.parentNodeIds?.size ?: 0
    }

}