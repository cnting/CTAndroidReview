package com.example.plugin.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Dialog;
import android.app.DirectAction;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.app.PictureInPictureParams;
import android.app.PictureInPictureUiState;
import android.app.SharedElementCallback;
import android.app.TaskStackBuilder;
import android.app.VoiceInteractor;
import android.app.assist.AssistContent;
import android.content.AttributionSource;
import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextParams;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.LocusId;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.media.session.MediaController;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
import android.os.UserHandle;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Display;
import android.view.DragAndDropPermissions;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toolbar;
import android.window.SplashScreen;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/**
 * HostActivityDelegator作为委托者的接口。主要提供它的委托方法的super方法，
 * 以便Delegate可以通过这个接口调用到Activity的super方法。
 */
public interface HostActivityDelegator {
  Activity getHostActivity();

  void setVisible(boolean arg0);

  int getTaskId();

  void dump(String arg0, FileDescriptor arg1, PrintWriter arg2, String[] arg3);

  ComponentName getComponentName();

  void startIntentSenderFromChild(Activity arg0, IntentSender arg1, int arg2, Intent arg3, int arg4,
      int arg5, int arg6) throws IntentSender.SendIntentException;

  void startIntentSenderFromChild(Activity arg0, IntentSender arg1, int arg2, Intent arg3, int arg4,
      int arg5, int arg6, Bundle arg7) throws IntentSender.SendIntentException;

  void overridePendingTransition(int arg0, int arg1);

  Activity getParent();

  boolean isDestroyed();

  void setResult(int arg0, Intent arg1);

  void setResult(int arg0);

  CharSequence getTitle();

  void setContentView(int arg0);

  void setContentView(View arg0, ViewGroup.LayoutParams arg1);

  void setContentView(View arg0);

  void setTitle(CharSequence arg0);

  void setTitle(int arg0);

  Uri getReferrer();

  String getCallingPackage();

  boolean isFinishing();

  void finishAffinity();

  void finishFromChild(Activity arg0);

  void finishAfterTransition();

  void finishActivity(int arg0);

  void finishActivityFromChild(Activity arg0, int arg1);

  void finishAndRemoveTask();

  boolean releaseInstance();

  PendingIntent createPendingResult(int arg0, Intent arg1, int arg2);

  void setRequestedOrientation(int arg0);

  int getRequestedOrientation();

  boolean isTaskRoot();

  boolean moveTaskToBack(boolean arg0);

  String getLocalClassName();

  SharedPreferences getPreferences(int arg0);

  boolean isLaunchedFromBubble();

  Object getSystemService(String arg0);

  void setTitleColor(int arg0);

  int getTitleColor();

  void setTaskDescription(ActivityManager.TaskDescription arg0);

  void setProgressBarVisibility(boolean arg0);

  void setProgressBarIndeterminateVisibility(boolean arg0);

  void setProgressBarIndeterminate(boolean arg0);

  void setProgress(int arg0);

  void setSecondaryProgress(int arg0);

  void setVolumeControlStream(int arg0);

  int getVolumeControlStream();

  void setMediaController(MediaController arg0);

  MediaController getMediaController();

  void runOnUiThread(Runnable arg0);

  boolean isImmersive();

  boolean setTranslucent(boolean arg0);

  boolean requestVisibleBehind(boolean arg0);

  void setImmersive(boolean arg0);

  void setVrModeEnabled(boolean arg0, ComponentName arg1) throws
      PackageManager.NameNotFoundException;

  ActionMode startActionMode(ActionMode.Callback arg0);

  ActionMode startActionMode(ActionMode.Callback arg0, int arg1);

  boolean shouldUpRecreateTask(Intent arg0);

  boolean navigateUpTo(Intent arg0);

  boolean navigateUpToFromChild(Activity arg0, Intent arg1);

  Intent getParentActivityIntent();

  void setEnterSharedElementCallback(SharedElementCallback arg0);

  void setExitSharedElementCallback(SharedElementCallback arg0);

  void postponeEnterTransition();

  void startPostponedEnterTransition();

  DragAndDropPermissions requestDragAndDropPermissions(DragEvent arg0);

  void startLockTask();

  void stopLockTask();

  void showLockTaskEscapeMessage();

  void setShowWhenLocked(boolean arg0);

  void setInheritShowWhenLocked(boolean arg0);

  void setTurnScreenOn(boolean arg0);

  Intent getIntent();

  void setIntent(Intent arg0);

  void setLocusContext(LocusId arg0, Bundle arg1);

  Application getApplication();

  boolean isChild();

  WindowManager getWindowManager();

  Window getWindow();

  LoaderManager getLoaderManager();

  View getCurrentFocus();

