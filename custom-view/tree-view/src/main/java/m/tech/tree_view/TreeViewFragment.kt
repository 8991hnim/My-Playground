package m.tech.tree_view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.fragment_tree_view.*
import kotlinx.coroutines.launch
import m.tech.tree_view.model.NodeState
import java.util.*

/**
 * @author 89hnim
 * @since 27/09/2021
 */
class TreeViewFragment : Fragment(R.layout.fragment_tree_view) {

    object NodeStateSelected : NodeState()

    abstract class A {
        abstract val id: String
        var isChecked: Boolean = false

        abstract fun shallowCopy(): A
    }

    data class B(override val id: String) : A(){
        override fun shallowCopy(): B {
            return B(id).apply {
                this@B.isChecked = isChecked
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val b = B("ID")
        b.isChecked = true
        Log.d("icd2", "onViewCreated: ${b.isChecked}")
        Log.d("icd2", "onViewCreated: ${b.shallowCopy().isChecked}")

        tree_view.initialize(
            itemLayoutRes = R.layout.item_node,
            nodes = SampleModelV2.getList(),
            isSupportMargin = true,
            showAllNodes = false,
            onBind = { view, position, item ->
                val myItem = (item as? SampleModel) ?: return@initialize
                val ivArrow = view.findViewById<AppCompatImageView>(R.id.iv_arrow)
                val rbCheck = view.findViewById<RadioButton>(R.id.rb_check)
                val tvNode = view.findViewById<TextView>(R.id.node_name)

                tvNode.text = myItem.name
                if (myItem.isLeaf) {
                    ivArrow.visibility = View.INVISIBLE
                } else {
                    ivArrow.visibility = View.VISIBLE
                }

                ivArrow.rotation = 0f
                val rotateDegree = if (myItem.isExpanded) 90f else 0f
                ivArrow.animate().rotationBy(rotateDegree).start()

                rbCheck.isChecked = myItem.isSelected

                rbCheck.setOnClickListener {
                    tree_view.selectNode(
                        item.nodeId,
                        !item.isSelected
                    ) //will trigger onNodeSelected
                }

                ivArrow.setOnClickListener {
                    if (myItem.isExpanded) {
                        tree_view.collapseNode(myItem.nodeId)
                    } else {
                        tree_view.expandNode(myItem.nodeId)
                    }
                }
            }, onNodeSelected = { node, child, isSelected ->
                tree_view.setSelectedNode(arrayListOf(node).apply { addAll(child) }, isSelected)
                tree_view.requestUpdateTree()
            })

        btn_result.setOnClickListener {
            val list = tree_view.getSelectedNodes()
            list.forEach {
                Log.d("icd", "onViewCreated: ${(it as SampleModel).name}")
            }
        }
    }

    companion object {
        fun getNodes(): MutableList<SampleModel> {
            val parentId1 = UUID.randomUUID().toString()
            val parentId2 = UUID.randomUUID().toString()
            val parentId3 = UUID.randomUUID().toString()
            val parentId4 = UUID.randomUUID().toString()
            val parentId5 = UUID.randomUUID().toString()

            return mutableListOf(
                SampleModel(
                    parentId1,
                    emptyList(),
                    "sample_1",
                    "Sample 1: Sample Node Root",
                    false
                ),
                SampleModel(
                    parentId2,
                    emptyList(),
                    "sample_2",
                    "Sample 2: Sample Node Root",
                    false
                ),
                SampleModel(
                    parentId3,
                    listOf(parentId1),
                    "sample_3",
                    "Sample 3: A Node of Sample 1",
                    false
                ),
                SampleModel(
                    parentId4,
                    listOf(parentId1),
                    "sample_4",
                    "Sample 4 A Node of Sample 1",
                    false
                ),
                SampleModel(
                    parentId5,
                    emptyList(),
                    "sample_5",
                    "Sample 5: Sample Node Root Has No Child",
                    false
                ),
                SampleModel(
                    UUID.randomUUID().toString(),
                    listOf(parentId2),
                    "sample_6",
                    "Sample 6: Child of Sample 2",
                    false
                ),
                SampleModel(
                    UUID.randomUUID().toString(),
                    listOf(parentId3, parentId1),
                    "sample_7",
                    "Sample 7: Child of Sample 3",
                    false
                ),
                SampleModel(
                    UUID.randomUUID().toString(),
                    listOf(parentId3, parentId1),
                    "sample_8",
                    "Sample 8: Child of Sample 3",
                    false
                ),
            )

        }
    }
}