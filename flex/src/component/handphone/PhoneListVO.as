package component.handphone
{
	[RemoteClass(alias="com.m.mobile.PhoneListVO")]
	[Bindable]
	public class PhoneListVO
	{
		public var phoneSection:int;
		public var groupKey:int;
		public var phoneName:String;
		public var phoneNumber:String;
		
		public static const SECTION_MEMBER:uint=0;
		public static const SECTION_GROUP:uint=1;
		
		public function PhoneListVO(){
			
			phoneSection = 0;
			groupKey = 0;
			phoneName = "";
			phoneNumber = "";
		}
	}
}