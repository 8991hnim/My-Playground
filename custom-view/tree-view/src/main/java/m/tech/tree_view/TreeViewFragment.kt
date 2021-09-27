package m.tech.tree_view

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.fragment_tree_view.*
import kotlinx.coroutines.launch
import java.util.*

/**
 * @author 89hnim
 * @since 27/09/2021
 */
class TreeViewFragment : Fragment(R.layout.fragment_tree_view) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            tree_view.initialize(
                itemLayoutRes = R.layout.item_node,
                nodes = getNodes(),
                showAllNodes = false,
                onBind = { view, position, item ->
                    val myItem = (item as? SampleModel) ?: return@initialize
                    view.findViewById<TextView>(R.id.node_name).text = myItem.name
                    view.setOnClickListener {
                        if (myItem.isExpanded) {
                            tree_view.collapseNode(myItem.nodeId)
                        } else {
                            tree_view.expandNode(myItem.nodeId)
                        }
                    }
                }
            )
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
                    null,
                    "sample_1",
                    "Sample 1: Sample Node Root",
                    false
                ),
                SampleModel(
                    parentId2,
                    null,
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
                    null,
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