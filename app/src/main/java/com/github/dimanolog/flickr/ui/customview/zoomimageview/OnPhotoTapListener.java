/*
 * Copyright (c) 2018 FORS Development Center
 * Trifonovskiy tup. 3, Moscow, 129272, Russian Federation
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * FORS Development Center ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with FORS.
 */

package com.github.dimanolog.flickr.ui.customview.zoomimageview;

import android.widget.ImageView;

/**
 * A callback to be invoked when the Photo is tapped with a single
 * tap.
 */
public interface OnPhotoTapListener {

    /**
     * A callback to receive where the user taps on a photo. You will only receive a callback if
     * the user taps on the actual photo, tapping on 'whitespace' will be ignored.
     *
     * @param view ImageView the user tapped.
     * @param x    where the user tapped from the of the Drawable, as percentage of the
     *             Drawable width.
     * @param y    where the user tapped from the top of the Drawable, as percentage of the
     *             Drawable height.
     */
    void onPhotoTap(ImageView view, float x, float y);
}
