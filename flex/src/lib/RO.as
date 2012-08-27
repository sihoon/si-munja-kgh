package lib
{
	import flash.external.ExternalInterface;
	
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	public class RO extends RemoteObject
	{
		private var rsltFunction:Function = null;
		
		public function RO(destination:String=null)
		{
			super(destination);
			this.showBusyCursor = false;
			this.addEventListener(FaultEvent.FAULT, remoteObjectFaultEventHandler);
		}
		
		public function set(destination:String, rsltListener:Function ):void {
			
			if (rsltFunction != null) 
				this.removeEventListener(ResultEvent.RESULT, rsltFunction);
				
			this.destination = destination;
			this.addEventListener(ResultEvent.RESULT, rsltListener);
			
			rsltFunction = rsltListener;
		}
		
		private function remoteObjectFaultEventHandler(event:FaultEvent):void{
			
			trace(event.fault);
			ExternalInterface.call("alert",event.fault.faultDetail);
		}
		
		public function get method():RemoteObject {
			return RemoteObject(this);
		}
	}
}