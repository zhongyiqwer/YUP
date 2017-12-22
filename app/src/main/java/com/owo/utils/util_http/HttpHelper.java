package com.owo.utils.util_http;


import com.owo.model.YouTuTag;
import com.owo.module_a_selectlabel.bean.BeanTag;
import com.owo.module_b_explore.bean.BeanRecyclerViewItem;
import com.owo.module_b_explore.bean.BeanViewPagerItem;
import com.owo.module_b_home.bean.BeanTask;
import com.owo.module_b_personal.bean.BeanComment;
import com.owo.module_b_personal.bean.BeanLike;
import com.owo.module_b_personal.bean.BeanUser;
import com.owo.module_c_detail.detail_activity_comment.view_comment.BeanActCmt;
import com.owo.utils.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.HttpClient;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.ls.LSException;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 网络请求
 */
public class HttpHelper {
    private static final String TAG = "uploadFile";
    private static final int TIME_OUT = 10 * 1000; // 超时时间
    private static final String CHARSET = "utf-8"; // 设置编码


    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url    发送请求的URL
     * @param params 请求参数，请求参数应该是name1=value1&name2=value2的形式。
     * @return URL所代表远程资源的响应
     */
    public static String sendGet(String url, String params) {
        String str = "";
        try {
            String urlPath = null;
            if (params == null)
                urlPath = url;
            else
                urlPath = url + "?" + params;
            System.out.println(urlPath);
            HttpGet request = new HttpGet(urlPath);
            request.setHeader("Accept", "application/json");
            request.setHeader("Content-type", "application/json");
            request.addHeader("charset", HTTP.UTF_8);

            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
            // 设置连接超时
            HttpConnectionParams.setSoTimeout(httpParams, TIME_OUT);
            // 设置请求超时
            request.setParams(httpParams);

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(request);
            HttpEntity responseEntity = response.getEntity();

            InputStreamReader isr = new InputStreamReader(
                    responseEntity.getContent());

            // 使用缓冲一行行的读入，加速InputStreamReader的速度
            BufferedReader buffer = new BufferedReader(isr);
            String inputLine = null;

            StringBuilder resultData = new StringBuilder("");
            while ((inputLine = buffer.readLine()) != null) {
                resultData.append(inputLine);
                resultData.append("\n");
            }
            buffer.close();
            isr.close();
            str = resultData.toString();
            System.out.println(str);
            if (str.charAt(0) == '<')
                str = null;
            return str;
        } catch (Exception e) {
            str = " 发送GET请求出现异常：" + e;
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 同时传送文件和字符串
     *
     * @param url
     * @param paramHM
     * @param fileHM
     */

    public static String postData(String url, HashMap<String, String>
            paramHM, HashMap<String, String> fileHM) throws Exception {

        ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
        HttpClient client = new DefaultHttpClient();// 开启一个客户端 HTTP 请求
        HttpPost post = new HttpPost(url);//创建 HTTP POST 请求
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setCharset(Charset.forName(HTTP.UTF_8));//设置请求的编码格式
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//设置浏览器兼容模式

        if (paramHM != null) {
            Iterator iter = paramHM.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                StringBody stringBody =
                        new StringBody(val.toString(), contentType);
                builder.
                        addPart(key.toString(),
                                stringBody);
            }
        }

        if (fileHM != null) {
            Iterator iter1 = fileHM.entrySet().iterator();
            while (iter1.hasNext()) {
                Map.Entry entry = (Map.Entry) iter1.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                builder.addBinaryBody(key.toString(),
                        new File(val.toString()));
            }
        }

        HttpEntity entity = builder.build();// 生成 HTTP POST 实体
        post.setEntity(entity);//设置请求参数
        HttpResponse response = client.execute(post);// 发起请求 并返回请求的响应
        String res = EntityUtils.toString(response.getEntity());
        System.out.println(res);
        return res;
    }


    /**
     * 传键值对
     *
     * @param url
     * @param paramHM
     * @param fileHM
     */

    public static String postDataTxtBody(String url, HashMap<String, String>
            paramHM, HashMap<String, String> fileHM) throws Exception {

        ContentType contentType = ContentType.create("multipart/form-data", HTTP.UTF_8);
        HttpClient client = new DefaultHttpClient();// 开启一个客户端 HTTP 请求
        HttpPost post = new HttpPost(url);//创建 HTTP POST 请求
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setCharset(Charset.forName(HTTP.UTF_8));//设置请求的编码格式
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//设置浏览器兼容模式

        if (paramHM != null) {
            Iterator iter = paramHM.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                StringBody stringBody =
                        new StringBody(val.toString(), contentType);
                builder.
                        addPart(key.toString(),
                                stringBody);
            }
        }

        if (fileHM != null) {
            Iterator iter1 = fileHM.entrySet().iterator();
            while (iter1.hasNext()) {
                Map.Entry entry = (Map.Entry) iter1.next();
                Object key = entry.getKey();
                Object val = entry.getValue();
                builder.addTextBody(key.toString(),
                        val.toString());
            }
        }

        HttpEntity entity = builder.build();// 生成 HTTP POST 实体
        post.setEntity(entity);//设置请求参数
        HttpResponse response = client.execute(post);// 发起请求 并返回请求的响应
        String res = EntityUtils.toString(response.getEntity());
        System.out.println(res);
        return res;
    }

    public static int getCode(String jsonStr) throws JSONException {
        int code = 0;
        if (jsonStr == null) {
            code = 0;
        } else {
            JSONObject jsonObject = new JSONObject(jsonStr);
            code = jsonObject.getInt("code");
        }
        return code;
    }

    public static String getMsg(String jsonStr) throws JSONException {
        String msg;
        if (jsonStr == null) {
            msg = "";
        } else {
            JSONObject jsonObject = new JSONObject(jsonStr);
            msg = jsonObject.getString("msg");
        }
        return msg;
    }


    public static String getLocation(String jsonStr) throws JSONException {
        if (jsonStr == null)
            return "";
        else {
            String str = jsonStr.substring(29, jsonStr.length() - 1);
            JSONObject jsonObject = new JSONObject(str);
            int status = jsonObject.getInt("status");
            String location = "";
            if (status == 0) {
                String objStr = jsonObject.getString("result");
                JSONObject locObj = new JSONObject(objStr);
                location = locObj.getString("formatted_address");
            }
            return location;
        }
    }

    public static Object AnalysisData(
            String jsonStr) throws JSONException {

        if (getCode(jsonStr) == 200) {
            JSONObject jsonObject = new JSONObject(jsonStr);
            Object data = jsonObject.get("data");
            return data;
        } else {
            return null;
        }

    }


    public static HashMap<String, Object> getVersion(
            String jsonStr) throws JSONException {

        JSONObject jsonObject = new JSONObject(jsonStr);
        String objStr = jsonObject.getString("data");
        JSONObject obj = new JSONObject(objStr);
        HashMap<String, Object> hm = new HashMap<>();
        hm.put("version", obj
                .getInt("version"));
        hm.put("name", obj
                .getString("name"));
        hm.put("url", obj
                .getString("url"));
        return hm;

    }


    public static HashMap<String, Object> AJgetUserByInviteCode(
            String jsonStr) throws JSONException {

        if (getCode(jsonStr) == 200) {
            JSONObject jsonObject = new JSONObject(jsonStr);
            String objStr = jsonObject.getString("data");
            JSONObject obj = new JSONObject(objStr);
            HashMap<String, Object> hm = new HashMap<>();
            hm.put("id", obj
                    .getInt("id"));
            hm.put("username", obj
                    .getString("username"));
            hm.put("avator", obj
                    .getString("avator"));
            hm.put("stauts", obj
                    .getInt("stauts"));
            hm.put("isAvailable", obj
                    .getInt("isAvailable"));
            return hm;
        } else {
            return null;
        }
    }

