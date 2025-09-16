package com.cwlib.pathsafe.rest;

import android.app.Activity;
import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cwlib.pathsafe.beans.AccountInfo;
import com.cwlib.pathsafe.beans.DeviceDetailBean;
import com.cwlib.pathsafe.config.AESHelper;
import com.cwlib.pathsafe.helpers.UserSessions;
 import com.cwlib.pathsafe.beans.LoginBean;
import com.cwlib.pathsafe.utils.AppUrls;
import com.cwlib.pathsafe.utils.SFProgress;
import com.cwlib.pathsafe.R;
import com.google.gson.Gson;
import com.ttlock.bl.sdk.util.DigitUtil;

import java.util.HashMap;
import java.util.Map;

 import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import org.json.JSONObject;

public class ApiCall {
    public Activity mContext = null;
    public UserSessions mUserSessions = null;

    public ApiCall(Context applicationContext) {
    }
    public ApiCall(Activity ctx) {
        this.mContext = ctx;
        mUserSessions = new UserSessions(mContext);
    }
    public void userLogin(OnResponse<UniverSelObjct> onResponse, String strParams) {
        try {
            SFProgress.showProgressDialog(mContext, true);
        } catch (Exception e) {
        }
        ApiRequest mRequest = RetrofitRequest.getRetrofitInstance(1, 2).create(ApiRequest.class);
        mRequest.callApi(strParams).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    SFProgress.hideProgressDialog(mContext);
                } catch (Exception e) {
                }
                try {
                    if (response != null) {
                        try {
                            String strResp = new AESHelper().etsDecryption(response.body().toString(), mContext);

                             LoginBean mBean = new Gson().fromJson(strResp, LoginBean.class);
                             onResponse.onSuccess(new UniverSelObjct(mBean, AppUrls.PSX_LOGIN_API, "true", ""));
                        } catch (Exception e) {
                            e.printStackTrace();
                            onResponse.onError(AppUrls.PSX_LOGIN_API, mContext.getResources().getString(R.string.something_wrong));
                        }
                    } else {
                        onResponse.onError(AppUrls.PSX_LOGIN_API, mContext.getResources().getString(R.string.something_wrong));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                try {
                    SFProgress.hideProgressDialog(mContext);
                } catch (Exception e) {
                }
                t.printStackTrace();
                onResponse.onError(AppUrls.PSX_LOGIN_API, mContext.getResources().getString(R.string.something_wrong));
            }
        });
    }
    public void callApi(OnResponse<UniverSelObjct> onResponse, Boolean isTrue, String params, String apiType) {
        if (isTrue) {
            try {
                SFProgress.showProgressDialog(mContext, true);
            } catch (Exception e) {
            }
        }
        ApiRequest mRequest = RetrofitRequest.getRetrofitInstance(1,2).create(ApiRequest.class);
        mRequest.callApi(params).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (isTrue) {
                    try {
                        SFProgress.hideProgressDialog(mContext);
                    } catch (Exception e) {
                    }
                }
                try {
                    if (response != null) {
                        try {
                            onResponse.onSuccess(new UniverSelObjct(response.body(), apiType, "true", ""));
                        } catch (Exception e) {
                            e.printStackTrace();
                            onResponse.onError(apiType, mContext.getResources().getString(R.string.something_wrong));
                        }
                    } else {
                        onResponse.onError(apiType, mContext.getResources().getString(R.string.something_wrong));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    onResponse.onError(apiType, mContext.getResources().getString(R.string.something_wrong));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if (isTrue) {
                    try {
                        SFProgress.hideProgressDialog(mContext);
                    } catch (Exception e) {
                    }
                }
                t.printStackTrace();
                try{
                    onResponse.onError(apiType, mContext.getResources().getString(R.string.something_wrong));
                }catch (Exception ex){

                }
            }
        });
    }

    public void getLockData(OnResponse<UniverSelObjct> onResponse,  String... params) {
        StringRequest postRequest = new StringRequest(Request.Method.POST, AppUrls.BASE_URL_TTLOCK1+AppUrls.PSX_API_TTLOCK_GET_LOCKDATA,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String res = response;
                        try {
                            JSONObject obj = new JSONObject(response);
                             if (obj.has("lockData")){
                                 DeviceDetailBean mBean = new Gson().fromJson(response,DeviceDetailBean.class);
                                 onResponse.onSuccess(new UniverSelObjct((mBean !=null)?mBean:new DeviceDetailBean(), AppUrls.PSX_API_TTLOCK_GET_LOCKDATA, "true", ""));
                             }else{
                                onResponse.onError(AppUrls.PSX_API_TTLOCK_GET_LOCKDATA, mContext.getResources().getString(R.string.something_wrong));
                            }
                      /*      if (obj.has("lockData")){
                                String electricQuantity = obj.getString("electricQuantity");
                                String lockData = obj.getString("lockData");
                                String keyId = obj.getString("keyId");
                                String lockMac = obj.getString("lockMac");
                                String noKeyPwd = obj.getString("noKeyPwd");
                                GuestShutterLockModel mBean1 = checkLockData(guestShutterLockModel);
                                guestShutterLockModel.setLockData(mBean1.getLockData());
                                guestShutterLockModel.setMACID(mBean1.getMACID());
                                hitUpdateLockDataApi(lockData, lockMac, guestShutterLockModel.getBtlockid());
                                //                                lockOpenProcess(lockData, lockMac);
                            }*/

                        } catch (Exception e) {
                            onResponse.onError(AppUrls.PSX_API_TTLOCK_GET_LOCKDATA, mContext.getResources().getString(R.string.something_wrong));
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        onResponse.onError(AppUrls.PSX_API_TTLOCK_GET_LOCKDATA, mContext.getResources().getString(R.string.something_wrong));
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> prms = new HashMap<String, String>();
                prms.put("accessToken",params[1] );
                prms.put("lockId", params[2]);
                prms.put("date", String.valueOf(System.currentTimeMillis()));
                prms.put("clientId", params[0]);
                return prms;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        Volley.newRequestQueue(mContext).add(postRequest);
    }
    public void ttlockAuth(OnResponse<UniverSelObjct> onResponse) {
        try {
            SFProgress.showProgressDialog(mContext, true);
        } catch (Exception e) {
        }
        ApiService mRequest = RetrofitRequest.getRetrofitInstance(3, 3).create(ApiService.class);
        mRequest.auth(ApiService.CLIENT_ID, ApiService.CLIENT_SECRET, "info@pathsecurex.com", DigitUtil.getMD5("Pss@1987")).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    SFProgress.hideProgressDialog(mContext);
                } catch (Exception e) {
                }
                try {
                    if (response != null) {
                        try {
                            String strResp = response.body().toString();
                            AccountInfo mBean = new Gson().fromJson(strResp, AccountInfo.class);
                            onResponse.onSuccess(new UniverSelObjct(mBean, AppUrls.PSX_API_TTLOCK_AUTH_TOKEN, "true", ""));
                        } catch (Exception e) {
                            e.printStackTrace();
                            onResponse.onError(AppUrls.PSX_API_TTLOCK_AUTH_TOKEN, mContext.getResources().getString(R.string.something_wrong));
                        }
                    } else {
                        onResponse.onError(AppUrls.PSX_API_TTLOCK_AUTH_TOKEN, mContext.getResources().getString(R.string.something_wrong));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    onResponse.onError(AppUrls.PSX_API_TTLOCK_AUTH_TOKEN, mContext.getResources().getString(R.string.something_wrong));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                try {
                    SFProgress.hideProgressDialog(mContext);
                } catch (Exception e) {
                }
                t.printStackTrace();
                onResponse.onError(AppUrls.PSX_API_TTLOCK_AUTH_TOKEN, mContext.getResources().getString(R.string.something_wrong));
            }
        });
    }

}
