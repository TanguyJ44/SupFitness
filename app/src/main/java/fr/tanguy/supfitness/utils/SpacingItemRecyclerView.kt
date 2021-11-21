package fr.tanguy.supfitness.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView

class SpacingItemRecyclerView(private val verticalSpaceHeight: Int, private val horizontalSpaceHeight: Int) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.bottom = verticalSpaceHeight
        outRect.right = horizontalSpaceHeight
        outRect.left = horizontalSpaceHeight
    }
}