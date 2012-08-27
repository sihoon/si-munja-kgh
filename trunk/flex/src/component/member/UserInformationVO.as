package component.member
{
	[RemoteClass(alias="com.m.member.UserInformationVO")]
	[Bindable]
	public class UserInformationVO
	{
		public var user_id:String = "";
		public var user_name:String = "";
		public var phone_return:String = "";
		public var line:String = "";
		public var point:String = "";
		public var levaeYN:String = "";
		public var unit_cost:int = 0;
		public var hp:String = "";
		public var jumin_no:String = "";
		public function UserInformationVO()	{}
	}
}