package com.hasyolu.readcampus.ui.library

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.hasyolu.readcampus.R
import com.hasyolu.readcampus.databinding.FragmentLibraryBinding
import com.hasyolu.readcampus.model.BookBean
import com.hasyolu.readcampus.ui.main.MainActivity
import com.hasyolu.readcampus.ui.novelRead.NovelReadActivity
import com.hasyolu.readcampus.ui.square.RVOScrollListener
import com.hasyolu.readcampus.ui.square.SquareAdapter
import com.hasyolu.readcampus.ui.square.SquareFragment
import com.youth.banner.indicator.CircleIndicator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class LibraryFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: LibraryViewModel.AssistedFactory

    @VisibleForTesting
    val libraryViewModel: LibraryViewModel by viewModels {
        LibraryViewModel.provideFactory(viewModelFactory)
    }

    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!

    private lateinit var topIcBookCity: AppCompatTextView
    private lateinit var topSearch: RelativeLayout

    private var bannerAdapter: MyBannerAdapter? = null

    private lateinit var recommend0: ImageView
    private lateinit var recommend1: ImageView
    private lateinit var recommend2: ImageView
    private lateinit var recommend3: ImageView
    private lateinit var recommend4: ImageView
    private lateinit var recommend5: ImageView

    private lateinit var moreRecyclerView: RecyclerView
    //书籍列表适配器
    private lateinit var squareAdapter: SquareAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initListener()
        initData()
    }

    private fun initView() {
        topIcBookCity = binding.libaryTopbar.topbarIcBookcity
        topSearch = binding.libaryTopbar.topbarSearch

        recommend0 = binding.libabryRecommend.recommend0
        recommend1 = binding.libabryRecommend.recommend1
        recommend2 = binding.libabryRecommend.recommend2
        recommend3 = binding.libabryRecommend.recommend3
        recommend4 = binding.libabryRecommend.recommend4
        recommend5 = binding.libabryRecommend.recommend5

        moreRecyclerView = binding.libabryMore.moreRecyclerView

        initRecyclerView()
        initBanner()
    }

    @SuppressLint("CheckResult")
    private fun initListener() {
        topIcBookCity.setOnClickListener {
            findNavController().navigate(R.id.action_libraryFragment_to_squareFragment)
        }
        topSearch.setOnClickListener {
            MaterialDialog(requireContext()).show {
                title(text = "搜索小说")
                input(
                    hint = "输入 书名、作者",
                    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
                ) { _, text ->
                    val keyword = text.toString()
                    if (keyword.isNotBlank()) {
                        searchData(keyWord = keyword)
                    } else {
                        libraryViewModel.toastMsg("关键字不能为空")
                    }
                }
                positiveButton(text = "搜索")
            }
        }

        // 点击项目事件
        squareAdapter.itemClickListener = object : SquareAdapter.OnBookItemClickListener {
            override fun openItem(t: BookBean) {
                libraryViewModel.fetchBookInfo(t.url)
            }
        }

        //添加列表滚动事件
        moreRecyclerView.addOnScrollListener(object :
            RVOScrollListener(moreRecyclerView.layoutManager as LinearLayoutManager) {
            override fun loadMoreItems() {
                libraryViewModel.fetchSearch()
            }

            override fun totalPageCount(): Int {
                return libraryViewModel.getTotalPage()
            }

            override fun isLastPage(): Boolean {
                return libraryViewModel.isLastPage()
            }

            override fun isLoading(): Boolean {
                return libraryViewModel.isLoading()
            }
        })
        binding.libabryMore.moreText.setOnClickListener {
            libraryViewModel.fetchSearch(1)
        }
        binding.libabryRecommend.recommendChange.setOnClickListener {
            val typeName = libraryViewModel.getTypes()[3]
            libraryViewModel.fetchRecommendDate(typeName)
        }

    }

    private fun initData() {

        val parseName = libraryViewModel.getParses()[0]
        libraryViewModel.fetchShopName(parseName)

        val typeName = libraryViewModel.getTypes()[10]
        val recommendTypeName = libraryViewModel.getTypes()[3]
        searchData(typeName = typeName)
        libraryViewModel.fetchRecommendDate(recommendTypeName)

        //错误通知事件
        libraryViewModel.toast.observe(viewLifecycleOwner) {
            if (!it.isNullOrBlank()) {
                Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
            }
        }
        //根据类型搜索
        libraryViewModel.searchType.observe(viewLifecycleOwner) {
            fetchPage(it.currentPage, it.totalPage, it.bookBeans)
        }
        //根据关键字搜索
        libraryViewModel.searchKeyWord.observe(viewLifecycleOwner) {
            fetchPage(it.currentPage, it.totalPage, it.bookBeans)
        }
        libraryViewModel.recommendLiveDate.observe(viewLifecycleOwner) {
            fetchRecommend(it.bookBeans)
        }
        //新增书籍
        libraryViewModel.bookInserted.observe(viewLifecycleOwner) {
            libraryViewModel.toastMsg("加入书架成功")
        }
        //更新书籍信息
        libraryViewModel.bookInfo.observe(viewLifecycleOwner) {
            if (it?.bookBean != null) {
                val bookBean = it.bookBean
                libraryViewModel.fetchBook(bookBean)
                //小说详情
                MaterialDialog(requireContext()).show {
                    title(text = bookBean.name)
                    message(text = "作者：${bookBean.author}\n类别：${bookBean.category}\n状态：${bookBean.status}\n简介：${bookBean.desc}")
                    positiveButton(text = "开始阅读") {
                        val book = libraryViewModel.getBookBean()
                        if (book != null) {
                            // 打开 书籍
                            NovelReadActivity.startFromActivity(requireActivity(), book)
                        } else {
                            libraryViewModel.toastMsg("打开书籍异常，请重新获取书籍")
                        }
                    }
                    negativeButton(text = "加入书架") {
                        //加入书架
                        libraryViewModel.insertBook()
                    }
                    lifecycleOwner(requireActivity())
                }
            }
        }
    }

    private fun fetchRecommend(bookBeans: ArrayList<BookBean>) {
        if(bookBeans.size < 6) return
        recommend0.load(bookBeans[0].cover)
        recommend1.load(bookBeans[1].cover)
        recommend2.load(bookBeans[2].cover)
        recommend3.load(bookBeans[3].cover)
        recommend4.load(bookBeans[4].cover)
        recommend5.load(bookBeans[5].cover)
    }

    private fun initBanner() {
        val banner = binding.topbarBanner

        bannerAdapter = MyBannerAdapter(emptyList()){
            when(it) {
                0 -> {

                }
                1 -> {

                }
                2 -> {

                }
            }
        }

        banner.addBannerLifecycleObserver(this) //添加生命周期观察者
            .setAdapter(bannerAdapter).indicator = CircleIndicator(requireContext())
    }

    private fun initRecyclerView() {
        squareAdapter = SquareAdapter()
        moreRecyclerView.apply {
            adapter = squareAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun fetchPage(currentPage:Int,totalPage:Int,bookList:List<BookBean>) {
        if (bookList.isNotEmpty()) {
            if (currentPage == 1) {
                squareAdapter.refreshItems(bookList)
            } else {
                squareAdapter.addItems(bookList)
            }
        }
        libraryViewModel.fetchPage(currentPage + 1, totalPage)
    }

    /**
     * 加载数据
     */
    private fun searchData(
        keyWord: String = "",
        typeName: String = ""
    ) {
        if (keyWord.isBlank()) {
            // 根据类型搜索
            libraryViewModel.fetchSearchType(typeName, 1)
        } else {
            //根据关键字搜索
            libraryViewModel.fetchSearchKeyWord(keyWord, 1)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}