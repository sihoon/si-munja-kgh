package lib
{
	import flash.events.TimerEvent;
	import flash.external.ExternalInterface;
	import flash.net.URLRequest;
	import flash.net.navigateToURL;
	import flash.utils.Timer;
	
	import mx.controls.Alert;
	import mx.events.CloseEvent;
	import mx.managers.PopUpManager;
	

	public class SLibrary
	{
		private static const B_TRACE:Boolean = false;
		public function SLibrary()
		{
		}
		
		public static function bTrace(obj:Object):void {
			
			if (B_TRACE)
				trace("S--->"+obj);
		}
		
		public static function getNumber(argString:String):String {
			
			if (argString == null || argString.length < 1)
				return argString;
			
			var str:String = argString;			
			var length:int = str.length;
			var ch:String = null;
			var rslt:String = "";
			
			for (var i:int = 0; i < length; i++) {
				
				ch = str.charAt(i);
				if ( str.charCodeAt(i)> 47 &&  str.charCodeAt(i)< 58 )
					rslt += ch;
			}
			
			return rslt;
		}
		
		public static function bKoreaPhoneCheck(s:String):Boolean {			
			
			var chkInvaildChar:RegExp = /[^0-9\-]/g;			
			if (chkInvaildChar.test(s))	return false;			
			
			var chkPhoneNum:RegExp = /^0[17][016789]-?\d{3,4}-?\d{4}$/;
			if (!chkPhoneNum.test(s)) return false;
			
			return true;
		}
		
		public static function addTwoSizeNumer(num:int):String {
			
			if (num < 10) return "0" + String(num) ;
			else return String(num);
		}
		
		public static function remainByte(myBytes:Number,maxBytes:Number):Number {
			var activityBytes:Number = maxBytes - myBytes;
			return activityBytes;
		}
		
		/**
		 * 문자열을 세어 줍니다.
		 * <listing>
		 * var char:String = "가나다라마바사아차카";
		 * getLengthTo(char);
		 * outPut>> 10
		 * </listing>
		 */
		public static function getByte(str:String):int {
			var lengLength:int = str.length;
			var len:int = 0;
			for(var i:int=0; i<lengLength; ++i) {
				if(is2Bytes(str.charCodeAt(i))) {
					len+=2;
				} else {
					++len;
				}
			}
			return len;
		}
		
		//getLengthTo를 위한 메서드
		public static function is2Bytes(c:int):Boolean {
			if( c>0 && c<255 ) {
				return false;
			} else {
				return true;
			}
		}
		
		public static function cutByteTo(str:String, toByte:Number):String {
			var lengLength:int = str.length;
			var len:int = 0;
			var resultString:String = "";
			
			for(var i:int = 0; i < lengLength; ++i) {
				if( str.charCodeAt(i) < 256 ) {
					++len;
				} else {
					len+=2;
				}
				
				if ( len <= toByte )
					resultString += str.charAt(i);
				else
					break;
			}
			return resultString;
		}
		
		public static function get10to16(str16:String):Number {
			
			return parseInt(str16, 16);
		}
		
		public static function alert2(text:String = "", title:String = "smspia.com", destroyTime:Number = 1000, iconClass:Class = null):Alert {
			
			var myAlert:Alert;
			var myTimer:Timer;
			// destroyTime 후 타이머 발생
			
			function timerEventHandler(evt:TimerEvent):void {
				
				myTimer.stop();// 타이머 종료				
				mx.managers.PopUpManager.removePopUp(myAlert); // Alert 제거				
			}
			
			myTimer = new Timer(destroyTime,1); // 타이머를 설정된 값에 맞게 Setting
			myTimer.addEventListener(TimerEvent.TIMER, timerEventHandler); // 이벤트핸들러 등록		
			
			myAlert = Alert.show(text, title, 4.0, null, null, iconClass, 4.0); // Alert 창 표시			
			myTimer.start(); // 타이머 시작
			
			return myAlert;
		}
		
		public static function trim(argString:String):String {
			
			if (argString == null || argString.length < 1)
				return argString;
			
			var str:String = argString;			
			var length:int = str.length;
			var ch:String = null;
			var rslt:String = "";
			
			for (var i:int = 0; i < length; i++) {
				
				ch = str.charAt(i);
				if (ch != " ")
					rslt += ch;
			}
			
			return rslt;
		}
		
		public static function alert(msg:String):void {
			
			ExternalInterface.call("alert", msg);
		}
		
		public static function javascript(msg:String):void {
			
			var u:URLRequest = new URLRequest("javascript:" + msg + "");
			navigateToURL(u,"_self");
		}
		
		public static function addComma(str:String):String {
			var rslt:String = new String(str);
			var strLen:int = str.length;
			if (strLen > 3) {
			
				var commaNum:int = int((strLen-1)/3);
				var index:int = strLen - commaNum*3;
				var num:int = 0;
				rslt = str.slice(0,index);
				for(var i:int = 0; i < commaNum; i++){
					num = index+i*3;
					rslt += ","+str.slice(num, num+3);
				}

			}
			
			return rslt;
		}
		
		public static function checkJumin(value:String):String {
			
			var result:String = null;
			var strRRN:String;
			var keyNum:Array = [2, 3, 4, 5, 6, 7, 8, 9, 2, 3, 4, 5];
			var sum:int = 0;
			
			strRRN = (value.toString()).replace(new RegExp("-", "g"), "");
			
			
			// 입력받은 주민번호가 - 기호를 제외한 13자리인지 확인
			if(strRRN.length != 13)
			{
				result = "13자리가 아닙니다.";
				
			}
			else
			{
				for(var i:int=0; i<12; i++)	{
					// 0~12번째 자리의 값을 각각 키값으로 곱하여 모두 합산한다.
					sum += parseInt(strRRN.substr(i, 1)) * keyNum[i];
				}
				
				var strRRNCheckNum:int = parseInt(strRRN.substr(12, 1));	// 주민번호의 13번째 숫자
				var caculateCheckNum:int = (11-(sum%11))%10;				// 실제 계산한 숫자
				
				// 주민번호 13번째 숫자가 실제 계산값과 맞는지 확인
				if(strRRNCheckNum != caculateCheckNum){
					result = "잘못된 주민등록번호입니다.";
				}
			}
			return result;
		}
	}
}