package lib
{
	import flash.events.Event;
	import flash.net.FileReference;
	import flash.utils.ByteArray;
	
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import lib.SLibrary;
	
	public class FileUploadByRemoteObjectEvent extends Event
	{
		public static const SELECT:String = "select";
		public static const COMPLETE:String = "complete"; 
		public static const RESULT:String = "result";
		public static const FAULT:String = "fault";
		// enable  properties  상태를 보관 유지하는  public 변수를 정의한다
		public var isEnabled:Boolean;
		
		public var event:Event;
		
		public var data:ByteArray;
		public var fileName:String;
		public var result:Object;
		public var fault:Object;

		public function FileUploadByRemoteObjectEvent(type:String, event:Event, bubbles:Boolean=false, cancelable:Boolean=false){
			
			SLibrary.bTrace("FileUploadByRemoteObjectEvent");
			super(type, bubbles, cancelable);
			// 새로운  properties 를 설정한다
			this.isEnabled = isEnabled;
			var refUploadFile:FileReference = null;
			
			switch (type) {
				
				case FileUploadByRemoteObjectEvent.SELECT:
					
				break;
				
				case FileUploadByRemoteObjectEvent.COMPLETE:
					
					refUploadFile= event.currentTarget as FileReference; 	
					this.data = new ByteArray(); 
					refUploadFile.data.readBytes(this.data,0,refUploadFile.data.length);					
					this.fileName = refUploadFile.name;
				break;
				
				case FileUploadByRemoteObjectEvent.RESULT:
					
					result = (event as ResultEvent).result;
				break;
				
				case FileUploadByRemoteObjectEvent.FAULT:
					
					this.fault = (event as FaultEvent).fault;
				break;
			}

		}
		
		// 상속 받은 clone() 메소드를 재정의(override) 한다 
		override public function clone() :Event {
			SLibrary.bTrace("clone");
			return new FileUploadByRemoteObjectEvent(type, event, isEnabled);
		}

	}
}