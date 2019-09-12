var exec = require('cordova/exec');

exports.scanBarcode = function (success, error) {
    exec(success, error, 'Hyperscanner', 'scanBarcode');
};


exports.scanRFIDcode = function (success, error) {
    exec(success, error, 'Hyperscanner', 'scanRFIDcode');
};
