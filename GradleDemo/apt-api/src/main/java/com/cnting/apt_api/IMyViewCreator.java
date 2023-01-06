package com.cnting.apt_api;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by cnting on 2023/1/6
 */
public interface IMyViewCreator {

    View createView(String name, Context context, AttributeSet attributes);
}
