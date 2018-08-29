package com.deepaksharma.webaddicted.fileWrite;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.deepaksharma.webaddicted.Logger;

public class FileWrite {

    public static void writeFile(@NonNull Context context, @NonNull String message) {
        if(Build.VERSION.SDK_INT >= 23){
            Intent intent = new Intent(context, PermissionActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            new Logger().permissionInit(message);
        }
        new Logger().permissionInit(message);
    }

    public static boolean logFile(String strLog) {
        /*String systemTime = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss").format(System
                .currentTimeMillis());
        Log.d("FileWrite", "logFile: "+ systemTime);
        Log.d("FileWrite", "logFile Msg: "+ strLog);*/
        File dirFile = new File(Environment.getExternalStorageDirectory() + "/TALOG");
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File file = new File(dirFile, "talog" + ".txt");
        FileOutputStream fileOutputStream = null;
        try {
            String stackString = strLog;
            if (stackString.length() > 0) {
                file.createNewFile();
                fileOutputStream = new FileOutputStream(file,true);
                fileOutputStream.write(stackString.getBytes());
                fileOutputStream.flush();
                fileOutputStream.close();
                return true;
            }
        } catch (FileNotFoundException fileNotFoundException) {
            Log.e("TAG", "File not found!", fileNotFoundException);
        } catch (IOException ioException) {
            Log.e("TAG", "Unable to write to file!", ioException);
        }
        return false;
    }


}
