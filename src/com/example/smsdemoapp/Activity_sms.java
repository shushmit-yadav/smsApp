package com.example.smsdemoapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Contacts.People.Phones;
import android.telephony.gsm.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class Activity_sms extends Activity {
	Button btnContact;
	Button btnSendSMS;
	EditText etNumber;
	EditText etMessage;
	String cNumber;
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_layout);
        btnSendSMS = (Button) findViewById(R.id.btnSendSMS);
        btnContact = (Button) findViewById(R.id.btnContact);
        etNumber = (EditText) findViewById(R.id.etNumber);
        etMessage = (EditText) findViewById(R.id.etMessage);
    }

    public void SendMessage(View view){
    	String PhoneNo = etNumber.getText().toString();
		String Message = etMessage.getText().toString();
		try {
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(PhoneNo, null, Message, null, null);
			Toast.makeText(getApplicationContext(), "SMS Sent!",
						Toast.LENGTH_LONG).show();
		}catch(Exception ex){
			Toast.makeText(getApplicationContext(),
			"SMS faild, please try again later!",
			Toast.LENGTH_LONG).show();
			ex.printStackTrace();
		}
    }
    public void getContact(View view){
    	Intent intent = new Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI);
    	startActivityForResult(intent, 1);
    }
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
      super.onActivityResult(reqCode, resultCode, data);

      switch (reqCode) {
        case (1) :
          if (resultCode == Activity.RESULT_OK) {
            Uri contactData = data.getData();
            Cursor c =  managedQuery(contactData, null, null, null, null);
            if (c.moveToFirst()) {
            	String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
            	String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            	if(hasPhone.equalsIgnoreCase("1")){
            		 Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"="+id, null,null);
                     phones.moveToFirst();
                     cNumber = phones.getString(phones.getColumnIndex("data1"));
            	}
             
            }
          }
        etNumber.setText(cNumber);
        break;
      }
    }
}
