package ac.kr.kaist.numberchanger;

import ac.kr.kaist.numberchanger.utils.ContactUtils;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private ContactUtils contactUtil;
	private Button btn010;
	private Button btn82;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		init();
		
		
						
	}
	
	public void init(){
		
		contactUtil = new ContactUtils(this);
		
		btn010 = (Button) findViewById(R.id.main_activity_btn_to_010);
		btn82 = (Button) findViewById(R.id.main_activity_btn_to_82);
		
		btn010.setOnClickListener(onClickListener);
		btn82.setOnClickListener(onClickListener);
				
	}
	
	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String tag = v.getTag().toString(); 
			
			UpdateContacts(tag);
		}
	};
	
	
	public void UpdateContacts(String tag){
		
		int count = 0;
		
		if(tag.equals(btn010.getTag().toString())){
			
			count = contactUtil.UpdateContacts(ContactUtils.PHONE_NUMBER_TO_010);
			
		} else if(tag.equals(btn82.getTag().toString())){
			
			count = contactUtil.UpdateContacts(ContactUtils.PHONE_NUMBER_TO_82);
			
		}
		
		Toast.makeText(this, count + "개의 연락처 업데이트", Toast.LENGTH_SHORT).show();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
