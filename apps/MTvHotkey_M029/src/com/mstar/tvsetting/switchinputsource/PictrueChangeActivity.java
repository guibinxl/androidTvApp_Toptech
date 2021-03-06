//<MStar Software>
//******************************************************************************
// MStar Software
// Copyright (c) 2010 - 2015 MStar Semiconductor, Inc. All rights reserved.
// All software, firmware and related documentation herein ("MStar Software") are
// intellectual property of MStar Semiconductor, Inc. ("MStar") and protected by
// law, including, but not limited to, copyright law and international treaties.
// Any use, modification, reproduction, retransmission, or republication of all
// or part of MStar Software is expressly prohibited, unless prior written
// permission has been granted by MStar.
//
// By accessing, browsing and/or using MStar Software, you acknowledge that you
// have read, understood, and agree, to be bound by below terms ("Terms") and to
// comply with all applicable laws and regulations:
//
// 1. MStar shall retain any and all right, ownership and interest to MStar
//    Software and any modification/derivatives thereof.
//    No right, ownership, or interest to MStar Software and any
//    modification/derivatives thereof is transferred to you under Terms.
//
// 2. You understand that MStar Software might include, incorporate or be
//    supplied together with third party's software and the use of MStar
//    Software may require additional licenses from third parties.
//    Therefore, you hereby agree it is your sole responsibility to separately
//    obtain any and all third party right and license necessary for your use of
//    such third party's software.
//
// 3. MStar Software and any modification/derivatives thereof shall be deemed as
//    MStar's confidential information and you agree to keep MStar's
//    confidential information in strictest confidence and not disclose to any
//    third party.
//
// 4. MStar Software is provided on an "AS IS" basis without warranties of any
//    kind. Any warranties are hereby expressly disclaimed by MStar, including
//    without limitation, any warranties of merchantability, non-infringement of
//    intellectual property rights, fitness for a particular purpose, error free
//    and in conformity with any international standard.  You agree to waive any
//    claim against MStar for any loss, damage, cost or expense that you may
//    incur related to your use of MStar Software.
//    In no event shall MStar be liable for any direct, indirect, incidental or
//    consequential damages, including without limitation, lost of profit or
//    revenues, lost or damage of data, and unauthorized system use.
//    You agree that this Section 4 shall still apply without being affected
//    even if MStar Software has been modified by MStar in accordance with your
//    request or instruction for your use, except otherwise agreed by both
//    parties in writing.
//
// 5. If requested, MStar may from time to time provide technical supports or
//    services in relation with MStar Software to you for your use of
//    MStar Software in conjunction with your or your customer's product
//    ("Services").
//    You understand and agree that, except otherwise agreed by both parties in
//    writing, Services are provided on an "AS IS" basis and the warranty
//    disclaimer set forth in Section 4 above shall apply.
//
// 6. Nothing contained herein shall be construed as by implication, estoppels
//    or otherwise:
//    (a) conferring any license or right to use MStar name, trademark, service
//        mark, symbol or any other identification;
//    (b) obligating MStar or any of its affiliates to furnish any person,
//        including without limitation, you and your customers, any assistance
//        of any kind whatsoever, or any information; or
//    (c) conferring any license or right under any intellectual property right.
//
// 7. These terms shall be governed by and construed in accordance with the laws
//    of Taiwan, R.O.C., excluding its conflict of law rules.
//    Any and all dispute arising out hereof or related hereto shall be finally
//    settled by arbitration referred to the Chinese Arbitration Association,
//    Taipei in accordance with the ROC Arbitration Law and the Arbitration
//    Rules of the Association by three (3) arbitrators appointed in accordance
//    with the said Rules.
//    The place of arbitration shall be in Taipei, Taiwan and the language shall
//    be English.
//    The arbitration award shall be final and binding to both parties.
//
//******************************************************************************
//<MStar Software>

package com.mstar.tvsetting.switchinputsource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Instrumentation;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mstar.android.MIntent;
import com.mstar.android.MKeyEvent;
import com.mstar.android.tv.TvAtscChannelManager;
import com.mstar.android.tv.TvChannelManager;
import com.mstar.android.tv.TvCommonManager;
import com.mstar.android.tv.TvIsdbChannelManager;
import com.mstar.android.tv.TvPipPopManager;
import com.mstar.android.tv.TvPvrManager;
import com.mstar.android.tv.TvS3DManager;
import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.dtv.atsc.vo.AtscMainListChannelInformation;
import com.mstar.tvsetting.R;
import com.mstar.tvsetting.hotkey.BroadcastRev;
import com.mstar.tvsetting.hotkey.InputSourceItem;
import com.mstar.tvsetting.hotkey.SwitchPageHelper;

public class PictrueChangeActivity extends Activity {
	public static final String TAG = "PictrueChangeActivity";

	private ArrayList<InputSourceItem> mGalleryItemList = new ArrayList<InputSourceItem>();

//	private InputSourceGalleryAdapter mInputSourceGalleryAdapter = null;

	private int mLastPosition = 0;

	private int mCurrentPosition = 0;

	private int[] mSourceListFlag = new int[TvCommonManager.INPUT_SOURCE_NUM];

	private static final int[] mSourceListInvisible = {
			TvCommonManager.INPUT_SOURCE_STORAGE,
			TvCommonManager.INPUT_SOURCE_KTV,
			TvCommonManager.INPUT_SOURCE_JPEG,
			TvCommonManager.INPUT_SOURCE_DTV2,
			TvCommonManager.INPUT_SOURCE_STORAGE2,
			TvCommonManager.INPUT_SOURCE_DIV3,
			TvCommonManager.INPUT_SOURCE_SCALER_OP,
			TvCommonManager.INPUT_SOURCE_RUV };


