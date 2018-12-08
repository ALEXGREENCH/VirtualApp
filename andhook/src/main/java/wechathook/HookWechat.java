package wechathook;

import android.content.Context;
import android.util.Log;

import andhook.lib.AndHook;
import andhook.lib.HookHelper;

public class HookWechat {
    public static String TAG = "HookWechat";
    private static HookWechat instance = null;
    public static HookWechat getInstance(){
        if(instance == null){
            synchronized (HookWechat.class){
                instance = new HookWechat();
            }
        }
        return instance;
    }
    public void run (Context context){
        Log.e(TAG,"start:::run");
        AndHook.ensureNativeLibraryLoaded(null);
        HookHelper.applyHooks(HookWechatFunction.class , context.getClassLoader());
    }
}
