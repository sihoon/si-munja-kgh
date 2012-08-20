function juminValidate(jumin) {
	var tab = [2, 3, 4, 5, 6, 7, 8, 9, 2, 3, 4, 5];
	var num = new Array();
	num[0] = jumin.substring(0,1);
	num[1] = jumin.substring(1,2);
	num[2] = jumin.substring(2,3);
	num[3] = jumin.substring(3,4);
	num[4] = jumin.substring(4,5);
	num[5] = jumin.substring(5,6);
	num[6] = jumin.substring(6,7);
	num[7] = jumin.substring(7,8);
	num[8] = jumin.substring(8,9);
	num[9] = jumin.substring(9,10);
	num[10] = jumin.substring(10,11);
	num[11] = jumin.substring(11,12);
	num[12] = jumin.substring(12,13);

	var sum = 0;
	
	for(var i = 0; i < 12; i++) { 
		sum += num[i] * tab[i];
	}

	return ((11-(sum % 11)) % 10 == num[12])? true:false;

}

function isRegNo(no) {

	no = no.replace("-","");
	
	if (no.length != 13) {
		return false;
	}
	var arr_regno = no.split("");

	var arr_wt = new Array(1,2,1,2,1,2,1,2,1,2,1,2);
	var iSum_regno = 0;
	var iCheck_digit = 0;

	for (var i = 0; i < 12; i++) {
		iSum_regno += eval(arr_regno[i]) * eval(arr_wt[i]);
	}

	iCheck_digit = 10 - (iSum_regno % 10);
	iCheck_digit = iCheck_digit %10;

	if(iCheck_digit != arr_regno[12]) {
		return false;
	}

	return true;
}



function check1() {

	if (!document.getElementById("sub1ok").checked) {
		alert("이용약관에 동의 하세요.");
		return false;
	}
	
	if (!document.getElementById("sub2ok").checked) {
		alert("개인정보보호방침에 동의 하세요.");
		return false;
	}

	if (!document.getElementById("sub3ok").checked) {
		alert("스팸약관에 동의 하세요.");
		return false;
	}
	
	window.location.href='?content=join3';
}

function check2() {
	var f = document.form;
	
	if (!f.name.value) { alert("이름을 입력하세요."); f.name.focus(); return false; }
	if (!f.jumin1.value) { alert("주민등록번호를 입력 하세요."); f.jumin1.focus(); return false; }
	if (!f.jumin2.value) { alert("주민등록번호를 입력 하세요."); f.jumin2.focus(); return false; }
	
	if ( !juminValidate( f.jumin1.value + f.jumin2.value ) ) {
		alert("잘못된 주민등록 번호 입니다.");
		f.jumin1.focus();
		return false;
	}
	f.submit();
}

function check_join() {
	
	var f = document.form;
		
	if (!f.join_id.value) { alert("아이디를 입력해주세요"); f.user_id.focus(); return false; }
	if (!f.id_ok.value) { alert("아이디를 확인해주세요"); f.user_id.focus(); return false; }
	if (!f.passwd1.value) {	alert("비밀번호를 입력해주세요"); f.passwd1.focus(); return false; }
	if (f.passwd1.value.length < 6) { alert("비밀번호는 6자 이상으로 입력하세요.");	f.passwd1.focus(); return false; }
	if (f.passwd1.value != f.passwd2.value) { alert("비밀번호와 비밀번호 확인이 다릅니다."); f.passwd1.focus(); return false; }
//	if (!f.name.value || !f.jumin1.value || !f.jumin2.value) { alert("주민등록번호가 확인 되지 않았습니다. 다시 시도 하십시요."); return false; }
	if (!f.name.value) { alert("이름을 입력해주세요"); f.name.focus(); return false; }
	if (!f.hp.value) {	alert("핸드폰 번호를 입력해주세요"); f.hp.focus(); return false; }
	if (!f.email.value) { alert("이메일을 입력해주세요"); f.email.focus(); return false; }
	if (!isValidEmail(f.email.value)) { alert("잘못된 형식의 이메일 주소 입니다."); f.email.focus(); return false; }
	
	/*if (!f.captchaInput.value) { alert("자동가입 방지를 넣어 주세요"); f.captchaInput.focus(); return false; }*/
	
	f.target = "nobody";	
	f.action = "member/_join.jsp";
	f.submit();
	return false;
}

