package com.snbc.plugin;

import com.snbc.device.config.MyLogFile;
import com.snbc.device.hal.ILocker;
import com.snbc.device.impl.HostLockerImpl;
import com.snbc.device.impl.LockerBankImpl;
import com.snbc.device.hal.IBarCodeReader;
import com.snbc.device.impl.BarCodeReaderImpl;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.LOG;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class SNBC extends CordovaPlugin {

    private IBarCodeReader mBarCodeReader = new BarCodeReaderImpl();

    @Override
    public boolean execute(String action, JSONArray args,  CallbackContext callbackContext) throws JSONException {
        MyLogFile.SetWriteFlag(true);
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
        } else if ("START_READ_CODE".equals(action)) {
            startReadCode(callbackContext);
            return true;
        } else if ("STOP_READ_CODE".equals(action)) {
            stopReadCode();
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

    private void startReadCode(final CallbackContext callbackContext) {
        mBarCodeReader.addListener(new IBarCodeReader.IBarCodeReaderListener() {
            @Override
            public void OnCodeReadCallBack(int iErrorId, String strBarCode) {
                Log.i("startReadCode ===== ", "iErrorId: " + iErrorId);
                mBarCodeReader.removeListener(this);
                try {
                    JSONObject obj = new JSONObject();
                    obj.put("errorCode", iErrorId);
                    obj.put("strBarCode", strBarCode);
                    callbackContext.success(obj);
                } catch (Exception e) {
                    callbackContext.error(e.getMessage());
                }
            }
        });
        mBarCodeReader.startReadCode(120);
    }

    private void stopReadCode() {
        mBarCodeReader.stopReadCode();
    }
}