package sc.gys.wcx.and.help;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* 作者  王从文 */
/* 全局采用注解模式 */
/* 旺苍县公安局巡防系统 json数据解析 */

/**
 * @ 解释: 解析用户登陆信息 GetJsonRegiste
 * @ 解释:
 *
 */

public class JSON {

    /* 用户登陆信息解析 */
    public static Map<String, String> GetJsonRegiste(String mJson) {
        System.out.println(mJson);
        if(mJson.length() == 0) {
            throw new IllegalArgumentException("传入参数不能为空 大小为"+mJson.length()+" is null option");
        }else {
            if(mJson == "err") {
                throw new IllegalArgumentException("账号密码错误"+mJson+" is 'error' ");

            }else {
                try {
                    Map<String, String> map=new HashMap<>();

                    JSONObject object=new JSONObject(mJson);
                    map.put("email", object.optString("email"));
                    map.put("headPortrait", object.optString("headPortrait"));
                    map.put("id", object.optString("id"));
                    map.put("nickname", object.optString("nickname"));
                    map.put("numId", object.optString("numId"));
                    map.put("password", object.optString("password"));
                    map.put("phone", object.optString("phone"));
                    map.put("qrCode", object.optString("qrCode"));
                    map.put("salt", object.optString("salt"));
                    map.put("truename", object.optString("truename"));
                    map.put("username", object.optString("username"));
                    map.put("weixin", object.optString("weixin"));
                    map.put("danweiid", object.optString("danweiid")); //单位ID
                    map.put("jobid", object.optString("jobid"));       //警员编号
                    map.put("zhiwu", object.optString("zhiwu"));       //职务
                    map.put("police", object.optString("police"));     //警察单位

                    if(map.size() != 0)
                        return map;

                } catch (JSONException e) {
                    throw new IllegalArgumentException("在解析 GetJsonRegiste 发生未知异常" + mJson);
                }
            }
        }
        return new HashMap<>();
    }

    /* 统一数据JSON */
    /**
     * @ 解释 使用于当只有一层的时候 jsonArray -  jsonObject
     * @param mJson 传递过来的原始参数
     * @param keyString 需要解析的键值对关系
     */
    public static List<Map<String,String>> GetJson(String mJson, String[] keyString){
        if(mJson == null || keyString == null){
            return new ArrayList<>();
        }

        try {
            List<Map<String,String>> data = new ArrayList<>();

            //便利所有的键
            List<String> mKey = new ArrayList<>();
            for (String str:keyString) {
                mKey.add(str);
            }

            JSONArray arr = new JSONArray(mJson);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject object=arr.getJSONObject(i);

                Map<String, String> map=new HashMap<>();
                for (int j = 0; j < mKey.size(); j++) {
                    map.put(mKey.get(j), object.optString(mKey.get(j)));
                }
                data.add(map);
            }
            return data;
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return new ArrayList<>();
    }


    /**
     * {@linkplain=Android} 适应与当只有一层Object的时候
     * 使用此方法
     * @param json
     * @param key string[]{}
     * @return 返回的类型  Map<String, Object>
     */
    public static Map<String, Object> getObjectJson(String json,String[] key) {
        try {
            if(json == null || key.length == 0)
                return new HashMap<>();
            //返回的集合
            Map<String, Object> data=new HashMap<>();
            JSONObject object=new JSONObject(json);
            List<String> mKey=new ArrayList<>();
            for (int i = 0; i < key.length; i++) {
                mKey.add(key[i]);
            }

            for (int i = 0; i < mKey.size(); i++) {
                data.put(mKey.get(i), object.get(mKey.get(i)));
            }
            if(data.size() != 0)
                return data;

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new HashMap<>();
    }


}
