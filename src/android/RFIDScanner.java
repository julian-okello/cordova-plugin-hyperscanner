package cordova.plugin.hyperscanner;

import android.app.Activity;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.rscja.deviceapi.RFIDWithUHF;
import com.rscja.deviceapi.exception.ConfigurationException;

public class RFIDScanner {

    private RFIDWithUHF rfidWithUHF;
    private Activity activity;
    private OnScannerCallback scannerCallback;

    private boolean scanning = false;

    private cordova.plugin.hyperscanner.BeepHelper beepHelper;

    public interface OnScannerCallback {
        void success(String rfidtag);

        void error(String error);
    }

    public RFIDScanner(Activity activity, OnScannerCallback scannerCallback) {
        this.activity = activity;
        this.scannerCallback = scannerCallback;

        new InitTask().execute();
    }

    public void startScan(String inventoryFlag) {

        String strUII = null;
        String strEPC = null;

        if(inventoryFlag.equals("single")) {

            strUII = rfidWithUHF.inventorySingleTag();
            if (!TextUtils.isEmpty(strUII)) {

                strEPC = rfidWithUHF.convertUiiToEPC(strUII);
                scannerCallback.success(strEPC);
            } else {
                scannerCallback.error("No EPC Found");
            }
        }
    }

    public void stopScan() {
        if(rfidWithUHF != null && scanning) {
            rfidWithUHF.free();
        }
    }

    public void destroy() {
        if(rfidWithUHF != null) {
            rfidWithUHF.free();
        }
    }

    private class InitTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            boolean result = false;


            if(rfidWithUHF == null) {
                try {
                    rfidWithUHF = RFIDWithUHF.getInstance();
                } catch (ConfigurationException e) {
                    e.printStackTrace();
                }
            }

            if(rfidWithUHF != null) {
                result = rfidWithUHF.init();
            }

            return result;

        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }


}
