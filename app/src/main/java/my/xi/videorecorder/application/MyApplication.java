package my.xi.videorecorder.application;

import android.app.Application;

public class MyApplication extends Application {

    private static MyApplication application;

    public static MyApplication getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public void exit(int status) {
        System.exit(status);
    }


}
