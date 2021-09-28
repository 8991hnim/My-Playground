package m.tech.tree_view

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_tree_view.*
import m.tech.tree_view.treeview.NodeState
import m.tech.tree_view.treeview.NodeViewDataV2
import m.tech.tree_view.treeview.TreeView

/**
 * @author 89hnim
 * @since 27/09/2021
 */
class TreeViewFragment : Fragment(R.layout.fragment_tree_view),
    TreeView.Listener<SampleModelV2> {

    private lateinit var treeViewManager: TreeView<SampleModelV2>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        treeViewManager = TreeView()
        treeViewManager.initialize(
            recyclerView = tree_view,
            itemLayoutRes = R.layout.item_node,
            nodes = SampleModelV2.getList(),
            isSupportMargin = true,
            showAllNodes = false,
            listener = this
        )

        btn_result.setOnClickListener {
            val list = treeViewManager.getSelectedNodes()
            list.forEach {
                Log.d("icd", "onViewCreated: $it")
            }
        }
    }

    object NodeStateDisabled : NodeState()

    override fun onBind(
        holder: View,
        position: Int,
        item: NodeViewDataV2<SampleModelV2>,
        data: SampleModelV2
    ) {
        val ivArrow = holder.findViewById<AppCompatImageView>(R.id.iv_arrow)
        val rbCheck = holder.findViewById<CheckBox>(R.id.rb_check)
        val tvNode = holder.findViewById<TextView>(R.id.node_name)

        tvNode.text = data.name
        if (item.isLeaf) {
            ivArrow.visibility = View.INVISIBLE
        } else {
            ivArrow.visibility = View.VISIBLE
        }

        ivArrow.rotation = 0f
        val rotateDegree = if (item.isExpanded) 90f else 0f
        ivArrow.animate().rotationBy(rotateDegree).start()

        rbCheck.isChecked = item.isSelected

        rbCheck.setOnClickListener {
            treeViewManager.selectNode(
                item.nodeId,
                !item.isSelected
            ) //will trigger onNodeSelected
        }

        if (item.nodeState == NodeStateDisabled) {
            rbCheck.backgroundTintList = ColorStateList.valueOf(Color.GRAY)
            rbCheck.isEnabled = false
        } else {
            rbCheck.backgroundTintList = ColorStateList.valueOf(Color.GREEN)
            rbCheck.isEnabled = true
        }

        holder.setOnClickListener {
            if (item.isExpanded) {
                treeViewManager.collapseNode(item.nodeId)
            } else {
                treeViewManager.expandNode(item.nodeId)
            }
        }
    }

    override fun onNodeSelected(
        node: NodeViewDataV2<SampleModelV2>,
        child: List<NodeViewDataV2<SampleModelV2>>,
        isSelected: Boolean
    ) {
        treeViewManager.setSelectedNode(arrayListOf(node).apply { addAll(child) }, isSelected)
        treeViewManager.setNodesState(
            child.map { it.nodeId },
            if (isSelected) NodeStateDisabled else null
        )
        treeViewManager.requestUpdateTree()
    }
}