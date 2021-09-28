package m.tech.tree_view.treeview

interface NodeData<T> {
    fun getNodeChild(): List<NodeData<T>>

    val nodeViewId: String

    fun areItemsTheSame(item: NodeData<T>): Boolean

    fun areContentsTheSame(item: NodeData<T>): Boolean
}