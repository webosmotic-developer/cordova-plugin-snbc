package com.snbc.plugin;

import com.snbc.device.hal.ILocker;
import com.snbc.device.impl.HostLockerImpl;
import com.snbc.device.impl.LockerBankImpl;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;

public class SNBC extends CordovaPlugin {
    @Override
    public boolean execute(String action, JSONArray args,  CallbackContext callbackContext) throws JSONException {
        if ("OPEN_SLOT".equals(action)) {
            openSlot(args.getString(0), args.getString(1), callbackContext);
            return true;
        } else if ("LOCKER_STATUS".equals(action)) {
            getLockerStatus(args.getString(0), args.getString(1), callbackContext);
            return true;
        } else if ("OPEN_LED".equals(action)) {
            openLed(args.getString(0), callbackContext);
            return true;
        } else if ("CLOSE_LED".equals(action)) {
            closeLed(args.getString(0), callbackContext);
            return true;
        }
        return false;
    }

    private void openSlot(String iSlotIDStr, String iCabinetIDStr, CallbackContext callbackContext) {
        if (iSlotIDStr == null || iSlotIDStr.length() == 0) {
            callbackContext.error("Slot ID required.");
        } else if (iCabinetIDStr == null || iCabinetIDStr.length() == 0) {
            callbackContext.error("Cabinet ID required.");
        } else {
            int iSlotID = Integer.parseInt(iSlotIDStr);
            int iCabinetID = Integer.parseInt(iCabinetIDStr);
            ILocker mLocker = iCabinetID == 0x00 ? new HostLockerImpl() : new LockerBankImpl();
            callbackContext.success(mLocker.openSlot(iCabinetID, iSlotID).toString());
        }
    }

    private void getLockerStatus(String iSlotIDStr, String iCabinetIDStr, CallbackContext callbackContext) {
        if (iSlotIDStr == null || iSlotIDStr.length() == 0) {
            callbackContext.error("Slot ID required.");
        } else if (iCabinetIDStr == null || iCabinetIDStr.length() == 0) {
            callbackContext.error("Cabinet ID required.");
        } else {
            int iSlotID = Integer.parseInt(iSlotIDStr);
            int iCabinetID = Integer.parseInt(iCabinetIDStr);
            ILocker mLocker = iCabinetID == 0x00 ? new HostLockerImpl() : new LockerBankImpl();
            callbackContext.success(mLocker.getLockerStatus(iCabinetID, iSlotID).toString());
        }
    }

    private void openLed(String iCabinetIDStr, CallbackContext callbackContext) {
        if (iCabinetIDStr == null || iCabinetIDStr.length() == 0) {
            callbackContext.error("Cabinet ID required.");
        } else {
            int iCabinetID = Integer.parseInt(iCabinetIDStr);
            ILocker mLocker = iCabinetID == 0x00 ? new HostLockerImpl() : new LockerBankImpl();
            mLocker.openLed();
        }
    }

    private void closeLed(String iCabinetIDStr, CallbackContext callbackContext) {
        if (iCabinetIDStr == null || iCabinetIDStr.length() == 0) {
            callbackContext.error("Cabinet ID required.");
        } else {
            int iCabinetID = Integer.parseInt(iCabinetIDStr);
            ILocker mLocker = iCabinetID == 0x00 ? new HostLockerImpl() : new LockerBankImpl();
            mLocker.closeLed();
        }
    }
}