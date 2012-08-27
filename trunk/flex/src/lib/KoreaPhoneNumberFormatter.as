package lib
{
	import mx.formatters.Formatter;
	import mx.formatters.SwitchSymbolFormatter;
	
	public class KoreaPhoneNumberFormatter extends Formatter
	{
		public function KoreaPhoneNumberFormatter()
		{
			super();
		}
		
		//format() 메소드를 override 한다
		override public function format(value:Object) : String{
			
			var emptyStr:String = "";
			var valStr:String = String(value);
			var resultStr:String = emptyStr;
			
			if(!(valStr != null && valStr.length > 0))
				return emptyStr;
			
			var regExp:RegExp = /[^0-9|\-]+/g
			if(regExp.test(valStr))
				return emptyStr;
			
			var ssf:SwitchSymbolFormatter = new SwitchSymbolFormatter();
			
			
			valStr = valStr.replace(/\-/g,"");
			if(valStr.length == 10)
				resultStr = ssf.formatValue("###-###-####",valStr);
			else if(valStr.length == 11)
				resultStr = ssf.formatValue("###-####-####",valStr);
			else
				resultStr = valStr;
			
			
			return resultStr;
		}
	}
}