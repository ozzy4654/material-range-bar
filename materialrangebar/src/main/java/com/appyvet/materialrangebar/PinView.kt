/*
 * Copyright 2014, Appyvet, Inc.
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
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

/**
 * Represents a thumb in the RangeBar slider. This is the handle for the slider
 * that is pressed and slid.
 */
class PinView(context: Context?) : View(context) {
    // Radius (in pixels) of the touch area of the thumb.
    private var mTargetRadiusPx = 0f

    // Indicates whether this thumb is currently pressed and active.
    private var mIsPressed = false

    // The y-position of the thumb in the parent view. This should not change.
    private var mY = 0f

    // The current x-position of the thumb in the parent view.
    private var mX = 0f

    // mPaint to draw the thumbs if attributes are selected
    private var mTextPaint: Paint? = null
    private var mPin: Drawable? = null
    private var mValue: String? = null

    // Radius of the new thumb if selected
    private var mPinRadiusPx = 0
    private var mPinPadding = 0f
    private var mTextYPadding = 0f
    private val mBounds = Rect()
    private var mRes: Resources? = null
    private var mDensity = 0f
    private var mCirclePaint: Paint? = null
    var mCircleBoundaryPaint: Paint? = null
    private var mCircleRadiusPx = 0f
    private var mCircleBoundaryRadiusPx = 0f
    private var formatter: IRangeBarFormatter? = null
    private var mMinPinFont = RangeBar.DEFAULT_MIN_PIN_FONT_SP
    private var mMaxPinFont = RangeBar.DEFAULT_MAX_PIN_FONT_SP
    private var mPinsAreTemporary = false
    private var mHasBeenPressed = false
    private var pinColor = 0

    // Initialization //////////////////////////////////////////////////////////
    fun setFormatter(mFormatter: IRangeBarFormatter?) {
        formatter = mFormatter
    }

