package lib
{
	import flash.events.Event;
	import flash.net.FileReference;
	import flash.utils.ByteArray;
	
	import lib.SLibrary;
	
	import mx.core.UIComponent;
	import mx.rpc.AsyncToken;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;


	public class FileUploadByRemoteObject extends UIComponent
	{
		
		private var ro:RemoteObject = null;
		private var refUploadFile:FileReference; 
		//{name: , size: , status: , realsize:}
		private var _UploadFiles:Array = new Array();
		
		private static const STATUS_FINISHED:String = "finished";
		private static const STATUS_UPLOAD:String = "uploading";
		private static const STATUS_ERROR:String = "error";
		
		/*###############################
		#	public Function				#
		###############################*/
		public function FileUploadByRemoteObject( destination:String ){
			
			SLibrary.bTrace("FileUploadByRemoteObject");
			this.roInit(destination);
		}
		
		public function set UploadFiles(UploadFiles:Array):void { this._UploadFiles = UploadFiles; }
		public function get UploadFiles():Array { return this._UploadFiles; }
		
		public function set remoteObject(ro:RemoteObject):void { this.ro = ro; }
		public function get remoteObject():RemoteObject { return this.ro; }
		
		public function addFiles():void { 
			
			SLibrary.bTrace("addFiles");
			refUploadFile = new  FileReference(); 
			refUploadFile.browse(); 
			refUploadFile.addEventListener(Event.SELECT,onFileSelect); 
			refUploadFile.addEventListener(Event.COMPLETE,onFileComplete); 
		} 
		
		// Called to format number to file size 
		public function formatFileSize(numSize:Number):String { 
			
			SLibrary.bTrace("formatFileSize");
			var strReturn:String; 
			numSize = Number(numSize / 1000); 
			strReturn = String(numSize.toFixed(1) + " KB"); 
			if (numSize > 1000) { 
				numSize = numSize / 1000; 
				strReturn = String(numSize.toFixed(1) + " MB"); 
				if (numSize > 1000) { 
					numSize = numSize / 1000; 
					strReturn = String(numSize.toFixed(1) + " GB"); 
				} 
			}    
			return strReturn; 
		} 
		
		/*###############################
		#	private Function			#
		###############################*/
		private function roInit(destination:String):void {
			
			SLibrary.bTrace("roInit");
			ro = new RemoteObject();
			ro.showBusyCursor = true;
			ro.destination = destination;
			ro.addEventListener( ResultEvent.RESULT, resultHandler );
			ro.addEventListener( FaultEvent.FAULT, faultHandler );			
		}
		
		/*###############################
		#	EventHandler				#
		###############################*/
		private function resultHandler(event:ResultEvent):void {
			
			SLibrary.bTrace("resultHandler");
			for ( var i:int = 0 ; i <  _UploadFiles.length ; i++ ) { 
				if( _UploadFiles[i].name == event.token.kind ) { 
					_UploadFiles[i].status = STATUS_FINISHED;  
					break; 
				} 
			}
			var fure:FileUploadByRemoteObjectEvent = new FileUploadByRemoteObjectEvent(FileUploadByRemoteObjectEvent.RESULT, event);
			fure.isEnabled = true;
			this.dispatchEvent(fure);
		}
		
		private function faultHandler(event:FaultEvent):void {
			
			SLibrary.bTrace("faultHandler");
			for ( var i:int = 0 ; i <  _UploadFiles.length ; i++ ) { 
				if( _UploadFiles[i].name == event.token.kind ) { 
					_UploadFiles[i].status = STATUS_ERROR;  
					break; 
				} 
			}
			var fure:FileUploadByRemoteObjectEvent = new FileUploadByRemoteObjectEvent(FileUploadByRemoteObjectEvent.FAULT, event);
			fure.isEnabled = true;
			this.dispatchEvent(fure);

		}
		
		// Called when a file is selected 
		private function onFileSelect(event:Event):void { 
			
			SLibrary.bTrace("onFileSelect");
			
			_UploadFiles.push({  name:refUploadFile.name, 
				size:formatFileSize(refUploadFile.size), 
				status:"initial",
				realsize:String(refUploadFile.size)
			});   
			
			refUploadFile.load(); 
			for ( var i:int = 0 ; i <  _UploadFiles.length ; i++ ) { 
				if( _UploadFiles[i].name == refUploadFile ) { 
					_UploadFiles[i].status = "loaded";  
					break; 
				} 
			}
			var fure:FileUploadByRemoteObjectEvent = new FileUploadByRemoteObjectEvent(FileUploadByRemoteObjectEvent.SELECT, event);
			fure.isEnabled = true;
			this.dispatchEvent(fure);
		}
		
		private function onFileComplete(event:Event):void {
			
			SLibrary.bTrace("onFileComplete");
			
			for ( var i:int = 0 ; i <  _UploadFiles.length ; i++ ) { 
				if( _UploadFiles[i].name == refUploadFile ) { 
					_UploadFiles[i].status = "upload";  
					break; 
				} 
			}
			var fure:FileUploadByRemoteObjectEvent = new FileUploadByRemoteObjectEvent(FileUploadByRemoteObjectEvent.COMPLETE, event);
			fure.isEnabled = true;
			this.dispatchEvent(fure);
		} 
		
	}
}