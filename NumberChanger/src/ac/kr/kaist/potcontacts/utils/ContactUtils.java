package ac.kr.kaist.potcontacts.utils;


import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ac.kr.kaist.potcontacts.classes.Contact;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.telephony.PhoneNumberUtils;
import android.util.Log;

public class ContactUtils {
	
	public final static int PHONE_NUMBER_TO_82 = 0;
	public final static int PHONE_NUMBER_TO_010 = 1;
	
	private ContentResolver cr;
	private Cursor cursor;
	
	public ContactUtils(Context context){
		
		cr = context.getContentResolver();
		cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
	}
	
	public ArrayList<Contact> ReadContacts(int flag){
		
		int count = cursor.getCount();
		ArrayList<Contact> contacts = new ArrayList<Contact>();
		
		if(count > 0){
			
			Contact contact = new Contact();
						
			Log.d("ContactNumber", count + "\n");
			
			//개인정보 읽기
			cursor.moveToFirst();
			while(cursor.moveToNext()){
								
				//아이디
				String id = cursor.getString(
						cursor.getColumnIndex(ContactsContract.Contacts._ID)); 
				contact.setId(id);
				
				//이름
				String name = cursor.getString(
						cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				contact.setName(name);
				
				//String phoneNum = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
				
				//전화번호 유무 있을경우 1, 아닐 경우 0
				int phoneNumFlag = Integer.parseInt(cursor.getString(
								cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));  
				
				//log 입력
				String log = id + " / " + name + " / ";		
				
				if(phoneNumFlag > 0){
					
					//전화번호 select from db
					Cursor pCur = cr.query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
							null, 
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?", 
							new String[]{id}, null);
					
					//전화번호 출력
					while (pCur.moveToNext()){
						
						//전화번호
						String phoneNum = pCur.getString(
								pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						
						//전화번호 하이픈 제거
						phoneNum = phoneNum.replace("-", "");
						
						if(flag == PHONE_NUMBER_TO_82 && phoneNum.substring(0, 2).equals("01")){
							
							phoneNum = "82" + phoneNum.substring(1);
							updateNumber(phoneNum, id);
							//updateCnt++;
							
						}else if(flag == PHONE_NUMBER_TO_010 && phoneNum.substring(0, 2).equals("82")){
							
							phoneNum = "0" + phoneNum.substring(2);
							updateNumber(phoneNum, id);
							//updateCnt++;
							
						}
												
						//전화번호 형식 지정
						phoneNum = PhoneNumberUtils.formatNumber(phoneNum);
						contact.addPhone_num(phoneNum);
						
						log += phoneNum + " / ";
						
					}
					
					log += "\n";
					Log.d("ReadContact", log);
					
					contacts.add(contact);
					
					pCur.close();
				}
			}
		}
		
		return contacts;
	}

		
	public boolean updateNumber(String number, String ContactId) {
		
		boolean success = true;
		String phnumexp = "^[0-9]*$";

		try {
			
			number = number.trim();

			if ((!number.equals("")) && (!match(number, phnumexp))) {
				
				success = false;
						
			} else {
				
				ContentResolver contentResolver = cr;

				String where = ContactsContract.Data.CONTACT_ID + " = ? AND "
						+ ContactsContract.Data.MIMETYPE + " = ?";
				
				String[] numberParams = new String[] {
						ContactId,
						ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE };

				ArrayList<android.content.ContentProviderOperation> ops = new ArrayList<android.content.ContentProviderOperation>();


				if (!number.equals("")) {

					ops.add(android.content.ContentProviderOperation
							.newUpdate(
									android.provider.ContactsContract.Data.CONTENT_URI)
							.withSelection(where, numberParams)
							.withValue(Phone.NUMBER, number).build());
				}
				
				contentResolver.applyBatch(ContactsContract.AUTHORITY, ops);
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
			success = false;
		}
		
		return success;
	}
	
	public boolean updateContact(String name, String number, String email, String ContactId) {
		
		boolean success = true;
		String phnumexp = "^[0-9]*$";

		try {
			
			name = name.trim();
			email = email.trim();
			number = number.trim();

			if (name.equals("") && number.equals("") && email.equals("")) {
				
				success = false;
				
			} else if ((!number.equals("")) && (!match(number, phnumexp))) {
				
				success = false;
				
			} else if ((!email.equals("")) && (!isEmailValid(email))) {
				
				success = false;
				
			} else {
				
				ContentResolver contentResolver = cr;

				String where = ContactsContract.Data.CONTACT_ID + " = ? AND "
						+ ContactsContract.Data.MIMETYPE + " = ?";

				String[] emailParams = new String[] {
						ContactId,
						ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE };
				
				String[] nameParams = new String[] {
						ContactId,
						ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE };
				
				String[] numberParams = new String[] {
						ContactId,
						ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE };

				ArrayList<android.content.ContentProviderOperation> ops = new ArrayList<android.content.ContentProviderOperation>();

				if (!email.equals("")) {
					ops.add(android.content.ContentProviderOperation
							.newUpdate(
									android.provider.ContactsContract.Data.CONTENT_URI)
							.withSelection(where, emailParams)
							.withValue(Email.DATA, email).build());
				}

				if (!name.equals("")) {
					ops.add(android.content.ContentProviderOperation
							.newUpdate(
									android.provider.ContactsContract.Data.CONTENT_URI)
							.withSelection(where, nameParams)
							.withValue(StructuredName.DISPLAY_NAME, name)
							.build());
				}

				if (!number.equals("")) {

					ops.add(android.content.ContentProviderOperation
							.newUpdate(
									android.provider.ContactsContract.Data.CONTENT_URI)
							.withSelection(where, numberParams)
							.withValue(Phone.NUMBER, number).build());
				}
				
				contentResolver.applyBatch(ContactsContract.AUTHORITY, ops);
			}
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
		}
		return success;
	}

	

	private boolean isEmailValid(String email) {
		String emailAddress = email.toString().trim();
		if (emailAddress == null)
			return false;
		else if (emailAddress.equals(""))
			return false;
		else if (emailAddress.length() <= 6)
			return false;
		else {
			String expression = "^[a-z][a-z|0-9|]*([_][a-z|0-9]+)*([.][a-z|0-9]+([_][a-z|0-9]+)*)?@[a-z][a-z|0-9|]*\\.([a-z][a-z|0-9]*(\\.[a-z][a-z|0-9]*)?)$";
			CharSequence inputStr = emailAddress;
			Pattern pattern = Pattern.compile(expression,
					Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(inputStr);
			if (matcher.matches())
				return true;
			else
				return false;
		}
	}

	private boolean match(String stringToCompare, String regularExpression) {
		boolean success = false;
		Pattern pattern = Pattern.compile(regularExpression);
		Matcher matcher = pattern.matcher(stringToCompare);
		if (matcher.matches())
			success = true;
		return success;
	}

}