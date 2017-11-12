package com.hoanganhtuan01101995.permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HoangAnhTuan on 9/5/2017.
 */

public class Permission {

    private OnPermissionFeedbackListener onPermissionFeedbackListener;
    private int requestCode;

    private Activity activity;

    Permission(Builder builder) {
        this.activity = builder.activity;
        this.onPermissionFeedbackListener = builder.onPermissionFeedbackListener;
    }

    public void askPermissions(int requestCode, String... permissions) {
        this.requestCode = requestCode;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            final List<String> permissionsToRequest = new ArrayList<>();
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsToRequest.add(permission);
                }
            }
            if (!permissionsToRequest.isEmpty()) {
                ActivityCompat.requestPermissions(activity, permissionsToRequest.toArray(new String[permissionsToRequest.size()]), this.requestCode);
            } else {
                onPermissionFeedbackListener.accept(this.requestCode);
            }
        } else {
            onPermissionFeedbackListener.accept(this.requestCode);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull int[] grantResults) {
        if (requestCode == this.requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onPermissionFeedbackListener.accept(this.requestCode);
            } else {
                onPermissionFeedbackListener.reject(this.requestCode);
            }
        }
    }

    public static class Builder {

        Permission.OnPermissionFeedbackListener onPermissionFeedbackListener;
        Activity activity;

        public static Builder with(Activity activity) {
            return new Builder(activity);
        }

        private Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder setOnPermissionFeedbackListener(Permission.OnPermissionFeedbackListener onPermissionFeedbackListener) {
            this.onPermissionFeedbackListener = onPermissionFeedbackListener;
            return this;
        }

        public Permission build(){
            return new Permission(this);
        }
    }

    public static interface OnPermissionFeedbackListener {
        void accept(int requestCode);

        void reject(int requestCode);
    }

}
