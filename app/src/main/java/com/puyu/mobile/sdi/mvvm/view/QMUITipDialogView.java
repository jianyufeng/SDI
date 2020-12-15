/*
 * Tencent is pleased to support the open source community by making QMUI_Android available.
 *
 * Copyright (C) 2017-2018 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the MIT License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.puyu.mobile.sdi.mvvm.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;

import com.puyu.mobile.sdi.R;


public class QMUITipDialogView extends QMUILinearLayout {

    private final int mMaxWidth;
    private final int mMiniWidth;
    private final int mMiniHeight;

    public QMUITipDialogView(Context context) {
        super(context);
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        int radius = QMUIDisplayHelper.dp2px(context, 15);
        Drawable background = getResources()
                .getDrawable(R.color.qmui_config_color_75_pure_black, null);
        int paddingHor = QMUIDisplayHelper.dp2px(context, 16);
        int paddingVer = QMUIDisplayHelper.dp2px(context, 14);
        setBackground(background);
        setPadding(paddingHor, paddingVer, paddingHor, paddingVer);
        setRadius(radius);
        mMaxWidth = QMUIDisplayHelper.dp2px(context, 270);
        mMiniWidth = QMUIDisplayHelper.dp2px(context, 120);
        mMiniHeight = QMUIDisplayHelper.dp2px(context, 40);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        if(widthSize > mMaxWidth){
            widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(mMaxWidth, widthMode);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        boolean needRemeasure = false;
        if(getMeasuredWidth() < mMiniWidth){
            widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(mMiniWidth, View.MeasureSpec.EXACTLY);
            needRemeasure = true;
        }

        if(getMeasuredHeight() < mMiniHeight){
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(mMiniHeight, View.MeasureSpec.EXACTLY);
            needRemeasure = true;
        }

        if(needRemeasure){
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
