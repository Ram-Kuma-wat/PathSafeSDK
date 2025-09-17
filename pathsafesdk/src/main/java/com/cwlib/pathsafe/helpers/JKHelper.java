package com.cwlib.pathsafe.helpers;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
  import com.cwlib.pathsafe.config.ETSConfigs;
import com.cwlib.pathsafe.listeners.OnAuthListener;
import com.cwlib.pathsafe.beans.AccountInfo;
import com.cwlib.pathsafe.beans.LoginBean;
import com.cwlib.pathsafe.rest.ApiCall;
import com.cwlib.pathsafe.rest.OnResponse;
import com.cwlib.pathsafe.rest.UniverSelObjct;
import com.cwlib.pathsafe.utils.AppUrls;
import com.cwlib.pathsafe.utils.CommonMethods;
import com.cwlib.pathsafe.utils.SFProgress;
import com.cwlib.pathsafe.R;
import com.google.gson.Gson;
import com.ttlock.bl.sdk.util.DigitUtil;

/**
 * Created by Mr.Mad on 02/02/2023.
 */
public class JKHelper extends Application implements OnResponse<UniverSelObjct> {
    Activity mActivity;
    int fromType = 0;
     OnAuthListener mListener;
    int mAuthCount = 0;

    public void makeTTAuthentication(Activity ctx, int fromType, OnAuthListener mListener) {
        this.mActivity = ctx;
        this.mListener = mListener;
        this.fromType = fromType;
        new ApiCall(ctx).ttlockAuth(this);
    }

    public void updateLockData(Activity ctx,String lockData,String lockMac, String lockId) {
        this.mActivity = ctx;
        LoginBean.InfoBean mBeanUser = UserSessions.getUserInfo(ctx);
        //changed to 12
        String params = AppUrls.PSX_UPDATE_LOCK_DATA + "&type=12&userdetailid=" + mBeanUser.getUserDetailId() + "&lockdata=" + lockData+ "&lockMac=" + lockMac+ "&lockId=" + lockId;
        ETSConfigs mETSConfigs = new ETSConfigs();
        String encParam = mETSConfigs.etsEncryption(mActivity, params);
        new ApiCall(ctx).callApi(this, false,encParam,AppUrls.PSX_UPDATE_LOCK_DATA);
    }

    @Override
    public void onSuccess(UniverSelObjct response) {
        try {
            switch (response.getMethodname()) {
                case AppUrls.PSX_API_TTLOCK_AUTH_TOKEN:
                    try {
                        Boolean isError = true;
                        String msg = "";
                        AccountInfo mAccountInfo = (AccountInfo) response.getResponse();
                        if (mAccountInfo != null) {
                            if (mAccountInfo.getErrcode() == 0) {
                                mAccountInfo.setMd5Pwd(DigitUtil.getMD5("Pss@1987"));
                                String str = new Gson().toJson(mAccountInfo);
                                UserSessions.saveTTAccountInfo(mActivity, str);
                                if (fromType == 1) {//
                                    // startActivity(Intent(this@LoginActivity, UserLockActivity::class.java))
                                } else if (fromType == 2) {//
                                    //startActivity(Intent(this@LoginActivity, GuestRoomList::class.java))
                                } /*else if (fromType == 3) {//Login Screen with no action
                                   // CommonMethods.moveWithClear(mActivity, LocksActivity.class);
                                }*/ else if (mListener != null) {
                                    mListener.onAuth(mAccountInfo);
                                }
                                isError = false;
                            } else {
                                msg = (CommonMethods.isValidString(mAccountInfo.getErrmsg())) ? mAccountInfo.getErrmsg() : mActivity.getString(R.string.error_tt_auth);
                            }
                        } else {
                            msg = (CommonMethods.isValidString(response.getMsg())) ? response.getMsg() : mActivity.getString(R.string.error_tt_auth);
                        }
                        if (isError) {
                            if (mAuthCount < 2) {
                                mAuthCount++;
                                makeTTAuthentication(mActivity, fromType, mListener);
                            } else {
                                if (fromType == 3 || fromType == 4) {
                                    if (mListener != null) {
                                        mListener.onAuth(mAccountInfo);
                                    }
                                } else {
                                    CommonMethods.errorDialog(mActivity, (CommonMethods.isValidString(msg)) ? msg : mActivity.getString(R.string.error_tt_auth), mActivity.getString(R.string.app_name), mActivity.getString(R.string.media_picker_ok));
                                }
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(String type, String error) {
        try {
            switch (type) {
                case AppUrls.PSX_API_TTLOCK_AUTH_TOKEN:
                    try {
                        if (mAuthCount < 2) {
                            mAuthCount++;
                            makeTTAuthentication(mActivity, fromType, mListener);
                        } else {
                            if (fromType == 3 || fromType == 4) {
                                if (mListener != null) {
                                    mListener.onAuth(null);
                                }
                            } else {
                                CommonMethods.errorDialog(mActivity, mActivity.getString(R.string.error_tt_auth), mActivity.getString(R.string.app_name), mActivity.getString(R.string.media_picker_ok));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
