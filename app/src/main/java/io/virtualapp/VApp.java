package io.virtualapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.flurry.android.FlurryAgent;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.stub.VASettings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.virtualapp.delegate.MyAppRequestListener;
import io.virtualapp.delegate.MyComponentDelegate;
import io.virtualapp.delegate.MyPhoneInfoDelegate;
import io.virtualapp.delegate.MyTaskDescriptionDelegate;
import jonathanfinerty.once.Once;

/**
 * @author Lody
 */
public class VApp extends MultiDexApplication {

    private static VApp gApp;
    private SharedPreferences mPreferences;

    public static VApp getApp() {
        return gApp;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mPreferences = base.getSharedPreferences("va", Context.MODE_MULTI_PROCESS);
        VASettings.ENABLE_IO_REDIRECT = true;
        VASettings.ENABLE_INNER_SHORTCUT = false;
        try {
           /**
             * andhook  add by tang 18/12/02
                    * 加载assets文件夹中的hook.jar文件到
                    * /data/user/0/com.godinsec.godinsec_private_space/app_HookDex目录下
                    */
            Log.e("HookDemo", "attachBaseContext:download");
            download("HookDex",base);

            VirtualCore.get().startup(base);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    /**
     * andhook  add by tang 18/12/02
     * 加载lib模块中的assets文件夹中的hook.jar文件到
     * /data/user/0/com.godinsec.godinsec_private_space/app_HookDex目录下
     */
    public String download(String module,Context context) {
        File dex = null;
        if (module.equals("HookDex")) {
            File check = context.getDir("HookDex", Context.MODE_PRIVATE);
            dex = new File(check, "hook.dex.jar");
            Log.e("HookDemo", "download::" + dex);
            if(!dex.exists()){
                try {
                    AssetManager assetManager = context.getAssets();
                    InputStream is = null;
                    is = assetManager.open("hook.jar");
                    FileOutputStream fos = new FileOutputStream(dex);
                    byte[] bytes = new byte[is.available()];
                    while ((is.read(bytes)) != -1) {
                        fos.write(bytes);
                    }
                    is.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return dex.getAbsolutePath();
    }
    @Override
    public void onCreate() {
        gApp = this;
        super.onCreate();
        VirtualCore virtualCore = VirtualCore.get();
        virtualCore.initialize(new VirtualCore.VirtualInitializer() {

            @Override
            public void onMainProcess() {
                Once.initialise(VApp.this);
                new FlurryAgent.Builder()
                        .withLogEnabled(true)
                        .withListener(() -> {
                            // nothing
                        })
                        .build(VApp.this, "48RJJP7ZCZZBB6KMMWW5");
            }

            @Override
            public void onVirtualProcess() {
                //listener components
                virtualCore.setComponentDelegate(new MyComponentDelegate());
                //fake phone imei,macAddress,BluetoothAddress
                virtualCore.setPhoneInfoDelegate(new MyPhoneInfoDelegate());
                //fake task description's icon and title
                virtualCore.setTaskDescriptionDelegate(new MyTaskDescriptionDelegate());
            }

            @Override
            public void onServerProcess() {
                virtualCore.setAppRequestListener(new MyAppRequestListener(VApp.this));
                virtualCore.addVisibleOutsidePackage("com.tencent.mobileqq");
                virtualCore.addVisibleOutsidePackage("com.tencent.mobileqqi");
                virtualCore.addVisibleOutsidePackage("com.tencent.minihd.qq");
                virtualCore.addVisibleOutsidePackage("com.tencent.qqlite");
                virtualCore.addVisibleOutsidePackage("com.facebook.katana");
                virtualCore.addVisibleOutsidePackage("com.whatsapp");
                virtualCore.addVisibleOutsidePackage("com.tencent.mm");
                virtualCore.addVisibleOutsidePackage("com.immomo.momo");
            }
        });
    }

    public static SharedPreferences getPreferences() {
        return getApp().mPreferences;
    }

}
