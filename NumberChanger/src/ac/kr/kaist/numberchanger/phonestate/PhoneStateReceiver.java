package ac.kr.kaist.numberchanger.phonestate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class PhoneStateReceiver extends BroadcastReceiver  {
	
	public static final String outgoing = "android.intent.action.NEW_OUTGOING_CALL" ;
	IntentFilter intentFilter = new IntentFilter(outgoing);
	     
    public void onReceive(Context context, final Intent intent) {
    	
    	String outgoingno = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
    	
    	//82�� ��ȭ�� �Ǵٸ�
    	if(outgoingno.substring(0,2).equals("82")){
    		
    		Log.d("outgoingnum", outgoingno);
    		//��ȭ��ȣ ����
    		this.setResultData("0"+ outgoingno.substring(1));    		    		

    	}
        
    }
}
