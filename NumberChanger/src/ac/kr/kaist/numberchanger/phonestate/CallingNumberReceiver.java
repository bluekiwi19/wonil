package ac.kr.kaist.numberchanger.phonestate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


/*
 * 
 * 권한
 * 
  		<uses-permission android:name="android.permission.PROCESS_OUTGOING_CALLS"/>
 * 
 * Manifest 등록 필요        
 *         
		<receiver android:name="ac.kr.kaist.numberchanger.phonestate.PhoneStateReceiver">
		    <intent-filter>
		        <action android:name="android.intent.action.NEW_OUTGOING_CALL"></action>
		        <action android:name="android.intent.action.PHONE_STATE"></action>
		    </intent-filter>
		</receiver>
 * 
 * 
 */


public class CallingNumberReceiver extends BroadcastReceiver  {
	
	//public static final String outgoing = "android.intent.action.NEW_OUTGOING_CALL" ;
	//IntentFilter intentFilter = new IntentFilter(outgoing);
	     
    public void onReceive(Context context, final Intent intent) {
    	
    	String outgoingno = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
    	
    	//82로 전화를 건다면
    	if(outgoingno.substring(0,2).equals("82")){
    		
    		Log.d("outgoingnum", outgoingno);
    		//전화번호 변경
    		this.setResultData("0"+ outgoingno.substring(2));    		    		

    	}
        
    }
}
