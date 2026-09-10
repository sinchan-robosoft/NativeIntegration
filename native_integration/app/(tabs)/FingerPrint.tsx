import BatteryModule from '@/modules/battery-module/src/BatteryModule';
import React from 'react';
import { Button, View } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';

const FingerPrint = () => {
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
  return (
    <SafeAreaView style = {{
        flex : 1,
        justifyContent : "center",
        alignItems : "center"
    }}>

        <View style={{ flex: 1, justifyContent: "center",gap : 2 }}>
          <Button
            title="Authenticate"
            onPress={handleAuthenticate}
          />
          
        </View>

    </SafeAreaView>
  )
}

export default FingerPrint