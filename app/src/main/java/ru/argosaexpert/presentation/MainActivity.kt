package ru.argosaexpert.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.argosaexpert.R
import ru.argosaexpert.presentation.utils.PermissionTools

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PermissionTools.checkAndRequestPermissions(this)
        PermissionTools.checkStoragePermissions(this)
    }
}