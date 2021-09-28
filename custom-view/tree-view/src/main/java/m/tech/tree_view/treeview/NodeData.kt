package m.tech.tree_view.treeview

interface NodeData<T> {
    fun getNodeChild(): List<NodeData<T>>

    val nodeViewId: String
}