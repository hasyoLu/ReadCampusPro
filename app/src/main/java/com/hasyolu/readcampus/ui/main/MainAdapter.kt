package com.hasyolu.readcampus.ui.main

import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hasyolu.readcampus.ui.library.LibraryFragment
import com.hasyolu.readcampus.ui.me.MeFragment
import com.hasyolu.readcampus.ui.shelf.ShelfFragment
import com.hasyolu.readcampus.ui.square.SquareFragment


class MainAdapter(fragmentActivity: FragmentActivity)
    : FragmentStateAdapter(fragmentActivity) {
    // 基础窗口
    private val fragments: SparseArray<Fragment> = SparseArray(4)

    // 初始化
    init {
        fragments.append(0, LibraryFragment())
        fragments.append(1, SquareFragment())
        fragments.append(2, ShelfFragment())
        fragments.append(3, MeFragment())
    }

    // 重新获取项目序号
    override fun getItemCount(): Int {
        return fragments.size()
    }

    //创建fragment
    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}