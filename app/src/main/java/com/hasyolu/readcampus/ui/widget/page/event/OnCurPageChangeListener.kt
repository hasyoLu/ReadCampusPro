package com.hasyolu.readcampus.ui.widget.page.event

interface OnCurPageChangeListener {
    fun hasPrev(): Boolean
    fun hasNext(): Boolean
    fun pageCancel()
}