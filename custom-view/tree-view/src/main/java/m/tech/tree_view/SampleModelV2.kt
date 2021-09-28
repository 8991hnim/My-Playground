package m.tech.tree_view

import m.tech.tree_view.treeview.NodeData
import java.util.*

data class SampleModelV2(
    val id: String,
    val name: String,
    val child: List<SampleModelV2>
) : NodeData<SampleModelV2> {
    override fun getNodeChild(): List<SampleModelV2> {
        return child
    }

    override val nodeViewId: String = id

    companion object {
        fun getList() = arrayListOf(
            SampleModelV2(
                "ID_1", "Sample 1", listOf(
                    SampleModelV2("ID_1_1", "Child 1.1: child of 1", emptyList()),
                    SampleModelV2("ID_1_2", "Child 1.2: child of 1", emptyList()),
                )
            ),
            SampleModelV2(
                "ID_2", "Sample 2", listOf(
                    SampleModelV2("ID_2_1", "Child 2.1: child of 2", emptyList()),
                    SampleModelV2(
                        "ID_2_2", "Child 2.2: child of 2", listOf(
                            SampleModelV2("ID_2_2_1", "Child of 2.2", emptyList())
                        )
                    ),
                    SampleModelV2(
                        "ID_2_3", "Child 2.3: child of 2", listOf(
                            SampleModelV2(
                                "ID_2_3_1", "Child of 2.3", listOf(
                                    SampleModelV2(
                                        "ID_2_3_1_1", "Child of 2.3.1", emptyList()
                                    )
                                )
                            ),
                        )
                    )
                )
            ),
            SampleModelV2("ID_3", "Sample 3", emptyList()),
            SampleModelV2("ID_4", "Sample $", emptyList()),
        )
    }
}