package ru.argosaexpert.presentation.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker

object PermissionTools {
    private val REQUIRED_PERMISSION_LIST = arrayOf(
        Manifest.permission.ACCESS_WIFI_STATE,  // WIFI connected products
        Manifest.permission.ACCESS_COARSE_LOCATION,  // Maps
        Manifest.permission.ACCESS_NETWORK_STATE,  // WIFI connected products
        Manifest.permission.ACCESS_FINE_LOCATION,  // Maps
        Manifest.permission.CHANGE_WIFI_STATE,  // Changing between WIFI and USB connection
        Manifest.permission.WRITE_EXTERNAL_STORAGE,  //  files
        Manifest.permission.READ_EXTERNAL_STORAGE,  //  files
        Manifest.permission.MANAGE_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
        Manifest.permission.READ_MEDIA_IMAGES,
        Manifest.permission.CAMERA,
        Manifest.permission.NEARBY_WIFI_DEVICES
    )

    private const val REQUEST_PERMISSION_CODE = 12345
    private val missingPermission: MutableList<String> = ArrayList()

    fun checkAccessAllFiles(activity: Activity, action:() -> Unit) {
        if (Build.VERSION.SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                try {
                    val intent = Intent("android.settings.MANAGE_APP_ALL_FILES_ACCESS_PERMISSION")
                    intent.addCategory("android.intent.category.DEFAULT")
                    intent.data = Uri.parse(
                        String.format(
                            "package:%s",
                            *arrayOf<Any>(activity.applicationContext.applicationContext.packageName)
                        )
                    )
                    activity.startActivityForResult(intent, 2296)
                } catch (e: Exception) {
                    val intent2 = Intent()
                    intent2.action = "android.settings.MANAGE_ALL_FILES_ACCESS_PERMISSION"
                    activity.startActivityForResult(intent2, 2296)
                }
            } else action()
        } else action()
    }

    fun checkStoragePermissions(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf("android.permission.READ_MEDIA_IMAGES"),
                REQUEST_PERMISSION_CODE
            )
        }
        if (Build.VERSION.SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                try {
                    val intent = Intent("android.settings.MANAGE_APP_ALL_FILES_ACCESS_PERMISSION")
                    intent.addCategory("android.intent.category.DEFAULT")
                    intent.data = Uri.parse(
                        String.format(
                            "package:%s",
                            *arrayOf<Any>(activity.applicationContext.applicationContext.packageName)
                        )
                    )
                    activity.startActivityForResult(intent, 2296)
                } catch (e: Exception) {
                    val intent2 = Intent()
                    intent2.action = "android.settings.MANAGE_ALL_FILES_ACCESS_PERMISSION"
                    activity.startActivityForResult(intent2, 2296)
                }
            }
        } else if (PermissionChecker.checkSelfPermission(
                activity.applicationContext,
                "android.permission.WRITE_EXTERNAL_STORAGE"
            ) != PermissionChecker.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf("android.permission.WRITE_EXTERNAL_STORAGE"),
                REQUEST_PERMISSION_CODE
            )
        }
    }

    /**
     * Checks if there is any missing permissions, and
     * requests runtime permission if needed.
     */
    fun checkAndRequestPermissions(appCompatActivity: Activity) {
        for (permission in REQUIRED_PERMISSION_LIST) {
            if (!isPermissionGranted(context = appCompatActivity, permission = permission))
                missingPermission.add(permission)
        }
        if (missingPermission.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                appCompatActivity,
                missingPermission.toTypedArray(),
                REQUEST_PERMISSION_CODE
            )
        }
    }

    /**
     * Проверяет, предоставлено ли указанное разрешение, и запрашивает его, если нет.
     *
     * @param appCompatActivity Активити, в контексте которого осуществляется проверка.
     * @param permission Разрешение, которое необходимо проверить.
     * @param requestCode Код запроса для идентификации запроса разрешения.
     * @return `true`, если разрешение уже предоставлено, `false` - если оно запрашивается.
     */
    fun checkPermissionGranted(
        appCompatActivity: Activity,
        permission: String,
        requestCode: Int
    ): Boolean {
        return if (!isPermissionGranted(context = appCompatActivity, permission = permission)) {
            ActivityCompat.requestPermissions(appCompatActivity, arrayOf(permission), requestCode)
            false
        } else true
    }

    /**
     * Проверяет, предоставлены ли все из указанных разрешений, и показывает настройки, если нет.
     *
     * @param context Контекст для доступа к разрешениям.
     * @param permissions Список разрешений для проверки.
     * @param description Описание, показываемое в диалоговом окне настроек.
     * @param permissionsGrantedAction Действие, выполняемое при предоставлении всех разрешений.
     */
    fun checkPermissions(
        context: Context,
        permissions: List<String>,
        description: String,
        permissionsGrantedAction: () -> Unit,
    ) {
        if (isAtLeastOnePermissionDenied(context = context, permissions = permissions))
            showSettingsAlert(context = context, description = description)
        else permissionsGrantedAction()
    }

    /**
     * Проверяет, предоставлено ли указанное разрешение, и показывает настройки, если нет.
     *
     * @param context Контекст для доступа к разрешению и диалоговому окну.
     * @param permission Разрешение для проверки.
     * @param description Описание, показываемое в диалоговом окне настроек.
     * @param permissionGrantedAction Действие, выполняемое при предоставлении разрешения.
     */
    fun checkPermission(
        context: Context,
        permission: String,
        description: String,
        permissionGrantedAction: () -> Unit,
    ) {
        if (isPermissionGranted(context = context, permission = permission))
            permissionGrantedAction()
        else showSettingsAlert(context = context, description = description)
    }

    /**
     * Отображает диалоговое окно с предложением перейти в настройки приложения.
     *
     * @param context Контекст, в котором будет показан диалог.
     * @param description Описание действия, требующего разрешения.
     */
    private fun showSettingsAlert(context: Context, description: String) {
        AlertDialog.Builder(context).apply {
            setMessage(description)
            setPositiveButton("Дать разрешение") { dialog, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:" + context.packageName)
                context.startActivity(intent)
                dialog.dismiss()
            }
            setNegativeButton("Закрыть") { dialog, _ -> dialog.dismiss() }
            show()
        }
    }

    /**
     * Проверяет, отклонено ли хотя бы одно из указанных разрешений.
     *
     * @param context Контекст для доступа к разрешениям.
     * @param permissions Список разрешений для проверки.
     * @return `true`, если хотя бы одно разрешение отклонено, иначе `false`.
     */
    private fun isAtLeastOnePermissionDenied(context: Context, permissions: List<String>): Boolean {
        for (permission in permissions) {
            val isNeedToCheckStoragePermission = isNeedToCheckStoragePermission(permission)
            val isPermissionDenied = isPermissionDenied(context = context, permission = permission)
            if (!isNeedToCheckStoragePermission && isPermissionDenied) return true
        }

        return false
    }

    /**
     * Проверяет версию SDK, если она выше версии S (API 31) то
     * запрашивать разрешения на запись и чтение в папку с программой не требуется
     * @param permission Разрешение для проверки.
     * @return `true`, если нужно запрашивать разрешение, иначе `false`.
     */

    private fun isNeedToCheckStoragePermission(permission: String): Boolean {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S) return false
        val isWriteExternalStoragePermission =
            permission == Manifest.permission.WRITE_EXTERNAL_STORAGE
        val isReadExternalStoragePermission =
            permission == Manifest.permission.READ_EXTERNAL_STORAGE
        val isManageExternalStoragePermission =
            permission == Manifest.permission.MANAGE_EXTERNAL_STORAGE
        return isWriteExternalStoragePermission || isReadExternalStoragePermission || isManageExternalStoragePermission
    }

    /**
     * Проверяет, предоставлено ли указанное разрешение.
     *
     * @param context Контекст для доступа к разрешению.
     * @param permission Разрешение для проверки.
     * @return `true`, если разрешение предоставлено, иначе `false`.
     */
    private fun isPermissionGranted(context: Context, permission: String): Boolean =
        ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED

    /**
     * Проверяет, отклонено ли указанное разрешение.
     *
     * @param context Контекст для доступа к разрешению.
     * @param permission Разрешение для проверки.
     * @return `true`, если разрешение отклонено, иначе `false`.
     */
    private fun isPermissionDenied(context: Context, permission: String): Boolean =
        ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_DENIED
}