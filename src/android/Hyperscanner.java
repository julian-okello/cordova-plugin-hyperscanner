package cordova.plugin.hyperscanner;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;

import org.apache.cordova.PluginResult;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.widget.Toast;

import android.content.SharedPreferences;

/**
 * This class echoes a string called from JavaScript.
 */
public class Hyperscanner extends CordovaPlugin {

    private static final String ACTION_SCAN_BARCODE = "scanBarcode";
    private static final String ACTION_SCAN_RFID = "scanRFIDcode";

    private CallbackContext PUBLIC_CALLBACK = null;

    cordova.plugin.hyperscanner.BarcodeScanner barcodeScanner;

    cordova.plugin.hyperscanner.RFIDScanner rfidScanner;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        
        //logResult("Initialise stuff here");

        pref = cordova.getActivity().getSharedPreferences("BIGBOXAFRICA", Context.MODE_PRIVATE);
        editor = pref.edit();

        barcodeScanner = new cordova.plugin.hyperscanner.BarcodeScanner(cordova.getActivity(), new cordova.plugin.hyperscanner.BarcodeScanner.OnScannerCallback() {
            @Override
            public void success(String barcode) {
                logResult(barcode);
            }

            @Override
            public void error(String error) {
                logResult(error);
            }
        });

        rfidScanner = new cordova.plugin.hyperscanner.RFIDScanner(cordova.getActivity(), new cordova.plugin.hyperscanner.RFIDScanner.OnScannerCallback() {
            @Override
            public void success(String rfidtag) {
                logResult(rfidtag);
            }

            @Override
            public void error(String error) {
                logResult(error);
            }
        });
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        PUBLIC_CALLBACK = callbackContext;

        if (ACTION_SCAN_BARCODE.equals(action)) {

            logResult("You can now scan barcodes");

            //barcodeScanner.startScan();

        } else if (ACTION_SCAN_RFID.equals(action)) {

            //logResult("You can now scan RFID Tags");

            rfidScanner.startScan("single");

        }

        PluginResult result = new PluginResult(PluginResult.Status.NO_RESULT);
        result.setKeepCallback(true);

        return true;
    }

    public void logResult(String body) {
        
        Toast.makeText(cordova.getActivity(), body, Toast.LENGTH_SHORT).show();

        PluginResult result = new PluginResult(PluginResult.Status.OK, body);
        result.setKeepCallback(true);

        PUBLIC_CALLBACK.sendPluginResult(result);
        return;

    }

    @Override
    public void onDestroy(){
        this.destroyScanner();
        super.onDestroy();
    }

    public void destroyScanner() {
        if(barcodeScanner != null) {
            barcodeScanner.destroy();
            barcodeScanner = null;
        }
    }
}
