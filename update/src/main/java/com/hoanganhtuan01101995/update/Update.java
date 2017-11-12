package com.hoanganhtuan01101995.update;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Hoang Anh Tuan on 11/1/2017.
 */

public class Update {

    private static final String VERSION = "version";

    private Context context;

    public static Builder with(Context context) {
        return new Builder(context);
    }

    public static class Builder {
        private Update update;

        Builder(Context context) {
            update = new Update(context);
        }

        public Update build() {
            update.checkVersion();
            return update;
        }
    }

    private int versionCodeLocation;

    private DatabaseReference databaseReference;
    private String packageName;

    Update(Context context) {
        this.context = context;
        packageName = context.getPackageName();
        packageName = packageName.replace(".", "");
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private void checkVersion() {
        try {
            infoFromLocal();
            databaseReference.child(packageName)
                    .child(VERSION)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            removerListener();
                            infoFromServer(dataSnapshot);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            removerListener();
                        }

                        private void removerListener() {
                            databaseReference.child(packageName)
                                    .child(VERSION)
                                    .removeEventListener(this);
                        }
                    });
        } catch (Exception ignored) {
        }
    }

    private void infoFromServer(DataSnapshot dataSnapshot) {
        try {
            int versionCodeServer = Integer.parseInt(dataSnapshot.getValue().toString());
            if (versionCodeLocation < versionCodeServer) {
                UpdateDialog.instance(context);
            } else if (versionCodeLocation > versionCodeServer) {
                throw new Exception();
            }
        } catch (Exception e) {
            uploadNewVersion();
        }
    }

    private void infoFromLocal() throws PackageManager.NameNotFoundException {
        PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        versionCodeLocation = packageInfo.versionCode;
    }

    private void uploadNewVersion() {
        databaseReference.child(packageName)
                .child(VERSION)
                .setValue(versionCodeLocation);
    }

}
