package ru.d3st.sberschool.applelist

import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


class ApplesTouchHelperCallback constructor(private val mAdapter: ItemTouchHelperAdapter) :
    ItemTouchHelper.Callback() {

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        var dragFlags: Int = 0
        var swipeFlags: Int = 0
        if (viewHolder::class.java == DataAdapter.AppleViewHolder::class.java) {

            dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            swipeFlags = ItemTouchHelper.END
        }
        if (viewHolder::class.java == DataAdapter.BasketViewHolder::class.java) {

            dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            swipeFlags = ItemTouchHelper.START
        }
        return makeMovementFlags(dragFlags, swipeFlags)

    }

    override fun onMove(
        recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        Log.i("AppleTouchHelper", "recycler = $recyclerView.")
        if (viewHolder::class.java == DataAdapter.AppleViewHolder::class.java) {
            mAdapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
        }

        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        mAdapter.onItemDismiss(viewHolder.adapterPosition)
    }

    override fun onSelectedChanged(
        viewHolder: RecyclerView.ViewHolder?,
        actionState: Int
    ) {
        // We only want the active item
        if (actionState != ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (viewHolder is ItemTouchHelperViewHolder) {
                val itemViewHolder = viewHolder as ItemTouchHelperViewHolder
                itemViewHolder.onItemSelected()
            }
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        if (viewHolder is ItemTouchHelperViewHolder) {
            val itemViewHolder = viewHolder as ItemTouchHelperViewHolder
            itemViewHolder.onItemClear()
        }
    }


}