	private static final int[] mSourceListItemvisible = {
			TvCommonManager.INPUT_SOURCE_DTV,
			TvCommonManager.INPUT_SOURCE_ATV,
			TvCommonManager.INPUT_SOURCE_CVBS,
			TvCommonManager.INPUT_SOURCE_CVBS2,
			TvCommonManager.INPUT_SOURCE_CVBS3,
			TvCommonManager.INPUT_SOURCE_YPBPR,
			TvCommonManager.INPUT_SOURCE_VGA,
			TvCommonManager.INPUT_SOURCE_HDMI,
			TvCommonManager.INPUT_SOURCE_HDMI2,
			TvCommonManager.INPUT_SOURCE_HDMI3,
			TvCommonManager.INPUT_SOURCE_HDMI4,
			TvCommonManager.INPUT_SOURCE_STORAGE };


	private int mInputSource = TvCommonManager.INPUT_SOURCE_ATV;

	private int mAntennaType = TvChannelManager.DTV_ANTENNA_TYPE_NONE;

	private ArrayList<Integer> mKeyQueue = new ArrayList<Integer>();

	private static final int KEYQUEUE_SIZE = 4;

	// the password of entrying Design menu
	private static final String GOODKEYCODES = String
			.valueOf(KeyEvent.KEYCODE_2)
			+ String.valueOf(KeyEvent.KEYCODE_5)
			+ String.valueOf(KeyEvent.KEYCODE_8)
			+ String.valueOf(KeyEvent.KEYCODE_0);

	// the password of entrying factory menu
	private static final String FACTORYEYCODES = String
			.valueOf(KeyEvent.KEYCODE_1)
			+ String.valueOf(KeyEvent.KEYCODE_9)
			+ String.valueOf(KeyEvent.KEYCODE_7)
			+ String.valueOf(KeyEvent.KEYCODE_9);

	private static final int SETIS_START = -100;

	private static final int SETIS_END_COMPLETE = -101;

	private static final int FUNCTION_DISABLED = 0;

	private static final int CHANNELMANAGER_ATSC_ATV_ID = -1;


	private TvCommonManager mTvCommonmanager = null;

	private TvChannelManager mTvChannelManager = null;

	private TvS3DManager mTvS3DManager = null;

	private TvPvrManager mTvPvrManager = null;

	public TvPipPopManager mTvPipPopManager = null;

	private int mInputSourceOfRecording = 0;

	private AlertDialog mStopRecordDialog = null;

	private SourceViewBroadcastReceiver mSourceViewBroadcastReceiver = null;

	private ThreeDModeObserver mObserverThreeDMode = null;

	private boolean mIsSourceNeedSwitch = false;

	private boolean mIsChangingSource = false;

	private boolean mInitialPosition = false;

	private boolean mIsPreviewOn = false;

	private int mTvSystem = 0;

	private static final int IDX_POS_CABLE = 1;

	private static final int IDX_POS_AIR = 7;

	private RefreshPreviewModeWindowListener mRefreshPreviewModeWindowListener = null;

	private int mIndexOfUserDefineItem = 0;

	private ListView listView;
	
	private int[] typeFlag = { 3, 0, 1, 1, 6, 1, 1, 1, 1, 1, 1, -1, -1, -1, -1,
			-1, 5, 5, 5, 5, -1, -1, -1, 4, 4, 4, 4, 4, 0, -1, -1, -1 };

