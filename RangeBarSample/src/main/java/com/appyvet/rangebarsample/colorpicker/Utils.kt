/*******************************************************************************
 * Copyright 2013 Gabriele Mariotti
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.appyvet.rangebarsample.colorpicker

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import com.appyvet.rangebarsample.R

/**
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
object Utils {
    fun isTablet(context: Context): Boolean {
        return ((context.resources.configuration.screenLayout
                and Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE)
    }

    /**
     * Utility class for colors
     *
     * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
     */
    object ColorUtils {
        /**
         * Create an array of int with colors
         *
         * @param context
         * @return
         */
        fun colorChoice(context: Context): IntArray? {
            var mColorChoices: IntArray? = null
            val color_array = context.resources.getStringArray(R.array.default_color_choice_values)
            if (color_array != null && color_array.size > 0) {
                mColorChoices = IntArray(color_array.size)
                for (i in color_array.indices) {
                    mColorChoices[i] = Color.parseColor(color_array[i])
                }
            }
            return mColorChoices
        }

        /**
         * Parse whiteColor
         *
         * @return
         */
        fun parseWhiteColor(): Int {
            return Color.parseColor("#FFFFFF")
        }
    }
}