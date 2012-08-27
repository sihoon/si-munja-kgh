package component.notic
{
	[RemoteClass(alias="com.m.notic.NoticVO")]
	public class NoticVO
	{
		public var idx:int = 0;
		public var title:String = "";
		public var content:String = "";
		public var writer:String = "";
		public var timeWrite:String = "";
		public var cnt:int = 0;
		public function NoticVO()
		{
		}
	}
}