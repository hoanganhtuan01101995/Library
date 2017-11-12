package com.hoanganhtuan01101995.admob.video;

import android.app.Activity;

/**
 * Created by Hoang Anh Tuan on 11/10/2017.
 */

public interface VideoAdService {
    void resume(Activity activity);

    void pause(Activity activity);

    void destroy(Activity activity);

    void show();
}
