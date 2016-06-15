package com.mintcode.launchr.base;

import java.util.Locale;

import com.mintcode.launchr.R;
import com.mintcode.launchr.consts.LauchrConst;
import com.mintcode.launchr.util.Const;
import com.mintcode.launchr.util.CustomToast;
import com.mintcode.launchr.view.DialogLoading;
import com.mintcode.launchr.view.WorkHubDialogLoading;
import com.mintcode.launchrnetwork.OnResponseListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

/**
 * 基础fragment，所有fragment继承它
 *
 * @author KevinQiao
 */
public class BaseFragment extends Fragment implements OnResponseListener, OnClickListener {

    /**
     * loading
     */
    private DialogLoading mLoading;

    private WorkHubDialogLoading mWorkHubLoading;

    protected String loginName;

    protected String toLoginName;

    private boolean mIsRunning = false;


    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getToLoginName() {
        return toLoginName;
    }

    public void setToLoginName(String toLoginName) {
        this.toLoginName = toLoginName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Const.mConfig = getResources().getConfiguration();
        initView();

    }

    @Override
    public void onResume() {
        super.onResume();
        startRunning();
    }

    @Override
    public void onPause() {
        super.onPause();
        pauseRunning();
    }

    private void startRunning() {
        mIsRunning = true;
    }

    private void pauseRunning() {
        mIsRunning = false;
    }

    public boolean isRunning() {
        return mIsRunning;
    }

    /**
     * 实例化view
     */
    private void initView() {


        if (mWorkHubLoading == null) {
            mWorkHubLoading = WorkHubDialogLoading.creatDialog(getActivity());
        }
//		if(LauchrConst.IS_WORKHUB && mWorkHubLoading==null){
//			mWorkHubLoading = WorkHubDialogLoading.creatDialog(getActivity());
//		}else if (mLoading == null) {
//			mLoading = DialogLoading.creatDialog(getActivity());
//		}
    }

    /**
     * 显示loading框
     */
    public void showLoading() {
        if (getActivity() != null && !getActivity().isFinishing()) {


            if (mWorkHubLoading != null) {
                if (!mWorkHubLoading.isShowing()) {
                    mWorkHubLoading.show();
                    mWorkHubLoading.start();
                }

            }
//			if(LauchrConst.IS_WORKHUB && mWorkHubLoading!=null){
//				if(!mWorkHubLoading.isShowing()){
//					mWorkHubLoading.show();
//					mWorkHubLoading.start();
//				}
//			}else if (mLoading != null) {
//				if (!mLoading.isShowing()) {
////					if(mLoading.getContext() != null){
//						mLoading.show();
//						mLoading.start();
////					}
//				}
//			}
        }
    }

    /**
     * 隐藏loading
     */
    public void dismissLoading() {
        if (getActivity() != null && !getActivity().isFinishing()) {
            if (mWorkHubLoading != null) {
                if (mWorkHubLoading.isShowing()) {
                    mWorkHubLoading.dismiss();
                }
            } else if (mLoading != null) {
                if (mLoading.isShowing()) {
                    mLoading.dismiss();
                }
            }
//			if(mWorkHubLoading!=null){
//				if(mWorkHubLoading.isShowing()){
//					mWorkHubLoading.dismiss();
//				}
//			}else if (mLoading != null) {
//				if (mLoading.isShowing()) {
//					mLoading.dismiss();
//				}
//			}
        }

    }

    /**
     * 弹出提示
     *
     * @param text
     */
    public void toast(String text) {
        if ((text != null) && !text.equals("")) {
            if (getActivity() != null) {
                CustomToast.showToast(getActivity(), text, 2000);
            }
        }
    }

    @Override
    public void onResponse(Object response, String taskId, boolean rawData) {

    }

    @Override
    public boolean isDisable() {
        return false;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    /**
     * 数据获取异常提示
     *
     * @return
     */
    public void showNetWorkGetDataException() {
        if (getActivity() != null) {
            String str = getString(R.string.network_exception);
            toast(str);
        }
    }

    /**
     * 无网络访问提示
     */
    public void showNoNetWork() {
        if (getActivity() != null) {
            String str = getString(R.string.no_network);
            toast(str);
        }
    }

}
