package com.example.mybaselibrary.mvvm.ui

import android.graphics.Color
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.mybaselibrary.R
import com.example.mybaselibrary.adapter.MagicIndicatorAdapter
import com.example.mybaselibrary.databinding.FragmentBaseViewPagerBinding
import com.example.mybaselibrary.extentions.dp2px
import com.example.mybaselibrary.extentions.no
import com.example.mybaselibrary.mvvm.viewmodel.DataBindingConfig
import com.example.mybaselibrary.net.resource.Resource
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

/**
 *Create by lvhaoran
 *on 2019/9/23
 */
abstract class BaseViewPagerFragment : LibraryFragment<Any>() {

    protected val fragmentList = mutableListOf<Fragment>()
    protected val mPagerBinding by lazy { getViewBinding() as FragmentBaseViewPagerBinding }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_base_view_pager)
    }

    override fun initializeUI() {
        super.initializeUI()
        createIndicatorTopView()?.let {
            mPagerBinding.indicatorTopContainer.visibility = View.VISIBLE
            mPagerBinding.indicatorTopContainer.addView(it)
        }
        createIndicatorBottomView()?.let {
            mPagerBinding.indicatorBottomContainer.visibility = View.VISIBLE
            mPagerBinding.indicatorBottomContainer.addView(it)
        }
        needTabIndicator().no {
            mPagerBinding.tab.visibility = View.GONE
        }
        mPagerBinding.tab.setBackgroundColor(getTabBackgroundColor())
        mPagerBinding.tab.elevation = getElevation()
        fragmentList.clear()
        fragmentList.addAll(getFragments())
        initPager(mPagerBinding.pager)
    }

    abstract fun getTabTitleList(): List<String>

    abstract fun getFragments(): List<Fragment>

    /**
     * recyclerview adapter 或者 fragmentStateAdapter
     */
    fun bindAdapter(): PagerAdapter {
        return object :
            FragmentStatePagerAdapter(childFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getItem(position: Int): Fragment {
                return fragmentList[position]
            }

            override fun getCount(): Int {
                return fragmentList.size
            }

        }
    }

    open fun initPager(pager: ViewPager) {
        val cachePageSize = cachePageSize()
        if (cachePageSize > 0) {
            pager.offscreenPageLimit = cachePageSize
        }
        pager.adapter = bindAdapter()
        mPagerBinding.tab.navigator = CommonNavigator(context!!)
            .apply {
                isAdjustMode = getTabMode()
                adapter = MagicIndicatorAdapter(getTabTitleList(), pager)
                    .apply {
                        setNormalColor(getTabUnSelectColor())
                        setSelectedColor(getTabSelectColor())
                    }
            }
        pager.addOnPageChangeListener(pagerCallBack)
    }

    private val pagerCallBack = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            this@BaseViewPagerFragment.onPageScrolled(
                position,
                positionOffset,
                positionOffsetPixels
            )
        }

        override fun onPageSelected(position: Int) {
            this@BaseViewPagerFragment.onPageSelected(position)
        }

        override fun onPageScrollStateChanged(state: Int) {
            this@BaseViewPagerFragment.onPageScrollStateChanged(state)
        }
    }

    open fun onPageScrolled(
        position: Int,
        positionOffset: Float,
        positionOffsetPixels: Int
    ) {
        mPagerBinding.tab.onPageScrolled(position, positionOffset, positionOffsetPixels)
    }

    open fun onPageSelected(position: Int) {
        mPagerBinding.tab.onPageSelected(position)
    }

    open fun onPageScrollStateChanged(state: Int) {
        mPagerBinding.tab.onPageScrollStateChanged(state)
    }

    open fun getElevation(): Float {
        return 3.dp2px().toFloat()
    }

    open fun getTabMode(): Boolean {
        return true
    }

    open fun getTabBackgroundColor(): Int {
        return Color.WHITE
    }

    open fun getTabUnSelectColor(): Int {
        return Color.parseColor("#A0A0A0")
    }

    open fun getTabSelectColor(): Int {
        return Color.BLACK
    }

    open fun getTabIndicatorColor(): Int {
        return Color.BLACK
    }

    open fun getTabIndicatorHeight(): Int {
        return 2.dp2px()
    }

    open fun cachePageSize(): Int = 0

    open fun needTabIndicator(): Boolean = true

    open fun createIndicatorBottomView(): View? = null

    open fun createIndicatorTopView(): View? = null

    override fun observableUI(observer: Observer<Resource<Any>>) {
    }

    override fun subscribeUI(data: Any?) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mPagerBinding.pager.removeOnPageChangeListener(pagerCallBack)
    }
}