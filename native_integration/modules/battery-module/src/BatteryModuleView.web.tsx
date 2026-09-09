import * as React from 'react';

import { BatteryModuleViewProps } from './BatteryModule.types';

export default function BatteryModuleView(props: BatteryModuleViewProps) {
  return (
    <div>
      <iframe
        style={{ flex: 1 }}
        src={props.url}
        onLoad={() => props.onLoad({ nativeEvent: { url: props.url } })}
      />
    </div>
  );
}
