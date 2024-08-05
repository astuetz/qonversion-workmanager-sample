package com.astuetz.qonversionworkmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.astuetz.qonversionworkmanager.ui.theme.QonversionWorkManagerTheme
import kotlin.time.Duration.Companion.seconds

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val context = LocalContext.current

            QonversionWorkManagerTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Button(
                        onClick = {
                            GetOfferingsWorker.schedule(
                                context = context,
                                timestamp = System.currentTimeMillis() + 15.seconds.inWholeMilliseconds,
                            )
                        }
                    ) {
                        Text(text = "Schedule Worker")
                    }
                }
            }
        }
    }
}
