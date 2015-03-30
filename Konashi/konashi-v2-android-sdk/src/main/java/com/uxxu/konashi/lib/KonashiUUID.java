package com.uxxu.konashi.lib;

import java.util.UUID;

/**
 * konashiで使用するGATTのUUID
 * 
 * @author monakaz, YUKAI Engineering
 * http://konashi.ux-xu.com
 * ========================================================================
 * Copyright 2014 Yukai Engineering Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
public class KonashiUUID {
    public static final String BASE_UUID_STRING = "-03FB-40DA-98A7-B0DEF65C2D4B";

    // Battery service UUID
    public static final UUID BATTERY_SERVICE_UUID                 = UUID.fromString("229B180F" + BASE_UUID_STRING);
    public static final UUID BATTERY_LEVEL_UUID                   = UUID.fromString("229B2A19" + BASE_UUID_STRING);
    
    // konashi service UUID
    public static final UUID KONASHI_SERVICE_UUID                 = UUID.fromString("229BFF00" + BASE_UUID_STRING);

    // konashi characteristics
    public static final UUID PIO_SETTING_UUID                     = UUID.fromString("229B3000" + BASE_UUID_STRING);
    public static final UUID PIO_PULLUP_UUID                      = UUID.fromString("229B3001" + BASE_UUID_STRING);
    public static final UUID PIO_OUTPUT_UUID                      = UUID.fromString("229B3002" + BASE_UUID_STRING);
    public static final UUID PIO_INPUT_NOTIFICATION_UUID          = UUID.fromString("229B3003" + BASE_UUID_STRING);
    
    public static final UUID PWM_CONFIG_UUID                      = UUID.fromString("229B3004" + BASE_UUID_STRING);
    public static final UUID PWM_PARAM_UUID                       = UUID.fromString("229B3005" + BASE_UUID_STRING);
    public static final UUID PWM_DUTY_UUID                        = UUID.fromString("229B3006" + BASE_UUID_STRING);

    public static final UUID ANALOG_DRIVE_UUID                    = UUID.fromString("229B3007" + BASE_UUID_STRING);
    public static final UUID ANALOG_READ0_UUID                    = UUID.fromString("229B3008" + BASE_UUID_STRING);
    public static final UUID ANALOG_READ1_UUID                    = UUID.fromString("229B3009" + BASE_UUID_STRING);
    public static final UUID ANALOG_READ2_UUID                    = UUID.fromString("229B300A" + BASE_UUID_STRING);

    public static final UUID I2C_CONFIG_UUID                      = UUID.fromString("229B300B" + BASE_UUID_STRING);
    public static final UUID I2C_START_STOP_UUID                  = UUID.fromString("229B300C" + BASE_UUID_STRING);
    public static final UUID I2C_WRITE_UUID                       = UUID.fromString("229B300D" + BASE_UUID_STRING);
    public static final UUID I2C_READ_PARAM_UUID                  = UUID.fromString("229B300E" + BASE_UUID_STRING);
    public static final UUID I2C_READ_UUID                        = UUID.fromString("229B300F" + BASE_UUID_STRING);

    public static final UUID UART_CONFIG_UUID                     = UUID.fromString("229B3010" + BASE_UUID_STRING);
    public static final UUID UART_BAUDRATE_UUID                   = UUID.fromString("229B3011" + BASE_UUID_STRING);
    public static final UUID UART_TX_UUID                         = UUID.fromString("229B3012" + BASE_UUID_STRING);
    public static final UUID UART_RX_NOTIFICATION_UUID            = UUID.fromString("229B3013" + BASE_UUID_STRING);

    public static final UUID HARDWARE_RESET_UUID                  = UUID.fromString("229B3014" + BASE_UUID_STRING);
    public static final UUID HARDWARE_LOW_BAT_NOTIFICATION_UUID   = UUID.fromString("229B3015" + BASE_UUID_STRING);
    
    // konashi characteristic configuration
    public static final UUID CLIENT_CHARACTERISTIC_CONFIG         = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
}