    public static HashMap<String, Object> AJgetRecordByID(
            String jsonStr) throws JSONException {

        if (getCode(jsonStr) == 200) {
            JSONObject jsonObject = new JSONObject(jsonStr);
            String objStr = jsonObject.getString("data");
            JSONObject obj = new JSONObject(objStr);
            HashMap<String, Object> hm = new HashMap<>();
            hm.put("id", obj
                    .getInt("id"));
            hm.put("latitude", obj
                    .getString("latitude"));
            hm.put("longtitude", obj
                    .getString("longtitude"));
            hm.put("totalsteps", obj
                    .getInt("totalsteps"));
            hm.put("loverid", obj
                    .getString("loverid"));
            hm.put("radius", obj
                    .getInt("radius"));
            return hm;
        } else {
            return null;
        }
    }

    public static HashMap<String, Object> AJgetHalfByID(
            String jsonStr) throws JSONException {

        if (getCode(jsonStr) == 200) {
            JSONObject jsonObject = new JSONObject(jsonStr);
            String objStr = jsonObject.getString("data");
            JSONObject obj = new JSONObject(objStr);
            HashMap<String, Object> hm = new HashMap<>();
            hm.put("id", obj
                    .getInt("id"));
            hm.put("latitude", obj
                    .getString("latitude"));
            hm.put("longtitude", obj
                    .getString("longtitude"));
            hm.put("sex", obj
                    .getString("sex"));
            hm.put("avatar", obj
                    .getString("avator"));
            hm.put("checkindays", obj
                    .getInt("checkindays"));
            hm.put("stepstoday", obj
                    .getInt("stepstoday"));
            hm.put("username", obj
                    .getString("username"));
            return hm;
        } else {
            return null;
        }
    }

    public static HashMap<String, Object> AJgetFinishStatusByUID(
            String jsonStr) throws JSONException {

        if (getCode(jsonStr) == 200) {
            JSONObject jsonObject = new JSONObject(jsonStr);
            String objStr = jsonObject.getString("data");
            JSONObject obj = new JSONObject(objStr);
            HashMap<String, Object> hm = new HashMap<>();
            hm.put("finishstatus", obj
                    .getInt("finishstatus"));
            hm.put("id", obj
                    .getInt("id"));
            hm.put("userid", obj
                    .getInt("userid"));
            return hm;
        } else {
            return null;
        }
    }

    public static HashMap<String, Object> AJgetLocByUID(
            String jsonStr) throws JSONException {

        if (getCode(jsonStr) == 200) {
            JSONObject jsonObject = new JSONObject(jsonStr);
            String objStr = jsonObject.getString("data");
            JSONObject obj = new JSONObject(objStr);
            HashMap<String, Object> hm = new HashMap<>();
            hm.put("latitude", obj
                    .getString("latitud"));
            hm.put("longtitude", obj
                    .getString("longtitud"));
            return hm;
        } else {
            return null;
        }
    }

    public static HashMap<String, Object> AnalysisUserInfo(
            String jsonStr) throws JSONException {

        int code = getCode(jsonStr);
        if (code != 0) {
            JSONObject jsonObject = new JSONObject(jsonStr);
            if (code == 200) {
                String objStr = jsonObject.getString("data");
                if (objStr != null) {
                    JSONObject obj = new JSONObject(objStr);
                    HashMap<String, Object> hm = new HashMap<>();
                    int age = (obj.get("age") == null ? -1 : obj.getInt("age"));
                    String avatar = (obj.get("avator") == null ? "" : obj.getString("avator"));
                    String backimage = (obj.get("backimage") == null ? "" : obj.getString("backimage"));
                    int checkindays = (obj.get("checkindays") == null ? -1 : obj.getInt("checkindays"));
                    int exp = (obj.get("exp") == null ? -1 : obj.getInt("exp"));
                    int height = (obj.get("height") == null ? -1 : obj.getInt("height"));
                    int weight = (obj.get("weight") == null ? -1 : obj.getInt("weight"));
                    String hobby = (obj.get("hobby") == null ? "" : obj.getString("hobby"));
                    int id = (obj.get("id") == null ? -1 : obj.getInt("id"));
                    String invitecode = (obj.get("invitecode") == null ? "" : obj.getString("invitecode"));
                    String longtitude = (obj.get("longtitude") == null ? "" : obj.getString("longtitude"));
                    String latitude = (obj.get("latitude") == null ? "" : obj.getString("latitude"));
                    String password = (obj.get("password") == null ? "" : obj.getString("password"));
                    String phonenumber = (obj.get("phonenumber") == null ? "" : obj.getString("phonenumber"));
                    String qq = (obj.get("qq") == null ? "" : obj.getString("qq"));
                    String wexid = (obj.get("wexid") == null ? "" : obj.getString("wexid"));
                    String sex = (obj.get("sex") == null ? "" : obj.getString("sex"));
                    String signature = (obj.get("signature") == null ? "" : obj.getString("signature"));
                    int stauts = (obj.get("stauts") == null ? -1 : obj.getInt("stauts"));
                    String username = (obj.get("username") == null ? "" : obj.getString("username"));
                    int level = (obj.get("level") == null ? -1 : obj.getInt("level"));
                    int stepstoday = (obj.get("stepstoday") == null ? -1 : obj.getInt("stepstoday"));

                    hm.put("age", age);
                    hm.put("avatar", avatar);
                    hm.put("backimage", backimage);
                    hm.put("checkindays", checkindays);
                    hm.put("exp", exp);
                    hm.put("height", height);
                    hm.put("weight", weight);
                    hm.put("hobby", hobby);
                    hm.put("id", id);
                    hm.put("phonenumber", phonenumber);
                    hm.put("qq", qq);
                    hm.put("signature", signature);
                    hm.put("username", username);
                    hm.put("level", level);
                    hm.put("status", stauts);
                    hm.put("invitecode", invitecode);
                    hm.put("longtitude", longtitude);
                    hm.put("latitude", latitude);
                    hm.put("password", password);
                    hm.put("wexid", wexid);
                    hm.put("sex", sex);
                    hm.put("stepstoday", stepstoday);
                    hm.put("regdate", obj.getString("regdate"));
                    hm.put("money", obj.getInt("money"));

                    return hm;
                } else return null;
            } else return null;
        } else {
            return null;
        }

    }

