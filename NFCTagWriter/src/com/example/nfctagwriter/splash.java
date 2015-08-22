package com.example.nfctagwriter;


import java.io.IOException;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

public class splash extends Activity{

    NfcAdapter mAdapter;
    
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		mAdapter = NfcAdapter.getDefaultAdapter(this);
        
        if (mAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_SHORT).show();
            finish();
            return;
 
        }
     
        if (!mAdapter.isEnabled()) {
        	Toast.makeText(this, "NFC Disabled", Toast.LENGTH_SHORT).show();
        } else {
        	Toast.makeText(this, "NFC Enabled", Toast.LENGTH_SHORT).show();
        }
        
        
		
		Thread timer=new Thread(){
			
			public void run(){
				
				try{
					sleep(1000);
				}catch(InterruptedException e){
					e.printStackTrace();
					
				}finally{
					Intent sread=new Intent("com.example.nfctagwriter.MENULIST");
					startActivity(sread);
				}
			}
		};
		timer.start();
	}

	@Override
    protected void onResume() {
        super.onResume();
        enableNfcWrite();
    }
	
     
    @Override
    protected void onPause() {
        super.onPause();
        disableNfcWrite();
        finish();
    }
    
    @TargetApi(Build.VERSION_CODES.GINGERBREAD) @SuppressLint("NewApi")
    private void enableNfcWrite(){
    	  NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);
    	  IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
    	  IntentFilter[] writeTagFilters = new IntentFilter[] { tagDetected };
    	  adapter.enableForegroundDispatch(this, getPendingIntent(), writeTagFilters, null);
    	}

    	private PendingIntent getPendingIntent() {
		return PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
	}

		@TargetApi(Build.VERSION_CODES.GINGERBREAD) 
    	private void disableNfcWrite(){
    	  NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);
    	  adapter.disableForegroundDispatch(this);
    	}
		
		
		@TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1) @Override
	    protected void onNewIntent(Intent intent) {
	    	if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
	    	    Tag discoveredTag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
	    	    
	    	    Ndef ndef= Ndef.get(discoveredTag);
	    	   
	    	    
	    	    
	    	   
	    	   try{
	    		   ndef.connect();
	    		   NdefMessage ndefMessage=ndef.getNdefMessage();
	    		   
	    		   NdefRecord record= ndefMessage.getRecords()[0];
	    		   byte[] payload =record.getPayload();
	    		   
	    		   ndef.close();
	    	   }catch(IOException e)
	    	   {
	    		   
	    	   }
	    	   catch(FormatException e){
	    		   
	    	   }

	   	  
	    	}
	    	  }

}
