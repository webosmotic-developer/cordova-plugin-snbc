var exec = require('cordova/exec');

exports.openSlot = function (iSlotID, iCabinetID, success, error) {
    exec(success, error, 'SNBC', 'OPEN_SLOT', [iSlotID, iCabinetID]);
};

exports.getLockerStatus = function (iSlotID, iCabinetID, success, error) {
    exec(success, error, 'SNBC', 'LOCKER_STATUS', [iSlotID, iCabinetID]);
};

exports.openLed = function (iCabinetID, success, error) {
    exec(success, error, 'SNBC', 'OPEN_LED', [iCabinetID]);
};

exports.closeLed = function (iCabinetID, success, error) {
    exec(success, error, 'SNBC', 'CLOSE_LED', [iCabinetID]);
};

exports.startReadCode = function (success, error) {
    exec(success, error, 'SNBC', 'START_READ_CODE', []);
};

exports.stopReadCode = function () {
    exec(null, null, 'SNBC', 'STOP_READ_CODE', []);
};