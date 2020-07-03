package com.example.mybaselibrary.adapter

import android.content.Context
import android.graphics.Color
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.mybaselibrary.extentions._onClickWithoutFast
import com.example.mybaselibrary.extentions.dp2px
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView

/**
 *Create by lvhaoran
 *on 2019/8/7
 */
class MagicIndicatorAdapter(
    private val titles: List<String>,
    private val pager: Any
) : CommonNavigatorAdapter() {

    private var normalColor: Int = 0
    private var selectedColor: Int = 0
    private var colorList: IntArray? = null
    var indicatorListener: IndicatorListener? = null


    override fun getTitleView(context: Context, index: Int): IPagerTitleView {
        return ColorTransitionPagerTitleView(context)
            .apply {
                normalColor = if (normalColor == 0) Color.GRAY else normalColor
                selectedColor = if (selectedColor == 0) Color.BLACK else selectedColor
                text = titles[index]
                _onClickWithoutFast {
                    if (pager is ViewPager) {
                        pager.currentItem = index
                    } else if (pager is ViewPager2) {
                        pager.currentItem = index
                    }
                    indicatorListener?.onSelect(index)
                }
            }
    }

    override fun getCount(): Int = titles.size

    override fun getIndicator(context: Context): IPagerIndicator {
        return LinePagerIndicator(context)
            .apply {
                mode = LinePagerIndicator.MODE_WRAP_CONTENT
                colorList?.let { setColors(*it) }
                    ?: setColors(Color.BLACK)
                lineHeight = 2.dp2px().toFloat()
            }
    }

    fun setSelectedColor(selectedColor: Int) {
        this.selectedColor = selectedColor
    }

    fun setNormalColor(normalColor: Int) {
        this.normalColor = normalColor
    }

    fun setColors(vararg colors: Int) {
        this.colorList = colors
    }

    interface IndicatorListener {
        fun onSelect(index: Int)
    }
}