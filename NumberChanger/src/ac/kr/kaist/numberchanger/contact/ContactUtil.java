package ac.kr.kaist.numberchanger.contact;

import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.util.Log;

public class ContactUtil {
	
	public final static int PHONE_NUMBER_TO_82 = 0;
	public final static int PHONE_NUMBER_TO_010 = 1;
	
	private ContentResolver cr;
	private Cursor cursor;
	
	public ContactUtil(Context context){
		
		cr = context.getContentResolver();
		cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		
		//ReadContact();
		//ModifyContact(PHONE_NUMBER_TO_010);
		
		WriteNumber("869", "010-9286-1829" , "821092861829");
		
	}
	
	public void ReadContact(){
		
		int count = cursor.getCount();
		
		if(count > 0){
			
			Log.d("ContactNumber", count + "\n");
			
			//�������� �б�
			cursor.moveToFirst();
			while(cursor.moveToNext()){
				
				//���̵�
				String id = cursor.getString(
						cursor.getColumnIndex(ContactsContract.Contacts._ID)); 
				
				//�̸�
				String name = cursor.getString(
						cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)); 
				
				//String phoneNum = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
				
				//��ȭ��ȣ ���� ������� 1, �ƴ� ��� 0
				int phoneNumFlag = Integer.parseInt(cursor.getString(
								cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));  
				
				//log �Է�
				String log = id + " / " + name + " / ";		
				
				if(phoneNumFlag > 0){
					
					//��ȭ��ȣ select from db
					Cursor pCur = cr.query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
							null, 
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?", 
							new String[]{id}, null);
					
					//��ȭ��ȣ ���
					while (pCur.moveToNext()){
						
						//��ȭ��ȣ
						String phoneNum = pCur.getString(
								pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						
						//��ȭ��ȣ ������ ����
						phoneNum = phoneNum.replace("-", "");
						
						//��ȭ��ȣ ���� ����
						phoneNum = PhoneNumberUtils.formatNumber(phoneNum);
						
						log += phoneNum + " / ";
						
					}
					
					log += "\n";
					Log.d("ReadContact", log);
					
					pCur.close();
					
				}
			}
		}
	}
	
	public void ModifyContact(int numberFlag){
		
		int count = cursor.getCount();
		
		if(count > 0){
			
			Log.d("ContactNumber", count + "\n");
			
			//�������� �б�
			cursor.moveToFirst();
			while(cursor.moveToNext()){
				
				//���̵�
				String id = cursor.getString(
						cursor.getColumnIndex(ContactsContract.Contacts._ID)); 
				
				//�̸�
				String name = cursor.getString(
						cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)); 
				
				//String phoneNum = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
				
				//��ȭ��ȣ ���� ������� 1, �ƴ� ��� 0
				int phoneNumFlag = Integer.parseInt(cursor.getString(
								cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));  
				
				//log �Է�
				String log = id + " / " + name + " / ";		
				
				if(phoneNumFlag > 0){
					
					//��ȭ��ȣ select from db
					Cursor pCur = cr.query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
							null, 
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?", 
							new String[]{id}, null);
					
					//��ȭ��ȣ ���
					while (pCur.moveToNext()){
						
						//��ȭ��ȣ
						String phoneNum = pCur.getString(
								pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						
						
						
						//��ȭ��ȣ ������ ����
						phoneNum = phoneNum.replace("-", "");
						
						//82�� ����
						if(numberFlag == PHONE_NUMBER_TO_82){
							
							if(phoneNum.substring(0, 3).equals("010")){
								
								phoneNum = "82" + phoneNum.substring(1);
							}
							
						// 010���� ����	
						}else if(numberFlag == PHONE_NUMBER_TO_010){
							
							if(phoneNum.substring(0, 2).equals("82")){
								
								phoneNum = "0" + phoneNum.substring(2);
								

							}
						}
						
						//��ȭ��ȣ ���� ����
						phoneNum = PhoneNumberUtils.formatNumber(phoneNum);
						
						log += phoneNum + " / ";
						
					}
					
					log += "\n";
					
					Log.d("ReadContact", log);
					
					pCur.close();
					
				}
			}
		}
	}
	
	public void WriteNumber(String id, String phone1, String phone2){
		
		ContentValues values = new ContentValues();
		
		values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phone2);
		
		
		cr.update(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				values,
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND" + 
				ContactsContract.CommonDataKinds.Phone.NUMBER + " = ? ", 
				new String[]{id, phone1});
		
	}

}