    /**
     * The view is created empty with a default constructor. Use init to set all the initial
     * variables for the pin
     *
     * @param ctx                 Context
     * @param y                   The y coordinate to raw the pin (i.e. the bar location)
     * @param pinRadiusDP         the initial size of the pin
     * @param pinColor            the color of the pin
     * @param textColor           the color of the value text in the pin
     * @param circleRadius        the radius of the selector circle
     * @param circleColor         the color of the selector circle
     * @param circleBoundaryColor The color of the selector circle boundary
     * @param circleBoundarySize  The size of the selector circle boundary line
     * @param minFont             the minimum font size for the pin text
     * @param maxFont             the maximum font size for the pin text
     * @param pinsAreTemporary    whether to show the pin initially or just the circle
     */
    fun init(ctx: Context, y: Float, pinRadiusDP: Float, pinColor: Int, textColor: Int,
             circleRadius: Float, circleColor: Int, circleBoundaryColor: Int, circleBoundarySize: Float, minFont: Float, maxFont: Float, pinsAreTemporary: Boolean) {
        mRes = ctx.resources
        mPin = ContextCompat.getDrawable(ctx, R.drawable.rotate)
        mDensity = resources.displayMetrics.density
        mMinPinFont = minFont / mDensity
        mMaxPinFont = maxFont / mDensity
        mPinsAreTemporary = pinsAreTemporary
        mPinPadding = (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15f, mRes!!.displayMetrics).toInt()).toFloat()
        mCircleRadiusPx = circleRadius
        mTextYPadding = (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3.5f, mRes!!.displayMetrics).toInt()).toFloat()
        // If one of the attributes are set, but the others aren't, set the
        // attributes to default
        mPinRadiusPx = if (pinRadiusDP == -1f) {
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_THUMB_RADIUS_DP,
                    mRes!!.displayMetrics).toInt()
        } else {
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pinRadiusDP,
                    mRes!!.displayMetrics).toInt()
        }
        //Set text size in px from dp
        val textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 15f,
                mRes!!.displayMetrics).toInt()

        // Creates the paint and sets the Paint values
        mTextPaint = Paint()
        mTextPaint!!.color = textColor
        mTextPaint!!.isAntiAlias = true
        mTextPaint!!.textSize = textSize.toFloat()
        // Creates the paint and sets the Paint values
        mCirclePaint = Paint()
        mCirclePaint!!.color = circleColor
        mCirclePaint!!.isAntiAlias = true
        if (circleBoundarySize != 0f) {
            mCircleBoundaryPaint = Paint()
            mCircleBoundaryPaint!!.style = Paint.Style.STROKE
            mCircleBoundaryPaint!!.color = circleBoundaryColor
            mCircleBoundaryPaint!!.strokeWidth = circleBoundarySize
            mCircleBoundaryPaint!!.isAntiAlias = true
            mCircleBoundaryRadiusPx = mCircleRadiusPx - mCircleBoundaryPaint!!.strokeWidth / 2
        }
        this.pinColor = pinColor

        // Sets the minimum touchable area, but allows it to expand based on
        // image size
        val targetRadius = Math.max(MINIMUM_TARGET_RADIUS_DP, mPinRadiusPx.toFloat()).toInt()
        mTargetRadiusPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, targetRadius.toFloat(),
                mRes!!.displayMetrics)
        mY = y
    }

    fun changeBoundaryTint(paintColor : Int) {
        mCircleBoundaryPaint?.color = paintColor
    }

    /**
     * Set the x value of the pin
     *
     * @param x set x value of the pin
     */
    override fun setX(x: Float) {
        mX = x
    }

    /**
     * Get the x value of the pin
     *
     * @return x float value of the pin
     */
    override fun getX(): Float {
        return mX
    }

    /**
     * Set the value of the pin
     *
     * @param x String value of the pin
     */
    fun setXValue(x: String?) {
        mValue = x
    }

    /**
     * Determine if the pin is pressed
     *
     * @return true if is in pressed state
     * false otherwise
     */
    override fun isPressed(): Boolean {
        return mIsPressed
    }

    /**
     * Sets the state of the pin to pressed
     */
    fun press() {
        mIsPressed = true
        mHasBeenPressed = true
    }

    /**
     * Set size of the pin and padding for use when animating pin enlargement on press
     *
     * @param size    the size of the pin radius
     * @param padding the size of the padding
     */
    fun setSize(size: Float, padding: Float) {
        mPinPadding = padding.toInt().toFloat()
        mPinRadiusPx = size.toInt()
        invalidate()
    }

    /**
     * Release the pin, sets pressed state to false
     */
    fun release() {
        mIsPressed = false
    }

    /**
     * Determines if the input coordinate is close enough to this thumb to
     * consider it a press.
     *
     * @param x the x-coordinate of the user touch
     * @param y the y-coordinate of the user touch
     * @return true if the coordinates are within this thumb's target area;
     * false otherwise
     */
    fun isInTargetZone(x: Float, y: Float): Boolean {
        return (Math.abs(x - mX) <= mTargetRadiusPx
                && Math.abs(y - mY + mPinPadding) <= mTargetRadiusPx)
    }

    //Draw the circle regardless of pressed state. If pin size is >0 then also draw the pin and text
    override fun draw(canvas: Canvas) {
        canvas.drawCircle(mX, mY, mCircleRadiusPx, mCirclePaint!!)

        //Draw the circle boundary only if mCircleBoundaryPaint was initialized
        if (mCircleBoundaryPaint != null) canvas.drawCircle(mX, mY, mCircleBoundaryRadiusPx, mCircleBoundaryPaint!!)

        //Draw pin if pressed
        if (mPinRadiusPx > 0 && (mHasBeenPressed || !mPinsAreTemporary)) {
            mBounds[mX.toInt() - mPinRadiusPx, mY.toInt() - mPinRadiusPx * 2 - mPinPadding.toInt(), mX.toInt() + mPinRadiusPx] = mY.toInt() - mPinPadding.toInt()
            mPin!!.bounds = mBounds
            var text = mValue
            if (formatter != null) {
                text = formatter!!.format(text)
            }
            calibrateTextSize(mTextPaint, text, mBounds.width().toFloat())
            mTextPaint!!.getTextBounds(text, 0, text!!.length, mBounds)
            mTextPaint!!.textAlign = Paint.Align.CENTER
            DrawableCompat.setTint(mPin!!, pinColor)
            mPin!!.draw(canvas)
            canvas.drawText(text,
                    mX, mY - mPinRadiusPx - mPinPadding + mTextYPadding,
                    mTextPaint!!)
        }
        super.draw(canvas)
    }

    // Private Methods /////////////////////////////////////////////////////////////////
    //Set text size based on available pin width.
    private fun calibrateTextSize(paint: Paint?, text: String?, boxWidth: Float) {
        paint!!.textSize = 10f
        val textSize = paint.measureText(text)
        var estimatedFontSize = boxWidth * 8 / textSize / mDensity
        if (estimatedFontSize < mMinPinFont) {
            estimatedFontSize = mMinPinFont
        } else if (estimatedFontSize > mMaxPinFont) {
            estimatedFontSize = mMaxPinFont
        }
        paint.textSize = estimatedFontSize * mDensity
    }

    companion object {
        // Private Constants ///////////////////////////////////////////////////////
        // The radius (in dp) of the touchable area around the thumb. We are basing
        // this value off of the recommended 48dp Rhythm. See:
        // http://developer.android.com/design/style/metrics-grids.html#48dp-rhythm
        private const val MINIMUM_TARGET_RADIUS_DP = 24f

        // Sets the default values for radius, normal, pressed if circle is to be
        // drawn but no value is given.
        private const val DEFAULT_THUMB_RADIUS_DP = 14f
    }
}