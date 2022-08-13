/*
 * Tencent is pleased to support the open source community by making Tencent Shadow available.
 * Copyright (C) 2019 THL A29 Limited, a Tencent company.  All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *     https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.plugin.activity

import android.app.*
import android.app.assist.AssistContent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.content.res.Resources.Theme
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.os.PersistableBundle
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.view.ContextMenu.ContextMenuInfo
import android.view.accessibility.AccessibilityEvent
import androidx.annotation.RequiresApi
import com.example.plugin.PluginManager
import com.example.plugin.PluginRuntimeInfo
import java.util.function.Consumer

/**
 * 壳子Activity与插件Activity转调关系的实现类
 * 它是抽象的是因为它缺少必要的业务信息.业务必须继承这个类提供业务信息.
 *
 * @author cubershi
 */
class PluginActivityDelegate : HostActivityDelegate {

    private lateinit var mHostActivityDelegator: HostActivityDelegator
    private lateinit var pluginActivity: PluginActivity
    private var mPluginActivityCreated = false

    fun setDelegator(hostActivityDelegator: HostActivityDelegator) {
        mHostActivityDelegator = hostActivityDelegator
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val pluginInitBundle = savedInstanceState ?: mHostActivityDelegator.intent.extras!!
        val pluginName = pluginInitBundle.getString(PluginManager.KEY_PLUGIN)
        val pluginActivityClassName = pluginInitBundle.getString(PluginManager.KEY_COMPONENT)
        val pluginRuntimeInfo = PluginManager.pluginRuntimeInfo[pluginName]
            ?: throw NoClassDefFoundError("找不到插件:$pluginName")
        mHostActivityDelegator.intent.setExtrasClassLoader(pluginRuntimeInfo.classLoader)

        try {
            pluginActivity = (pluginRuntimeInfo.classLoader.loadClass(pluginActivityClassName)
                .newInstance() as PluginActivity)
            initPluginActivity(pluginActivity, pluginRuntimeInfo)
            mPluginActivityCreated = true
            pluginActivity.onCreate(savedInstanceState)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    private fun initPluginActivity(
        pluginActivity: PluginActivity,
        pluginRuntimeInfo: PluginRuntimeInfo
    ) {
        pluginActivity.setHostActivityDelegator(mHostActivityDelegator)
        pluginActivity.classLoader = pluginRuntimeInfo.classLoader
        pluginActivity.resources = pluginRuntimeInfo.resources
        pluginActivity.setHostContextAsBase(mHostActivityDelegator.hostActivity)
    }


    override fun isChangingConfigurations(): Boolean {
        return pluginActivity.isChangingConfigurations
    }

    override fun finish() {
        pluginActivity.finish()
    }

    override fun getClassLoader(): ClassLoader {
        return pluginActivity.classLoader
    }

    override fun getLayoutInflater(): LayoutInflater? {
        return pluginActivity.layoutInflater
    }

    override fun getResources(): Resources? {
        return if (mPluginActivityCreated) {
            pluginActivity.resources
        } else {
            Resources.getSystem()
        }
    }

    override fun recreate() {
        pluginActivity.recreate()
    }

    override fun getCallingActivity(): ComponentName? {
        return pluginActivity.callingActivity
    }

    override fun onCreate(arg0: Bundle?, arg1: Any?) {
        pluginActivity.onCreate(arg0, arg1 as PersistableBundle?)
    }

    override fun onProvideReferrer(): Uri? {
        return pluginActivity.onProvideReferrer()
    }

    override fun onActivityReenter(arg0: Int, arg1: Intent?) {
        pluginActivity.onActivityReenter(arg0, arg1)
    }

    override fun onCreateView(arg0: String?, arg1: Context?, arg2: AttributeSet?): View? {
        return pluginActivity.onCreateView(arg0, arg1, arg2)
    }

    override fun onCreateView(
        arg0: View?,
        arg1: String?,
        arg2: Context?,
        arg3: AttributeSet?
    ): View? {
        return pluginActivity.onCreateView(arg0, arg1, arg2, arg3)
    }

    override fun onVisibleBehindCanceled() {
        pluginActivity.onVisibleBehindCanceled()
    }

    override fun onEnterAnimationComplete() {
        pluginActivity.onEnterAnimationComplete()
    }

    override fun onWindowStartingActionMode(arg0: ActionMode.Callback?): ActionMode? {
        return pluginActivity.onWindowStartingActionMode(arg0)
    }

    override fun onWindowStartingActionMode(arg0: ActionMode.Callback?, arg1: Int): ActionMode? {
        return pluginActivity.onWindowStartingActionMode(arg0, arg1)
    }

    override fun onActionModeStarted(arg0: ActionMode?) {
        pluginActivity.onActionModeStarted(arg0)
    }

    override fun onActionModeFinished(arg0: ActionMode?) {
        pluginActivity.onActionModeFinished(arg0)
    }

    override fun onRestoreInstanceState(arg0: Bundle?, arg1: Any?) {
        pluginActivity.onRestoreInstanceState(arg0, arg1 as PersistableBundle?)
    }

    override fun onPostCreate(arg0: Bundle?, arg1: Any?) {
        pluginActivity.onPostCreate(arg0, arg1 as PersistableBundle?)
    }

    override fun onStateNotSaved() {
        pluginActivity.onStateNotSaved()
    }

    override fun onTopResumedActivityChanged(arg0: Boolean) {
        pluginActivity.onTopResumedActivityChanged(arg0)
    }

    override fun onLocalVoiceInteractionStarted() {
        pluginActivity.onLocalVoiceInteractionStarted()
    }

    override fun onLocalVoiceInteractionStopped() {
        pluginActivity.onLocalVoiceInteractionStopped()
    }

    override fun onSaveInstanceState(arg0: Bundle?, arg1: Any?) {
        pluginActivity.onSaveInstanceState(arg0, arg1 as PersistableBundle?)
    }

    override fun onCreateThumbnail(arg0: Bitmap?, arg1: Canvas?): Boolean {
        return pluginActivity.onCreateThumbnail(arg0, arg1)
    }

    override fun onCreateDescription(): CharSequence? {
        return pluginActivity.onCreateDescription()
    }

    override fun onProvideAssistData(arg0: Bundle?) {
        pluginActivity.onProvideAssistData(arg0)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onProvideAssistContent(arg0: Any?) {
        pluginActivity.onProvideAssistContent(arg0 as AssistContent?)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onGetDirectActions(arg0: Any?, arg1: Any?) {
        pluginActivity.onGetDirectActions(
            arg0 as CancellationSignal?,
            arg1 as Consumer<List<DirectAction?>?>?
        )
    }

    override fun onPerformDirectAction(arg0: String?, arg1: Bundle?, arg2: Any?, arg3: Any?) {
    }

    override fun onProvideKeyboardShortcuts(arg0: Any?, arg1: Menu?, arg2: Int) {
        pluginActivity.onProvideKeyboardShortcuts(arg0 as List<KeyboardShortcutGroup?>?, arg1, arg2)
    }

    override fun onMultiWindowModeChanged(arg0: Boolean, arg1: Configuration?) {
        pluginActivity.onMultiWindowModeChanged(arg0, arg1)
    }

    override fun onMultiWindowModeChanged(arg0: Boolean) {
        pluginActivity.onMultiWindowModeChanged(arg0)
    }

    override fun onPictureInPictureModeChanged(arg0: Boolean) {
        pluginActivity.onPictureInPictureModeChanged(arg0)
    }

    override fun onPictureInPictureModeChanged(arg0: Boolean, arg1: Configuration?) {
        pluginActivity.onPictureInPictureModeChanged(arg0, arg1)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onPictureInPictureUiStateChanged(arg0: Any?) {
        pluginActivity.onPictureInPictureUiStateChanged(arg0 as PictureInPictureUiState?)
    }

    override fun onPictureInPictureRequested(): Boolean {
        return pluginActivity.onPictureInPictureRequested()
    }

    override fun onConfigurationChanged(arg0: Configuration) {
        pluginActivity.onConfigurationChanged(arg0)
    }

    override fun onRetainNonConfigurationInstance(): Any? {
        return pluginActivity.onRetainNonConfigurationInstance()
    }

    override fun onLowMemory() {
        pluginActivity.onLowMemory()
    }

    override fun onTrimMemory(arg0: Int) {
        pluginActivity.onTrimMemory(arg0)
    }

    override fun onAttachFragment(arg0: Fragment?) {
        pluginActivity.onAttachFragment(arg0)
    }

    override fun onKeyDown(arg0: Int, arg1: KeyEvent?): Boolean {
        return pluginActivity.onKeyDown(arg0, arg1)
    }

    override fun onKeyLongPress(arg0: Int, arg1: KeyEvent?): Boolean {
        return pluginActivity.onKeyLongPress(arg0, arg1)
    }

    override fun onKeyUp(arg0: Int, arg1: KeyEvent?): Boolean {
        return pluginActivity.onKeyUp(arg0, arg1)
    }

    override fun onKeyMultiple(arg0: Int, arg1: Int, arg2: KeyEvent?): Boolean {
        return pluginActivity.onKeyMultiple(arg0, arg1, arg2)
    }

    override fun onBackPressed() {
        pluginActivity.onBackPressed()
    }

    override fun onKeyShortcut(arg0: Int, arg1: KeyEvent?): Boolean {
        return pluginActivity.onKeyShortcut(arg0, arg1)
    }

    override fun onTouchEvent(arg0: MotionEvent?): Boolean {
        return pluginActivity.onTouchEvent(arg0)
    }

    override fun onTrackballEvent(arg0: MotionEvent?): Boolean {
        return pluginActivity.onTrackballEvent(arg0)
    }

    override fun onGenericMotionEvent(arg0: MotionEvent?): Boolean {
        return pluginActivity.onGenericMotionEvent(arg0)
    }

    override fun onUserInteraction() {
        pluginActivity.onUserInteraction()
    }

    override fun onWindowAttributesChanged(arg0: WindowManager.LayoutParams?) {
        if (mPluginActivityCreated) {
            pluginActivity.onWindowAttributesChanged(arg0)
        }
    }

    override fun onContentChanged() {
        pluginActivity.onContentChanged()
    }

    override fun onWindowFocusChanged(arg0: Boolean) {
        pluginActivity.onWindowFocusChanged(arg0)
    }

    override fun onAttachedToWindow() {
        pluginActivity.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        pluginActivity.onDetachedFromWindow()
    }

    override fun onCreatePanelView(arg0: Int): View? {
        return pluginActivity.onCreatePanelView(arg0)
    }

    override fun onCreatePanelMenu(arg0: Int, arg1: Menu): Boolean {
        return pluginActivity.onCreatePanelMenu(arg0, arg1)
    }

    override fun onPreparePanel(arg0: Int, arg1: View?, arg2: Menu): Boolean {
        return pluginActivity.onPreparePanel(arg0, arg1, arg2)
    }

    override fun onMenuOpened(arg0: Int, arg1: Menu): Boolean {
        return pluginActivity.onMenuOpened(arg0, arg1)
    }

    override fun onMenuItemSelected(arg0: Int, arg1: MenuItem): Boolean {
        return pluginActivity.onMenuItemSelected(arg0, arg1)
    }

    override fun onPanelClosed(arg0: Int, arg1: Menu) {
        pluginActivity.onPanelClosed(arg0, arg1)
    }

    override fun onCreateOptionsMenu(arg0: Menu?): Boolean {
        return pluginActivity.onCreateOptionsMenu(arg0)
    }

    override fun onPrepareOptionsMenu(arg0: Menu?): Boolean {
        return pluginActivity.onPrepareOptionsMenu(arg0)
    }

    override fun onOptionsItemSelected(arg0: MenuItem?): Boolean {
        return pluginActivity.onOptionsItemSelected(arg0)
    }

    override fun onNavigateUp(): Boolean {
        return pluginActivity.onNavigateUp()
    }

    override fun onCreateNavigateUpTaskStack(arg0: Any?) {
        pluginActivity.onCreateNavigateUpTaskStack(arg0 as TaskStackBuilder?)
    }

    override fun onPrepareNavigateUpTaskStack(arg0: Any?) {
        pluginActivity.onPrepareNavigateUpTaskStack(arg0 as TaskStackBuilder?)
    }

    override fun onOptionsMenuClosed(arg0: Menu?) {
        pluginActivity.onOptionsMenuClosed(arg0)
    }

    override fun onCreateContextMenu(arg0: ContextMenu?, arg1: View?, arg2: ContextMenuInfo?) {
        pluginActivity.onCreateContextMenu(arg0, arg1, arg2)
    }

    override fun onContextItemSelected(arg0: MenuItem?): Boolean {
        return pluginActivity.onContextItemSelected(arg0)
    }

    override fun onContextMenuClosed(arg0: Menu?) {
        pluginActivity.onContextMenuClosed(arg0)
    }

    override fun onSearchRequested(): Boolean {
        return pluginActivity.onSearchRequested()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onSearchRequested(arg0: Any?): Boolean {
        return pluginActivity.onSearchRequested(arg0 as SearchEvent?)
    }

    override fun onRequestPermissionsResult(arg0: Int, arg1: Array<String?>?, arg2: IntArray?) {
        pluginActivity.onRequestPermissionsResult(arg0, arg1, arg2)
    }

    override fun onPointerCaptureChanged(arg0: Boolean) {
        pluginActivity.onPointerCaptureChanged(arg0)
    }

    override fun onStop() {
        pluginActivity.onStop()
    }

    override fun onStart() {
        pluginActivity.onStart()
    }

    override fun onActivityResult(arg0: Int, arg1: Int, arg2: Intent?) {
        pluginActivity.onActivityResult(arg0, arg1, arg2)
    }

    override fun onTitleChanged(arg0: CharSequence?, arg1: Int) {
        pluginActivity.onTitleChanged(arg0, arg1)
    }

    override fun onChildTitleChanged(arg0: Activity?, arg1: CharSequence?) {
        TODO("Not yet implemented")
    }

    override fun onRestoreInstanceState(arg0: Bundle?) {
        pluginActivity.onRestoreInstanceState(arg0)
    }

    override fun onPostCreate(arg0: Bundle?) {
        pluginActivity.onPostCreate(arg0)
    }

    override fun onRestart() {
        pluginActivity.onRestart()
    }

    override fun onResume() {
        pluginActivity.onResume()
    }

    override fun onPostResume() {
        pluginActivity.onPostResume()
    }

    override fun onNewIntent(arg0: Intent?) {
        pluginActivity.onNewIntent(arg0)
    }

    override fun onSaveInstanceState(arg0: Bundle?) {
        pluginActivity.onSaveInstanceState(arg0)
    }

    override fun onPause() {
        pluginActivity.onPause()
    }

    override fun onUserLeaveHint() {
        pluginActivity.onUserLeaveHint()
    }

    override fun onDestroy() {
        pluginActivity.onDestroy()
    }

    override fun onCreateDialog(arg0: Int, arg1: Bundle?): Dialog? {
        return pluginActivity.onCreateDialog(arg0, arg1)
    }

    override fun onCreateDialog(arg0: Int): Dialog? {
        return pluginActivity.onCreateDialog(arg0)
    }

    override fun onPrepareDialog(arg0: Int, arg1: Dialog?, arg2: Bundle?) {
        pluginActivity.onPrepareDialog(arg0, arg1, arg2)
    }

    override fun onPrepareDialog(arg0: Int, arg1: Dialog?) {
        pluginActivity.onPrepareDialog(arg0, arg1)
    }

    override fun onApplyThemeResource(arg0: Theme?, arg1: Int, arg2: Boolean) {
        if (mPluginActivityCreated)
            pluginActivity.onApplyThemeResource(arg0, arg1, arg2)
    }

    override fun dispatchKeyEvent(arg0: KeyEvent?): Boolean {
        return pluginActivity.dispatchKeyEvent(arg0)
    }

    override fun dispatchKeyShortcutEvent(arg0: KeyEvent?): Boolean {
        return pluginActivity.dispatchKeyShortcutEvent(arg0)
    }

    override fun dispatchTouchEvent(arg0: MotionEvent?): Boolean {
        return pluginActivity.dispatchTouchEvent(arg0)
    }

    override fun dispatchTrackballEvent(arg0: MotionEvent?): Boolean {
        return pluginActivity.dispatchTrackballEvent(arg0)
    }

    override fun dispatchGenericMotionEvent(arg0: MotionEvent?): Boolean {
        return pluginActivity.dispatchGenericMotionEvent(arg0)
    }

    override fun dispatchPopulateAccessibilityEvent(arg0: AccessibilityEvent?): Boolean {
        return pluginActivity.dispatchPopulateAccessibilityEvent(arg0)
    }

}
