package lib
{
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ICollectionView;
	import mx.collections.XMLListCollection;
	import mx.controls.treeClasses.DefaultDataDescriptor;
	import mx.controls.treeClasses.ITreeDataDescriptor;
	import mx.core.mx_internal;
	
	public class FilteredTreeDataDescriptor implements ITreeDataDescriptor
	{
		private var _filter:Function;
		
		public function FilteredTreeDataDescriptor(filterFunction:Function)
		{
			_filter = filterFunction;
		}
		
		public function getChildren(node:Object, model:Object=null):ICollectionView
		{
			return _filter(node);
		}
		
		public function hasChildren(node:Object, model:Object=null):Boolean
		{
			return getChildren(node, model).length > 0;
		}
		
		public function isBranch(node:Object, model:Object=null):Boolean
		{
			return hasChildren(node, model);
		}
		
		public function getData(node:Object, model:Object=null):Object
		{
			//Not implemented
			return null;
		}
		
		public function addChildAt(parent:Object, newChild:Object, index:int, model:Object=null):Boolean
		{
			//Not implemented
			return false;
		}
		
		public function removeChildAt(parent:Object, child:Object, index:int, model:Object=null):Boolean
		{
			//Not implemented
			return false;
		}
		
		///////////////////////////////////
		// End
		///////////////////////////////////
	}
}