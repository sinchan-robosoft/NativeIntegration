package expo.modules.batterymodule

import android.content.Context
import android.os.BatteryManager
import android.os.Handler
import android.os.Looper

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

import expo.modules.kotlin.Promise
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition

class BatteryModule : Module() {

  override fun definition() = ModuleDefinition {

    Name("BatteryModule")

    // ---------------------------------------------------------
    // 1. Get Battery Level
    // ---------------------------------------------------------

    Function("getBatteryLevel") {

      val context = appContext.reactContext
        ?: throw Exception("React context is not available")

      val batteryManager =
        context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager

      batteryManager.getIntProperty(
        BatteryManager.BATTERY_PROPERTY_CAPACITY
      )
    }


    // ---------------------------------------------------------
    // 2. Native Login View
    // ---------------------------------------------------------

    View(BatteryModuleView::class) {
      Events("onLogin")
    }


    // ---------------------------------------------------------
    // 3. Biometric Authentication
    // ---------------------------------------------------------

    AsyncFunction("authenticate") { promise: Promise ->

      // Get the current Android Activity
      val activity = appContext.currentActivity

      // Activity doesn't exist
      if (activity == null) {

        promise.reject(
          "NO_ACTIVITY",
          "Current activity is not available",
          null
        )

        return@AsyncFunction
      }


      // BiometricPrompt requires FragmentActivity
      if (activity !is FragmentActivity) {

        promise.reject(
          "INVALID_ACTIVITY",
          "Current activity is not a FragmentActivity",
          null
        )

        return@AsyncFunction
      }


      // -------------------------------------------------------
      // Check whether biometric authentication is available
      // -------------------------------------------------------

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


      // -------------------------------------------------------
      // Get the main/UI thread executor
      // -------------------------------------------------------

      val executor =
        ContextCompat.getMainExecutor(activity)


      // -------------------------------------------------------
      // IMPORTANT:
      // BiometricPrompt must be created and started
      // on the Android main thread.
      // -------------------------------------------------------

      Handler(Looper.getMainLooper()).post {

        val biometricPrompt =
          BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {

              // -----------------------------------------------
              // Authentication successful
              // -----------------------------------------------

              override fun onAuthenticationSucceeded(
                result: BiometricPrompt.AuthenticationResult
              ) {

                promise.resolve(
                  mapOf(
                    "success" to true
                  )
                )
              }


              // -----------------------------------------------
              // Authentication error
              // -----------------------------------------------

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


              // -----------------------------------------------
              // Authentication failed
              // -----------------------------------------------

              override fun onAuthenticationFailed() {

                // This does NOT mean the entire authentication
                // process has ended.
                //
                // The user can try their fingerprint again.
              }
            }
          )


        // -----------------------------------------------------
        // Configure the biometric dialog
        // -----------------------------------------------------

        val promptInfo =
          BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Authenticate to continue")
            .setNegativeButtonText("Cancel")
            .build()


        // -----------------------------------------------------
        // Show biometric prompt
        // -----------------------------------------------------

        biometricPrompt.authenticate(promptInfo)
      }
    }
  }
}