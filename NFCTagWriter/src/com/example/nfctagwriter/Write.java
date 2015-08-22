package com.example.nfctagwriter;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Write extends Activity {

	public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";
    
    String sd,sp;
    int l=1,k=1,j=1;
    
    Button bWrite;
    EditText eInput;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write);
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);bWrite=(Button)findViewById(R.id.button1);
		 eInput=(EditText)findViewById(R.id.editText1);
		 
		 bWrite.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					sp=eInput.getText().toString();
					l=2;
					k=2;
					setContentView(R.layout.activity_erase);
				}
			});
	}
	
	
	protected void onResume() {
       super.onResume();
       enableNfcWrite();
   }
	
    
   @Override
   protected void onPause() {
       super.onPause();
       disableNfcWrite();
   }
    
   private PendingIntent getPendingIntent() {
   	  return PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
   	}
   
   
   @TargetApi(Build.VERSION_CODES.GINGERBREAD) @SuppressLint("NewApi")
   private void enableNfcWrite(){
   	  NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);
   	  IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
   	  IntentFilter[] writeTagFilters = new IntentFilter[] { tagDetected };
   	  adapter.enableForegroundDispatch(this, getPendingIntent(), writeTagFilters, null);
   	}

   	@TargetApi(Build.VERSION_CODES.GINGERBREAD) 
   	private void disableNfcWrite(){
   	  NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);
   	  adapter.disableForegroundDispatch(this);
   	}
  
   	@SuppressLint("NewApi") private void writeSomeStuffToTag(Tag tag) {
   		  Ndef ndefTag = Ndef.get(tag);
   		  byte[] stringBytes = sp.getBytes();
   		  NdefRecord dataToWrite = NdefRecord.createMime("text/plain", stringBytes);
   		  try {
				ndefTag.connect();
				ndefTag.writeNdefMessage(new NdefMessage(dataToWrite));
	    		ndefTag.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (FormatException e) {
				e.printStackTrace();
			}
   		  
   		}
   @TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1) @Override
   protected void onNewIntent(Intent intent) {
   	if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
   	    Tag discoveredTag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
   	    
   	    Ndef ndef= Ndef.get(discoveredTag);
   	    Log.d(TAG, ndef.getType().toString());
   	    
   	    Log.d(TAG, ndef.isWritable() ?"true":"false");
   	   
   	   try{
   		   ndef.connect();
   		   NdefMessage ndefMessage=ndef.getNdefMessage();
   		   Log.d(TAG, ndefMessage.toString());
   		   Log.d(TAG, Integer.toString(ndef.getMaxSize()));
   		   NdefRecord record= ndefMessage.getRecords()[0];
   		   byte[] payload =record.getPayload();
   		   Log.d(TAG, new String(payload));
   		   sd=new String(payload);
   		   ndef.close();
   	   }catch(IOException e)
   	   {
   		   Log.e(TAG, "IOException");
   	   }
   	   catch(FormatException e){
   		   
   	   }
   	   
   	   if(l==2){
   		   setContentView(R.layout.activity_writesuccseful);
  	    	writeSomeStuffToTag(discoveredTag);
  	    	l=1;
  	    	k=1;
  	    	}
   	  }

   }
	

}