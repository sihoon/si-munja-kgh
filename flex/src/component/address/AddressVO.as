package component.address
{
	[RemoteClass(alias="com.m.address.AddressVO")]
	public class AddressVO
	{
		public static const GROUP:int = 0;
		public static const ADDRESS:int = 1;
		
		public var idx:int = 0;
		public var user_id:String = "";
		public var grp:int = 0;
		public var grpName:String = "";
		public var name:String = "";
		public var phone:String = "";
		public var memo:String = "";
		public var writedate:String = "";
		
		public function AddressVO()
		{
		}
	}
}