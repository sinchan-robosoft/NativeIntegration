import { registerWebModule, NativeModule } from 'expo';

import { BatteryModuleEvents } from './BatteryModule.types';

class BatteryModule extends NativeModule<BatteryModuleEvents> {
  PI = Math.PI;
  async setValueAsync(value: string): Promise<void> {
    this.emit('onChange', { value });
  }
  hello() {
    return 'Hello world! 👋';
  }
}

export default registerWebModule(BatteryModule, 'BatteryModule');
