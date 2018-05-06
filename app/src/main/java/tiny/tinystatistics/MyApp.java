package tiny.tinystatistics;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * 类描述
 * 创建者:tiny
 * 日期:18/4/18
 */

public class MyApp extends Application {
    public static Context mContext;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mContext = base;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        String data = "[\n" +
                "    {\n" +
                "        \"interfaceName\": \"tiny.tinystatistics.HomeFragment\",\n" +
                "        \"idName\": \"tv_test\",\n" +
                "        \"event\": \"testFragment\"\n" +
                "    },\n" +
                "    {\n" +
                "        \"interfaceName\": \"tiny.tinystatistics.MainActivity\",\n" +
                "        \"idName\": \"tv_test\",\n" +
                "        \"event\": \"testActivity\"\n" +
                "    }\n" +
                "]";
        SharedPreferences sp = getSharedPreferences("test", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("data", data);
        editor.apply();
    }
}
