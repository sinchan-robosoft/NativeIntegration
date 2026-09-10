import BatteryModule from '@/modules/battery-module/src/BatteryModule';
import { SafeAreaView } from 'react-native-safe-area-context';

export default function TabTwoScreen() {
  return (
    <SafeAreaView style = {{
      flex : 1,
      justifyContent : "center",
      alignItems : "center"
    }}>

      <BatteryModule.BatteryModuleView
          style={{ width: "100%", height: 300, color: "black" }}
          onLogin={(event: any) => {
            const { email, password } = event.nativeEvent;

            console.log("Email:", email);
            console.log("Password:", password);
          }}
        />

    </SafeAreaView>
  );
}
