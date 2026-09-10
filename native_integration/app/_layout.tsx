import { DarkTheme, DefaultTheme, ThemeProvider } from '@react-navigation/native';
import { StatusBar } from 'expo-status-bar';
import 'react-native-reanimated';

import { useColorScheme } from '@/hooks/use-color-scheme';
import BatteryModule from '@/modules/battery-module/src/BatteryModule';
import { Button, Text, View } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';

export const unstable_settings = {
  anchor: '(tabs)',
};

export default function RootLayout() {
  const colorScheme = useColorScheme();
  const batteryLevel = BatteryModule.BatteryModule.getBatteryLevel();
  const handleAuthenticate = async () => {
    try {
      const result =
        await BatteryModule.BatteryModule.authenticate();

      console.log("Biometric result:", result);

      if (result.success) {
        console.log("Authentication successful!");
      } else {
        console.log("Authentication failed:", result.error);
      }

    } catch (error) {
      console.error("Biometric error:", error);
    }
  };
  const handleNotificationPermission = async () => {
  try {
    const result =
      await BatteryModule.BatteryModule.requestNotificationPermission();

    console.log("Permission result:", result);
  } catch (error) {
    console.error("Permission error:", error);
  }
};
  const handleNotification = async () => {
  try {
    const result =
      await BatteryModule.BatteryModule.showNotification(
        "Native Notification",
        "Hello from Kotlin!"
      );

    console.log("Notification result:", result);
  } catch (error) {
    console.error("Notification error:", error);
  }
};
  return (
    <ThemeProvider value={colorScheme === 'dark' ? DarkTheme : DefaultTheme}>
      <SafeAreaView style={{
        flex: 1,
        justifyContent: "flex-start",
        alignItems: "stretch",
        backgroundColor: "white"
      }}>
        <Text>
          Hii, battery level is : {batteryLevel}
        </Text>
        <BatteryModule.BatteryModuleView
          style={{ width: "100%", height: 300, color: "black" }}
          onLogin={(event: any) => {
            const { email, password } = event.nativeEvent;

            console.log("Email:", email);
            console.log("Password:", password);
          }}
        />
        <View style={{ flex: 1, justifyContent: "center",gap : 2 }}>
          <Button
            title="Authenticate"
            onPress={handleAuthenticate}
          />
          <View
            style = {{
              display : "flex",
              flexDirection : "row",
              justifyContent : "center",
              gap : 3
            }}
          >
            <Button
            title="Get Permission"
            onPress={handleNotificationPermission}
          />
          <Button
            title="Show Notification"
            onPress={handleNotification}
          />
          </View>
          
        </View>

      </SafeAreaView>

      <StatusBar style="auto" />
    </ThemeProvider>
  );
}
