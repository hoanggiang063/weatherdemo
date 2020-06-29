package com.architecture.cleanmvvm.core.security;

import android.content.Context;

import java.util.BitSet;

public class SecurityMonitor {

    enum ENV_CHECK {
        ROOT(0),
        DEBUG(1),
        EMULATOR(2),
        XPOSED(3),
        CUSTOM_FIRMWARE(4),
        INTEGRITY(5),
        WIRELESS_SECURITY(6);

        private final int idx;

        ENV_CHECK(final int newIdx) {
            idx = newIdx;
        }

        public int getValue() {
            return idx;
        }
    }

    static BitSet envChecks = new BitSet();

    public SecurityMonitor(Context context) {
        doProbe(context);
    }

    public boolean isSafeEnviroment() {
        return !(envChecks.get(ENV_CHECK.ROOT.getValue()) ||
                envChecks.get(ENV_CHECK.EMULATOR.getValue()) ||
                envChecks.get(ENV_CHECK.XPOSED.getValue()));
    }

    public void doProbe(Context ctx) {
    }

    public static void positiveRootCheck(Object data) {
        envChecks.set(ENV_CHECK.ROOT.getValue());
    }

    public static void negativeRootCheck(Object data) {
        envChecks.clear(ENV_CHECK.ROOT.getValue());
    }

    public static void positiveDebugCheck(Object data) {
        envChecks.set(ENV_CHECK.DEBUG.getValue());
    }

    public static void negativeDebugCheck(Object data) {
        envChecks.clear(ENV_CHECK.DEBUG.getValue());
    }

    public static void positiveEmulatorCheck(Object data) {
        envChecks.set(ENV_CHECK.EMULATOR.getValue());
    }

    public static void negativeEmulatorCheck(Object data) {
        envChecks.clear(ENV_CHECK.EMULATOR.getValue());
    }

    public static void positiveXposedCheck(Object data) {
        envChecks.set(ENV_CHECK.XPOSED.getValue());
    }

    public static void negativeXposedCheck(Object data) {
        envChecks.clear(ENV_CHECK.XPOSED.getValue());
    }

    public static void positiveCustomFirmwareCheck(Object data) {
        envChecks.set(ENV_CHECK.CUSTOM_FIRMWARE.getValue());
    }

    public static void negativeCustomFirmwareCheck(Object data) {
        envChecks.clear(ENV_CHECK.CUSTOM_FIRMWARE.getValue());
    }

    public static void positiveIntegrityCheck(Object data) {
        envChecks.set(ENV_CHECK.INTEGRITY.getValue());
    }

    public static void negativeIntegrityCheck(Object data) {
        envChecks.clear(ENV_CHECK.INTEGRITY.getValue());
    }

    public static void positiveWirelessSecurityCheck(Object data) {
        envChecks.set(ENV_CHECK.WIRELESS_SECURITY.getValue());
    }

    public static void negativeWirelessSecurityCheck(Object data) {
        envChecks.clear(ENV_CHECK.WIRELESS_SECURITY.getValue());
    }
}
