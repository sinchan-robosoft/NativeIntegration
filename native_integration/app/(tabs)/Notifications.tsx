import BatteryModule from '@/modules/battery-module/src/BatteryModule';
import React from 'react';
import { Button, View } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';

const Notifications = () => {
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
    <SafeAreaView
    style = {{
        flex : 1,
        justifyContent : "center",
        alignItems : "center"
    }}>
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
    </SafeAreaView>
    
  )
}

export default Notifications