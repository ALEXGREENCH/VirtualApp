package wechathook;

import android.util.Log;

import andhook.lib.HookHelper;

public class HookWechatFunction {

    @HookHelper.Hook(value = "com.tencent.mm.ui.MMActivity" , name = "onResume")
    public static void onResume(Object thiz){
        Log.e(HookWechat.TAG , "com.tencent.mm.ui.MMActivity:::onResume");
        HookHelper.invokeVoidOrigin(thiz);
    }
    @HookHelper.Hook(value = "com.tencent.mm.sdk.platformtools.bk", name = "UZ")
    public static long uz(Class staticClass){
        Log.e(HookWechat.TAG , "com.tencent.mm.sdk.platformtools.bk:::UZ:::hook;");
        long orginal = HookHelper.invokeLongOrigin(staticClass);
        return  orginal;
    }
    @HookHelper.Hook(value = "java.lang.reflect.Field", name = "get")
    public static Object get(Object thiz, Object object){
        Log.e(HookWechat.TAG , "java.lang.reflect.Field:::get:::hook:");
        return HookHelper.invokeObjectOrigin(thiz, object);
    }
    //@HookHelper.Hook(value = "")
}
