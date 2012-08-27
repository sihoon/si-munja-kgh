package component.excel
{
	import mx.collections.ArrayCollection;

	[RemoteClass(alias="com.m.excel.ExcelLoaderResultVO")]
	[Bindable]
	public class ExcelLoaderResultVO
	{
		public var bResult:Boolean;
		public var strDescription:String;
		public var list:ArrayCollection;
		
		public function ExcelLoaderResultVO()
		{
		}
	}
}