package lib
{
	import flash.events.Event;
	
	public class CustomEvent extends Event
	{
		// enable  properties  상태를 보관 유지하는  public 변수를 정의한다
		public var isEnabled:Boolean;
		
		public var obj:Object;		
		
		public function CustomEvent(type:String, obj:Object, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
			
			this.isEnabled = true;
			this.obj = obj;
		}
		
		// 상속 받은 clone() 메소드를 재정의(override) 한다 
		override public function clone() :Event {

			return new CustomEvent(type, obj, isEnabled);
		}
	}
}