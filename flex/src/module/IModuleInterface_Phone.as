package module
{
	import flash.events.IEventDispatcher;
	public interface IModuleInterface_Phone extends IEventDispatcher {
		
		function setMessage(msg:String):void;
		function addList(phone:String):void;
		function addListArray(arr:Array):void;
		function changeLMS():void;
		function changeSMS():void;
		function changeMMS():void;
	}
}