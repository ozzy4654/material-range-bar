/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.appyvet.rangebarsample.colorpicker

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import com.appyvet.rangebarsample.Component
import com.appyvet.rangebarsample.R
import com.appyvet.rangebarsample.colorpicker.ColorPickerSwatch.OnSwatchColorSelectedListener

/**
 * A dialog which takes in as input an array of colors and creates a palette allowing the user to
 * select a specific color swatch, which invokes a listener.
 */
open class ColorPickerDialog : androidx.fragment.app.DialogFragment(), OnSwatchColorSelectedListener {
    /**
     * Interface for a callback when a color square is selected.
     */
    interface OnColorSelectedListener {
        /**
         * Called when a specific color square has been selected.
         */
        fun onColorSelected(color: Int, component: Component?)
    }

    protected var mAlertDialog: AlertDialog? = null
    protected var mTitleResId = R.string.color_picker_default_title
    var colors: IntArray? = null
        private set
    protected var mSelectedColor = 0
    protected var mColumns = 0
    protected var mSize = 0
    private var mComponent: Component? = null
    private var mPalette: ColorPickerPalette? = null
    private var mProgress: ProgressBar? = null
    protected var mListener: OnColorSelectedListener? = null
    fun initialize(titleResId: Int, colors: IntArray?, selectedColor: Int, columns: Int, size: Int, component: Component?) {
        setArguments(titleResId, columns, size)
        setColors(colors, selectedColor)
        mComponent = component
    }

    fun setArguments(titleResId: Int, columns: Int, size: Int) {
        val bundle = Bundle()
        bundle.putInt(KEY_TITLE_ID, titleResId)
        bundle.putInt(KEY_COLUMNS, columns)
        bundle.putInt(KEY_SIZE, size)
        arguments = bundle
    }

    fun setOnColorSelectedListener(listener: OnColorSelectedListener?) {
        mListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mTitleResId = arguments!!.getInt(KEY_TITLE_ID)
            mColumns = arguments!!.getInt(KEY_COLUMNS)
            mSize = arguments!!.getInt(KEY_SIZE)
        }
        if (savedInstanceState != null) {
            colors = savedInstanceState.getIntArray(KEY_COLORS)
            mSelectedColor = (savedInstanceState.getSerializable(KEY_SELECTED_COLOR) as Int?)!!
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val activity = activity
        val view = LayoutInflater.from(getActivity()).inflate(R.layout.color_picker_dialog, null)
        mProgress = view.findViewById<View>(android.R.id.progress) as ProgressBar
        mPalette = view.findViewById<View>(R.id.color_picker) as ColorPickerPalette
        mPalette!!.init(mSize, mColumns, this)
        if (colors != null) {
            showPaletteView()
        }
        mAlertDialog = AlertDialog.Builder(activity)
                .setTitle(mTitleResId)
                .setView(view)
                .create()
        return mAlertDialog!!
    }

    override fun onSwatchColorSelected(color: Int) {
        if (mListener != null) {
            mListener!!.onColorSelected(color, mComponent)
        }
        if (targetFragment is OnSwatchColorSelectedListener) {
            val listener = targetFragment as OnColorSelectedListener
            listener.onColorSelected(color, mComponent)
        }
        if (color != mSelectedColor) {
            mSelectedColor = color
            // Redraw palette to show checkmark on newly selected color before dismissing.
            mPalette!!.drawPalette(colors, mSelectedColor)
        }
        dismiss()
    }

    fun showPaletteView() {
        if (mProgress != null && mPalette != null) {
            mProgress!!.visibility = View.GONE
            refreshPalette()
            mPalette!!.visibility = View.VISIBLE
        }
    }

    fun showProgressBarView() {
        if (mProgress != null && mPalette != null) {
            mProgress!!.visibility = View.VISIBLE
            mPalette!!.visibility = View.GONE
        }
    }

    fun setColors(colors: IntArray?, selectedColor: Int) {
        if (this.colors != colors || mSelectedColor != selectedColor) {
            this.colors = colors
            mSelectedColor = selectedColor
            refreshPalette()
        }
    }

    fun setColors(colors: IntArray) {
        if (this.colors != colors) {
            this.colors = colors
            refreshPalette()
        }
    }

    private fun refreshPalette() {
        if (mPalette != null && colors != null) {
            mPalette!!.drawPalette(colors, mSelectedColor)
        }
    }

    var selectedColor: Int
        get() = mSelectedColor
        set(color) {
            if (mSelectedColor != color) {
                mSelectedColor = color
                refreshPalette()
            }
        }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntArray(KEY_COLORS, colors)
        outState.putSerializable(KEY_SELECTED_COLOR, mSelectedColor)
    }

    companion object {
        const val SIZE_LARGE = 1
        const val SIZE_SMALL = 2
        protected const val KEY_TITLE_ID = "title_id"
        protected const val KEY_COLORS = "colors"
        protected const val KEY_SELECTED_COLOR = "selected_color"
        protected const val KEY_COLUMNS = "columns"
        protected const val KEY_SIZE = "size"
        fun newInstance(titleResId: Int, colors: IntArray?, selectedColor: Int,
                        columns: Int, size: Int, component: Component?): ColorPickerDialog {
            val ret = ColorPickerDialog()
            ret.initialize(titleResId, colors, selectedColor, columns, size, component)
            return ret
        }
    }
}