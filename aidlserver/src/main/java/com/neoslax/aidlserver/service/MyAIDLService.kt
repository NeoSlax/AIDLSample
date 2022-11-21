package com.neoslax.aidlserver.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.neoslax.common_aidl.ISumNumsAIDL

class MyAIDLService : Service() {

    override fun onBind(intent: Intent?): IBinder {
        return object : ISumNumsAIDL.Stub() {
            override fun sumTwoNumbers(a: Int, b: Int): Int {
                return a + b
            }
        }
    }


}