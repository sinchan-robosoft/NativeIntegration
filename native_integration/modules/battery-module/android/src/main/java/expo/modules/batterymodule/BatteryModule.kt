package expo.modules.batterymodule

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.BatteryManager
import android.os.Build
import android.os.Handler
import android.os.Looper

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

import expo.modules.kotlin.Promise
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition

class BatteryModule : Module() {

  override fun definition() = ModuleDefinition {

    Name("BatteryModule")

    Function("getBatteryLevel") {

      val context = appContext.reactContext
        ?: throw Exception("React context is not available")

      val batteryManager =
        context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager

      batteryManager.getIntProperty(
        BatteryManager.BATTERY_PROPERTY_CAPACITY
      )
    }

    View(BatteryModuleView::class) {
      Events("onLogin")
    }

    AsyncFunction("authenticate") { promise: Promise ->

      val activity = appContext.currentActivity

      if (activity == null) {
        promise.reject(
          "NO_ACTIVITY",
          "Current activity is not available",
          null
        )
        return@AsyncFunction
      }

      if (activity !is FragmentActivity) {
        promise.reject(
          "INVALID_ACTIVITY",
          "Current activity is not a FragmentActivity",
          null
        )
        return@AsyncFunction
      }

      val biometricManager =
        BiometricManager.from(activity)

      val canAuthenticate =
        biometricManager.canAuthenticate(
          BiometricManager.Authenticators.BIOMETRIC_STRONG
        )

      if (canAuthenticate != BiometricManager.BIOMETRIC_SUCCESS) {
        promise.reject(
          "BIOMETRIC_UNAVAILABLE",
          "Biometric authentication is not available",
          null
        )
        return@AsyncFunction
      }

      val executor =
        ContextCompat.getMainExecutor(activity)

      Handler(Looper.getMainLooper()).post {

        val biometricPrompt =
          BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {

              override fun onAuthenticationSucceeded(
                result: BiometricPrompt.AuthenticationResult
              ) {
                promise.resolve(
                  mapOf(
                    "success" to true
                  )
                )
              }

              override fun onAuthenticationError(
                errorCode: Int,
                errString: CharSequence
              ) {
                promise.resolve(
                  mapOf(
                    "success" to false,
                    "errorCode" to errorCode,
                    "error" to errString.toString()
                  )
                )
              }

              override fun onAuthenticationFailed() {
              }
            }
          )

        val promptInfo =
          BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Authenticate to continue")
            .setNegativeButtonText("Cancel")
            .build()

        biometricPrompt.authenticate(promptInfo)
      }
    }

    AsyncFunction("requestNotificationPermission") { promise: Promise ->

      val activity = appContext.currentActivity

      if (activity == null) {
        promise.reject(
          "NO_ACTIVITY",
          "Current activity is not available",
          null
        )
        return@AsyncFunction
      }

      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        promise.resolve(true)
        return@AsyncFunction
      }

      if (
        ContextCompat.checkSelfPermission(
          activity,
          Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
      ) {
        promise.resolve(true)
        return@AsyncFunction
      }

      ActivityCompat.requestPermissions(
        activity,
        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
        1001
      )

      promise.resolve(false)
    }

    AsyncFunction("showNotification") { title: String, message: String, promise: Promise ->

      val context = appContext.reactContext

      if (context == null) {
        promise.reject(
          "NO_CONTEXT",
          "React context is not available",
          null
        )
        return@AsyncFunction
      }

      if (
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
        ContextCompat.checkSelfPermission(
          context,
          Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
      ) {
        promise.reject(
          "NOTIFICATION_PERMISSION_DENIED",
          "Notification permission has not been granted",
          null
        )
        return@AsyncFunction
      }

      val channelId = "default"

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        val channel =
          NotificationChannel(
            channelId,
            "Default Notifications",
            NotificationManager.IMPORTANCE_DEFAULT
          )

        val notificationManager =
          context.getSystemService(NotificationManager::class.java)

        notificationManager.createNotificationChannel(channel)
      }

      val notification =
        NotificationCompat.Builder(context, channelId)
          .setSmallIcon(android.R.drawable.ic_dialog_info)
          .setContentTitle(title)
          .setContentText(message)
          .setPriority(NotificationCompat.PRIORITY_DEFAULT)
          .setAutoCancel(true)
          .build()

      val notificationManager =
        ContextCompat.getSystemService(
          context,
          NotificationManager::class.java
        )

      notificationManager?.notify(
        System.currentTimeMillis().toInt(),
        notification
      )

      promise.resolve(true)
    }
  }
}