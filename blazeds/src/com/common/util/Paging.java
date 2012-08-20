package com.common.util;

/*
 * Author : Si
 * 
 * Paging Class
 * 
 * @properties
 * 		firstLink , prevLink, nextLink , lastLink : set up image or text if you want.
 * 		delimiter : set up image or text
 * 		prewrap , postwrap : currentPage design wrapper tag
 */

public class Paging {

	private int totalRows = 0;
	private int currentPage = 1;
	private int pageSize = 10;
	private int blockSize = 10;	
	private int totalPages;
	private int totalBlocks;
	private int startPageNum;
	private int endPageNum;
	private int currentBlock;

	private String amp = "";
	
	// for design
	public String firstLink = "[<<]";
	public String firstOffLink = "";
	public String firstBlockLink = "[<]";
	public String firstBlockOffLink = "";
	public String prevLink = "이전";
	public String prevOffLink = "";
	public String nextLink = "다음";
	public String nextOffLink = "";
	public String lastBlockLink = "[>]";
	public String lastBlockOffLink = "";
	public String lastLink = "[>>]";
	public String lastOffLink = "";
	
	public String delimiter = "|";
	
	// current Page Wrapper
	public String preWrap = "<b style=\"display:inline;\" class='here'>";
	public String postWrap = "</b>";
	
	public String linkPage = "";
	public String queryString = "";
	public String pg = "pg";
	
	// result temp object
	public StringBuffer pageString = new StringBuffer();
	
	public Paging(int currentPage , int pageSize , int blockSize , int totalRows)
	{
		this.currentPage = currentPage;
		this.pageSize = pageSize;
		this.blockSize = blockSize;
		this.totalRows = totalRows;
		
		initialize();
	} 
	
	public void initialize()
	{	
		this.totalPages = (int)Math.ceil((double)this.totalRows / this.pageSize);
		this.totalBlocks = (int)Math.ceil((double)this.totalPages / this.blockSize);
		this.currentBlock = (int)Math.ceil((double)((this.currentPage - 1) / this.blockSize)) + 1;		
		this.startPageNum = ((this.currentBlock - 1) * this.blockSize) + 1;
		this.endPageNum = this.startPageNum + this.blockSize;
		
		
	}
	
	public void prePrint()
	{
		// set first block link//" + (((this.currentBlock - 2) * this.pageSize) + 1) + "
		if(this.currentBlock > 1)
			pageString.append("<a href=\"" + this.linkPage + "?" + this.queryString + this.amp + pg +"=1\">" + this.firstLink + "</a> ");
		else
			pageString.append(this.firstOffLink + " ");
		
		// set next prev block link
		if(this.currentBlock > 1)
			pageString.append("<a href=\"" + this.linkPage + "?" + this.queryString + this.amp + pg +"=" + (( (this.currentBlock-2) * this.blockSize) + 1) + "\">" + this.firstBlockLink + "</a> ");
		else
			pageString.append(this.firstBlockOffLink + " ");
		
		// set prev page link
		if(this.currentPage > 1)
			pageString.append("<a href=\"" + this.linkPage + "?" + this.queryString + this.amp + pg +"=" + (this.currentPage  - 1) + "\"  class='prev'>" + this.prevLink + "</a> ");
		else
			pageString.append(this.prevOffLink + " ");		
	}
	
	public void postPrint()
	{
		// set next page link
		if(this.currentPage < this.totalPages )
			pageString.append("<a href=\"" + this.linkPage + "?" + this.queryString + this.amp + pg +"=" + (this.currentPage + 1) + "\"  class='next'>" + this.nextLink + "</a> ");
		else
			pageString.append(this.nextOffLink + " ");
		
		// set next block link
		if(this.currentBlock < this.totalBlocks)
			pageString.append("<a href=\"" + this.linkPage + "?" + this.queryString + this.amp + pg +"=" + ((this.currentBlock * this.blockSize) + 1) + "\">" + this.lastBlockLink + "</a> ");
		else
			pageString.append(this.lastBlockOffLink);
		
		// set last page link // this.currentPage * this.pageSize) + 1
		if(this.currentBlock < this.totalBlocks)
			pageString.append("<a href=\"" + this.linkPage + "?" + this.queryString + this.amp + pg +"=" + this.totalPages + "\">" + this.lastLink + "</a> ");
		else
			pageString.append(this.lastOffLink);
	}
	
	public void printList()
	{	
		for(int i = startPageNum ; i <= endPageNum ; i++)
		{
			if(i > this.totalPages || i == endPageNum)
				break;
			else if(i > startPageNum)
				pageString.append(this.delimiter);
			
			if(i == this.currentPage)			
				pageString.append(" " + this.preWrap + i +  this.postWrap + " ");
			else
				pageString.append(" <a href=\"" + this.linkPage + "?" + this.queryString + this.amp + pg +"=" + i + "\"  class='other'>" + i + "</a> ");
		}
	}
	
	public String print()
	{
		// set amp if already to set up queryString property
		if(!this.queryString.equals(""))
			this.amp = "&";
		
		if(this.totalPages > 1)
		{
			this.prePrint();
			this.printList();
			this.postPrint();
		}
		
		return(pageString.toString());
	}
	
	/**
	 * @param args
	 */
	/*
	public static void main(String[] args) throws Exception {
		

		Paging pg = new Paging(1 , 10, 10 , 11);
		pg.linkPage = "pagenum.jsp";
		pg.queryString = "param1=test&param2=test2";
		
		// for design
		pg.firstLink = "<img src=\"/first.gif\">";
		pg.prevLink = "<img src=\"/prev.gif\">";
		pg.nextLink = "<img src=\"/next.gif\">";
		pg.lastLink = "<img src=\"/last.gif\">";
		
		pg.firstOffLink = "[<<]";
		pg.prevOffLink = "[<]";
		pg.nextOffLink = "[>]";
		pg.lastOffLink = "[>>]";
		
		pg.delimiter = "||";
		
		
		//print
		System.out.println(pg.print());
	}
	*/
}
