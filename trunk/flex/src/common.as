import component.member.UserInformationVO;

import lib.CustomEvent;

import module.IModuleInterface_Phone;

import mx.events.FlexEvent;
import mx.events.ModuleEvent;

[Bindable]
public var bLogin:Boolean = false;
public var login_id:String = null;

// phone 이외의 모듈에서 phone 함수 호출
public function phoneFunction(code:String, obj:Object):void {
	var ichild:* = mPhone.child as IModuleInterface_Phone;
	
	if (mPhone.child != null) {

		if (code == "message") ichild.setMessage(obj as String); 
		else if (code == "addMessage") ichild.setMessageAdd(obj as String);
		else if (code == "addPhone") ichild.addList(obj as String);
		else if (code == "addPhoneArray") ichild.addListArray(obj as Array);
		else if (code == "addPhoto") ichild.setPhoto(obj as String);
		else if (code == "sms") ichild.changeSMS();
		else if (code == "lms") ichild.changeLMS();
		else if (code == "mms") ichild.changeMMS();
		else if (code == "messageAuto") ichild.setAutoMessage(obj as Boolean);
		
		
	}else ExternalInterface.call("alert", "phone 모듈을 찾을 수 없습니다.");
}

// 모듈에서 서브 호출 하는 공통 함수
public function subLoad(sub:String):void {
	
	mSub.unloadModule();
	if (sub == "address") mSub.loadModule("module/MAddress.swf");
	else if (sub == "excel") mSub.loadModule("module/MExcel.swf");
	else if (sub == "photo") mSub.loadModule("module/MPhoto.swf");
	else if (sub == "sent") mSub.loadModule("module/MSent.swf");
	else if (sub == "copypaste") mSub.loadModule("module/MCopyPaste.swf");
	else if (sub == "emoticon")  mSub.loadModule("module/MEmoticon.swf");
	else if (sub == "char") mSub.loadModule("module/MSpecChar.swf");
	
	
	//subContainer.visible = true;
}
