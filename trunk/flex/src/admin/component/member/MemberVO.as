package admin.component.member
{
	[RemoteClass(alias="com.m.member.MemberVO")]
	public class MemberVO
	{
		public var idx:int;
		public var user_id:String = "";
		public var passwd:String = "";
		public var user_type:String = "";
		public var user_name:String = "";
		public var jumin_no:String = "";
		public var email:String = "";
		public var phone_return:String = "";
		public var hp:String = "";
		public var unit_cost:String = "";
		public var line:String = "";
		public var memo:String = "";
		public var timeLogin:String = "";
		public var timeJoin:String = "";
		public var leaveYN:String = "";
		public var point:int = 0;
		
		public function MemberVO()
		{
		}
	}
}