    public static ArrayList<HashMap<String, Object>> AJUsers2(
            String jsonStr) throws JSONException {
        ArrayList<JSONObject> jsonArrayList = TOJsonArrayMsg(jsonStr);
        if (jsonArrayList == null) {
            return null;
        } else {

            ArrayList<HashMap<String, Object>> pArrayList = new ArrayList<>();
            for (int i = 0; i < jsonArrayList.size(); i++) {
                HashMap<String, Object> hm = new HashMap<>();
                hm.put("id", jsonArrayList.get(i)
                        .getInt("id"));
                hm.put("age", jsonArrayList.get(i)
                        .getInt("age"));
                hm.put("avatar", jsonArrayList.get(i)
                        .getString("avator"));
                hm.put("backimage", jsonArrayList.get(i)
                        .getString("backimage"));
                hm.put("checkindays", jsonArrayList.get(i)
                        .getInt("checkindays"));
                hm.put("status", jsonArrayList.get(i)
                        .getInt("stauts"));
                hm.put("exp", jsonArrayList.get(i)
                        .getInt("exp"));
                hm.put("height", jsonArrayList.get(i)
                        .getInt("height"));
                hm.put("weight", jsonArrayList.get(i)
                        .getInt("weight"));
                hm.put("hobby", jsonArrayList.get(i)
                        .getString("hobby"));
                hm.put("phonenumber", jsonArrayList.get(i)
                        .getString("phonenumber"));
                hm.put("qq", jsonArrayList.get(i)
                        .getString("qq"));
                hm.put("signature", jsonArrayList.get(i)
                        .getString("signature"));
                hm.put("username", jsonArrayList.get(i)
                        .getString("username"));
                hm.put("level", jsonArrayList.get(i)
                        .getInt("level"));
                hm.put("invitecode", jsonArrayList.get(i)
                        .getString("invitecode"));
                hm.put("longtitude", jsonArrayList.get(i)
                        .getString("longtitude"));
                hm.put("latitude", jsonArrayList.get(i)
                        .getString("latitude"));
                hm.put("password", jsonArrayList.get(i)
                        .getString("password"));
                hm.put("wexid", jsonArrayList.get(i)
                        .getString("wexid"));
                hm.put("sex", jsonArrayList.get(i)
                        .getString("sex"));
                hm.put("stepstoday", jsonArrayList.get(i)
                        .getInt("stepstoday"));
                hm.put("regdate", jsonArrayList.get(i)
                        .getString("regdate"));
                hm.put("money", jsonArrayList.get(i)
                        .getInt("money"));
                pArrayList.add(hm);
            }
            return pArrayList;

        }

    }

    public static ArrayList<HashMap<String, Object>> AJUsers(
            String jsonStr) throws JSONException {
        ArrayList<JSONObject> jsonArrayList = TOJsonArray(jsonStr);
        if (jsonArrayList == null) {
            return null;
        } else {

            ArrayList<HashMap<String, Object>> pArrayList = new ArrayList<>();
            for (int i = 0; i < jsonArrayList.size(); i++) {
                HashMap<String, Object> hm = new HashMap<>();
                hm.put("id", jsonArrayList.get(i)
                        .getInt("id"));
                hm.put("age", jsonArrayList.get(i)
                        .getInt("age"));
                hm.put("avatar", jsonArrayList.get(i)
                        .getString("avator"));
                hm.put("backimage", jsonArrayList.get(i)
                        .getString("backimage"));
                hm.put("checkindays", jsonArrayList.get(i)
                        .getInt("checkindays"));
                hm.put("status", jsonArrayList.get(i)
                        .getInt("stauts"));
                hm.put("exp", jsonArrayList.get(i)
                        .getInt("exp"));
                hm.put("height", jsonArrayList.get(i)
                        .getInt("height"));
                hm.put("weight", jsonArrayList.get(i)
                        .getInt("weight"));
                hm.put("hobby", jsonArrayList.get(i)
                        .getString("hobby"));
                hm.put("phonenumber", jsonArrayList.get(i)
                        .getString("phonenumber"));
                hm.put("qq", jsonArrayList.get(i)
                        .getString("qq"));
                hm.put("signature", jsonArrayList.get(i)
                        .getString("signature"));
                hm.put("username", jsonArrayList.get(i)
                        .getString("username"));
                hm.put("level", jsonArrayList.get(i)
                        .getInt("level"));
                hm.put("invitecode", jsonArrayList.get(i)
                        .getString("invitecode"));
                hm.put("longtitude", jsonArrayList.get(i)
                        .getString("longtitude"));
                hm.put("latitude", jsonArrayList.get(i)
                        .getString("latitude"));
                hm.put("password", jsonArrayList.get(i)
                        .getString("password"));
                hm.put("wexid", jsonArrayList.get(i)
                        .getString("wexid"));
                hm.put("sex", jsonArrayList.get(i)
                        .getString("sex"));
                hm.put("stepstoday", jsonArrayList.get(i)
                        .getInt("stepstoday"));
                hm.put("regdate", jsonArrayList.get(i)
                        .getString("regdate"));
                hm.put("money", jsonArrayList.get(i)
                        .getInt("money"));
                pArrayList.add(hm);
            }
            return pArrayList;

        }

    }


    public static ArrayList<HashMap<String, Object>> AJgetPhotoRedord(
            String jsonStr) throws JSONException {
        ArrayList<JSONObject> jsonArrayList = TOJsonArray(jsonStr);
        if (jsonArrayList == null) {
            return null;
        } else {

            ArrayList<HashMap<String, Object>> pArrayList = new ArrayList<>();
            for (int i = 0; i < jsonArrayList.size(); i++) {
                HashMap<String, Object> hm = new HashMap<>();
                hm.put("userName", jsonArrayList.get(i)
                        .getString("userName"));
                hm.put("avatar", jsonArrayList.get(i)
                        .getString("avatar"));
                hm.put("photo", jsonArrayList.get(i)
                        .getString("photo"));
                hm.put("publishDate", jsonArrayList.get(i)
                        .getString("publishDate"));
                hm.put("content", jsonArrayList.get(i)
                        .getString("content"));
                hm.put("longtitude", jsonArrayList.get(i)
                        .getString("longtitude"));
                hm.put("latitude", jsonArrayList.get(i)
                        .getString("latitude"));
                hm.put("isMe", jsonArrayList.get(i)
                        .getInt("isMe"));
                pArrayList.add(hm);
            }
            return pArrayList;

        }

    }

    public static ArrayList<HashMap<String, Object>> AJgetTextRedord(
            String jsonStr) throws JSONException {
        ArrayList<JSONObject> jsonArrayList = TOJsonArray(jsonStr);
        if (jsonArrayList == null) {
            return null;
        } else {

            ArrayList<HashMap<String, Object>> pArrayList = new ArrayList<>();
            for (int i = 0; i < jsonArrayList.size(); i++) {
                HashMap<String, Object> hm = new HashMap<>();
                hm.put("userName", jsonArrayList.get(i)
                        .getString("userName"));
                hm.put("avatar", jsonArrayList.get(i)
                        .getString("avatar"));
                hm.put("text", jsonArrayList.get(i)
                        .getString("text"));
                hm.put("publishDate", jsonArrayList.get(i)
                        .getString("publishDate"));
                hm.put("longtitude", jsonArrayList.get(i)
                        .getString("longtitude"));
                hm.put("latitude", jsonArrayList.get(i)
                        .getString("latitude"));
                hm.put("isMe", jsonArrayList.get(i)
                        .getInt("isMe"));
                pArrayList.add(hm);
            }
            return pArrayList;

        }

    }

    public static HashMap<String, Object> AJgetTreasure(
            String jsonStr) throws JSONException {
        int code = getCode(jsonStr);
        if (code != 0) {
            JSONObject jsonObject = new JSONObject(jsonStr);
            if (code == 200) {
                String objStr = jsonObject.getString("data");
                if (objStr != null) {
                    JSONObject obj = new JSONObject(objStr);
                    HashMap<String, Object> hm = new HashMap<>();
                    hm.put("id", obj
                            .getInt("id"));
                    hm.put("money", obj
                            .getInt("money"));
                    hm.put("userID", obj
                            .getInt("userid"));
                    hm.put("treasureCode", obj
                            .getInt("treasurecode"));
                    hm.put("longtitude", obj
                            .getString("longtitude"));
                    hm.put("latitude", obj
                            .getString("latitude"));
                    return hm;
                } else {
                    return null;
                }
            } else return null;
        } else return null;
    }

