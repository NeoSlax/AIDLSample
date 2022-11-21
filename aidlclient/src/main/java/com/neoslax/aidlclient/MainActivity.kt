package com.neoslax.aidlclient

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.neoslax.aidlclient.ui.theme.AIDLSampleTheme
import com.neoslax.common_aidl.ISumNumsAIDL

class MainActivity : ComponentActivity() {

    private var twoNumSum: ISumNumsAIDL? = null

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AIDLSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,

                    ) {
                    val a = remember {
                        mutableStateOf("0")
                    }

                    val b = remember {
                        mutableStateOf("0")
                    }

                    val result = remember {
                        mutableStateOf("0")
                    }
//Box(modifier = Modifier.fillMaxSize())
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TextField(
                            value = a.value,
                            onValueChange = { a.value = it },
                            label = {},
                            singleLine = true
                        )
                        TextField(
                            value = b.value,
                            onValueChange = { b.value = it },
                            label = {},
                            singleLine = true
                        )
                        Text(text = result.value)
                        Button(onClick = {
                            twoNumSum?.let {
                                val sum = it.sumTwoNumbers(a.value.toInt(), b.value.toInt())
                                result.value = sum.toString()
                            }
                        }) {
                            Text(text = "Calculate")
                        }
                    }
                }
            }
        }
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            twoNumSum = ISumNumsAIDL.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            twoNumSum = null
        }
    }

    override fun onStart() {
        super.onStart()
        bindService(createExplicitIntent(), serviceConnection, BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        unbindService(serviceConnection)
    }

    private fun createExplicitIntent(): Intent {
        val intent = Intent("com.neslax.aidl.CUSTOM_REMOTE")
        val services = packageManager.queryIntentServices(intent, 0)
        if (services.isEmpty()) {
            throw IllegalStateException("Приложение-сервер не установлено")
        }
        return Intent(intent).apply {
            val resolveInfo = services[0]
            val packageName = resolveInfo.serviceInfo.packageName
            val className = resolveInfo.serviceInfo.name
            component = ComponentName(packageName, className)
        }
    }
}



