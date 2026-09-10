
import BatteryModule from '@/modules/battery-module/src/BatteryModule';
import { Text } from 'react-native';
import { SafeAreaView } from 'react-native-safe-area-context';

export default function HomeScreen() {
  const batteryLevel = BatteryModule.BatteryModule.getBatteryLevel();
  return (
    <SafeAreaView style={{
      flex : 1,
      justifyContent : "center",
      alignItems : "center"
    }}>
      <Text>
          Hii, battery level is : {batteryLevel}
        </Text>
    </SafeAreaView>
  );
}
