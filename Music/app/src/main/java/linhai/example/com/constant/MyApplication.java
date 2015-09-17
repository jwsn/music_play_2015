package linhai.example.com.constant;

import android.app.Application;
import android.content.Context;
import android.view.WindowManager;

import org.litepal.LitePalApplication;

/**
 * Created by linhai on 15/5/9.
 */
public class MyApplication extends Application {
    /**
     * 创建全局变量
     * 全局变量一般都比较倾向于创建一个单独的数据类文件，并使用static静态变量
     * 这里使用了在Applicatioin中添加数据的方法实现全局变量
     * 注意在AndroidManifest.xml中的Application节点添加Android:name=".MyApplication"属性
     */
    private WindowManager.LayoutParams wmParms = new WindowManager.LayoutParams();

    public WindowManager.LayoutParams getMywnParams(){
        return wmParms;
    }

    private static Context context;

    @Override
    public void onCreate(){
        context = getApplicationContext();
        LitePalApplication.initialize(context);
    }

    public static Context getContext(){
        return context;
    }
}