    public static HashMap<String, Object> AJgetCapsule(
            String jsonStr) throws JSONException {
        int code = getCode(jsonStr);
        if (code != 0) {
            JSONObject jsonObject = new JSONObject(jsonStr);
            if (code == 200) {
                String objStr = jsonObject.getString("data");
                if (objStr != null) {
                    JSONObject obj = new JSONObject(objStr);
                    HashMap<String, Object> hm = new HashMap<>();
                    hm.put("id", obj
                            .getInt("id"));
                    hm.put("itemID", obj
                            .getInt("itemid"));
                    hm.put("openDay", obj
                            .getString("openday"));
                    hm.put("receiverID", obj
                            .getInt("receiverid"));
                    hm.put("state", obj
                            .getInt("state"));
                    hm.put("content", obj
                            .getString("content"));
                    hm.put("userID", obj
                            .getInt("userid"));
                    hm.put("photo", obj
                            .getString("photo"));
                    return hm;
                } else {
                    return null;
                }
            } else return null;
        } else return null;
    }


    public static ArrayList<HashMap<String, Object>> AJgetAllTreasures(
            String jsonStr) throws JSONException {
        ArrayList<JSONObject> jsonArrayList = TOJsonArray(jsonStr);
        if (jsonArrayList == null) {
            return null;
        } else {

            ArrayList<HashMap<String, Object>> pArrayList = new ArrayList<>();
            for (int i = 0; i < jsonArrayList.size(); i++) {
                HashMap<String, Object> hm = new HashMap<>();
                hm.put("id", jsonArrayList.get(i)
                        .getInt("id"));
                hm.put("money", jsonArrayList.get(i)
                        .getInt("money"));
                hm.put("userID", jsonArrayList.get(i)
                        .getInt("userid"));
                hm.put("treasureCode", jsonArrayList.get(i)
                        .getString("treasurecode"));
                hm.put("longtitude", jsonArrayList.get(i)
                        .getString("longtitude"));
                hm.put("latitude", jsonArrayList.get(i)
                        .getString("latitude"));
                pArrayList.add(hm);
            }
            return pArrayList;

        }

    }


    public static ArrayList<HashMap<String, Object>> AJgetMsgByRID(
            String jsonStr) throws JSONException {
        ArrayList<JSONObject> jsonArrayList = TOJsonArray(jsonStr);
        if (jsonArrayList == null) {
            return null;
        } else {

            ArrayList<HashMap<String, Object>> pArrayList = new ArrayList<>();
            for (int i = 0; i < jsonArrayList.size(); i++) {
                HashMap<String, Object> hm = new HashMap<>();
                hm.put("id", jsonArrayList.get(i)
                        .getInt("messageID"));
                hm.put("userID", jsonArrayList.get(i)
                        .getInt("userID"));
                hm.put("isRead", jsonArrayList.get(i)
                        .getInt("isRead"));
                hm.put("msgDate", jsonArrayList.get(i)
                        .getString("msgDate"));
                hm.put("avatar", jsonArrayList.get(i)
                        .getString("avatar"));
                hm.put("msgContent", jsonArrayList.get(i)
                        .getString("msgContent"));
                hm.put("msgType", jsonArrayList.get(i)
                        .getString("msgType"));
                hm.put("userName", jsonArrayList.get(i)
                        .getString("userName"));
                pArrayList.add(hm);
            }
            return pArrayList;

        }

    }

    public static ArrayList<HashMap<String, Object>> AJgetMommentByUID(
            String jsonStr) throws JSONException {
        ArrayList<JSONObject> jsonArrayList = TOJsonArray(jsonStr);
        if (jsonArrayList == null) {
            return null;
        } else {

            ArrayList<HashMap<String, Object>> pArrayList = new ArrayList<>();
            for (int i = 0; i < jsonArrayList.size(); i++) {
                HashMap<String, Object> hm = new HashMap<>();
                hm.put("userID", jsonArrayList.get(i)
                        .getInt("userid"));
                hm.put("avatar", jsonArrayList.get(i)
                        .getString("avatar"));
                hm.put("username", jsonArrayList.get(i)
                        .getString("username"));
                hm.put("commentID", jsonArrayList.get(i)
                        .getInt("commentid"));
                hm.put("id", jsonArrayList.get(i)
                        .getInt("id"));
                hm.put("momentContent", jsonArrayList.get(i)
                        .getString("momentcontent"));
                hm.put("momentImage", jsonArrayList.get(i)
                        .getString("momentimage"));
                hm.put("momentDate", jsonArrayList.get(i)
                        .getString("momentdate"));
                hm.put("momentType", jsonArrayList.get(i)
                        .getInt("momenttype"));
                pArrayList.add(hm);
            }
            return pArrayList;

        }

    }

    public static ArrayList<HashMap<String, Object>> AJFriends(
            String jsonStr) throws JSONException {
        ArrayList<JSONObject> jsonArrayList = TOJsonArrayF(jsonStr);
        if (jsonArrayList == null) {
            return null;
        } else {

            ArrayList<HashMap<String, Object>> pArrayList = new ArrayList<>();
            for (int i = 0; i < jsonArrayList.size(); i++) {
                HashMap<String, Object> hm = new HashMap<>();
                hm.put("userID", jsonArrayList.get(i)
                        .getInt("userID"));
                hm.put("avatar", jsonArrayList.get(i)
                        .getString("avatar"));
                hm.put("height", jsonArrayList.get(i)
                        .getInt("height"));
                hm.put("weight", jsonArrayList.get(i)
                        .getInt("weight"));
                hm.put("level", jsonArrayList.get(i)
                        .getInt("level"));
                hm.put("backImage", jsonArrayList.get(i)
                        .getString("backImage"));
                hm.put("age", jsonArrayList.get(i)
                        .getInt("age"));
                hm.put("hobby", jsonArrayList.get(i)
                        .getString("hobby"));
                hm.put("userName", jsonArrayList.get(i)
                        .getString("userName"));
                hm.put("signature", jsonArrayList.get(i)
                        .getString("signature"));
                hm.put("sex", jsonArrayList.get(i)
                        .getString("sex"));
                pArrayList.add(hm);
            }
            return pArrayList;

        }

    }

    public static ArrayList<HashMap<String, Object>> AJNei(
            String jsonStr) throws JSONException {
        ArrayList<JSONObject> jsonArrayList = TOJsonArray(jsonStr);
        if (jsonArrayList == null) {
            return null;
        } else {

            ArrayList<HashMap<String, Object>> pArrayList = new ArrayList<>();
            for (int i = 0; i < jsonArrayList.size(); i++) {
                HashMap<String, Object> hm = new HashMap<>();
                hm.put("userID", jsonArrayList.get(i)
                        .getInt("userID"));
                hm.put("avatar", jsonArrayList.get(i)
                        .getString("avatar"));
                hm.put("sex", jsonArrayList.get(i)
                        .getString("sex"));
                hm.put("distance", jsonArrayList.get(i)
                        .getDouble("distance"));
                pArrayList.add(hm);
            }
            return pArrayList;

        }

    }

    public static ArrayList<HashMap<String, Object>> AJgetAllLS(
            String jsonStr) throws JSONException {
        ArrayList<JSONObject> jsonArrayList = TOJsonArray(jsonStr);
        if (jsonArrayList == null) {
            return null;
        } else {

            ArrayList<HashMap<String, Object>> pArrayList = new ArrayList<>();
            for (int i = 0; i < jsonArrayList.size(); i++) {
                HashMap<String, Object> hm = new HashMap<>();
                hm.put("loverboyid", jsonArrayList.get(i)
                        .getInt("loverboyid"));
                hm.put("lovergirlid", jsonArrayList.get(i)
                        .getInt("lovergirlid"));
                hm.put("loveindex", jsonArrayList.get(i)
                        .getInt("loveindex"));
                hm.put("boyavatar", jsonArrayList.get(i)
                        .getString("boyavatar"));
                hm.put("boyname", jsonArrayList.get(i)
                        .getString("boyname"));
                hm.put("girlname", jsonArrayList.get(i)
                        .getString("girlname"));
                hm.put("girlavatar", jsonArrayList.get(i)
                        .getString("girlavatar"));
                hm.put("loverid", jsonArrayList.get(i)
                        .getString("loverid"));
                hm.put("lovetime", jsonArrayList.get(i)
                        .getString("lovetime"));
                hm.put("state", jsonArrayList.get(i)
                        .getInt("state"));
                pArrayList.add(hm);
            }
            return pArrayList;

        }

    }

