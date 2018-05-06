package tiny.tinystatistics.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tiny.tinystatistics.MyApp;

import static android.content.Context.MODE_PRIVATE;

/**
 * 类描述
 * 创建者:tiny
 * 日期:18/3/13
 */

public class StatisticsUtils {
    public static String mCurrentFragment = "";
    public static String mCurrentActivity = "";
    public static Map<String, String> maps = new HashMap<>();

    static {
        SharedPreferences sp = MyApp.mContext.getSharedPreferences("test", MODE_PRIVATE);
        String data = sp.getString("data", "");
        try {
            JSONArray ja = new JSONArray(data);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                String n1 = jo.optString("interfaceName");
                String n2 = jo.optString("idName");
                String event = jo.optString("event");
                maps.put(n1 + n2, event);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void onActivityResume(Activity activity) {
        mCurrentActivity = activity.getClass().getName();

    }

    public static void onClick(View view, String className) {
        Log.i("lpc", "className is " + className);
        String format = className.substring(0, className.lastIndexOf("$")).replaceAll("/", ".");
        Log.i("lpc", "format className is " + format);
        String idName = view.getResources().getResourceName(view.getId());
        String id2 = idName.substring(idName.lastIndexOf("/") + 1);
        if (maps.containsKey(format + id2)) {
            Log.i("lpc", "view click " + view.getResources().getResourceName(view.getId()));
        }
    }

    public static void onFragmentResume(Fragment fragment) {
        mCurrentFragment = fragment.getClass().getName();
    }

    public static void setFragmentUserVisibleHint(Fragment fragment) {
        if (fragment.getUserVisibleHint()) {
            mCurrentFragment = fragment.getClass().getName();
        }
    }

    public static void onFragmentHiddenChanged(Fragment fragment) {
        if (!fragment.isHidden()) {
            mCurrentFragment = fragment.getClass().getName();
        }
    }

}
