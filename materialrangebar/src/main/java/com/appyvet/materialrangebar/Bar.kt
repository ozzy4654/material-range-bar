/*
 * Copyright 2013, Edmodo, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this work except in compliance with the License.
 * You may obtain a copy of the License in the LICENSE file, or at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.appyvet.materialrangebar

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.TypedValue
import java.util.*

/**
 * This class represents the underlying gray bar in the RangeBar (without the
 * thumbs).
 */
class Bar(ctx: Context,
          x: Float,
          y: Float,
          length: Float,
          tickCount: Int,
          tickHeight: Float,
          barWeight: Float,
          barColor: Int,
          isBarRounded: Boolean) {
    // Member Variables ////////////////////////////////////////////////////////
    private val mRes: Resources
    private val mBarPaint: Paint
    private val mTickPaint: Paint
    private var mLabelPaint: Paint? = null

    /**
     * Get the x-coordinate of the left edge of the bar.
     *
     * @return x-coordinate of the left edge of the bar
     */
    // Left-coordinate of the horizontal bar.
    val leftX: Float

    /**
     * Get the x-coordinate of the right edge of the bar.
     *
     * @return x-coordinate of the right edge of the bar
     */
    val rightX: Float
    private val mY: Float
    private var mNumSegments: Int
    private var mTickDistance: Float
    private val mTickHeight: Float
    private var mTickLabelColor = 0
    private var mTickLabelSelectedColor = 0
    private var mTickTopLabels: Array<CharSequence>? = null
    private var mTickBottomLabels: Array<CharSequence>? = null
    private var mTickDefaultLabel: String? = null
    private var mTickLabelSize = 0f
    private var mTickDefaultColor = 0
    private var mTickColors: List<Int>? = ArrayList()

    /**
     * Bar constructor
     *
     * @param ctx              the context
     * @param x                the start x co-ordinate
     * @param y                the y co-ordinate
     * @param length           the length of the bar in px
     * @param tickCount        the number of ticks on the bar
     * @param tickHeight       the height of each tick
     * @param tickDefaultColor the color of all ticks
     * @param barWeight        the weight of the bar
     * @param barColor         the color of the bar
     * @param isBarRounded     if the bar has rounded edges or not
     */
    constructor(ctx: Context,
                x: Float,
                y: Float,
                length: Float,
                tickCount: Int,
                tickHeight: Float,
                tickDefaultColor: Int,
                barWeight: Float,
                barColor: Int,
                isBarRounded: Boolean) : this(ctx, x, y, length, tickCount, tickHeight, barWeight, barColor, isBarRounded) {
        mTickDefaultColor = tickDefaultColor
        mTickPaint.color = tickDefaultColor
    }

    /**
     * Bar constructor
     *
     * @param ctx              the context
     * @param x                the start x co-ordinate
     * @param y                the y co-ordinate
     * @param length           the length of the bar in px
     * @param tickCount        the number of ticks on the bar
     * @param tickHeight       the height of each tick
     * @param barWeight        the weight of the bar
     * @param barColor         the color of the bar
     * @param isBarRounded     if the bar has rounded edges or not
     * @param tickLabelColor   the color of each tick's label
     * @param tickTopLabels    the top label of each tick
     * @param tickBottomLabels the top label of each tick
     */
    constructor(ctx: Context,
                x: Float,
                y: Float,
                length: Float,
                tickCount: Int,
                tickHeight: Float,
                barWeight: Float,
                barColor: Int,
                isBarRounded: Boolean,
                tickLabelColor: Int,
                tickLabelSelectedColor: Int,
                tickTopLabels: Array<CharSequence>?,
                tickBottomLabels: Array<CharSequence>?,
                tickDefaultLabel: String?,
                tickLabelSize: Float) : this(ctx, x, y, length, tickCount, tickHeight, barWeight, barColor, isBarRounded) {
        if (tickTopLabels != null || tickBottomLabels != null) {
            // Creates the paint and sets the Paint values
            mLabelPaint = Paint()
            mLabelPaint!!.color = tickLabelColor
            mLabelPaint!!.isAntiAlias = true
            mTickLabelColor = tickLabelColor
            mTickLabelSelectedColor = tickLabelSelectedColor
            mTickTopLabels = tickTopLabels
            mTickBottomLabels = tickBottomLabels
            mTickDefaultLabel = tickDefaultLabel
            mTickLabelSize = tickLabelSize
        }
    }

    /**
     * Bar constructor
     *
     * @param ctx              the context
     * @param x                the start x co-ordinate
     * @param y                the y co-ordinate
     * @param length           the length of the bar in px
     * @param tickCount        the number of ticks on the bar
     * @param tickHeight       the height of each tick
     * @param tickDefaultColor the default color of all ticks
     * @param barWeight        the weight of the bar
     * @param barColor         the color of the bar
     * @param isBarRounded     if the bar has rounded edges or not
     * @param tickLabelColor   the color of each tick's label
     * @param tickTopLabels    the top label of each tick
     * @param tickBottomLabels the top label of each tick
     */
    constructor(ctx: Context,
                x: Float,
                y: Float,
                length: Float,
                tickCount: Int,
                tickHeight: Float,
                tickDefaultColor: Int,
                barWeight: Float,
                barColor: Int,
                isBarRounded: Boolean,
                tickLabelColor: Int,
                tickLabelSelectedColor: Int,
                tickTopLabels: Array<CharSequence>?,
                tickBottomLabels: Array<CharSequence>?,
                tickDefaultLabel: String?,
                tickLabelSize: Float) : this(ctx, x, y, length, tickCount, tickHeight, barWeight, barColor, isBarRounded, tickLabelColor, tickLabelSelectedColor, tickTopLabels, tickBottomLabels, tickDefaultLabel, tickLabelSize) {
        mTickDefaultColor = tickDefaultColor
        mTickPaint.color = tickDefaultColor
    }

    /**
     * Bar constructor
     *
     * @param ctx              the context
     * @param x                the start x co-ordinate
     * @param y                the y co-ordinate
     * @param length           the length of the bar in px
     * @param tickCount        the number of ticks on the bar
     * @param tickHeight       the height of each tick
     * @param tickDefaultColor defualt tick color
     * @param tickColors       the colors of each tick
     * @param barWeight        the weight of the bar
     * @param barColor         the color of the bar
     * @param isBarRounded     if the bar has rounded edges or not
     * @param tickLabelColor   the color of each tick's label
     * @param tickTopLabels    the top label of each tick
     * @param tickBottomLabels the top label of each tick
     */
    constructor(ctx: Context,
                x: Float,
                y: Float,
                length: Float,
                tickCount: Int,
                tickHeight: Float,
                tickDefaultColor: Int,
                tickColors: List<Int>?,
                barWeight: Float,
                barColor: Int,
                isBarRounded: Boolean,
                tickLabelColor: Int,
                tickLabelSelectedColor: Int,
                tickTopLabels: Array<CharSequence>?,
                tickBottomLabels: Array<CharSequence>?,
                tickDefaultLabel: String?,
                tickLabelSize: Float) : this(ctx, x, y, length, tickCount, tickHeight, barWeight, barColor, isBarRounded, tickLabelColor, tickLabelSelectedColor, tickTopLabels, tickBottomLabels, tickDefaultLabel, tickLabelSize) {
        mTickDefaultColor = tickDefaultColor
        mTickColors = tickColors
    }
    // Package-Private Methods /////////////////////////////////////////////////
    /**
     * Draws the bar on the given Canvas.
     *
     * @param canvas Canvas to draw on; should be the Canvas passed into {#link
     * View#onDraw()}
     */
    fun draw(canvas: Canvas) {
        canvas.drawLine(leftX, mY, rightX, mY, mBarPaint)
    }

    /**
     * Gets the x-coordinate of the nearest tick to the given x-coordinate.
     *
     * @param thumb the thumb to find the nearest tick for
     * @return the x-coordinate of the nearest tick
     */
    fun getNearestTickCoordinate(thumb: PinView): Float {
        val nearestTickIndex = getNearestTickIndex(thumb)
        return leftX + nearestTickIndex * mTickDistance
    }

    /**
     * Gets the zero-based index of the nearest tick to the given thumb.
     *
     * @param thumb the Thumb to find the nearest tick for
     * @return the zero-based index of the nearest tick
     */
    fun getNearestTickIndex(thumb: PinView): Int {
        var tickIndex = ((thumb.x - leftX + mTickDistance / 2f) / mTickDistance).toInt()
        if (tickIndex > mNumSegments) {
            tickIndex = mNumSegments
        } else if (tickIndex < 0) {
            tickIndex = 0
        }
        return tickIndex
    }

    fun getTickX(tickIndex: Int): Float {
        return leftX + (rightX - leftX) / mNumSegments * tickIndex
    }

    /**
     * Set the number of ticks that will appear in the RangeBar.
     *
     * @param tickCount the number of ticks
     */
    fun setTickCount(tickCount: Int) {
        val barLength = rightX - leftX
        mNumSegments = tickCount - 1
        mTickDistance = barLength / mNumSegments
    }

    private fun getTickLabel(index: Int, labels: Array<CharSequence>?): String? {
        return if (index >= labels!!.size) {
            mTickDefaultLabel
        } else labels[index].toString()
    }

    private fun getTickTopLabel(index: Int): String? {
        return getTickLabel(index, mTickTopLabels)
    }

    private fun getTickBottomLabel(index: Int): String? {
        return getTickLabel(index, mTickBottomLabels)
    }
    // Private Methods /////////////////////////////////////////////////////////
    /**
     * Draws the tick marks on the bar.
     *
     * @param canvas Canvas to draw on; should be the Canvas passed into {#link
     * View#onDraw()}
     */
    @JvmOverloads
    fun drawTicks(canvas: Canvas, pinRadius: Float, rightThumb: PinView, leftThumb: PinView? = null) {
        var paintLabel = false
        if (mLabelPaint != null) {
            paintLabel = true
            val textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTickLabelSize,
                    mRes.displayMetrics).toInt()
            mLabelPaint!!.textSize = textSize.toFloat()
        }

        // Loop through and draw each tick (except final tick).
        for (i in 0 until mNumSegments) {
            val x = i * mTickDistance + leftX
            canvas.drawCircle(x, mY, mTickHeight, getTick(i))
            if (paintLabel) {
                if (mTickTopLabels != null) drawTickLabel(canvas, getTickTopLabel(i), x, pinRadius, i == 0, false, true, rightThumb, leftThumb)
                if (mTickBottomLabels != null) drawTickLabel(canvas, getTickBottomLabel(i), x, pinRadius, i == 0, false, false, rightThumb, leftThumb)
            }
        }
        // Draw final tick. We draw the final tick outside the loop to avoid any
        // rounding discrepancies.
        canvas.drawCircle(rightX, mY, mTickHeight, getTick(mNumSegments))

        // Draw final tick's label outside the loop
        if (paintLabel) {
            if (mTickTopLabels != null) drawTickLabel(canvas, getTickTopLabel(mNumSegments), rightX, pinRadius, false, true, true, rightThumb, leftThumb)
            if (mTickBottomLabels != null) drawTickLabel(canvas, getTickBottomLabel(mNumSegments), rightX, pinRadius, false, true, false, rightThumb, leftThumb)
        }
    }

    private fun drawTickLabel(canvas: Canvas, label: String?, x: Float, pinRadius: Float,
                              first: Boolean, last: Boolean, isTop: Boolean, rightThumb: PinView, leftThumb: PinView?) {
        val labelBounds = Rect()
        mLabelPaint!!.getTextBounds(label, 0, label!!.length, labelBounds)
        var xPos = x - labelBounds.width() / 2
        if (first) {
            xPos += mTickHeight
        } else if (last) {
            xPos -= mTickHeight
        }
        var isSelected = rightThumb.x == x
        if (!isSelected && leftThumb != null) {
            isSelected = leftThumb.x == x
        }
        if (isSelected) {
            mLabelPaint!!.color = mTickLabelSelectedColor
        } else {
            mLabelPaint!!.color = mTickLabelColor
        }
        val yPos: Float
        yPos = if (isTop) {
            mY - labelBounds.height() - pinRadius
        } else {
            mY + labelBounds.height() + pinRadius
        }
        canvas.drawText(label, xPos, yPos, mLabelPaint!!)
    }

    private fun getTick(index: Int): Paint {
        if (mTickColors != null && index < mTickColors!!.size) {
            mTickPaint.color = mTickColors!![index]
        } else {
            mTickPaint.color = mTickDefaultColor
        }
        return mTickPaint
    }
    // Constructor /////////////////////////////////////////////////////////////
    /**
     * Bar constructor
     *
     * @param ctx          the context
     * @param x            the start x co-ordinate
     * @param y            the y co-ordinate
     * @param length       the length of the bar in px
     * @param tickCount    the number of ticks on the bar
     * @param tickHeight   the height of each tick
     * @param barWeight    the weight of the bar
     * @param barColor     the color of the bar
     * @param isBarRounded if the bar has rounded edges or not
     */
    init {
        mRes = ctx.resources
        leftX = x
        rightX = x + length
        mY = y
        mNumSegments = tickCount - 1
        mTickDistance = length / mNumSegments
        mTickHeight = tickHeight
        // Initialize the paint.
        mBarPaint = Paint()
        mBarPaint.color = barColor
        mBarPaint.strokeWidth = barWeight
        mBarPaint.isAntiAlias = true
        if (isBarRounded) {
            mBarPaint.strokeCap = Paint.Cap.ROUND
        }
        mTickPaint = Paint()
        mTickPaint.strokeWidth = barWeight
        mTickPaint.isAntiAlias = true
    }
}