package com.cnting.apt_api;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by cnting on 2023/1/6
 */
public class MyViewCreatorDelegate implements IMyViewCreator {
    private IMyViewCreator myViewCreator;

    private MyViewCreatorDelegate() {
        try {
            Class clazz = Class.forName("com.cnting.gradledemo.MyViewCreatorImpl");
            myViewCreator = (IMyViewCreator) clazz.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public static MyViewCreatorDelegate getInstance() {
        return Holder.instance;
    }

    private static final class Holder {
        private static final MyViewCreatorDelegate instance = new MyViewCreatorDelegate();
    }

    @Override
    public View createView(String name, Context context, AttributeSet attributes) {
        if (myViewCreator != null) {
            return myViewCreator.createView(name, context, attributes);
        }
        return null;
    }
}
