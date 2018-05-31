package me.sunnydaydev.curencyconverter.coreui.binding.observable

import android.databinding.ListChangeRegistry
import android.databinding.ObservableList

/**
 * Created by sunny on 31.05.2018.
 * mail: mail@sunnydaydev.me
 */

class SwapableObservableList<T>: ArrayList<T>(), ObservableList<T> {

    @Transient
    private var listeners: ListChangeRegistry = ListChangeRegistry()

    override fun addOnListChangedCallback(listener: ObservableList.OnListChangedCallback<out ObservableList<T>>) {
        listeners.add(listener)
    }

    override fun removeOnListChangedCallback(listener: ObservableList.OnListChangedCallback<out ObservableList<T>>) {
        listeners.remove(listener)
    }

    override fun add(element: T): Boolean {
        add(size -1, element, true)
        return true
    }

    override fun add(index: Int, element: T) = add(index, element, true)

    override fun addAll(elements: Collection<T>): Boolean {
        val oldSize = size
        val added = super.addAll(elements)
        if (added) {
            notifyAdd(oldSize, size - oldSize)
        }
        return added
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        val added = super.addAll(index, elements)
        if (added) {
            notifyAdd(index, elements.size)
        }
        return added
    }

    override fun clear() {
        val oldSize = size
        super.clear()
        if (oldSize != 0) {
            notifyRemove(0, oldSize)
        }
    }

    override fun remove(element: T): Boolean {
        val index = indexOf(element)
        return if (index >= 0) {
            removeAt(index)
            true
        } else {
            false
        }
    }

    override fun removeAt(index: Int): T = removeAt(index, true)

    override fun set(index: Int, element: T): T = set(index, element, true)

    override fun removeRange(fromIndex: Int, toIndex: Int) {
        super.removeRange(fromIndex, toIndex)
        notifyRemove(fromIndex, toIndex - fromIndex)
    }

    fun move(fromIndex: Int, toIndex: Int) {

        if (fromIndex == toIndex) return

        add(toIndex, removeAt(fromIndex, false), false)
        notifyMoved(fromIndex, toIndex, 1)

    }

    fun swap(fromIndex: Int, toIndex: Int) {

        add(toIndex, removeAt(fromIndex, false), false)
        notifyMoved(fromIndex, toIndex, 1)
        add(fromIndex, removeAt(toIndex + 1, false), false)
        notifyMoved(toIndex + 1, fromIndex, 1)

    }

    fun add(index: Int, element: T, notify: Boolean) {

        super.add(index, element)
        if (notify) {
            notifyAdd(index, 1)
        }

    }

    private fun set(index: Int, element: T, notify: Boolean): T {

        val prev = super.set(index, element)
        if (notify) {
            listeners.notifyChanged(this, index, 1)
        }
        return prev

    }

    private fun removeAt(index: Int, notify: Boolean): T {

        val element = super.removeAt(index)
        if (notify) {
            notifyRemove(index, 1)
        }
        return element

    }

    private fun notifyAdd(start: Int, count: Int) {
        listeners.notifyInserted(this, start, count)
    }

    private fun notifyMoved(from: Int, to: Int, count: Int) {
        listeners.notifyMoved(this, from, to, count)
    }

    private fun notifyRemove(start: Int, count: Int) {
        listeners.notifyRemoved(this, start, count)
    }

}