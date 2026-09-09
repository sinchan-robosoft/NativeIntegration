import { requireNativeView } from 'expo';
import * as React from 'react';

import { BatteryModuleViewProps } from './BatteryModule.types';

const NativeView: React.ComponentType<BatteryModuleViewProps> =
  requireNativeView('BatteryModule');

export default function BatteryModuleView(props: BatteryModuleViewProps) {
  return <NativeView {...props} />;
}
