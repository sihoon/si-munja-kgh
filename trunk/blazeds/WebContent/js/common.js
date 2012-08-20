function _addFavorite(title, url) {
	if (window.sidebar) {
		// 파이어폭스(Firefox)
		window.sidebar.addPanel(title, url, "");
	}
	else if(window.opera && window.print) {
		// 오페라(Opera)
		var elem = document.createElement('a');
		elem.setAttribute('href', url);
		elem.setAttribute('title', title);
		elem.setAttribute('rel', 'sidebar');
		elem.click();
	}
	else if(document.all) {
		// 인터넷익스플로러(IE)
		window.external.addFavorite(url, title);
	}
}

function openWindow(url, id, width, height)
{
	var w = window.open(url,id,"width="+width+" height="+height+",resizeable=no , scrollbars=no");
	w.focus();
	return false;
}

function radioValue(obj) {
	
	var rslt = "";
	for( var i = 0; i < obj.length; i++){
		if(obj[i].checked) rslt = obj[i].value;
	}

	return rslt;
}

function billingMethod() {
	
	var f = document.form;
	var rslt = radioValue(f.method);

	if (rslt == "cash") {
		document.getElementById("cashBox").style.display = "block";
		document.getElementById("etcBox").style.display = "none";
	}else {
		document.getElementById("cashBox").style.display = "none";
		document.getElementById("etcBox").style.display = "block";
	}
}

function billingCheck() {

	var f = document.form;
	var method = radioValue(f.method);
	var amount = radioValue(f.amount);
	if (method=="") {
		alert("결제방식이 없습니다.");
		return;
	}
	
	if (amount == "") {
		alert("결제금액이 없습니다.");
		return;
	}
	
	document.formBilling.smethod.value = method;
	document.formBilling.amount.value = amount;
	document.formBilling.submit();
	
}

function billingCashCheck() {

	var f = document.form;
	var method = radioValue(f.method);
	var amount = radioValue(f.amount);
	var cash = radioValue(f.cash);
	var cashName = f.cashName.value;
	if (method=="") {
		alert("결제방식이 없습니다.");
		return;
	}
	
	if (amount == "") {
		alert("결제금액이 없습니다.");
		return;
	}
	
	if (cash == "") {
		alert("계좌선택이 없습니다.");
		return;
	}
	
	if (cashName == "") {
		alert("계좌선택이 없습니다.");
		return;
	}
	document.formBillingCash.smethod.value = method;
	document.formBillingCash.amount.value = amount;
	document.formBillingCash.cash.value = cash;
	document.formBillingCash.cashName.value = cashName;
	document.formBillingCash.submit();
	
}

function taxWindow(idx) {
	
	return openWindow("my/tax.jsp?idx="+idx,"tax",400,500);
}

function checkTax() {
	var f = document.form;
	if (!f.comp_name.value) {
		alert("회사명을 입력하세요.");
		f.comp_name.focus();
	}else if (!f.comp_no.value) {
		alert("사업자번호를 입력하세요.");
		f.comp_no.focus();
	}else if (!f.name.value) {
		alert("이름을 입력하세요.");
		f.name.focus();
	}else if (!f.addr.value) {
		alert("주소를 입력하세요.");
		f.addr.focus();
	}else if (!f.upte.value) {
		alert("업태를 입력하세요.");
		f.upte.focus();
	}else if (!f.upjong.value) {
		alert("업종을 입력하세요.");
		f.upjong.focus();
	}else if (!f.email.value) {
		alert("이메일을 입력하세요.");
		f.email.focus();
	} else {
		f.submit();
	}
}

function cardWindow(idx) {
	return openWindow("my/card.jsp?idx="+idx,"card",200,200);
}

function logincheck() {
	var f = document.loginForm;
	if (!f.user_id.value) {
		alert("아이디를 입력하세요.");
		return;
	}else if (!f.user_pw.value) {
		alert("비밀번호를 입력하세요.");
		return;
	}else {
		f.submit();
	}
}

function visible(obj) {
	if (obj.style.display == "none") obj.style.display = "block";
	else obj.style.display = "none";
}