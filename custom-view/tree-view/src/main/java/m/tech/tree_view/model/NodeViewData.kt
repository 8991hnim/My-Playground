package m.tech.tree_view.model

import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil

/**
 * @author minhta
 * @since 27/09/2021
 */
abstract class NodeViewData {

    abstract val nodeId: String

    abstract val parentNodeIds: List<String>?

    var nodeState: NodeState? = null

   abstract var isExpanded: Boolean

    internal var nodeLevel: Int = 0

    internal val hashCode: Int
        get() = nodeId.hashCode() + parentNodeIds.hashCode()

    abstract fun areItemsTheSame(item: NodeViewData): Boolean

    abstract fun areContentsTheSame(item: NodeViewData): Boolean

    abstract fun shallowCopy(): NodeViewData

    private fun internalAreItemsTheSame(item: NodeViewData): Boolean {
        return nodeId == item.nodeId
    }

    private fun internalAreContentsTheSame(item: NodeViewData): Boolean {
        return isExpanded == item.isExpanded && nodeState == item.nodeState && nodeLevel == item.nodeLevel
    }

    internal object DiffCallback : DiffUtil.ItemCallback<NodeViewData>() {
        override fun areItemsTheSame(
            oldItem: NodeViewData,
            newItem: NodeViewData
        ): Boolean {
            return oldItem.areItemsTheSame(newItem) && oldItem.internalAreItemsTheSame(newItem)
        }

        override fun areContentsTheSame(
            oldItem: NodeViewData,
            newItem: NodeViewData
        ): Boolean {
            return oldItem.areContentsTheSame(newItem) && oldItem.internalAreContentsTheSame(newItem)
        }
    }

    internal class LiveData : MutableLiveData<List<NodeViewData>>() {

        override fun setValue(value: List<NodeViewData>) {
            super.setValue(value.map { it.shallowCopy() })
        }

        override fun postValue(value: List<NodeViewData>) {
            super.postValue(value.map { it.shallowCopy() })
        }

    }

    internal fun toReadableString(): String {
        return "Node $nodeId: Child $parentNodeIds - NoteLevel: $nodeLevel - State $nodeState - Expanded $isExpanded"
    }

}