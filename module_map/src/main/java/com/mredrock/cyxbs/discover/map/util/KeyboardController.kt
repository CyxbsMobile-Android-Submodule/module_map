package com.mredrock.cyxbs.discover.map.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 *@author zhangzhe
 *@date 2020/8/18
 *@description
 */

object KeyboardController {
    /**
     * 隐藏键盘
     * 弹窗弹出的时候把键盘隐藏掉
     *
     * @param v 要获取焦点的view，一般填editText，隐藏时无所谓
     */
    fun hideInputKeyboard(context: Context, v: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

    /**
     * 弹起键盘
     */
    fun showInputKeyboard(context: Context, v: View?) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(v, 0)
    }
}