    public static HashMap<String, Object> AJLS(
            String jsonStr) throws JSONException {

        if (getCode(jsonStr) == 200) {
            JSONObject jsonObject = new JSONObject(jsonStr);
            String objStr = jsonObject.getString("data");
            JSONObject obj = new JSONObject(objStr);
            HashMap<String, Object> hm = new HashMap<>();

            hm.put("loverboyid", obj
                    .getInt("loverboyid"));
            hm.put("lovergirlid", obj
                    .getInt("lovergirlid"));
            hm.put("loveindex", obj
                    .getInt("loveindex"));
//            hm.put("boyavatar", obj
//                    .getString("boyavatar"));
//            hm.put("boyname", obj
//                    .getString("boyname"));
//            hm.put("girlname", obj
//                    .getString("girlname"));
//            hm.put("girlavatar", obj
//                    .getString("girlavatar"));
            hm.put("loverid", obj
                    .getString("loverid"));
            hm.put("lovetime", obj
                    .getString("lovetime"));
            hm.put("state", obj
                    .getInt("state"));
            return hm;
        } else {
            return null;
        }
    }


    public static int AJSumFollow(String string) throws JSONException {
        int result = -1;
        if (getCode(string) == 200) {
            result = (Integer) AnalysisData(string);
        }
        return result;
    }

    public static HashMap<String, Object> AJSteps(
            String jsonStr) throws JSONException {

        if (getCode(jsonStr) == 200) {
            JSONObject jsonObject = new JSONObject(jsonStr);
            String objStr = jsonObject.getString("data");
            JSONObject obj = new JSONObject(objStr);
            HashMap<String, Object> hm = new HashMap<>();
            hm.put("userID", obj
                    .getInt("userID"));
            hm.put("status", obj
                    .getInt("status"));
            hm.put("date", obj
                    .getString("date"));
            hm.put("stepsToday", obj
                    .getInt("stepsToday"));
            return hm;
        } else {
            return null;
        }
    }


