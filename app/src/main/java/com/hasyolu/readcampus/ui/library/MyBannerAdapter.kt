package com.hasyolu.readcampus.ui.library

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.hasyolu.readcampus.databinding.ItemBannerBinding
import com.hasyolu.readcampus.model.BannerBean
import com.youth.banner.adapter.BannerAdapter

class MyBannerAdapter(private val bannerBeanList: List<BannerBean>?, private val onClick : (type: Int) -> Unit)
    :BannerAdapter<BannerBean?, MyBannerAdapter.ViewHolder?>(bannerBeanList) {

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return  ViewHolder(ItemBannerBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
    override fun onBindView(holder: ViewHolder?, data: BannerBean?, position: Int, size: Int) {
        val item = bannerBeanList?.get(position) ?: return
        holder?.textView?.text = item.text
        holder?.imageView?.load(item.imageUrl)
        holder?.root?.setOnClickListener {
            onClick(item.type)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return bannerBeanList?.get(position)?.type ?: super.getItemViewType(position)
    }

    inner class ViewHolder(binding: ItemBannerBinding) : RecyclerView.ViewHolder(binding.root) {
        val textView = binding.bannerText
        val imageView = binding.bannerImage
        val root = binding.root
    }
}