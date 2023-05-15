package com.hasyolu.readcampus.ui.me

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.github.testpress.mikephil.charting.charts.PieChart
import com.github.testpress.mikephil.charting.data.PieData
import com.github.testpress.mikephil.charting.data.PieDataSet
import com.github.testpress.mikephil.charting.data.PieEntry
import com.github.testpress.mikephil.charting.formatter.PercentFormatter
import com.github.testpress.mikephil.charting.utils.ColorTemplate
import com.hasyolu.readcampus.constant.Constant
import com.hasyolu.readcampus.databinding.FragmentMyBinding
import com.hasyolu.readcampus.ui.base.ButtomDialog
import com.hasyolu.readcampus.ui.main.MainActivity
import com.hasyolu.readcampus.utils.CameraUtil
import com.hasyolu.readcampus.utils.FileUtil
import com.hasyolu.readcampus.utils.SpUtil
import java.io.File


class MeFragment : Fragment() {

    private var _binding: FragmentMyBinding? = null
    private val binding get() = _binding!!

    private var pieChart : PieChart?= null
    private var chartContainer: FrameLayout?= null

    private lateinit var imageView: ImageView

    private var dialog: ButtomDialog? = null

    /**
     * startActivityForResult()方法废弃,google更加建议使用Activity Result API来实现在两个Activity之间交换数据的功能。
     * 内置Contract:更简单实现权限申请,拍照，打开文件等
     * 参考：https://blog.csdn.net/guolin_blog/article/details/121063078?spm=1001.2014.3001.5501
     */
    private val takePictureRequestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            takePictureLauncher.launch(null)
        } else {
            Toast.makeText(requireContext(), "获取权限失败，请打开相关权限", Toast.LENGTH_LONG).show()
        }
    }

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        bitmap ?: return@registerForActivityResult
        updateImage(bitmap)
    }

    private fun updateImage(bitmap: Bitmap) {
        SpUtil.setStringValue("me_image", CameraUtil.base64Encode(bitmap))
        Glide.with(requireActivity()).load(bitmap).into(imageView)
    }

    private val choosePhotoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
        if(result.resultCode == Activity.RESULT_OK){
            val bitmap = result.data?.let { CameraUtil.getImageBitMapApi29Down(it,requireContext()) }
            bitmap ?: return@registerForActivityResult
            updateImage(bitmap)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyBinding.inflate(inflater, container, false)
        val root: View = binding.root
        chartContainer = binding.meChartContainer
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
        initData()
    }

    private fun initView() {
        imageView = binding.headImageView
        initChartView()
    }


    private fun initListener() {
        binding.switchVolume.setOnCheckedChangeListener { _, isChecked ->
            SpUtil.setBooleanValue("volume_turn_page", isChecked)
        }
        binding.clearCache.setOnClickListener { v ->
            AlertDialog.Builder(activity)
                .setMessage("确定要清除缓存么(将会删除所有已缓存章节)？").setNegativeButton("取消", null)
                .setPositiveButton("确定") { _, _ ->
                    FileUtil.deleteFile(Constant.BOOK_CACHE_PATH)
                    binding.tvCache.text = "0kb"
                }.show()
        }

        imageView.setOnClickListener {
            val builder: ButtomDialog.Builder = ButtomDialog.Builder(context)
            //添加条目，可多个
            builder.addMenu("相机") {
                dialog?.cancel()
                takePhotos()
            }.addMenu("相册") {
                dialog?.cancel()
                choosePhoto()
            }
            builder.setTitle("请选择方式") //添加标题
            builder.setCanCancel(true) //点击阴影时是否取消dialog，true为取消
            builder.setShadow(true) //是否设置阴影背景，true为有阴影
            builder.setCancelText("取消") //设置最下面取消的文本内容

            builder.setCancelListener {
                dialog!!.cancel()
                Toast.makeText(context, "取消", Toast.LENGTH_SHORT).show()
            }
            dialog = builder.create()
            dialog?.show()
        }
        binding.quit.setOnClickListener {
            (activity as? MainActivity)?.goToLoginActivity()
        }
    }

    private fun choosePhoto() {
        choosePhotoLauncher.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
    }
    private fun takePhotos() {
        takePictureRequestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
    }

    private fun initData() {

        SpUtil.getStringValue("me_image")?.let {
            Glide.with(requireActivity()).load(CameraUtil.base64Decode(it)).into(imageView)
        }


        initChartData()

        // 是否音量键控制翻页
        binding.switchVolume.isChecked = SpUtil.getBooleanValue("volume_turn_page", true)
        // 获取缓存文件大小
        val cacheSize = FileUtil.getDirSize(File(Constant.BOOK_CACHE_PATH)) / 1024
        //初始化缓存文件大小单位
        val unit: String = if (cacheSize in (0..1024)) {
            "kb"
        } else {
            "MB"
        }
        //附值
        binding.tvCache.text = "$cacheSize$unit"
    }

    private fun initChartView() {
        pieChart = PieChart(context)
        pieChart?.apply {
            val lp = FrameLayout.LayoutParams(1000, 1000)
            layoutParams = lp
        }
        chartContainer?.addView(pieChart)
    }
    private fun initChartData() {
        val values = arrayListOf<PieEntry>()
        values.add(PieEntry(40f, "历史军事"))
        values.add(PieEntry(20f, "文学经典"))
        values.add(PieEntry(30f, "科幻同人"))
        values.add(PieEntry(10f, "图书杂志"))

        //数据和颜色
        val dataColors = arrayListOf<Int>()
        for (c in ColorTemplate.VORDIPLOM_COLORS) dataColors.add(c)
        for (c in ColorTemplate.JOYFUL_COLORS) dataColors.add(c)
        for (c in ColorTemplate.COLORFUL_COLORS) dataColors.add(c)
        for (c in ColorTemplate.LIBERTY_COLORS) dataColors.add(c)
        for (c in ColorTemplate.PASTEL_COLORS) dataColors.add(c)
        dataColors.add(ColorTemplate.getHoloBlue())
        val mPieDataSet = PieDataSet(values, "Label")
        mPieDataSet.apply {
            valueFormatter = PercentFormatter()
            colors = dataColors
            valueTextColor = Color.BLACK // 设置百分比字体颜色
            valueTextSize = 20f // 设置百分比字体大小
        }
        val mPieData = PieData(mPieDataSet)
        pieChart?.setEntryLabelColor(Color.BLACK) // 设置图表扇形文字颜色
        pieChart?.data = mPieData
    }
}