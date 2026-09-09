package expo.modules.batterymodule

import android.content.Context
import android.graphics.Color
import android.text.InputType
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import expo.modules.kotlin.AppContext
import expo.modules.kotlin.viewevent.EventDispatcher
import expo.modules.kotlin.views.ExpoView

class BatteryModuleView(
  context: Context,
  appContext: AppContext
) : ExpoView(context, appContext) {

  // Event that we will send to React Native
  private val onLogin by EventDispatcher()

  private val container = LinearLayout(context).apply {
    orientation = LinearLayout.VERTICAL
    gravity = Gravity.CENTER
    setPadding(40, 40, 40, 40)
  }

  private val title = TextView(context).apply {
    text = "Login"
    textSize = 28f
    setTextColor(Color.BLACK)
    gravity = Gravity.CENTER
  }

  private val emailInput = EditText(context).apply {
    hint = "Email"
    setTextColor(Color.BLACK)
    inputType = InputType.TYPE_CLASS_TEXT or
        InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
  }

  private val passwordInput = EditText(context).apply {
    hint = "Password"
    setTextColor(Color.BLACK)
    inputType = InputType.TYPE_CLASS_TEXT or
        InputType.TYPE_TEXT_VARIATION_PASSWORD
  }

  private val loginButton = Button(context).apply {
    text = "LOGIN"
  }

  init {

    container.addView(
      title,
      LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
      )
    )

    container.addView(
      emailInput,
      LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
      )
    )

    container.addView(
      passwordInput,
      LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
      )
    )

    container.addView(
      loginButton,
      LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
      )
    )

    // Native Android button click
    loginButton.setOnClickListener {

      val email = emailInput.text.toString()
      val password = passwordInput.text.toString()

      // Send data to React Native
      onLogin(
        mapOf(
          "email" to email,
          "password" to password
        )
      )
    }

    addView(
      container,
      LayoutParams(
        LayoutParams.MATCH_PARENT,
        LayoutParams.WRAP_CONTENT
      )
    )
  }
}