  void registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks arg0);

  void unregisterActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks arg0);

  SplashScreen getSplashScreen();

  boolean isVoiceInteraction();

  boolean isVoiceInteractionRoot();

  VoiceInteractor getVoiceInteractor();

  boolean isLocalVoiceInteractionSupported();

  void startLocalVoiceInteraction(Bundle arg0);

  void stopLocalVoiceInteraction();

  void requestShowKeyboardShortcuts();

  void dismissKeyboardShortcutsHelper();

  boolean showAssist(Bundle arg0);

  void reportFullyDrawn();

  boolean isInMultiWindowMode();

  boolean isInPictureInPictureMode();

  void enterPictureInPictureMode();

  boolean enterPictureInPictureMode(PictureInPictureParams arg0);

  void setPictureInPictureParams(PictureInPictureParams arg0);

  int getMaxNumPictureInPictureActions();

  int getChangingConfigurations();

  Object getLastNonConfigurationInstance();

  FragmentManager getFragmentManager();

  Cursor managedQuery(Uri arg0, String[] arg1, String arg2, String[] arg3, String arg4);

  void startManagingCursor(Cursor arg0);

  void stopManagingCursor(Cursor arg0);

  <T extends View> T findViewById(int arg0);

  <T extends View> T requireViewById(int arg0);

  ActionBar getActionBar();

  void setActionBar(Toolbar arg0);

  void addContentView(View arg0, ViewGroup.LayoutParams arg1);

  TransitionManager getContentTransitionManager();

  void setContentTransitionManager(TransitionManager arg0);

  Scene getContentScene();

  void setFinishOnTouchOutside(boolean arg0);

  void setDefaultKeyMode(int arg0);

  boolean hasWindowFocus();

  void invalidateOptionsMenu();

  void openOptionsMenu();

  void closeOptionsMenu();

  void registerForContextMenu(View arg0);

  void unregisterForContextMenu(View arg0);

  void openContextMenu(View arg0);

  void closeContextMenu();

  boolean showDialog(int arg0, Bundle arg1);

  void showDialog(int arg0);

  void dismissDialog(int arg0);

  void removeDialog(int arg0);

  SearchEvent getSearchEvent();

  void startSearch(String arg0, boolean arg1, Bundle arg2, boolean arg3);

  void triggerSearch(String arg0, Bundle arg1);

  void takeKeyEvents(boolean arg0);

  boolean requestWindowFeature(int arg0);

  void setFeatureDrawableResource(int arg0, int arg1);

  void setFeatureDrawableUri(int arg0, Uri arg1);

  void setFeatureDrawable(int arg0, Drawable arg1);

  void setFeatureDrawableAlpha(int arg0, int arg1);

  MenuInflater getMenuInflater();

  void setTheme(int arg0);

  void requestPermissions(String[] arg0, int arg1);

  boolean shouldShowRequestPermissionRationale(String arg0);

  void startActivityForResult(Intent arg0, int arg1);

  void startActivityForResult(Intent arg0, int arg1, Bundle arg2);

  boolean isActivityTransitionRunning();

  void startIntentSenderForResult(IntentSender arg0, int arg1, Intent arg2, int arg3, int arg4,
      int arg5) throws IntentSender.SendIntentException;

  void startIntentSenderForResult(IntentSender arg0, int arg1, Intent arg2, int arg3, int arg4,
      int arg5, Bundle arg6) throws IntentSender.SendIntentException;

  void startActivity(Intent arg0, Bundle arg1);

  void startActivity(Intent arg0);

  void startActivities(Intent[] arg0, Bundle arg1);

  void startActivities(Intent[] arg0);

  void startIntentSender(IntentSender arg0, Intent arg1, int arg2, int arg3, int arg4, Bundle arg5)
      throws IntentSender.SendIntentException;

  void startIntentSender(IntentSender arg0, Intent arg1, int arg2, int arg3, int arg4) throws
      IntentSender.SendIntentException;

  boolean startActivityIfNeeded(Intent arg0, int arg1, Bundle arg2);

  boolean startActivityIfNeeded(Intent arg0, int arg1);

  boolean startNextMatchingActivity(Intent arg0, Bundle arg1);

  boolean startNextMatchingActivity(Intent arg0);

  void startActivityFromChild(Activity arg0, Intent arg1, int arg2, Bundle arg3);

  void startActivityFromChild(Activity arg0, Intent arg1, int arg2);

  void startActivityFromFragment(Fragment arg0, Intent arg1, int arg2);

  void startActivityFromFragment(Fragment arg0, Intent arg1, int arg2, Bundle arg3);

  AssetManager getAssets();

  void setTheme(Resources.Theme arg0);

  Resources.Theme getTheme();

  void applyOverrideConfiguration(Configuration arg0);

  File getDir(String arg0, int arg1);

  boolean deleteFile(String arg0);

  ContextParams getParams();

  File getCacheDir();

  String getPackageName();

  int checkPermission(String arg0, int arg1, int arg2);

  String[] fileList();

  Context createContext(ContextParams arg0);

  PackageManager getPackageManager();

  Context getBaseContext();

  ContentResolver getContentResolver();

  Looper getMainLooper();

  Executor getMainExecutor();

  Context getApplicationContext();

  String getOpPackageName();

  String getAttributionTag();

  ApplicationInfo getApplicationInfo();

  String getPackageResourcePath();

  String getPackageCodePath();

  SharedPreferences getSharedPreferences(String arg0, int arg1);

  boolean moveSharedPreferencesFrom(Context arg0, String arg1);

  boolean deleteSharedPreferences(String arg0);

  FileInputStream openFileInput(String arg0) throws FileNotFoundException;

  FileOutputStream openFileOutput(String arg0, int arg1) throws FileNotFoundException;

  File getFileStreamPath(String arg0);

  File getDataDir();

  File getFilesDir();

  File getNoBackupFilesDir();

  File getExternalFilesDir(String arg0);

  File[] getExternalFilesDirs(String arg0);

  File getObbDir();

  File[] getObbDirs();

  File getCodeCacheDir();

  File getExternalCacheDir();

  File[] getExternalCacheDirs();

  File[] getExternalMediaDirs();

  SQLiteDatabase openOrCreateDatabase(String arg0, int arg1, SQLiteDatabase.CursorFactory arg2,
      DatabaseErrorHandler arg3);

  SQLiteDatabase openOrCreateDatabase(String arg0, int arg1, SQLiteDatabase.CursorFactory arg2);

  boolean moveDatabaseFrom(Context arg0, String arg1);

  boolean deleteDatabase(String arg0);

  File getDatabasePath(String arg0);

  String[] databaseList();

  Drawable getWallpaper();

  Drawable peekWallpaper();

  int getWallpaperDesiredMinimumWidth();

  int getWallpaperDesiredMinimumHeight();

  void setWallpaper(Bitmap arg0) throws IOException;

  void setWallpaper(InputStream arg0) throws IOException;

  void clearWallpaper() throws IOException;

  void sendBroadcast(Intent arg0);

  void sendBroadcast(Intent arg0, String arg1);

  void sendOrderedBroadcast(Intent arg0, String arg1, String arg2, BroadcastReceiver arg3,
      Handler arg4, int arg5, String arg6, Bundle arg7);

  void sendOrderedBroadcast(Intent arg0, int arg1, String arg2, String arg3, BroadcastReceiver arg4,
      Handler arg5, String arg6, Bundle arg7, Bundle arg8);

  void sendOrderedBroadcast(Intent arg0, String arg1, BroadcastReceiver arg2, Handler arg3,
      int arg4, String arg5, Bundle arg6);

  void sendOrderedBroadcast(Intent arg0, String arg1);

  void sendBroadcastAsUser(Intent arg0, UserHandle arg1);

  void sendBroadcastAsUser(Intent arg0, UserHandle arg1, String arg2);

  void sendOrderedBroadcastAsUser(Intent arg0, UserHandle arg1, String arg2, BroadcastReceiver arg3,
      Handler arg4, int arg5, String arg6, Bundle arg7);

  void sendStickyBroadcast(Intent arg0);

  void sendStickyBroadcast(Intent arg0, Bundle arg1);

  void sendStickyOrderedBroadcast(Intent arg0, BroadcastReceiver arg1, Handler arg2, int arg3,
      String arg4, Bundle arg5);

  void removeStickyBroadcast(Intent arg0);

  void sendStickyBroadcastAsUser(Intent arg0, UserHandle arg1);

  void sendStickyOrderedBroadcastAsUser(Intent arg0, UserHandle arg1, BroadcastReceiver arg2,
      Handler arg3, int arg4, String arg5, Bundle arg6);

  void removeStickyBroadcastAsUser(Intent arg0, UserHandle arg1);

  Intent registerReceiver(BroadcastReceiver arg0, IntentFilter arg1, int arg2);

  Intent registerReceiver(BroadcastReceiver arg0, IntentFilter arg1);

  Intent registerReceiver(BroadcastReceiver arg0, IntentFilter arg1, String arg2, Handler arg3,
      int arg4);

  Intent registerReceiver(BroadcastReceiver arg0, IntentFilter arg1, String arg2, Handler arg3);

  void unregisterReceiver(BroadcastReceiver arg0);

  ComponentName startService(Intent arg0);

  ComponentName startForegroundService(Intent arg0);

  boolean stopService(Intent arg0);

  boolean bindService(Intent arg0, int arg1, Executor arg2, ServiceConnection arg3);

  boolean bindService(Intent arg0, ServiceConnection arg1, int arg2);

  boolean bindIsolatedService(Intent arg0, int arg1, String arg2, Executor arg3,
      ServiceConnection arg4);

  boolean bindServiceAsUser(Intent arg0, ServiceConnection arg1, int arg2, UserHandle arg3);

  void updateServiceGroup(ServiceConnection arg0, int arg1, int arg2);

  void unbindService(ServiceConnection arg0);

  boolean startInstrumentation(ComponentName arg0, String arg1, Bundle arg2);

  String getSystemServiceName(Class<?> arg0);

  int checkCallingPermission(String arg0);

  int checkCallingOrSelfPermission(String arg0);

  int checkSelfPermission(String arg0);

  void enforcePermission(String arg0, int arg1, int arg2, String arg3);

  void enforceCallingPermission(String arg0, String arg1);

  void enforceCallingOrSelfPermission(String arg0, String arg1);

  void grantUriPermission(String arg0, Uri arg1, int arg2);

  void revokeUriPermission(String arg0, Uri arg1, int arg2);

  void revokeUriPermission(Uri arg0, int arg1);

  int checkUriPermission(Uri arg0, String arg1, String arg2, int arg3, int arg4, int arg5);

  int checkUriPermission(Uri arg0, int arg1, int arg2, int arg3);

  int[] checkUriPermissions(List<Uri> arg0, int arg1, int arg2, int arg3);

  int checkCallingUriPermission(Uri arg0, int arg1);

  int[] checkCallingUriPermissions(List<Uri> arg0, int arg1);

  int checkCallingOrSelfUriPermission(Uri arg0, int arg1);

  int[] checkCallingOrSelfUriPermissions(List<Uri> arg0, int arg1);

  void enforceUriPermission(Uri arg0, String arg1, String arg2, int arg3, int arg4, int arg5,
      String arg6);

  void enforceUriPermission(Uri arg0, int arg1, int arg2, int arg3, String arg4);

  void enforceCallingUriPermission(Uri arg0, int arg1, String arg2);

  void enforceCallingOrSelfUriPermission(Uri arg0, int arg1, String arg2);

  Context createPackageContext(String arg0, int arg1) throws PackageManager.NameNotFoundException;

  Context createContextForSplit(String arg0) throws PackageManager.NameNotFoundException;

  Context createConfigurationContext(Configuration arg0);

  Context createDisplayContext(Display arg0);

  Context createWindowContext(Display arg0, int arg1, Bundle arg2);

  Context createWindowContext(int arg0, Bundle arg1);

  Context createAttributionContext(String arg0);

  AttributionSource getAttributionSource();

  boolean isRestricted();

  Display getDisplay();

  Context createDeviceProtectedStorageContext();

  boolean isDeviceProtectedStorage();

  boolean isUiContext();

  void registerComponentCallbacks(ComponentCallbacks arg0);

  void unregisterComponentCallbacks(ComponentCallbacks arg0);

  void sendBroadcastWithMultiplePermissions(Intent arg0, String[] arg1);

  void attachBaseContext(Context arg0);

  boolean isChangingConfigurations();

  void finish();

  ClassLoader getClassLoader();

  LayoutInflater getLayoutInflater();

  Resources getResources();

  void recreate();

  ComponentName getCallingActivity();

  ActionMode onWindowStartingActionMode(ActionMode.Callback arg0);

  ActionMode onWindowStartingActionMode(ActionMode.Callback arg0, int arg1);

  void onActionModeStarted(ActionMode arg0);

  void onActionModeFinished(ActionMode arg0);

  void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> arg0, Menu arg1, int arg2);

  void onWindowAttributesChanged(WindowManager.LayoutParams arg0);

  void onContentChanged();

  void onWindowFocusChanged(boolean arg0);

  void onAttachedToWindow();

  void onDetachedFromWindow();

  View onCreatePanelView(int arg0);

  boolean onCreatePanelMenu(int arg0, Menu arg1);

  boolean onPreparePanel(int arg0, View arg1, Menu arg2);

  boolean onMenuOpened(int arg0, Menu arg1);

  boolean onMenuItemSelected(int arg0, MenuItem arg1);

  void onPanelClosed(int arg0, Menu arg1);

  boolean onSearchRequested();

  boolean onSearchRequested(SearchEvent arg0);

  void onPointerCaptureChanged(boolean arg0);

  boolean dispatchKeyEvent(KeyEvent arg0);

  boolean dispatchKeyShortcutEvent(KeyEvent arg0);

  boolean dispatchTouchEvent(MotionEvent arg0);

  boolean dispatchTrackballEvent(MotionEvent arg0);

  boolean dispatchGenericMotionEvent(MotionEvent arg0);

  boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent arg0);

  boolean superIsChangingConfigurations();

  void superFinish();

  ClassLoader superGetClassLoader();

  LayoutInflater superGetLayoutInflater();

  Resources superGetResources();

  void superRecreate();

  ComponentName superGetCallingActivity();

  void superOnCreate(Bundle arg0, PersistableBundle arg1);

  Uri superOnProvideReferrer();

  void superOnActivityReenter(int arg0, Intent arg1);

  View superOnCreateView(String arg0, Context arg1, AttributeSet arg2);

  View superOnCreateView(View arg0, String arg1, Context arg2, AttributeSet arg3);

  void superOnVisibleBehindCanceled();

  void superOnEnterAnimationComplete();

  ActionMode superOnWindowStartingActionMode(ActionMode.Callback arg0);

  ActionMode superOnWindowStartingActionMode(ActionMode.Callback arg0, int arg1);

  void superOnActionModeStarted(ActionMode arg0);

  void superOnActionModeFinished(ActionMode arg0);

  void superOnRestoreInstanceState(Bundle arg0, PersistableBundle arg1);

  void superOnPostCreate(Bundle arg0, PersistableBundle arg1);

  void superOnStateNotSaved();

  void superOnTopResumedActivityChanged(boolean arg0);

  void superOnLocalVoiceInteractionStarted();

  void superOnLocalVoiceInteractionStopped();

  void superOnSaveInstanceState(Bundle arg0, PersistableBundle arg1);

  boolean superOnCreateThumbnail(Bitmap arg0, Canvas arg1);

  CharSequence superOnCreateDescription();

  void superOnProvideAssistData(Bundle arg0);

  void superOnProvideAssistContent(AssistContent arg0);

  void superOnGetDirectActions(CancellationSignal arg0, Consumer<List<DirectAction>> arg1);

  void superOnPerformDirectAction(String arg0, Bundle arg1, CancellationSignal arg2,
      Consumer<Bundle> arg3);

  void superOnProvideKeyboardShortcuts(List<KeyboardShortcutGroup> arg0, Menu arg1, int arg2);

  void superOnMultiWindowModeChanged(boolean arg0, Configuration arg1);

  void superOnMultiWindowModeChanged(boolean arg0);

  void superOnPictureInPictureModeChanged(boolean arg0);

  void superOnPictureInPictureModeChanged(boolean arg0, Configuration arg1);

  void superOnPictureInPictureUiStateChanged(PictureInPictureUiState arg0);

  boolean superOnPictureInPictureRequested();

  void superOnConfigurationChanged(Configuration arg0);

  Object superOnRetainNonConfigurationInstance();

  void superOnLowMemory();

  void superOnTrimMemory(int arg0);

  void superOnAttachFragment(Fragment arg0);

  boolean superOnKeyDown(int arg0, KeyEvent arg1);

  boolean superOnKeyLongPress(int arg0, KeyEvent arg1);

  boolean superOnKeyUp(int arg0, KeyEvent arg1);

  boolean superOnKeyMultiple(int arg0, int arg1, KeyEvent arg2);

  void superOnBackPressed();

  boolean superOnKeyShortcut(int arg0, KeyEvent arg1);

  boolean superOnTouchEvent(MotionEvent arg0);

  boolean superOnTrackballEvent(MotionEvent arg0);

  boolean superOnGenericMotionEvent(MotionEvent arg0);

  void superOnUserInteraction();

  void superOnWindowAttributesChanged(WindowManager.LayoutParams arg0);

  void superOnContentChanged();

  void superOnWindowFocusChanged(boolean arg0);

  void superOnAttachedToWindow();

  void superOnDetachedFromWindow();

  View superOnCreatePanelView(int arg0);

  boolean superOnCreatePanelMenu(int arg0, Menu arg1);

  boolean superOnPreparePanel(int arg0, View arg1, Menu arg2);

  boolean superOnMenuOpened(int arg0, Menu arg1);

  boolean superOnMenuItemSelected(int arg0, MenuItem arg1);

  void superOnPanelClosed(int arg0, Menu arg1);

  boolean superOnCreateOptionsMenu(Menu arg0);

  boolean superOnPrepareOptionsMenu(Menu arg0);

  boolean superOnOptionsItemSelected(MenuItem arg0);

  boolean superOnNavigateUp();

  boolean superOnNavigateUpFromChild(Activity arg0);

  void superOnCreateNavigateUpTaskStack(TaskStackBuilder arg0);

  void superOnPrepareNavigateUpTaskStack(TaskStackBuilder arg0);

  void superOnOptionsMenuClosed(Menu arg0);

  void superOnCreateContextMenu(ContextMenu arg0, View arg1, ContextMenu.ContextMenuInfo arg2);

  boolean superOnContextItemSelected(MenuItem arg0);

  void superOnContextMenuClosed(Menu arg0);

  boolean superOnSearchRequested();

  boolean superOnSearchRequested(SearchEvent arg0);

  void superOnRequestPermissionsResult(int arg0, String[] arg1, int[] arg2);

  void superOnPointerCaptureChanged(boolean arg0);

  void superOnStop();

  void superOnStart();

  void superOnCreate(Bundle arg0);

  void superOnActivityResult(int arg0, int arg1, Intent arg2);

  void superOnTitleChanged(CharSequence arg0, int arg1);

  void superOnChildTitleChanged(Activity arg0, CharSequence arg1);

  void superOnRestoreInstanceState(Bundle arg0);

  void superOnPostCreate(Bundle arg0);

  void superOnRestart();

  void superOnResume();

  void superOnPostResume();

  void superOnNewIntent(Intent arg0);

  void superOnSaveInstanceState(Bundle arg0);

  void superOnPause();

  void superOnUserLeaveHint();

  void superOnDestroy();

  Dialog superOnCreateDialog(int arg0, Bundle arg1);

  Dialog superOnCreateDialog(int arg0);

  void superOnPrepareDialog(int arg0, Dialog arg1, Bundle arg2);

  void superOnPrepareDialog(int arg0, Dialog arg1);

  void superOnApplyThemeResource(Resources.Theme arg0, int arg1, boolean arg2);

  boolean superDispatchKeyEvent(KeyEvent arg0);

  boolean superDispatchKeyShortcutEvent(KeyEvent arg0);

  boolean superDispatchTouchEvent(MotionEvent arg0);

  boolean superDispatchTrackballEvent(MotionEvent arg0);

  boolean superDispatchGenericMotionEvent(MotionEvent arg0);

  boolean superDispatchPopulateAccessibilityEvent(AccessibilityEvent arg0);

  void superSetVisible(boolean arg0);

  int superGetTaskId();

  void superDump(String arg0, FileDescriptor arg1, PrintWriter arg2, String[] arg3);

  ComponentName superGetComponentName();

  void superStartIntentSenderFromChild(Activity arg0, IntentSender arg1, int arg2, Intent arg3,
      int arg4, int arg5, int arg6) throws IntentSender.SendIntentException;

  void superStartIntentSenderFromChild(Activity arg0, IntentSender arg1, int arg2, Intent arg3,
      int arg4, int arg5, int arg6, Bundle arg7) throws IntentSender.SendIntentException;

  void superOverridePendingTransition(int arg0, int arg1);

  Activity superGetParent();

  boolean superIsDestroyed();

  void superSetResult(int arg0, Intent arg1);

  void superSetResult(int arg0);

  CharSequence superGetTitle();

  void superSetContentView(int arg0);

  void superSetContentView(View arg0, ViewGroup.LayoutParams arg1);

  void superSetContentView(View arg0);

  void superSetTitle(CharSequence arg0);

  void superSetTitle(int arg0);

  Uri superGetReferrer();

  String superGetCallingPackage();

  boolean superIsFinishing();

  void superFinishAffinity();

  void superFinishFromChild(Activity arg0);

  void superFinishAfterTransition();

  void superFinishActivity(int arg0);

  void superFinishActivityFromChild(Activity arg0, int arg1);

  void superFinishAndRemoveTask();

  boolean superReleaseInstance();

  PendingIntent superCreatePendingResult(int arg0, Intent arg1, int arg2);

  void superSetRequestedOrientation(int arg0);

  int superGetRequestedOrientation();

  boolean superIsTaskRoot();

  boolean superMoveTaskToBack(boolean arg0);

  String superGetLocalClassName();

  SharedPreferences superGetPreferences(int arg0);

  boolean superIsLaunchedFromBubble();

  Object superGetSystemService(String arg0);

  void superSetTitleColor(int arg0);

  int superGetTitleColor();

  void superSetTaskDescription(ActivityManager.TaskDescription arg0);

  void superSetProgressBarVisibility(boolean arg0);

  void superSetProgressBarIndeterminateVisibility(boolean arg0);

  void superSetProgressBarIndeterminate(boolean arg0);

  void superSetProgress(int arg0);

  void superSetSecondaryProgress(int arg0);

  void superSetVolumeControlStream(int arg0);

  int superGetVolumeControlStream();

  void superSetMediaController(MediaController arg0);

  MediaController superGetMediaController();

  void superRunOnUiThread(Runnable arg0);

  boolean superIsImmersive();

  boolean superSetTranslucent(boolean arg0);

  boolean superRequestVisibleBehind(boolean arg0);

  void superSetImmersive(boolean arg0);

  void superSetVrModeEnabled(boolean arg0, ComponentName arg1) throws
      PackageManager.NameNotFoundException;

  ActionMode superStartActionMode(ActionMode.Callback arg0);

  ActionMode superStartActionMode(ActionMode.Callback arg0, int arg1);

  boolean superShouldUpRecreateTask(Intent arg0);

  boolean superNavigateUpTo(Intent arg0);

  boolean superNavigateUpToFromChild(Activity arg0, Intent arg1);

  Intent superGetParentActivityIntent();

  void superSetEnterSharedElementCallback(SharedElementCallback arg0);

  void superSetExitSharedElementCallback(SharedElementCallback arg0);

  void superPostponeEnterTransition();

  void superStartPostponedEnterTransition();

  DragAndDropPermissions superRequestDragAndDropPermissions(DragEvent arg0);

  void superStartLockTask();

  void superStopLockTask();

  void superShowLockTaskEscapeMessage();

  void superSetShowWhenLocked(boolean arg0);

  void superSetInheritShowWhenLocked(boolean arg0);

  void superSetTurnScreenOn(boolean arg0);

  Intent superGetIntent();

  void superSetIntent(Intent arg0);

  void superSetLocusContext(LocusId arg0, Bundle arg1);

  Application superGetApplication();

  boolean superIsChild();

  WindowManager superGetWindowManager();

  Window superGetWindow();

  LoaderManager superGetLoaderManager();

  View superGetCurrentFocus();

  void superRegisterActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks arg0);

  void superUnregisterActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks arg0);

  SplashScreen superGetSplashScreen();

  boolean superIsVoiceInteraction();

  boolean superIsVoiceInteractionRoot();

  VoiceInteractor superGetVoiceInteractor();

  boolean superIsLocalVoiceInteractionSupported();

  void superStartLocalVoiceInteraction(Bundle arg0);

  void superStopLocalVoiceInteraction();

  void superRequestShowKeyboardShortcuts();

  void superDismissKeyboardShortcutsHelper();

  boolean superShowAssist(Bundle arg0);

  void superReportFullyDrawn();

  boolean superIsInMultiWindowMode();

  boolean superIsInPictureInPictureMode();

  void superEnterPictureInPictureMode();

  boolean superEnterPictureInPictureMode(PictureInPictureParams arg0);

  void superSetPictureInPictureParams(PictureInPictureParams arg0);

  int superGetMaxNumPictureInPictureActions();

  int superGetChangingConfigurations();

  Object superGetLastNonConfigurationInstance();

  FragmentManager superGetFragmentManager();

  Cursor superManagedQuery(Uri arg0, String[] arg1, String arg2, String[] arg3, String arg4);

  void superStartManagingCursor(Cursor arg0);

  void superStopManagingCursor(Cursor arg0);

  <T extends View> T superFindViewById(int arg0);

  <T extends View> T superRequireViewById(int arg0);

  ActionBar superGetActionBar();

  void superSetActionBar(Toolbar arg0);

  void superAddContentView(View arg0, ViewGroup.LayoutParams arg1);

  TransitionManager superGetContentTransitionManager();

  void superSetContentTransitionManager(TransitionManager arg0);

  Scene superGetContentScene();

  void superSetFinishOnTouchOutside(boolean arg0);

  void superSetDefaultKeyMode(int arg0);

  boolean superHasWindowFocus();

  void superInvalidateOptionsMenu();

  void superOpenOptionsMenu();

  void superCloseOptionsMenu();

  void superRegisterForContextMenu(View arg0);

  void superUnregisterForContextMenu(View arg0);

  void superOpenContextMenu(View arg0);

  void superCloseContextMenu();

  boolean superShowDialog(int arg0, Bundle arg1);

  void superShowDialog(int arg0);

  void superDismissDialog(int arg0);

  void superRemoveDialog(int arg0);

  SearchEvent superGetSearchEvent();

  void superStartSearch(String arg0, boolean arg1, Bundle arg2, boolean arg3);

  void superTriggerSearch(String arg0, Bundle arg1);

  void superTakeKeyEvents(boolean arg0);

  boolean superRequestWindowFeature(int arg0);

  void superSetFeatureDrawableResource(int arg0, int arg1);

  void superSetFeatureDrawableUri(int arg0, Uri arg1);

  void superSetFeatureDrawable(int arg0, Drawable arg1);

  void superSetFeatureDrawableAlpha(int arg0, int arg1);

  MenuInflater superGetMenuInflater();

  void superSetTheme(int arg0);

  void superRequestPermissions(String[] arg0, int arg1);

  boolean superShouldShowRequestPermissionRationale(String arg0);

  void superStartActivityForResult(Intent arg0, int arg1);

  void superStartActivityForResult(Intent arg0, int arg1, Bundle arg2);

  boolean superIsActivityTransitionRunning();

  void superStartIntentSenderForResult(IntentSender arg0, int arg1, Intent arg2, int arg3, int arg4,
      int arg5) throws IntentSender.SendIntentException;

  void superStartIntentSenderForResult(IntentSender arg0, int arg1, Intent arg2, int arg3, int arg4,
      int arg5, Bundle arg6) throws IntentSender.SendIntentException;

  void superStartActivity(Intent arg0, Bundle arg1);

  void superStartActivity(Intent arg0);

  void superStartActivities(Intent[] arg0, Bundle arg1);

  void superStartActivities(Intent[] arg0);

  void superStartIntentSender(IntentSender arg0, Intent arg1, int arg2, int arg3, int arg4,
      Bundle arg5) throws IntentSender.SendIntentException;

  void superStartIntentSender(IntentSender arg0, Intent arg1, int arg2, int arg3, int arg4) throws
      IntentSender.SendIntentException;

  boolean superStartActivityIfNeeded(Intent arg0, int arg1, Bundle arg2);

  boolean superStartActivityIfNeeded(Intent arg0, int arg1);

  boolean superStartNextMatchingActivity(Intent arg0, Bundle arg1);

  boolean superStartNextMatchingActivity(Intent arg0);

  void superStartActivityFromChild(Activity arg0, Intent arg1, int arg2, Bundle arg3);

  void superStartActivityFromChild(Activity arg0, Intent arg1, int arg2);

  void superStartActivityFromFragment(Fragment arg0, Intent arg1, int arg2);

  void superStartActivityFromFragment(Fragment arg0, Intent arg1, int arg2, Bundle arg3);

  AssetManager superGetAssets();

  void superSetTheme(Resources.Theme arg0);

  Resources.Theme superGetTheme();

  void superApplyOverrideConfiguration(Configuration arg0);

  File superGetDir(String arg0, int arg1);

  boolean superDeleteFile(String arg0);

  ContextParams superGetParams();

  File superGetCacheDir();

  String superGetPackageName();

  int superCheckPermission(String arg0, int arg1, int arg2);

  String[] superFileList();

  Context superCreateContext(ContextParams arg0);

  PackageManager superGetPackageManager();

  Context superGetBaseContext();

  ContentResolver superGetContentResolver();

  Looper superGetMainLooper();

  Executor superGetMainExecutor();

  Context superGetApplicationContext();

  String superGetOpPackageName();

  String superGetAttributionTag();

  ApplicationInfo superGetApplicationInfo();

  String superGetPackageResourcePath();

  String superGetPackageCodePath();

  SharedPreferences superGetSharedPreferences(String arg0, int arg1);

  boolean superMoveSharedPreferencesFrom(Context arg0, String arg1);

  boolean superDeleteSharedPreferences(String arg0);

  FileInputStream superOpenFileInput(String arg0) throws FileNotFoundException;

  FileOutputStream superOpenFileOutput(String arg0, int arg1) throws FileNotFoundException;

  File superGetFileStreamPath(String arg0);

  File superGetDataDir();

  File superGetFilesDir();

  File superGetNoBackupFilesDir();

  File superGetExternalFilesDir(String arg0);

  File[] superGetExternalFilesDirs(String arg0);

  File superGetObbDir();

  File[] superGetObbDirs();

  File superGetCodeCacheDir();

  File superGetExternalCacheDir();

  File[] superGetExternalCacheDirs();

  File[] superGetExternalMediaDirs();

  SQLiteDatabase superOpenOrCreateDatabase(String arg0, int arg1, SQLiteDatabase.CursorFactory arg2,
      DatabaseErrorHandler arg3);

  SQLiteDatabase superOpenOrCreateDatabase(String arg0, int arg1,
      SQLiteDatabase.CursorFactory arg2);

  boolean superMoveDatabaseFrom(Context arg0, String arg1);

  boolean superDeleteDatabase(String arg0);

  File superGetDatabasePath(String arg0);

  String[] superDatabaseList();

  Drawable superGetWallpaper();

  Drawable superPeekWallpaper();

  int superGetWallpaperDesiredMinimumWidth();

  int superGetWallpaperDesiredMinimumHeight();

  void superSetWallpaper(Bitmap arg0) throws IOException;

  void superSetWallpaper(InputStream arg0) throws IOException;

  void superClearWallpaper() throws IOException;

  void superSendBroadcast(Intent arg0);

  void superSendBroadcast(Intent arg0, String arg1);

  void superSendOrderedBroadcast(Intent arg0, String arg1, String arg2, BroadcastReceiver arg3,
      Handler arg4, int arg5, String arg6, Bundle arg7);

  void superSendOrderedBroadcast(Intent arg0, int arg1, String arg2, String arg3,
      BroadcastReceiver arg4, Handler arg5, String arg6, Bundle arg7, Bundle arg8);

  void superSendOrderedBroadcast(Intent arg0, String arg1, BroadcastReceiver arg2, Handler arg3,
      int arg4, String arg5, Bundle arg6);

  void superSendOrderedBroadcast(Intent arg0, String arg1);

  void superSendBroadcastAsUser(Intent arg0, UserHandle arg1);

  void superSendBroadcastAsUser(Intent arg0, UserHandle arg1, String arg2);

  void superSendOrderedBroadcastAsUser(Intent arg0, UserHandle arg1, String arg2,
      BroadcastReceiver arg3, Handler arg4, int arg5, String arg6, Bundle arg7);

  void superSendStickyBroadcast(Intent arg0);

  void superSendStickyBroadcast(Intent arg0, Bundle arg1);

  void superSendStickyOrderedBroadcast(Intent arg0, BroadcastReceiver arg1, Handler arg2, int arg3,
      String arg4, Bundle arg5);

  void superRemoveStickyBroadcast(Intent arg0);

  void superSendStickyBroadcastAsUser(Intent arg0, UserHandle arg1);

  void superSendStickyOrderedBroadcastAsUser(Intent arg0, UserHandle arg1, BroadcastReceiver arg2,
      Handler arg3, int arg4, String arg5, Bundle arg6);

  void superRemoveStickyBroadcastAsUser(Intent arg0, UserHandle arg1);

  Intent superRegisterReceiver(BroadcastReceiver arg0, IntentFilter arg1, int arg2);

  Intent superRegisterReceiver(BroadcastReceiver arg0, IntentFilter arg1);

  Intent superRegisterReceiver(BroadcastReceiver arg0, IntentFilter arg1, String arg2, Handler arg3,
      int arg4);

  Intent superRegisterReceiver(BroadcastReceiver arg0, IntentFilter arg1, String arg2,
      Handler arg3);

  void superUnregisterReceiver(BroadcastReceiver arg0);

  ComponentName superStartService(Intent arg0);

  ComponentName superStartForegroundService(Intent arg0);

  boolean superStopService(Intent arg0);

  boolean superBindService(Intent arg0, int arg1, Executor arg2, ServiceConnection arg3);

  boolean superBindService(Intent arg0, ServiceConnection arg1, int arg2);

  boolean superBindIsolatedService(Intent arg0, int arg1, String arg2, Executor arg3,
      ServiceConnection arg4);

  boolean superBindServiceAsUser(Intent arg0, ServiceConnection arg1, int arg2, UserHandle arg3);

  void superUpdateServiceGroup(ServiceConnection arg0, int arg1, int arg2);

  void superUnbindService(ServiceConnection arg0);

  boolean superStartInstrumentation(ComponentName arg0, String arg1, Bundle arg2);

  String superGetSystemServiceName(Class<?> arg0);

  int superCheckCallingPermission(String arg0);

  int superCheckCallingOrSelfPermission(String arg0);

  int superCheckSelfPermission(String arg0);

  void superEnforcePermission(String arg0, int arg1, int arg2, String arg3);

  void superEnforceCallingPermission(String arg0, String arg1);

  void superEnforceCallingOrSelfPermission(String arg0, String arg1);

  void superGrantUriPermission(String arg0, Uri arg1, int arg2);

  void superRevokeUriPermission(String arg0, Uri arg1, int arg2);

  void superRevokeUriPermission(Uri arg0, int arg1);

  int superCheckUriPermission(Uri arg0, String arg1, String arg2, int arg3, int arg4, int arg5);

  int superCheckUriPermission(Uri arg0, int arg1, int arg2, int arg3);

  int[] superCheckUriPermissions(List<Uri> arg0, int arg1, int arg2, int arg3);

  int superCheckCallingUriPermission(Uri arg0, int arg1);

  int[] superCheckCallingUriPermissions(List<Uri> arg0, int arg1);

  int superCheckCallingOrSelfUriPermission(Uri arg0, int arg1);

  int[] superCheckCallingOrSelfUriPermissions(List<Uri> arg0, int arg1);

  void superEnforceUriPermission(Uri arg0, String arg1, String arg2, int arg3, int arg4, int arg5,
      String arg6);

  void superEnforceUriPermission(Uri arg0, int arg1, int arg2, int arg3, String arg4);

  void superEnforceCallingUriPermission(Uri arg0, int arg1, String arg2);

  void superEnforceCallingOrSelfUriPermission(Uri arg0, int arg1, String arg2);

  Context superCreatePackageContext(String arg0, int arg1) throws
      PackageManager.NameNotFoundException;

  Context superCreateContextForSplit(String arg0) throws PackageManager.NameNotFoundException;

  Context superCreateConfigurationContext(Configuration arg0);

  Context superCreateDisplayContext(Display arg0);

  Context superCreateWindowContext(Display arg0, int arg1, Bundle arg2);

  Context superCreateWindowContext(int arg0, Bundle arg1);

  Context superCreateAttributionContext(String arg0);

  AttributionSource superGetAttributionSource();

  boolean superIsRestricted();

  Display superGetDisplay();

  Context superCreateDeviceProtectedStorageContext();

  boolean superIsDeviceProtectedStorage();

  boolean superIsUiContext();

  void superRegisterComponentCallbacks(ComponentCallbacks arg0);

  void superUnregisterComponentCallbacks(ComponentCallbacks arg0);

  void superSendBroadcastWithMultiplePermissions(Intent arg0, String[] arg1);

  void superAttachBaseContext(Context arg0);
}