	// zb20151224 add for factory source switch
	private Intent intent = null;
	// end
	protected Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == SETIS_START) {
				mIsChangingSource = true;
				if (mSourceViewBroadcastReceiver != null) {
					PictrueChangeActivity.this
							.unregisterReceiver(mSourceViewBroadcastReceiver);
					mSourceViewBroadcastReceiver = null;
				}
			} else if (msg.what == SETIS_END_COMPLETE) {
				Intent intent;

				intent = new Intent(
						"mstar.tvsetting.ui.intent.action.RootActivity");
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//				intent.putExtra("task_tag", "input_source_changed");
//				intent.putExtra("inputAntennaType", mAntennaType);
				startActivity(intent);
				finish();
				mIsChangingSource = false;
			} else if (msg.what == LittleDownTimer.TIME_OUT_MSG) {
				finish();
			}
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//To hide the title bar function, used in more than Android4.0 of the phone, run well
		setContentView(R.layout.input_source_list_view_m029);
		mTvCommonmanager = TvCommonManager.getInstance();
		mTvChannelManager = TvChannelManager.getInstance();
		mTvS3DManager = TvS3DManager.getInstance();
		mTvPvrManager = TvPvrManager.getInstance();
		mTvPipPopManager = TvPipPopManager.getInstance();
		mInputSource = mTvCommonmanager.getCurrentTvInputSource();
		mObserverThreeDMode = new ThreeDModeObserver(handler);
		mTvSystem = mTvCommonmanager.getCurrentTvSystem();
		mRefreshPreviewModeWindowListener = new RefreshPreviewModeWindowListener();
		if (mRefreshPreviewModeWindowListener != null) {
			TvManager.getInstance().setOnRefreshPreviewModeWindowListener(
					mRefreshPreviewModeWindowListener);
		}
		// end
		initLittleDownCounter();
	}

	private void initLittleDownCounter() {
		LittleDownTimer.setHandler(handler);
		int value = TvCommonManager.getInstance().getOsdTimeoutInSecond();
		if (value < 1) {
			value = 5;
		}
		if (value > 30) {
			LittleDownTimer.stopMenu();
		} else {
			LittleDownTimer.setMenuStatus(value);
		}
		LittleDownTimer.getInstance().start();
	}

	// FIXME: Temp solution, modify later if DeskPipEventListener is ready.
	private class RefreshPreviewModeWindowListener implements
			TvManager.OnRefreshPreviewModeWindowListener {
		@Override
		public void onRefreshPreviewModeWindow(int nEvt, int wParam, int lParam) {
			if (lParam == TvPipPopManager.PREVIEW_MODE_PROCESS_SUCCESS) {
				PictrueChangeActivity.this.runOnUiThread(new Runnable() {
					public void run() {
						linkAdapter.notifyDataSetChanged();
						listView.invalidate();
					}
				});
			}
		}
	}

	@Override
	public void onResume() {
		Log.d(TAG, "onResume()");
		super.onResume();
		mIsPreviewOn = isSourcePreviewOn();
		mIsChangingSource = false;
		getContentResolver().registerContentObserver(
				Uri.parse("content://mstar.tv.usersetting/systemsetting"),
				true, mObserverThreeDMode);

		startAllRefreshImageThread();

		mInputSource = mTvCommonmanager.getCurrentTvInputSource();
		init();
		mSourceViewBroadcastReceiver = new SourceViewBroadcastReceiver();
		mKeyQueue.clear();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.mstar.tv.service.COMMON_EVENT_SIGNAL_STATUS_UPDATE");
		filter.addAction("com.mstar.tv.service.COMMON_EVENT_SIGNAL_AUTO_SWITCH");
		PictrueChangeActivity.this.registerReceiver(
				mSourceViewBroadcastReceiver, filter);

		if (isSourcePreviewSettingEnabled() == true) {
			if (mIsPreviewOn == false) {
				Toast toast = Toast
						.makeText(PictrueChangeActivity.this,
								R.string.str_source_preview_warning,
								Toast.LENGTH_SHORT);
				toast.show();
			}
		}
		LittleDownTimer.resumeMenu();
	}

	@Override
	public void onPause() {
		Log.d(TAG, "onPause()");
		if (mIsPreviewOn == true) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						mTvPipPopManager.disablePreviewMode();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}).start();
		}
		getContentResolver().unregisterContentObserver(mObserverThreeDMode);
		LittleDownTimer.pauseMenu();
		super.onPause();
		finish();
	}

	@Override
	public void onStart() {
		Log.d(TAG, "onStart()");
		super.onStart();
	}

	@Override
	public void onStop() {
		Log.d(TAG, "onStop()");
		super.onStop();
	}

	Handler refreshGalleryHandler = new Handler() {
		public void handleMessage(Message msg) {
			mIsPreviewOn = isSourcePreviewOn();
			linkAdapter.notifyDataSetChanged();
			listView.invalidate();
			super.handleMessage(msg);
		}
	};

	private InputSourceAdapter linkAdapter;

	private void startAllRefreshImageThread() {
		if (mIsPreviewOn == true) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						mTvPipPopManager.enablePreviewMode();
					} catch (Exception e) {
						Thread.currentThread().interrupt();
					}
				}
			}).start();
		}
	}

	private void getInputSourcelist() {
		int[] sourceList;

		sourceList = mTvCommonmanager.getSourceList();

		if (sourceList != null) {
			for (int i = 0; (i < sourceList.length)
					&& (TvCommonManager.INPUT_SOURCE_NUM > i); i++) {
				if (i == TvCommonManager.INPUT_SOURCE_ATV
						&& mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
					mSourceListFlag[i] = FUNCTION_DISABLED;
					continue;
				}
                if(SystemProperties.getBoolean("persist.sys.no_dtv",false))
                {
                	if(i == TvCommonManager.INPUT_SOURCE_DTV)
                	{	
                		mSourceListFlag[i] = FUNCTION_DISABLED;
                		continue;	
                	}
                }
				mSourceListFlag[i] = sourceList[i];
			}
		}
		for (int i = 0; (i < mSourceListInvisible.length)
				&& (i < mSourceListFlag.length); i++) {
			if (TvCommonManager.INPUT_SOURCE_NUM > mSourceListInvisible[i]) {
				mSourceListFlag[mSourceListInvisible[i]] = FUNCTION_DISABLED;
			}
		}

	}

	private void init() {
		String[] tmpData = null;
		InputSourceItem inputSourceItem;
		int PreviewSourceFlag = 0;
		int focusPosition = 0;
		int currentSource = 0;
		boolean[] sourceStatusList;

		getInputSourcelist();

		listView = (ListView) findViewById(R.id.ListView);
		if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
			tmpData = getResources().getStringArray(
					R.array.str_arr_atsc_input_source_vals);
		} else if (TvCommonManager.TV_SYSTEM_ISDB == mTvSystem) {
			tmpData = getResources().getStringArray(
					R.array.str_arr_input_source_vals);
		}else if (SystemProperties.getBoolean("mstar.cus.onida", false)) {
			tmpData = getResources().getStringArray(
					R.array.str_arr_input_source_vals_onida);
		}else {
			tmpData = getResources().getStringArray(
					R.array.str_arr_input_source_vals);
		}
		listView.setVerticalScrollBarEnabled(false);
		mGalleryItemList.clear();
		sourceStatusList = mTvCommonmanager.GetInputSourceStatus();

		/*
		 * Always display the ATV/DTV icon due to source detection function
		 * cannot support to get ATV/DTV status.
		 */
		sourceStatusList[TvCommonManager.INPUT_SOURCE_ATV] = true;
		sourceStatusList[TvCommonManager.INPUT_SOURCE_DTV] = true;

        if (!SystemProperties.getBoolean("mstar.tuner.exist", true)) {
            mSourceListFlag[TvCommonManager.INPUT_SOURCE_ATV] = FUNCTION_DISABLED;
            mSourceListFlag[TvCommonManager.INPUT_SOURCE_DTV] = FUNCTION_DISABLED;

            sourceStatusList[TvCommonManager.INPUT_SOURCE_ATV] = false;
		    sourceStatusList[TvCommonManager.INPUT_SOURCE_DTV] = false;
		} else if (SystemProperties.getBoolean("persist.sys.no_dtv", false)) {
            mSourceListFlag[TvCommonManager.INPUT_SOURCE_DTV] = FUNCTION_DISABLED;
            sourceStatusList[TvCommonManager.INPUT_SOURCE_DTV] = false;
		}
        if(SystemProperties.getBoolean("persist.sys.no_tv", false)) {
			mSourceListFlag[TvCommonManager.INPUT_SOURCE_ATV] = FUNCTION_DISABLED;
            sourceStatusList[TvCommonManager.INPUT_SOURCE_ATV] = false;
		}

		currentSource = mTvCommonmanager.getCurrentTvInputSource();
		
		
	    int subSource;
		for (int index = 0; index < mSourceListItemvisible.length; index++) {
		    subSource = mSourceListItemvisible[index];
			if (TvCommonManager.INPUT_SOURCE_NUM <= subSource) {
				break;
			}
            if (mSourceListFlag[subSource] == FUNCTION_DISABLED) {
				continue;
			}
			if (subSource == currentSource) {
				focusPosition = PreviewSourceFlag;
			} else {
				if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
					if (TvCommonManager.INPUT_SOURCE_ATV == mTvCommonmanager
							.getCurrentTvInputSource()) {
						if (subSource == TvCommonManager.INPUT_SOURCE_DTV) {
							focusPosition = PreviewSourceFlag;
						}
					}
				}
			}

			inputSourceItem = new InputSourceItem();
			if(subSource==TvCommonManager.INPUT_SOURCE_ATV){
			if (SystemProperties.getBoolean("persist.sys.no_dtv", false)) {
            mSourceListFlag[TvCommonManager.INPUT_SOURCE_DTV] = FUNCTION_DISABLED;
            sourceStatusList[TvCommonManager.INPUT_SOURCE_DTV] = false;
			tmpData[subSource]=getResources().getString(R.string.str_source_no_dtv);
			inputSourceItem.setInputSourceName(tmpData[subSource]);	
			}
			}
			if(subSource==TvCommonManager.INPUT_SOURCE_CVBS)
			{
				if(mTvCommonmanager.getSourceSupportStatus(TvCommonManager.INPUT_SOURCE_CVBS2))
					inputSourceItem.setInputSourceName(tmpData[subSource]);
				else
					inputSourceItem.setInputSourceName(getResources().getString(R.string.str_source_av));
			}
			else if(subSource==TvCommonManager.INPUT_SOURCE_HDMI)
			{
				if(mTvCommonmanager.getSourceSupportStatus(TvCommonManager.INPUT_SOURCE_HDMI2))
					inputSourceItem.setInputSourceName(tmpData[subSource]);
				else
					inputSourceItem.setInputSourceName(getResources().getString(R.string.str_source_hdmi));
			}
			else if(subSource==TvCommonManager.INPUT_SOURCE_YPBPR)
			{
				if(mTvCommonmanager.getSourceSupportStatus(TvCommonManager.INPUT_SOURCE_YPBPR2))
					inputSourceItem.setInputSourceName(tmpData[subSource]);
				else
					inputSourceItem.setInputSourceName(getResources().getString(R.string.str_source_ypbpr));
			}
			else	
				inputSourceItem.setInputSourceName(tmpData[subSource]);
			inputSourceItem.setPositon(subSource);
			if (sourceStatusList != null) {
				inputSourceItem.setSignalFlag(sourceStatusList[subSource]);
				if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
					if (sourceStatusList[TvCommonManager.INPUT_SOURCE_ATV] == true) {
						/*
						 * For ATSC system, soure list icon "TV" is used for ATV
						 * and DTV.
						 */
						sourceStatusList[TvCommonManager.INPUT_SOURCE_DTV] = true;
					}
				}
			}/*
			 * else { inputSourceItem.setSignalFlag(false); }
			 */

			inputSourceItem.setTypeFlag(typeFlag[subSource]);
			mGalleryItemList.add(inputSourceItem);

			PreviewSourceFlag++;
		}

		int tmp = appendUserDefineItems();
		if (tmp > 0)
		    focusPosition = tmp;

		PreviewSourceFlag = 0;
		getcurrentInputSourceItem();//hexing20160615 add for show defalt source error
		initListItem(focusPosition);

		if ((focusPosition < TvCommonManager.INPUT_SOURCE_VGA
				|| focusPosition > TvCommonManager.INPUT_SOURCE_DTV || mInitialPosition)
				&& (focusPosition != TvCommonManager.INPUT_SOURCE_VGA2)
				&& (focusPosition != TvCommonManager.INPUT_SOURCE_VGA3)) {
			mInitialPosition = false;
			mCurrentPosition = 0;
		} else {
			int focusIdx;

			focusIdx = focusPosition;
			if (TvCommonManager.TV_SYSTEM_ISDB == mTvSystem) {
				int currentInputSource = mTvCommonmanager
						.getCurrentTvInputSource();
				int currentAntennaType = TvIsdbChannelManager.getInstance()
						.getAntennaType();
				if (TvCommonManager.INPUT_SOURCE_ATV == currentInputSource
						|| TvCommonManager.INPUT_SOURCE_DTV == currentInputSource) {
					if (TvIsdbChannelManager.DTV_ANTENNA_TYPE_CABLE == currentAntennaType) {
						focusIdx = IDX_POS_CABLE;
					} else if (TvIsdbChannelManager.DTV_ANTENNA_TYPE_AIR == currentAntennaType) {
						focusIdx = IDX_POS_AIR;
					}
				}
			}
			mCurrentPosition = focusIdx;
		}
		/*
		 * Workaround for mantis 0879640. It seems be frameworks issue that
		 * system is in touch mode and mViewPager may NOT recieve first key
		 * event from IR. The following code could be removed if framework fixes
		 * this issue.
		 */
		new Instrumentation().setInTouchMode(false);

		checkInputSourceForUserDefineItems();

		// zb20151224 add
		intent = getIntent();
		if (intent != null) {
			String hotkey_Source = intent.getStringExtra("hotkey_source");
			int source_index = intent.getIntExtra("source_index", -1);

			if (hotkey_Source != null && hotkey_Source.equals("1")) {
				setToNextSource();
			}
		}
		// end
	}

	private void initListItem(int focusPos) {
		Log.d("lxk", " ----initListItem()----");

		linkAdapter = new InputSourceAdapter(this, mGalleryItemList, true);
		listView.setAdapter(linkAdapter);
		// listView.requestFocus();
        listView.setSelection(focusPos);
        linkAdapter.setFocusPosition(focusPos);
		listView.setOnItemClickListener(lvOnItemClickListener);
		listView.setOnItemSelectedListener(lvOnItemSelectedListener);
		righttosource();
		
	}

	private void righttosource() {
		// TODO Auto-generated method stub
		listView.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				switch (keyCode) {
				case KeyEvent.KEYCODE_DPAD_RIGHT:
					//handleOnItemSelected();
					break;

				default:
					break;
				}
				return false;
			}
		});
	}

	private OnItemClickListener lvOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			BroadcastRev.listViewValue = position;

			InputSourceItem item = (InputSourceItem) linkAdapter
					.getItem(position);

			changeSource(item.getPositon());
		}

	};

	private OnItemSelectedListener lvOnItemSelectedListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1,
				int position, long arg3) {
			LittleDownTimer.resumeMenu();
			Log.d(TAG, "============resume timer=========");
			linkAdapter.setFocusPosition(position);
			mCurrentPosition = position;
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		}
	};

	// zb20151224 add
	private void enterSource(int source) {
		updateSourceInputType(mInputSource);
		mTvCommonmanager.setInputSource(source);
		if (source == TvCommonManager.INPUT_SOURCE_ATV) {
			mTvChannelManager.selectProgram(
					mTvChannelManager.getCurrentChannelNumber(),
					TvChannelManager.SERVICE_TYPE_ATV);
		} else if (source == TvCommonManager.INPUT_SOURCE_DTV) {
			mTvChannelManager.selectProgram(
					mTvChannelManager.getCurrentChannelNumber(),
					TvChannelManager.SERVICE_TYPE_DTV);
		}
		intent = new Intent("mstar.tvsetting.ui.intent.action.RootActivity");
		intent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);
		intent.putExtra("task_tag", "input_source_changed");
		startActivity(intent);
		finish();
	}
	@SuppressWarnings("deprecation")
	private void changeSource(int idx) {
		if (idx == TvCommonManager.INPUT_SOURCE_STORAGE) {
			intent = new Intent(MIntent.ACTION_START_MEDIA_BROWSER);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
			PictrueChangeActivity.this.startActivity(intent);
			return;
		}
		if(idx == TvCommonManager.INPUT_SOURCE_NONE){
			ActivityManager manager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
			List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(3) ;
			if(runningTaskInfos.get(1).topActivity.getPackageName().equals("com.jrm.localmm")){
				finish();
				return ;
			}
		}
		for (int i = 0; i < mGalleryItemList.size(); i++) {

			if (mGalleryItemList.get(i).getPositon() == idx) {
				mCurrentPosition = i;
				handleOnItemSelected();
				break;
			}
		}
	}

	private void setToNextSource() {
		int curSource = mTvCommonmanager.getCurrentTvInputSource();
		for (int i = 0; i < mGalleryItemList.size(); i++) {
			if (curSource == mGalleryItemList.get(mGalleryItemList.size() - 2)
					.getPositon()) {
				mInputSource = mGalleryItemList.get(0).getPositon();
				enterSource(mInputSource);
				break;
			}

			if (curSource == mGalleryItemList.get(i).getPositon()) {
				mInputSource = mGalleryItemList.get(i + 1).getPositon();
				enterSource(mInputSource);
				break;
			}
		}

	}

	// end

	@Override
	protected void onDestroy() {
		if (mStopRecordDialog != null && mStopRecordDialog.isShowing()) {
			mStopRecordDialog.dismiss();
		}

		if (mSourceViewBroadcastReceiver != null) {
			this.unregisterReceiver(mSourceViewBroadcastReceiver);
			mSourceViewBroadcastReceiver = null;
		}
		LittleDownTimer.stopMenu();
		super.onDestroy();
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		LittleDownTimer.resumeMenu();

		// wxl 2016/3/10 add for source menu cyclic switch
		int count = linkAdapter.getCount() - 1;
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			if (mLastPosition == 0) {
				listView.setSelection(count);
				linkAdapter.setFocusPosition(count);
				linkAdapter.notifyDataSetChanged();
				listView.invalidate();
				return true;
			}
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			if (mLastPosition == 0) {
				listView.setSelection(0);
				linkAdapter.setFocusPosition(0);
				linkAdapter.notifyDataSetChanged();
				listView.invalidate();
				return true;
			}
		}
		// end

		if (mapKeyPadToIR(keyCode, event)) {
			return true;
		}

		// don't handle this key when changing source
		if (mIsChangingSource && keyCode == MKeyEvent.KEYCODE_TV_SETTING) {
			return true;
		}

		if (keyCode == KeyEvent.KEYCODE_ENTER) {
			handleOnItemSelected();
			return true;
		}

		if (keyCode == MKeyEvent.KEYCODE_LIST) {
			Intent intent = new Intent(
					"com.mstar.tvsetting.ui.intent.action.ChannelActivity");
			intent.putExtra("ListId", 2);
			startActivity(intent);
			finish();
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_S
				|| keyCode == KeyEvent.KEYCODE_TV_INPUT) {
			// int position = listView.getSelectedItemPosition();
			// listView.performItemClick(listView.getChildAt(position),
			// position, listView.getItemIdAtPosition(position));
			finish();
			return true;
		}
