package admin.component.billing
{
	[RemoteClass(alias="com.m.billing.BillingVO")]
	public class BillingVO
	{
		public var idx:int = 0;
		public var user_id:String = "";
		public var method:String = "";
		public var amount:int = 0;
		public var order_no:String = ""; 
		public var unit_cost:String = "";
		public var remain_point:String = ""; 
		public var memo:String = "";
		public var admin_id:String = "";
		public var timeWrite:String = "";
		
		public function BillingVO()
		{
		}
	}
}