    /**
     * 解析评论列表
     *
     * @param string
     * @return
     * @throws JSONException
     */
    public static List<BeanComment> AJResultComment(String string) throws JSONException {
        List<BeanComment> result = new ArrayList<>();
        if (getCode(string) == 200) {
            JSONObject jsonObject = new JSONObject(string);
            JSONArray array = jsonObject.getJSONArray("data");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject1 = (JSONObject) array.get(i);
                BeanComment beanComment = new BeanComment();
                beanComment.setUserId((Integer) jsonObject1.get("userID"));
                beanComment.setUserAvatar((String) jsonObject1.get("avatar"));
                beanComment.setUserSex((String) jsonObject1.get("sex"));
                beanComment.setReplyDate((String) jsonObject1.get("replyDate"));
                beanComment.setReplayContent((String) jsonObject1.get("replyContent"));
                result.add(beanComment);
            }

        }
        return result;
    }

    /**
     * 解析点赞列表
     *
     * @param string
     * @return
     * @throws JSONException
     */
    public static List<BeanLike> AjResultLike(String string) throws JSONException {
        List<BeanLike> result = new ArrayList<>();
        if (getCode(string) == 200) {
            JSONObject object = new JSONObject(string);
            JSONArray array = object.getJSONArray("likes");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = (JSONObject) array.get(i);
                BeanLike beanLike = new BeanLike();
                beanLike.setUserId((Integer) jsonObject.get("userID"));
                beanLike.setUserAvatar((String) jsonObject.get("avatar"));
                beanLike.setLikeDate((String) jsonObject.get("likeDate"));
                result.add(beanLike);
            }
        }
        return result;
    }


    public static ArrayList<HashMap<String, Object>> AJItems(
            String jsonStr, boolean isCount) throws JSONException {
        ArrayList<JSONObject> jsonArrayList = TOJsonArray(jsonStr);
        if (jsonArrayList == null) {
            return null;
        } else {

            ArrayList<HashMap<String, Object>> pArrayList = new ArrayList<>();
            for (int i = 0; i < jsonArrayList.size(); i++) {
                HashMap<String, Object> hm = new HashMap<>();
                hm.put("id", jsonArrayList.get(i)
                        .getInt("id"));
                hm.put("itemPrivilege", jsonArrayList.get(i)
                        .getInt("itemPrivilege"));
                hm.put("itemLevel", jsonArrayList.get(i)
                        .getInt("itemLevel"));
                hm.put("itemtype", jsonArrayList.get(i)
                        .getInt("itemtype"));
                hm.put("itemprice", jsonArrayList.get(i)
                        .getInt("itemprice"));
                hm.put("itemfunction", jsonArrayList.get(i)
                        .getString("itemfunction"));
                hm.put("itemimage", jsonArrayList.get(i)
                        .getString("itemimage"));
                hm.put("itemname", jsonArrayList.get(i)
                        .getString("itemname"));
                hm.put("itemname", jsonArrayList.get(i)
                        .getString("itemname"));
                if (isCount) hm.put("count", jsonArrayList.get(i)
                        .getInt("count"));
                pArrayList.add(hm);
            }
            return pArrayList;

        }

    }

    public static ArrayList<HashMap<String, Object>> AJgetAllRecords(
            String jsonStr) throws JSONException {
        ArrayList<JSONObject> jsonArrayList = TOJsonArray(jsonStr);
        if (jsonArrayList == null) {
            return null;
        } else {

            ArrayList<HashMap<String, Object>> pArrayList = new ArrayList<>();
            for (int i = 0; i < jsonArrayList.size(); i++) {
                HashMap<String, Object> hm = new HashMap<>();
                hm.put("id", jsonArrayList.get(i)
                        .getInt("id"));
                hm.put("radius", jsonArrayList.get(i)
                        .getInt("radius"));
                hm.put("totalsteps", jsonArrayList.get(i)
                        .getInt("totalsteps"));
                hm.put("loverid", jsonArrayList.get(i)
                        .getString("loverid"));
                hm.put("longtitude", jsonArrayList.get(i)
                        .getString("longtitude"));
                hm.put("latitude", jsonArrayList.get(i)
                        .getString("latitude"));
                hm.put("avatarA", jsonArrayList.get(i)
                        .getString("avatarA"));
                hm.put("avatarB", jsonArrayList.get(i)
                        .getString("avatarB"));
                hm.put("loveAID", jsonArrayList.get(i)
                        .getInt("loveAID"));
                hm.put("loveBID", jsonArrayList.get(i)
                        .getInt("loveBID"));
                hm.put("nameA", jsonArrayList.get(i)
                        .getString("nameA"));
                hm.put("nameB", jsonArrayList.get(i)
                        .getString("nameB"));
                hm.put("backImageA", jsonArrayList.get(i)
                        .getString("backImageA"));
                pArrayList.add(hm);
            }
            return pArrayList;

        }

    }

    public static ArrayList<JSONObject> TOJsonArray(
            String jsonStr) throws JSONException {
        ArrayList<JSONObject> pArrayList = new ArrayList<>();
        if (jsonStr == null || jsonStr.length() == 0) {
            return pArrayList;
        } else {
            JSONObject jsonObject = new JSONObject(jsonStr);
            if (jsonObject.getInt("code") == 200) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        pArrayList.add(jsonArray.getJSONObject(i));
                    }
            }
            return pArrayList;
        }
    }

    public static ArrayList<JSONObject> TOJsonArrayMsg(
            String jsonStr) throws JSONException {
        ArrayList<JSONObject> pArrayList = new ArrayList<>();
        if (jsonStr == null || jsonStr.length() == 0) {
            return pArrayList;
        } else {
            JSONObject jsonObject = new JSONObject(jsonStr);
            if (jsonObject.getString("msg").equals("200")) {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    pArrayList.add(jsonArray.getJSONObject(i));
                }
            }
            return pArrayList;
        }
    }

    public static ArrayList<JSONObject> TOJsonArrayF(
            String jsonStr) throws JSONException {
        if (jsonStr == null) {
            return null;
        } else {
            JSONObject jsonObject = new JSONObject(jsonStr);
            if (jsonObject.getInt("code") == 200) {
                JSONArray jsonArray = jsonObject.getJSONArray("friend");

                ArrayList<JSONObject> pArrayList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    pArrayList.add(jsonArray.getJSONObject(i));
                }

                return pArrayList;

            } else
                return null;
        }
    }

    public static String postStatus(String id, String status) throws Exception {
        HashMap<String, String> paramHM = new HashMap<>();
        paramHM.put("id", id);
        paramHM.put("status", status);
        return HttpHelper.postData(MyURL.SET_FINISH_STATUS_BY_UID, paramHM, null);
    }


    public static ArrayList<YouTuTag> getTags(JSONObject jsonObject) {
        ArrayList<YouTuTag> tags = new ArrayList<>();
        YouTuTag youTuTag;
        if (jsonObject == null) {
            youTuTag = new YouTuTag();
            youTuTag.setTagName("error");
            youTuTag.setTagConfidence(0);
            tags.add(youTuTag);
        } else {
            try {
                if (jsonObject.getInt("errorcode") == 0
                        && jsonObject.getString("errormsg").equals("OK")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("tags");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        youTuTag = new YouTuTag();
                        youTuTag.setTagName(jsonArray.getJSONObject(i).getString("tag_name"));
                        youTuTag.setTagConfidence(jsonArray.getJSONObject(i).getInt("tag_confidence"));
                        tags.add(youTuTag);

                    }
                } else {
                    youTuTag = new YouTuTag();
                    youTuTag.setTagName("error");
                    youTuTag.setTagConfidence(0);
                    tags.add(youTuTag);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return tags;
    }

    /**
     * 解析
     *
     * @throws JSONException
     */
    public static HashMap<String, Object> AnalysisUid(
            String jsonStr) throws JSONException {

        if (getCode(jsonStr) == 100) {
            JSONObject jsonObject = new JSONObject(jsonStr);
            int uid = jsonObject.getInt("obj");
            HashMap<String, Object> hm = new HashMap<>();
            hm.put("userid", uid);
            return hm;
        } else {
            return null;
        }

    }

    /**
     * 解析
     *
     * @throws JSONException
     */
    public static HashMap<String, Object> AnalysisUserInfoPose(
            String jsonStr) throws JSONException {

        if (getCode(jsonStr) == 100) {

            JSONObject jsonObject = new JSONObject(jsonStr);
            String objStr = jsonObject.getString("obj");
            JSONObject obj = new JSONObject(objStr);
            HashMap<String, Object> hm = new HashMap<>();

            hm.put("userid", obj.getInt("userId"));
            hm.put("username", obj
                    .getString("userName"));
            hm.put("userphone", obj
                    .getString("userPhone"));
            hm.put("userpb", obj
                    .getInt("userPb"));
            hm.put("taskpic_url", obj
                    .getString("userPicUrl"));
            hm.put("password", obj
                    .getString("passWord"));
            return hm;
        } else {
            return null;
        }

    }

    /**
     * 解析
     *
     * @throws JSONException
     */
    public static HashMap<String, Object> AnalysisSinglePos(
            String jsonStr) throws JSONException {

        if (getCode(jsonStr) == 100) {

            JSONObject jsonObject = new JSONObject(jsonStr);
            String objStr = jsonObject.getString("obj");
            JSONObject obj = new JSONObject(objStr);
            HashMap<String, Object> hm = new HashMap<>();

            hm.put("posid", obj
                    .getInt("postId"));
            hm.put("typeid", obj
                    .getInt("typeId"));
            hm.put("tags", obj
                    .getString("tags"));
            hm.put("userid", obj
                    .getInt("userId"));
            hm.put("pospb", obj
                    .getInt("posePb"));
            hm.put("posname", obj
                    .getString("poseName"));
            hm.put("pos_pic_url", obj
                    .getString("posPicUrl"));
            hm.put("poscontent", obj
                    .getString("posContent"));

            return hm;
        } else {
            return null;
        }

    }


    /**
     * 解析
     *
     * @throws JSONException
     */
    public static ArrayList<HashMap<String, Object>> AnalysisUserInfo2(
            String jsonStr) throws JSONException {
        ArrayList<JSONObject> jsonArrayList = TOJsonArrayPose(jsonStr);
        if (jsonArrayList == null) {
            return null;
        } else {

            ArrayList<HashMap<String, Object>> pArrayList = new ArrayList<>();

            for (int i = 0; i < jsonArrayList.size(); i++) {
                HashMap<String, Object> hm = new HashMap<>();
                hm.put("userid", jsonArrayList.get(i)
                        .getInt("userId"));
                hm.put("username", jsonArrayList.get(i)
                        .getString("userName"));
                hm.put("userphone", jsonArrayList.get(i)
                        .getString("userPhone"));
                hm.put("userpb", jsonArrayList.get(i)
                        .getInt("userPb"));
                hm.put("taskpic_url", jsonArrayList.get(i)
                        .getString("userPicUrl"));
                hm.put("password", jsonArrayList.get(i)
                        .getString("passWord"));
                pArrayList.add(hm);

            }
            return pArrayList;


        }


    }

    /**
     * 解析
     *
     * @throws JSONException
     */
    public static ArrayList<HashMap<String, Object>> AnalysisPosInfo(
            String jsonStr) throws JSONException {
        ArrayList<JSONObject> jsonArrayList = TOJsonArrayPose(jsonStr);
        if (jsonArrayList == null) {
            return null;
        } else {

            ArrayList<HashMap<String, Object>> pArrayList = new ArrayList<>();

            for (int i = 0; i < jsonArrayList.size(); i++) {
                HashMap<String, Object> hm = new HashMap<>();
                hm.put("posid", jsonArrayList.get(i)
                        .getInt("postId"));
                hm.put("typeid", jsonArrayList.get(i)
                        .getInt("typeId"));
                hm.put("tags", jsonArrayList.get(i)
                        .getString("tags"));
                hm.put("pospb", jsonArrayList.get(i)
                        .getInt("posePb"));
                hm.put("posname", jsonArrayList.get(i)
                        .getString("poseName"));
                hm.put("pos_pic_url", jsonArrayList.get(i)
                        .getString("posPicUrl"));
                hm.put("poscontent", jsonArrayList.get(i)
                        .getString("posContent"));
                hm.put("userid", jsonArrayList.get(i)
                        .getInt("userId"));
                pArrayList.add(hm);

            }
            return pArrayList;
        }
    }

    /**
     * 解析
     *
     * @throws JSONException
     */
    public static ArrayList<HashMap<String, Object>> AnalysisTagInfo(
            String jsonStr) throws JSONException {
        ArrayList<JSONObject> jsonArrayList = TOJsonArrayPose(jsonStr);
        if (jsonArrayList == null) {
            return null;
        } else {

            ArrayList<HashMap<String, Object>> pArrayList = new ArrayList<>();

            for (int i = 0; i < jsonArrayList.size(); i++) {
                HashMap<String, Object> hm = new HashMap<>();
                hm.put("tagid", jsonArrayList.get(i)
                        .getInt("tagId"));
                hm.put("tag", jsonArrayList.get(i)
                        .getString("tag"));
                pArrayList.add(hm);

            }
            return pArrayList;


        }
    }


    /**
     * 解析
     *
     * @throws JSONException
     */
    public static ArrayList<HashMap<String, Object>> AnalysisPosInfo2(
            String jsonStr) throws JSONException {
        ArrayList<JSONObject> jsonArrayList = TOJsonArray2(jsonStr);
        if (jsonArrayList == null) {
            return null;
        } else {
            ArrayList<HashMap<String, Object>> pArrayList = new ArrayList<>();
            for (int i = 0; i < jsonArrayList.size(); i++) {
                HashMap<String, Object> hm = new HashMap<>();
                hm.put("posid", jsonArrayList.get(i)
                        .getInt("postId"));
                hm.put("typeid", jsonArrayList.get(i)
                        .getInt("typeId"));
                hm.put("tags", jsonArrayList.get(i)
                        .getString("tags"));
                hm.put("pospb", jsonArrayList.get(i)
                        .getInt("posePb"));
                hm.put("posname", jsonArrayList.get(i)
                        .getString("poseName"));
                hm.put("pos_pic_url", jsonArrayList.get(i)
                        .getString("posPicUrl"));
                hm.put("poscontent", jsonArrayList.get(i)
                        .getString("posContent"));
                hm.put("userid", jsonArrayList.get(i)
                        .getInt("userId"));
                pArrayList.add(hm);

            }
            return pArrayList;


        }
    }

    /**
     * 解析
     *
     * @throws JSONException
     */
    public static ArrayList<JSONObject> TOJsonArrayPose(
            String jsonStr) throws JSONException {
        if (jsonStr == null) {
            return null;
        } else {
            JSONObject jsonObject = new JSONObject(jsonStr);
            if (jsonObject.getInt("code") == 100) {
                JSONArray jsonArray = jsonObject.getJSONArray("obj");

                ArrayList<JSONObject> pArrayList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    pArrayList.add(jsonArray.getJSONObject(i));
                }

                return pArrayList;

            } else
                return null;
        }
    }

    /**
     * 解析
     *
     * @throws JSONException
     */
    public static ArrayList<JSONObject> TOJsonArray2(
            String jsonStr) throws JSONException {
        if (jsonStr == null) {
            return null;
        } else {
            JSONArray jsonArray = new JSONArray(jsonStr);
            ArrayList<JSONObject> pArrayList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                pArrayList.add(jsonArray.getJSONObject(i));
            }
            return pArrayList;
        }
    }


    /**
     * 根据jsonData任务数据获取数据
     *
     * @param jsonData
     * @return
     */
    public static List<BeanTask> getBeanTasksFromJson(String jsonData) {
        if (jsonData == null || jsonData.length()==0) {
            return null;
        }
        List<BeanTask> result = new ArrayList<>();
        try {
            List<JSONObject> tasks = TOJsonArray(jsonData);
            if (tasks!=null || tasks.size()>0) {
                for (int i = 0; i < tasks.size(); i++) {
                    result.add(getBeanTaskFromJsonObject(tasks.get(i)));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 根据data列表里面的数据一条一条的分开
     *
     * @param jsonObject
     * @return
     */

    public static BeanTask getBeanTaskFromJsonObject(JSONObject jsonObject) {
        BeanTask beanTask = new BeanTask();
        try {
            beanTask.setTaskId(jsonObject.getInt("id"));
            beanTask.setTaskUserID(jsonObject.getInt("taskuserid"));
            beanTask.setTaskType(jsonObject.getInt("tasktype"));
            beanTask.setTaskName((String) jsonObject.get("taskname"));
            beanTask.setTaskIamge((String) jsonObject.get("taskimage"));

            beanTask.setTaskContent((String) jsonObject.get("taskcontent"));
            beanTask.setTaskDeadLine(jsonObject.getString("taskdeadline"));
            beanTask.setTaskPublishTime(jsonObject.getString("taskpublishtime"));
            beanTask.setTaskLatitude((String) jsonObject.get("tasklatitude"));
            beanTask.setTaskLongitude((String) jsonObject.get("tasklongitude"));

            beanTask.setTaskTakenNum((Integer) jsonObject.get("tasktakennum"));
            beanTask.setTaskMaxNum((Integer) jsonObject.get("taskmaxnum"));
            beanTask.setTaskLabel((String) jsonObject.get("tasklabel"));
            beanTask.setTaskMoney((Integer) jsonObject.get("taskmoney"));
            beanTask.setTaskStatus((Integer) jsonObject.get("taskstatus"));

            beanTask.setTaskScore((float) jsonObject.getDouble("taskscore"));
            beanTask.setAvgScore((float) jsonObject.getDouble("avgscore"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return beanTask;
    }


    public static List<BeanTask> getBeanTasksFromJson2(String jsonData) {
        if (jsonData == null) {
            return null;
        }
        List<BeanTask> result = new ArrayList<>();
        try {
            List<JSONObject> tasks = TOJsonArray(jsonData);
            for (int i = 0; i < tasks.size(); i++) {
                result.add(getBeanTaskFromJsonObject2(tasks.get(i)));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static BeanTask getBeanTaskFromJsonObject2(JSONObject jsonObject) {
        BeanTask beanTask = new BeanTask();
        try {
            String objStr = jsonObject.getString("task");
            JSONObject taskObj = new JSONObject(objStr);
            beanTask.setTaskId(taskObj.getInt("id"));
            beanTask.setTaskUserID(taskObj.getInt("taskuserid"));
            beanTask.setTaskType(taskObj.getInt("tasktype"));
            beanTask.setTaskName((String) taskObj.get("taskname"));
            beanTask.setTaskIamge((String) taskObj.get("taskimage"));

            beanTask.setTaskContent((String) taskObj.get("taskcontent"));
            beanTask.setTaskDeadLine(taskObj.getString("taskdeadline"));
            beanTask.setTaskPublishTime(taskObj.getString("taskpublishtime"));
            beanTask.setTaskLatitude((String) taskObj.get("tasklatitude"));
            beanTask.setTaskLongitude((String) taskObj.get("tasklongitude"));

            beanTask.setTaskTakenNum((Integer) taskObj.get("tasktakennum"));
            beanTask.setTaskMaxNum((Integer) taskObj.get("taskmaxnum"));
            beanTask.setTaskLabel((String) taskObj.get("tasklabel"));
            beanTask.setTaskMoney((Integer) taskObj.get("taskmoney"));
            beanTask.setTaskStatus((Integer) taskObj.get("taskstatus"));

            beanTask.setTaskScore((float) taskObj.getDouble("taskscore"));
            beanTask.setAvgScore((float) taskObj.getDouble("avgscore"));
            beanTask.setAvatar(jsonObject.getString("avatar"));
            beanTask.setSex(Integer.parseInt(jsonObject.getString("sex").trim().length()==0?"0":jsonObject.getString("sex")));
            beanTask.setUserName(jsonObject.getString("userName"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return beanTask;
    }


    /**
     * 从服务器获取兴趣标签
     *
     * @param jsonData
     * @return
     * @throws JSONException
     */

    public static List<BeanTag> AJResultGetInterestTagBySex(String jsonData) throws JSONException {


        List<BeanTag> result = new ArrayList<>();
        if (getCode(jsonData) == 200) {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray dataArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject jsonObject1 = (JSONObject) dataArray.get(i);
                BeanTag beanTag = new BeanTag();
                beanTag.setId((Integer) jsonObject1.get("id"));
                beanTag.setType((Integer) jsonObject1.get("tagType"));
                beanTag.setTag((String) jsonObject1.get("tag"));
                result.add(beanTag);
            }

        }
        return result;
    }


    public static List<BeanRecyclerViewItem> AJResultGetUserByDis(String jsonData) throws JSONException {
        List<BeanRecyclerViewItem> result = new ArrayList<>();
        if (getCode(jsonData) == 200) {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray array = jsonObject.getJSONArray("data");
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject1 = (JSONObject) array.get(i);
                BeanRecyclerViewItem item = new BeanRecyclerViewItem();
                item.setDis(jsonObject1.getDouble("distance"));

                String objStr = jsonObject1.getString("user");
                JSONObject userObj = new JSONObject(objStr);

                item.setUrlAvatar((String) userObj.get("avator"));
                item.setUserId((Integer) userObj.get("id"));
                int sex = userObj.getString("sex").trim().length() == 0 ?
                        0 : Integer.parseInt(userObj.getString("sex").trim());
                item.setSex(sex);
                item.setAge(userObj.getInt("age"));
                item.setWeight(userObj.getInt("weight"));
                item.setHeight(userObj.getInt("height"));
                item.setSignature(userObj.getString("signature"));
                item.setUsername(userObj.getString("username"));
                item.setHobby(userObj.getString("hobby"));

                result.add(item);
            }
        }
        return result;
    }


    public static List<BeanUser> AJResultGetUsers(String jsonStr) throws  JSONException {
        if (jsonStr == null) {
            return null;
        }
        List<BeanUser> result = new ArrayList<>();
        try {
            List<JSONObject> users = TOJsonArray(jsonStr);
            for (int i = 0; i < users.size(); i++) {
                result.add(getBeanUser(users.get(i)));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static BeanUser getBeanUser(JSONObject jsonObject) {
        BeanUser beanUser = new BeanUser();
        try {
            beanUser.setId(jsonObject
                    .getInt("id"));
            beanUser.setAge(jsonObject
                    .getInt("age"));
            beanUser.setAvatar(jsonObject
                    .getString("avator"));
            beanUser.setBackImage(jsonObject
                    .getString("backimage"));
            beanUser.setCheckinDays(jsonObject
                    .getInt("checkindays"));
            beanUser.setStatus(jsonObject
                    .getInt("stauts"));
            beanUser.setExp(jsonObject
                    .getInt("exp"));
            beanUser.setHeight(jsonObject
                    .getInt("height"));
            beanUser.setWeight(jsonObject
                    .getInt("weight"));
            beanUser.setHobby(jsonObject
                    .getString("hobby"));
            beanUser.setPhoneNumber(jsonObject
                    .getString("phonenumber"));
            beanUser.setUserName(jsonObject
                    .getString("username"));
            beanUser.setQq(jsonObject
                    .getString("qq"));
            beanUser.setSignature(jsonObject
                    .getString("signature"));
            beanUser.setLevel(jsonObject
                    .getInt("level"));
            beanUser.setInviteCode(jsonObject
                    .getString("invitecode"));
            beanUser.setLatitude(jsonObject
                    .getString("latitude"));
            beanUser.setLongtitude(jsonObject
                    .getString("longtitude"));
            beanUser.setPassWord(jsonObject
                    .getString("password"));
            beanUser.setSex(jsonObject
                    .getString("sex"));
            beanUser.setRegDate(jsonObject
                    .getString("regdate"));
            beanUser.setWexID(jsonObject
                    .getString("wexid"));
            beanUser.setStepsToday(jsonObject
                    .getInt("stepstoday"));
            beanUser.setMoney(jsonObject.getInt("money"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return beanUser;
    }



    public static List<BeanActCmt> AJActCmt(String jsonStr) throws JSONException {
        if (jsonStr == null) {
            return null;
        }
        List<BeanActCmt> result = new ArrayList<>();
        try {
            List<JSONObject> tasks = TOJsonArray(jsonStr);
            for (int i = 0; i < tasks.size(); i++) {
                result.add(getActCmtFromJsonObject(tasks.get(i)));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static BeanActCmt getActCmtFromJsonObject(JSONObject jsonObject) {

        BeanActCmt beanActCmt = new BeanActCmt();
        try {
            String objStr = jsonObject.getString("taskComment");
            JSONObject actCmtObj = new JSONObject(objStr);
            beanActCmt.setId(actCmtObj.getInt("id"));
            beanActCmt.setCommentdate(actCmtObj.getString("commentdate"));
            beanActCmt.setTaskcomment(actCmtObj.getString("taskcomment"));
            beanActCmt.setTaskid(actCmtObj.getInt("taskid"));
            beanActCmt.setTaskscore((float) actCmtObj.getDouble("taskscore"));
            beanActCmt.setUserid(actCmtObj.getInt("userid"));

            beanActCmt.setAvator(jsonObject.getString("avator"));
            beanActCmt.setSex(jsonObject.getString("sex"));
            beanActCmt.setUserName(jsonObject.getString("userName"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return beanActCmt;
    }







    public static List<BeanViewPagerItem> AJResultGetUserByMatchIndex(String jsonStr) throws JSONException {
        if (jsonStr == null) {
            return null;
        }
        List<BeanViewPagerItem> result = new ArrayList<>();
        try {
            List<JSONObject> tasks = TOJsonArray(jsonStr);
            for (int i = 0; i < tasks.size(); i++) {
                result.add(getBeanUserFromJsonObject(tasks.get(i)));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static BeanViewPagerItem getBeanUserFromJsonObject(JSONObject jsonObject) {
        BeanUser beanUser = new BeanUser();
        BeanViewPagerItem beanViewPagerItem = new BeanViewPagerItem();
        try {
            String objStr = jsonObject.getString("user");
            JSONObject userObj = new JSONObject(objStr);
            beanUser.setId(userObj
                    .getInt("id"));
            beanUser.setAge(userObj
                    .getInt("age"));
            beanUser.setAvatar(userObj
                    .getString("avator"));
            beanUser.setBackImage(userObj
                    .getString("backimage"));
            beanUser.setCheckinDays(userObj
                    .getInt("checkindays"));
            beanUser.setStatus(userObj
                    .getInt("stauts"));
            beanUser.setExp(userObj
                    .getInt("exp"));
            beanUser.setHeight(userObj
                    .getInt("height"));
            beanUser.setWeight(userObj
                    .getInt("weight"));
            beanUser.setHobby(userObj
                    .getString("hobby"));
            beanUser.setPhoneNumber(userObj
                    .getString("phonenumber"));
            beanUser.setUserName(userObj
                    .getString("username"));
            beanUser.setQq(userObj
                    .getString("qq"));
            beanUser.setSignature(userObj
                    .getString("signature"));
            beanUser.setLevel(userObj
                    .getInt("level"));
            beanUser.setInviteCode(userObj
                    .getString("invitecode"));
            beanUser.setLatitude(userObj
                    .getString("latitude"));
            beanUser.setLongtitude(userObj
                    .getString("longtitude"));
            beanUser.setPassWord(userObj
                    .getString("password"));
            beanUser.setSex(userObj
                    .getString("sex"));
            beanUser.setRegDate(userObj
                    .getString("regdate"));
            beanUser.setWexID(userObj
                    .getString("wexid"));
            beanUser.setStepsToday(userObj
                    .getInt("stepstoday"));
            beanUser.setMoney(userObj.getInt("money"));
            beanViewPagerItem.setBeanUser(beanUser);
            beanViewPagerItem.setMatchDegree(jsonObject.getInt("matchIndex"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return beanViewPagerItem;
    }
}
