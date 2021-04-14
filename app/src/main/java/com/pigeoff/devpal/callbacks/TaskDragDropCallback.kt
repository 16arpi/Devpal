package com.pigeoff.devpal.callbacks

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.pigeoff.devpal.adapters.IssueAdapter
import com.pigeoff.devpal.util.UtilMain

class TaskDragDropCallback(adapter: IssueAdapter) : ItemTouchHelper.Callback() {

    val adapter = adapter

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipFlags = 0
        return makeMovementFlags(dragFlags, swipFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        adapter.updateItemsRange(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onMoved(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        fromPos: Int,
        target: RecyclerView.ViewHolder,
        toPos: Int,
        x: Int,
        y: Int
    ) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y)
        viewHolder as IssueAdapter.ViewHolder
        target as IssueAdapter.ViewHolder

        UtilMain().setMetroLine(viewHolder.imgDot, viewHolder.lineTop, viewHolder.lineBottom, adapter.itemCount, viewHolder.adapterPosition, adapter.getCurrentIssues().get(viewHolder.adapterPosition).type)
        UtilMain().setMetroLine(target.imgDot, target.lineTop, target.lineBottom, adapter.itemCount, target.adapterPosition, adapter.getCurrentIssues().get(target.adapterPosition).type)


    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }
}