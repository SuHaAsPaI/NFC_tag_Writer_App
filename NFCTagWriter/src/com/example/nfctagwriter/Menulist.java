package com.example.nfctagwriter;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.content.pm.ActivityInfo;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Menulist extends ListActivity{

	String classes[]={"Read","Write","Copy","Erase","About Tag","Developer"};
	 public static final String MIME_TEXT_PLAIN = "text/plain";
	    public static final String TAG = "NfcDemo";
	    
	     NfcAdapter mAdapter;
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD) @SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setListAdapter(new ArrayAdapter<String>(Menulist.this, android.R.layout.simple_expandable_list_item_1, classes));
		
		
		mAdapter = NfcAdapter.getDefaultAdapter(this);
        
         
        handleIntent(getIntent());
        
    }
	@Override
    protected void onResume() {
        super.onResume();
         
        /**
         * It's important, that the activity is in the foreground (resumed). Otherwise
         * an IllegalStateException is thrown.
         */
        setupForegroundDispatch(this, mAdapter);
    }
     
    @Override
    protected void onPause() {
        /**
         * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
         */
        stopForegroundDispatch(this, mAdapter);
         
        super.onPause();
    }
     
    @Override
    protected void onNewIntent(Intent intent) {
        /**
         * This method gets called, when a new Intent gets associated with the current activity instance.
         * Instead of creating a new activity, onNewIntent will be called. For more information have a look
         * at the documentation.
         *
         * In our case this method gets called, when the user attaches a Tag to the device.
         */
        handleIntent(intent);
    }
    
	private void handleIntent(Intent intent) {
    	
    	
    }
	
	@SuppressLint("NewApi") @TargetApi(Build.VERSION_CODES.GINGERBREAD) public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
 
        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);
 
        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};
 
        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }
         
        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }
 
    /**
     * @param activity The corresponding {@link BaseActivity} requesting to stop the foreground dispatch.
     * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
     */
    @SuppressLint("NewApi") @TargetApi(Build.VERSION_CODES.GINGERBREAD) public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }
		
	
	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		String cheese =classes[position];
		
				if(cheese=="About Tag")
					{
						cheese="About_Tag";
					}
				if(cheese=="Developer")
				{
					cheese="Help";
				}
		try{
		Class<?> ourclass = Class.forName("com.example.nfctagwriter."+cheese);
		Intent ourin =new Intent(Menulist.this,ourclass);
		startActivity(ourin);
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}
	
	
	
}

