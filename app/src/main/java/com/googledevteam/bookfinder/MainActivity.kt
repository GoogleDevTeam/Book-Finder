package com.googledevteam.bookfinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.googledevteam.bookfinder.ui.BookFinderScreen
import com.googledevteam.bookfinder.ui.theme.BookFinderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookFinderTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    BookFinderScreen()
                }
            }
        }
    }
}