function check_modify() {
	
	var f = document.form;
		
	if (!f.passwd1.value) {	alert("비밀번호를 입력해주세요"); f.passwd1.focus(); return false; }
	if (f.passwd1.value.length < 6) { alert("비밀번호는 6자 이상으로 입력하세요.");	f.passwd1.focus(); return false; }
	if (f.passwd1.value != f.passwd2.value) { alert("비밀번호와 비밀번호 확인이 다릅니다."); f.passwd1.focus(); return false; }
	
	if (!f.hp.value) {	alert("핸드폰 번호를 입력해주세요"); f.hp.focus(); return false; }
	if (!f.email.value) { alert("이메일을 입력해주세요"); f.email.focus(); return false; }
	if (!isValidEmail(f.email.value)) { alert("잘못된 형식의 이메일 주소 입니다."); f.email.focus(); return false; }
	/*if (!f.captchaInput.value) { alert("자동가입 방지를 넣어 주세요"); f.captchaInput.focus(); return false; }*/
	
	f.target = "nobody";	
	f.action = "member/_modify.jsp";
	f.submit();
	return false;
}

function moveCursor(before, next, length) {
	if (before.value.length==length) {
		next.focus() ;
		return;
	}
}

function isValidEmail(email_address) {
	
	// 이메일 주소를 판별하기 위한 정규식  
	//var format = /^[_0-9a-zA-Z-]+(\.[_0-9a-zA-Z-]+)*@[0-9a-zA-Z-]+(\.[0-9a-zA-Z-]+)*$/;
	var format = /^\s*[\w\~\-\.]+\@[\w\~\-]+(\.[\w\~\-]{2,12}){1,3}\s*$/g;
	// 인자 email_address를 정규식 format 으로 검색  
	if (email_address.search(format) != -1)  
	{  
	    // 정규식과 일치하는 문자가 있으면 true  
	    return true;  
	}  
	else  
	{  
	    // 없으면 false  
	    return false;  
	}  
}

$("#juminCheck").click(function() {
	var f = document.form;
	if (!f.name.value) { alert("이름을 입력하세요."); f.name.focus(); return false; }
	if (!f.jumin1.value) { alert("주민등록번호를 입력 하세요."); f.jumin1.focus(); return false; }
	if (!f.jumin2.value) { alert("주민등록번호를 입력 하세요."); f.jumin2.focus(); return false; }

	if ( !juminValidate( f.jumin1.value + f.jumin2.value ) && !isRegNo( f.jumin1.value + f.jumin2.value ) ) {
		alert("잘못된 주민등록 번호(법인번호) 입니다.");
		f.jumin1.focus();
		return false;
	} else {
		$.post(
			'member/jumin_check.jsp',
			{
				jumin : f.jumin1.value+f.jumin2.value
			}, 
			function(val) {
				if ($.trim(val) == "yes") {
					f.submit();
				} else {
					
					alert("사용 불가능한 주민등록번호(법인번호)입니다");
				}
			}
		);
		
	}
	
});

$("#join_id").keyup(function() {
	$("#id_ok").val("");
	var len = $("#join_id").val().length;
	var regExp = /^[a-z0-9_]{6,16}$/;
	
	if (len < 6) {
		$("#user_id_check").css("color","red");
		$("#user_id_check").text("아이디는 6자 이상으로 만들어주세요");
	}
	else if ( !regExp.test($("#join_id").val()) ) {
		$("#user_id_check").css("color","red");
		$("#user_id_check").text("영문 소문자와 숫자만 가능합니다.");
		
	} else {
		if (len >= 6) {
			$.post(
				'member/id_check.jsp',
				{
					user_id : $("#join_id").val()
				}, 
				function(val) {
					if ($.trim(val) == "yes") {
						$("#user_id_check").css("color","green");
						$("#user_id_check").text("사용 가능한 아이디입니다");
						$("#id_ok").val($("#join_id").val());
					} else {
						$("#user_id_check").css("color","red");
						$("#user_id_check").text("사용 불가능한 아이디입니다");
					}
				}
			);
		} else {
			$("#user_id_check").css("color","red");
			$("#user_id_check").text("아이디는 6자 이상으로 만들어주세요");
		}
	}
	
});

$("#passwd1").keyup(function() {
	var len = $("#passwd1").val().length;
	
	if (len < 6) {
		$("#passwd1_check").css("color","red");
		$("#passwd1_check").text("비밀번호는 6자 이상으로 입력하세요.");
	}else {
		$("#passwd1_check").css("color","green");
		$("#passwd1_check").text("사용 가능한 비밀번호입니다.");
	}
});

$("#passwd2").keyup(function() {
	var len = $("#passwd2").val().length;
	
	if ($("#passwd1").val() != $("#passwd2").val()) {
		$("#passwd2_check").css("color","red");
		$("#passwd2_check").text("입력한 비밀번호와 다릅니다.");
	}else {
		$("#passwd2_check").css("color","green");
		$("#passwd2_check").text("확인 되었습니다.");
	}
});