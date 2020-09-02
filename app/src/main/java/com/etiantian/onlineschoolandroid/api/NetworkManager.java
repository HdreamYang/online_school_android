package com.etiantian.onlineschoolandroid.api;

import android.net.Uri;

import com.etiantian.lib_network.client.CommonOkHttpClient;
import com.etiantian.lib_network.request.CommonRequest;
import com.etiantian.lib_network.request.RequestParams;
import com.etiantian.lib_network.response_handler.NormalResponseCallBack;
import com.etiantian.lib_network.response_handler.ResponseHandler;
import com.etiantian.onlineschoolandroid.constant.Const;
import com.etiantian.onlineschoolandroid.event.TokenEvent;
import com.etiantian.onlineschoolandroid.model.LoginModel;
import com.etiantian.onlineschoolandroid.tools.SharedPreferencesManager;


import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


///
/// @description 网络请求管理类 负责网络请求控制,接口管理等
/// @author waitwalker
/// @time 2020/9/2 9:04 AM
///
public class NetworkManager {
    static class HttpConstants {

        private static final String AppId = "C2ABCA7EBE1A93D1F0A1C3D9E8D6B79E";
        private static final String AppSecret = "2765F72C83B05066CB7B65F3650E3440";

        private static final String Base_URL = "https://school.etiantian.com/";

        // 登录
        public static String Login_URL = Base_URL + "authentication-center/authentication/login?";

        // 活动课
        public static String Activity_URL = Base_URL + "api-study-service/api/activity/prompt";
    }

    ///
    /// @description get 请求通用方法
    /// @param
    /// @return
    /// @author waitwalker
    /// @time 2020/9/2 9:03 AM
    ///
    public static void getRequest(String url, RequestParams params, RequestParams headers, NormalResponseCallBack callBack, Class<?> clazz) {
        CommonOkHttpClient.get(CommonRequest.createGetRequest(url, params, headers), new ResponseHandler(callBack, clazz));
    }

    ///
    /// @description post 请求通用方法
    /// @param
    /// @return
    /// @author waitwalker
    /// @time 2020/9/2 9:03 AM
    ///
    public static void postRequest(String url, RequestParams params, RequestParams headers, NormalResponseCallBack callBack, Class<?> clazz) {
        CommonOkHttpClient.post(CommonRequest.createPostRequest(url, params, headers), new ResponseHandler(callBack, clazz));
    }

    ///
    /// @description 拼接请求参数
    /// @param
    /// @return
    /// @author waitwalker
    /// @time 2020/9/2 9:03 AM
    ///
    public static String mapToQuery(RequestParams map) {
        final List<String> pairs = new ArrayList<String>();
        Map<String,String> hashMap =  map.urlParams;
        for (Map.Entry<String, String> entry: hashMap.entrySet()) {
           String encodedKey = Uri.encode(entry.getKey());
           String encodedValue = Uri.encode(entry.getValue());
           String keyValue = encodedKey + "=" + encodedValue;
           pairs.add(keyValue);
        }
        String joinedStr = String.join("&", pairs);
        return joinedStr;
    }

    ///
    /// @description 登录请求
    /// @param
    /// @return
    /// @author waitwalker
    /// @time 2020/9/2 11:27 AM
    ///
    public static void login(RequestParams params, NormalResponseCallBack callBack) {
        String url =  HttpConstants.Login_URL + mapToQuery(params);
        RequestParams headers = new RequestParams();
        String basic = HttpConstants.AppId + ":" + HttpConstants.AppSecret;
        String base64 = Base64.getEncoder().encodeToString(basic.getBytes());
        headers.put("Authorization", "Basic " + base64);
        postRequest(url,null, headers, callBack, LoginModel.class);
    }

    public static void activityCourseAlert(NormalResponseCallBack callBack) {
        String url =  HttpConstants.Activity_URL;
        RequestParams headers = new RequestParams();
        String token = getToken();
        if (token == null) return;
        headers.put("Authorization", "Bearer " + token);
        getRequest(url,null, headers, callBack, LoginModel.class);
    }

    ///
    /// @description 获取token
    /// @param
    /// @return
    /// @author waitwalker
    /// @time 2020/9/2 11:42 AM
    ///
    private static String getToken() {
        String token = SharedPreferencesManager.instance().getString("token");
        if (token.length() == 0) {
            EventBus.getDefault().post(new TokenEvent(Const.kCommonErrorCode, Const.kTokenIsEmpty));
            return null;
        }
        return token;
    }
}
