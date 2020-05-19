package com.vladtruta.kitchenapp.utils

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.*
import androidx.core.content.ContextCompat


object UIUtils {
    fun getDimension(@DimenRes resId: Int): Int {
        return KitchenApp.instance.resources.getDimensionPixelSize(resId)
    }

    fun getDimensionFloat(@DimenRes resId: Int): Float {
        return KitchenApp.instance.resources.getDimension(resId)
    }

    fun getString(@StringRes resId: Int, vararg args: Any?): String {
        return KitchenApp.instance.resources.getString(resId, *args)
    }

    fun getDrawable(@DrawableRes resId: Int): Drawable? {
        return ContextCompat.getDrawable(KitchenApp.instance, resId)
    }

    @ColorInt
    fun getColor(@ColorRes resId: Int): Int {
        return ContextCompat.getColor(KitchenApp.instance, resId)
    }

    fun dpToPx(dp: Float): Int {
        return (dp * KitchenApp.instance.resources.displayMetrics.density).toInt()
    }

    fun showSoftKeyboardFor(view: View) {
        if (view.requestFocus()) {
            val imm =
                view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    fun hideKeyboardFrom(view: View) {
        val inputMethodManager =
            view.context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

        view.clearFocus()
    }
}