// import { NativeModule, requireNativeModule } from 'expo';

// import { BatteryModuleEvents } from './BatteryModule.types';

// declare class BatteryModule extends NativeModule<BatteryModuleEvents> {
//   PI: number;
//   hello(): string;
//   setValueAsync(value: string): Promise<void>;
// }

// // This call loads the native module object from the JSI.
// export default requireNativeModule<BatteryModule>('BatteryModule');

import {
    requireNativeModule,
    requireNativeViewManager,
} from "expo-modules-core";

const BatteryModule = requireNativeModule("BatteryModule");

const BatteryModuleView = requireNativeViewManager("BatteryModule");

export default { BatteryModule, BatteryModuleView };