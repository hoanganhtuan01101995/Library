package com.hoanganhtuan01101995.update;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

/**
 * Created by Hoang Anh Tuan on 11/1/2017.
 */

class UpdateDialog extends Dialog implements View.OnClickListener {

    static void instance(Context context) {
        UpdateDialog updateDialog = new UpdateDialog(context);
        updateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        updateDialog.setCanceledOnTouchOutside(true);
        updateDialog.show();
    }

    private UpdateDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_update);
        initSizeDialog();

        Button btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);
    }

    private void initSizeDialog() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        if (this.getWindow() != null) layoutParams.copyFrom(this.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        this.getWindow().setAttributes(layoutParams);
    }

    @Override
    public void onClick(View view) {
        dismiss();
        Uri uri = Uri.parse("market://details?id=" + getContext().getApplicationContext().getPackageName());
        Intent rateAppIntent = new Intent(Intent.ACTION_VIEW, uri);
        if (getContext().getPackageManager().queryIntentActivities(rateAppIntent, 0).size() > 0) {
            getContext().startActivity(rateAppIntent);
        }
    }
}