//		if (SwitchPageHelper.goToMenuPage(this, keyCode) == true) {
//			return true;
//		} else 
		if (SwitchPageHelper.goToSourceInfo(this, keyCode) == true) {
			return true;
		} 

		mKeyQueue.add(keyCode);
		if (mKeyQueue.size() == KEYQUEUE_SIZE) {
			String keystr = intArrayListToString(mKeyQueue);
			if (keystr.equals(GOODKEYCODES)) {
				if (false == ActivityManager.isUserAMonkey()) {
					Intent intent;

					mKeyQueue.clear();
					intent = new Intent(
							"mstar.tvsetting.factory.intent.action.MainmenuActivity");
					startActivity(intent);
					finish();
					return true;
				}
			} else if (keystr.equals(FACTORYEYCODES)) {
				if (false == ActivityManager.isUserAMonkey()) {
					Intent intent;

					mKeyQueue.clear();
					intent = new Intent(
							"mstar.tvsetting.factory.intent.action.FactorymenuActivity");
					startActivity(intent);
					finish();
					return true;
				}
			} else {
				mKeyQueue.remove(0);
			}
		}

		return super.onKeyDown(keyCode, event);
	}

	private Boolean mapKeyPadToIR(int keyCode, KeyEvent event) {
		String deviceName = InputDevice.getDevice(event.getDeviceId())
				.getName();
		Log.d(TAG, "deviceName: " + deviceName);
		if (!deviceName.equals("MStar Smart TV Keypad")) {
			return false;
		}
		switch (keyCode) {
		case KeyEvent.KEYCODE_CHANNEL_UP:
			keyInjection(KeyEvent.KEYCODE_DPAD_UP);
			return true;
		case KeyEvent.KEYCODE_CHANNEL_DOWN:
			keyInjection(KeyEvent.KEYCODE_DPAD_DOWN);
			return true;
		case KeyEvent.KEYCODE_VOLUME_UP:
			keyInjection(KeyEvent.KEYCODE_DPAD_RIGHT);
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			keyInjection(KeyEvent.KEYCODE_DPAD_LEFT);
			return true;
		case KeyEvent.KEYCODE_TV_INPUT:
			keyInjection(KeyEvent.KEYCODE_ENTER);
			return true;
		default:
			return false;
		}
	}

	private void keyInjection(final int keyCode) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN
				|| keyCode == KeyEvent.KEYCODE_DPAD_UP
				|| keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
				|| keyCode == KeyEvent.KEYCODE_DPAD_LEFT
				|| keyCode == KeyEvent.KEYCODE_ENTER) {
			new Thread() {
				public void run() {
					try {
						Instrumentation inst = new Instrumentation();
						inst.sendKeyDownUpSync(keyCode);
					} catch (Exception e) {
						Log.e(TAG, e.toString());
					}
				}
			}.start();
		}
	}

	private String intArrayListToString(ArrayList<Integer> al) {
		String str = "";
		for (int i = 0; i < al.size(); ++i) {
			str += al.get(i).toString();
		}
		return str;
	}

	private class OnStopRecordCancelClickListener implements
			DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (mStopRecordDialog != null && mStopRecordDialog.isShowing()) {
				mStopRecordDialog.dismiss();
			}
			mInputSourceOfRecording = mTvCommonmanager
					.getCurrentTvInputSource();
			Log.i(TAG, "OnStopRecordCancelClickListener onClick");
		}
	}

	private class OnStopRecordConfirmClickListener implements
			DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (mStopRecordDialog != null && mStopRecordDialog.isShowing()) {
				mStopRecordDialog.dismiss();
			}
			mTvPvrManager.stopRecord();

			if (mGalleryItemList.get(mInputSourceOfRecording)
					.isUserDefineItem() == true) {
				if (mGalleryItemList.get(mInputSourceOfRecording)
						.getUserDefineItemType() == InputSourceItem.USER_DEFINE_ITEM_TYPE_FILEBROWSER) {
					startFileBrowser();
				}
			} else {
				changeInputSource(mInputSource, mInputSourceOfRecording);
			}
			dialog.dismiss();
		}
	}

	private boolean showmStopRecordDialog() {
		boolean bRet = true;

		do {
			if (mStopRecordDialog != null && mStopRecordDialog.isShowing()) {
				Log.e(TAG, "mStopRecordDialog already exist");
				bRet = false;
				break;
			}
			AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
			dialogBuilder = dialogBuilder
					.setTitle(R.string.str_stop_record_dialog_title);
			dialogBuilder = dialogBuilder
					.setMessage(R.string.str_stop_record_dialog_message);
			dialogBuilder = dialogBuilder.setPositiveButton(
					R.string.str_stop_record_dialog_confirm,
					new OnStopRecordConfirmClickListener());
			dialogBuilder = dialogBuilder.setNegativeButton(
					R.string.str_stop_record_dialog_cancel,
					new OnStopRecordCancelClickListener());
			if (dialogBuilder == null) {
				Log.e(TAG,
						"showmStopRecordDialog -- AlertDialog.Builder init fail");
				bRet = false;
				break;
			}
			mStopRecordDialog = dialogBuilder.create();
			if (mStopRecordDialog == null) {
				Log.e(TAG,
						"showmStopRecordDialog -- AlertDialog.Builder create dialog fail");
				bRet = false;
				break;
			}
			mStopRecordDialog.show();
		} while (false);
		return bRet;
	}

	// Mute Receiver Receive Broadcast form system
	private class SourceViewBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.d(TAG, "onReceive(), action = " + action);
			// TODO: Do not use hard coded action name
			/*
			 * The broadcasts are related to Source Auto Switch setting status:
			 * 
			 * OFF: SN sends the only one notification to notify the cable
			 * status changed: COMMON_EVENT_SIGNAL_STATUS_UPDATE ON: SN sends
			 * the two kinds of notification to indicate the cable status:
			 * COMMON_EVENT_SIGNAL_AUTO_SWITCH => for cable plug in
			 * COMMON_EVENT_SIGNAL_PLUG_OUT => for cable plug out
			 */
			if (action
					.equals("com.mstar.tv.service.COMMON_EVENT_SIGNAL_STATUS_UPDATE")
					|| action
							.equals("com.mstar.tv.service.COMMON_EVENT_SIGNAL_AUTO_SWITCH")
					|| action
							.equals("com.mstar.tv.service.COMMON_EVENT_SIGNAL_PLUG_OUT")) {
				int sourceIndex = intent.getIntExtra("SwitchSourceIndex",
						TvCommonManager.INPUT_SOURCE_NONE);
				if (TvCommonManager.INPUT_SOURCE_NONE != sourceIndex) {
					boolean[] sourceStatusList = mTvCommonmanager
							.GetInputSourceStatus();
					if (null != sourceStatusList) {
						final int nSize = mGalleryItemList.size();
						for (int index = 0; index < nSize; index++) {
							if (mGalleryItemList.get(index).getPositon() == sourceIndex) {
								mGalleryItemList.get(index).setSignalFlag(
										sourceStatusList[sourceIndex]);

								// Update status and icon of the given input
								// source

								// Refresh Screen

								linkAdapter.notifyDataSetChanged();
								listView.invalidate();
								break;
							}
						}
					}
				}
			}
		}
	}

	private class ThreeDModeObserver extends ContentObserver {
		private int systemAutoTime = 0;

		public ThreeDModeObserver(Handler handler) {
			super(handler);
		}

		public void onChange(boolean selfChange) {
			if (!mIsSourceNeedSwitch) {
				return;
			}
			mIsSourceNeedSwitch = false;
			if (systemAutoTime > 0) {
				Settings.Global.putInt(getContentResolver(),
						Settings.Global.AUTO_TIME, systemAutoTime);
			}
			new Thread(new Runnable() {
				@Override
				public void run() {
					// disable dualview
					if ((mTvPipPopManager.isPipModeEnabled() == true)
							&& (mTvPipPopManager.getCurrentPipMode() == TvPipPopManager.E_PIP_MODE_POP)) {
						int formatType = mTvS3DManager.getCurrent3dType();
						if (formatType == TvS3DManager.THREE_DIMENSIONS_TYPE_DUALVIEW) {
							mTvPipPopManager.disable3dDualView();
						}
					}
					if (mInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
						try {
							systemAutoTime = Settings.Global.getInt(
									getContentResolver(),
									Settings.Global.AUTO_TIME);
						} catch (SettingNotFoundException e) {
							systemAutoTime = 0;
						}
						if (systemAutoTime > 0) {
							Settings.Global.putInt(getContentResolver(),
									Settings.Global.AUTO_TIME, 0);
						}
					}
					Log.i(TAG,
							"startActivity SOURCE_CHANGE intent: inputSource = "
									+ mInputSource);
					Intent intent = new Intent(MIntent.ACTION_START_TV_PLAYER);
					intent.putExtra("inputSrc", mInputSource);
					intent.putExtra("inputAntennaType", mAntennaType);
					startActivity(intent);
					handler.sendEmptyMessage(SETIS_END_COMPLETE);
				}
			}).start();
		}
	}

	private boolean isSourcePreviewSettingEnabled() {
		int result = mTvCommonmanager.getSourcePreviewState();

		return (result == 1 ? true : false);
	}

	private boolean isSourcePreviewOn() {
		if (mTvPipPopManager.isPipModeEnabled() == true) {
			if ((mTvPipPopManager.getCurrentPipMode() == TvPipPopManager.E_PIP_MODE_PIP)
					|| (mTvPipPopManager.getCurrentPipMode() == TvPipPopManager.E_PIP_MODE_POP)
					|| (mTvPipPopManager.getCurrentPipMode() == TvPipPopManager.E_PIP_MODE_TRAVELING)) {
				return false;
			}
		}
		return isSourcePreviewSettingEnabled();
	}

	private void updateSourceInputType(int inputSourceTypeIdex) {
		long ret = -1;

		ContentValues vals = new ContentValues();
		vals.put("enInputSourceType", inputSourceTypeIdex);
		try {
			ret = getContentResolver().update(
					Uri.parse("content://mstar.tv.usersetting/systemsetting"),
					vals, null, null);
		} catch (SQLException e) {
		}
		if (ret == -1) {
			Log.i(TAG, "update tbl_PicMode_Setting ignored");
		}
	}

	private void executePreviousTask(final int position) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				mTvS3DManager
						.setDisplayFormatForUI(TvS3DManager.THREE_DIMENSIONS_DISPLAY_FORMAT_NONE);
				int inputSource = mGalleryItemList.get(position).getPositon();
				Log.i(TAG, "startActivity SOURCE_CHANGE intent: inputSource = "
						+ inputSource);
				if (inputSource == TvCommonManager.INPUT_SOURCE_DTV) {
					if (TvCommonManager.TV_SYSTEM_ISDB == mTvSystem) {
						TvIsdbChannelManager.getInstance().setAntennaType(
								TvIsdbChannelManager.DTV_ANTENNA_TYPE_AIR);
					}
					if (TvCommonManager.TV_SYSTEM_ATSC == mTvSystem) {
						AtscMainListChannelInformation info = TvAtscChannelManager
								.getInstance().getCurrentChannelInformation();
						if (info != null) {
							if (info.progId == CHANNELMANAGER_ATSC_ATV_ID) {
								inputSource = TvCommonManager.INPUT_SOURCE_ATV;
							}
						}
					}
				}
				Intent intent = new Intent(MIntent.ACTION_START_TV_PLAYER);
				intent.putExtra("inputSrc", inputSource);
				intent.putExtra("inputAntennaType", mAntennaType);
				startActivity(intent);
				try {
					Intent targetIntent;

					targetIntent = new Intent(
							"mstar.tvsetting.ui.intent.action.RootActivity");
					targetIntent.putExtra("task_tag", "input_source_changed");
					targetIntent.putExtra("no_change_source", true);
					if (targetIntent != null)
						startActivity(targetIntent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void changeInputSource(int inpSource, int position) {
		int curIntSource = mTvCommonmanager.getCurrentTvInputSource();

		mInputSource = inpSource;

		if ((curIntSource >= TvCommonManager.INPUT_SOURCE_STORAGE)
				&& (curIntSource != TvCommonManager.INPUT_SOURCE_VGA2)
				&& (curIntSource != TvCommonManager.INPUT_SOURCE_VGA3)) {
			Intent source_switch_from_storage = new Intent(
					"source.switch.from.storage");
			sendBroadcast(source_switch_from_storage);
			handler.sendEmptyMessage(SETIS_START);
			executePreviousTask(position);
		} else {
			new Thread(new Runnable() {
				@Override
				public void run() {
					// to ensure input-source is switched after database has
					// been saved, we'll do it later
					handler.sendEmptyMessage(SETIS_START);
					mIsSourceNeedSwitch = true;
					updateSourceInputType(mInputSource);
				}
			}).start();
		}
	}

	private int appendUserDefineItems() {
	    int desiredFocusPostion = 0;
		mIndexOfUserDefineItem = mGalleryItemList.size();

		if (!SystemProperties.getBoolean("persist.sys.no_usb", false)) {
    		InputSourceItem inputSourceItem = new InputSourceItem();
    		inputSourceItem.setInputSourceName(getResources().getString(
    				R.string.str_media_browser));

    		mSourceListFlag = Arrays.copyOf(mSourceListFlag,
    				mSourceListFlag.length + 1);
    		mSourceListFlag[mSourceListFlag.length - 1] = 1;

    		// Since it's not one of the TV input sources
    		inputSourceItem.setPositon(TvCommonManager.INPUT_SOURCE_NONE);

    		inputSourceItem
    				.setUserDefineItemType(InputSourceItem.USER_DEFINE_ITEM_TYPE_FILEBROWSER);

    		inputSourceItem.setTypeFlag(7);

    		mGalleryItemList.add(inputSourceItem);
    		if (mTvCommonmanager.getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_STORAGE)
    		    desiredFocusPostion = mGalleryItemList.size() - 1;
		}
		return desiredFocusPostion;
	}
	private void getcurrentInputSourceItem()//hexing20160615 add for show defalt source error
	{
		int curIntSource = mTvCommonmanager.getCurrentTvInputSource();
		
		int currentItem = 0;

		for (int i = 0; (i <= mSourceListItemvisible.length); i++)
			{
			if ((mTvCommonmanager.getSourceSupportStatus(mSourceListItemvisible[i]))||(mSourceListItemvisible[i]==TvCommonManager.INPUT_SOURCE_VGA))
				{
				
				if(SystemProperties.getBoolean("persist.sys.no_dtv",false))
					{
					if(mSourceListItemvisible[i]== TvCommonManager.INPUT_SOURCE_DTV)
            			continue;	
					}
				
				if(SystemProperties.getBoolean("persist.sys.no_atv",false))
				{
				if(mSourceListItemvisible[i]== TvCommonManager.INPUT_SOURCE_ATV)
        			continue;	
				}
				
				currentItem ++ ;
				if(curIntSource == mSourceListItemvisible[i])
					break;
				
				}
			}
		
		BroadcastRev.listViewValue=	currentItem -1;	
	}

	private void checkInputSourceForUserDefineItems() {
		if (mTvCommonmanager.getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_STORAGE) {
			/*
			 * if (mShowMode == 0) {
			 * mViewPager.setCurrentItem(mIndexOfUserDefineItem);
			 * mInputSourceGalleryAdapter.setPosition(mIndexOfUserDefineItem); }
			 */
			mCurrentPosition = mIndexOfUserDefineItem;
		}
	}

	private void handleOnItemSelected() {
		int position = mCurrentPosition;

		Log.d(TAG, "handleOnItemSelected(), position = " + position);

		if (mGalleryItemList.get(position).isUserDefineItem() == true) {
			if (mGalleryItemList.get(position).getUserDefineItemType() == InputSourceItem.USER_DEFINE_ITEM_TYPE_FILEBROWSER) {
				if (mTvSystem != TvCommonManager.TV_SYSTEM_ATSC) {
					boolean bRecordNow = mTvPvrManager.isRecording();

					if (bRecordNow) {
						mInputSourceOfRecording = position;
						showmStopRecordDialog();
						return;
					}
				}
				startFileBrowser();
				return;
			}
		}
		mInputSource = mGalleryItemList.get(position).getPositon();
		Log.d(TAG, "handleOnItemSelected(), input source = " + mInputSource);

		if (TvCommonManager.TV_SYSTEM_ISDB == mTvSystem) {
			if (IDX_POS_AIR == mCurrentPosition) {
				mAntennaType = TvIsdbChannelManager.DTV_ANTENNA_TYPE_AIR;
			} else if (IDX_POS_CABLE == mCurrentPosition) {
				mAntennaType = TvIsdbChannelManager.DTV_ANTENNA_TYPE_CABLE;
			}
		}

		if (mInputSource != mTvCommonmanager.getCurrentTvInputSource()) {
			if (mTvSystem == TvCommonManager.TV_SYSTEM_ATSC) {
				if ((mInputSource == TvCommonManager.INPUT_SOURCE_DTV)
						&& (mTvCommonmanager.getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_ATV)) {
					handler.sendEmptyMessage(SETIS_END_COMPLETE);
				} else {
					if (mInputSource == TvCommonManager.INPUT_SOURCE_DTV) {
						AtscMainListChannelInformation info = TvAtscChannelManager
								.getInstance().getCurrentChannelInformation();
						if (info != null) {
							if (info.progId == CHANNELMANAGER_ATSC_ATV_ID) {
								mInputSource = TvCommonManager.INPUT_SOURCE_ATV;
							}
						}
					}
					changeInputSource(mInputSource, position);
				}
			} else {
				boolean bRecordNow = false;
				bRecordNow = mTvPvrManager.isRecording();

				if (bRecordNow
						&& (mTvCommonmanager.getCurrentTvInputSource() == TvCommonManager.INPUT_SOURCE_DTV)) {
					mInputSourceOfRecording = position;
					showmStopRecordDialog();
				} else {
					changeInputSource(mInputSource, position);
				}
			}
		} else {
			handler.sendEmptyMessage(SETIS_END_COMPLETE);
		}
	}

	private void startFileBrowser() {
		Intent intent;
		intent = new Intent(MIntent.ACTION_START_MEDIA_BROWSER);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		PictrueChangeActivity.this.startActivity(intent);
	}
}
