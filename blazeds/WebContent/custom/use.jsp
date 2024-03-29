<%@page import="java.sql.SQLException"%>
<%@page import="com.common.VbyP"%>
<%@page import="com.m.member.SessionManagement"%>
<%@page import="com.m.member.UserInformationVO"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.common.util.SLibrary"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="/flexlib/swfobject.js"></script>
<script type="text/javascript" src="/member/member.js"></script>
<div id="phone_flex">
	<div id="flashContent" style="display:none;">
	    <p>
	        To view this page ensure that Adobe Flash Player version 
	        10.2.0 or greater is installed. 
	    </p>
	    <script type="text/javascript"> 
	        var pageHost = ((document.location.protocol == "https:") ? "https://" : "http://"); 
	        document.write("<a href='http://www.adobe.com/go/getflashplayer'><img src='" 
	                        + pageHost + "www.adobe.com/images/shared/download_buttons/get_flash_player.gif' alt='Get Adobe Flash player' /></a>" ); 
	    </script> 
	</div>
</div>
<div id="companywrap">
<img src="/images/top9.jpg" style="margin-bottom:10px" />
<img src="/images/privacy.gif" style="margin-bottom:10px" />
<table width="645" border="0" cellspacing="0" cellpadding="0" bgcolor="eaeaea">
  <tr>
    	<td style="padding:10px;"><table width="100%" border="0" cellspacing="0" cellpadding="0"><tr>
    	  <td><strong><img src="/images/icon.gif" align="absmiddle"> 제1조 (목적)</strong><br />
<br />
이 약관은 유진(이하 “회사”라 합니다)이 운영하는 문자랑(이하 “사이트”라 합니다)에서 제공하는 단문 메시지 서비스(이하 “서비스”라 합니다)를 이용함에 있어 “회사”와 이용자 간의 권리, 의무 및 책임사항을 규정함을 목적으로 합니다.<br />
※ 「PC통신 등을 이용하는 전자거래에 대해서도 그 성질에 반하지 않는 한 이 약관을 준용합니다」<br />
<br />
<img src="/images/line3.gif"><br />
<br />
<strong><img src="/images/icon.gif" alt="" align="absmiddle" /> 제2조 (정의)</strong><br />
<br />
<p>이 약관에서 사용하는 용어의 정의는 다음과 같습니다.</p>
<p>1. &quot;서비스&quot;라 함은 &quot;회원&quot;이 이용할 수 있는 단문 메시지 서비스 및 문자랑에서 제공하는 관련 제반 서비스를 의미합니다. <br />
  2. &quot;회원&quot;이라 함은 “회사”의 &quot;서비스&quot;에 접속하여 이 약관에 따라 &quot;회사&quot;와 이용계약을 체결하고 &quot;회사&quot;가 제공하는 &quot;서비스&quot;를 이용하는 고객을 말합니다. <br />
  3. &quot;아이디(ID)&quot;라 함은 &quot;회원&quot;의 식별과 &quot;서비스&quot; 이용을 위하여 &quot;회원&quot;이 정하고 &quot;회사&quot;가 승인하는 문자와 숫자의 조합을 의미합니다. <br />
  4. &quot;비밀번호&quot;라 함은 &quot;회원&quot;이 부여 받은 &quot;아이디와 일치되는 &quot;회원&quot;임을 확인하고 비밀보호를 위해 &quot;회원&quot; 자신이 정한 문자 또는 숫자의 조합을 의미합니다. <br />
  5. &quot;해지&quot;라 함은 &quot;회사&quot; 또는 &quot;회원&quot;이 이용계약을 해약하는 것을 말합니다.<br />
  6. &quot;유료서비스&quot;라 함은 &quot;회사&quot;가 유료로 제공하는 문자메시지 관련 제반 서비스를 의미합니다. <br />
  7. &quot;포인트&quot;라 함은 &quot;회사&quot;가 운영하는 &quot;유료서비스&quot; 이용을 위해 “회사”가 임의로 책정 또는 지급, 조정할 수 있는 &quot;서비스&quot; 상의 가상 데이터를 의미합니다.<br />
  8. &quot;충전&quot;이라 함은 &quot;회사&quot;가 정한 결제수단(무통장입금, 신용카드, 계좌이체, 가상계좌 등)을 이용하여 &quot;포인트&quot;를 구매하는 행위입니다.<br />
  9. &quot;스팸&quot;이라 함은 정보통신망을 통해 이용자가 원하지 않는데도 불구하고 일방적으로 전송 또는 게시되는 영리목적의 광고성 정보 입니다.<br />
  10. &quot;불법스팸&quot;이라 함은 정보통신망 이용촉진 및 정보보호 등에 관한 법률을 위반하여 전송 또는 게시되는 영리목적의 광고성 정보 입니다.<br />
  <br />
  <img src="/images/line3.gif"><br />
  <br />
  <strong><img src="/images/icon.gif" alt="" align="absmiddle" /> 제3조 (약관의 효력 및 변경)</strong><br />
  <br />
  “회사”는 필요하다고 인정되는 경우 이 약관을 변경할 수 있으며 사용자에게 명시함으로써 효력을 발생합니다.</p>
<p>1. 이 약관은 “회사”가 서비스에서 온라인으로 명시하거나 이용자에게 통지함으로써 효력이 발생합니다. <br />
  2. 이 약관의 적용기간은 이용자의 가입일로부터 해지 일까지로 규정합니다. 단, 채권 또는 채무관계가 있을 경우에는 채권, 채무의 완료 일까지로 규정합니다. <br />
  3. “회사”는 변경사유가 발생할 경우에 이 약관을 변경할 수 있으며, 변경된 약관은 지체 없이 공지합니다.<br />
  4. 회원은 변경된 약관 사항에 동의하지 않으면 서비스 이용을 중단하고 이용계약을 해지(탈퇴) 할 수 있습니다. 약관의 효력발생일 이후의 계속적인 서비스 이용은 약관의 변경사항에 동의한 것으로 간주 됩니다. <br />
    <br />
    <img src="/images/line3.gif"><br />
    <br />
    <strong><img src="/images/icon.gif" alt="" align="absmiddle" /> 제4조 (약관 외 준칙)</strong><br />
    <br />
  이 약관에 명시되지 아니한 사항은 이용안내에 관한 사항, 전기통신기본법, 전기통신사업법, 전자거래기본법, 전자서명법등에 관한 법률 및 기타 관계 법령이 규정한 바에 따릅니다.<br />
   <br />
   <img src="/images/line3.gif"><br />
    <br />
    <strong><img src="/images/icon.gif" alt="" align="absmiddle" /> 제5조 (서비스의 종류)</strong><br />
    <br />
    1. 서비스의 정의 <br />
    ① SMS(Short Message Service) 서비스: 이동전화 단문서비스의 데이터 통신기능을 활용하여 컴퓨터 등 정보처리 능력을 가지고 있는 장치와 무선단말기 사이에 80Byte 이하의 단문메시지(이하 'SMS')를 전송해 주는 서비스 및 기타 부가하여 제공하는 서비스<br />
    ② MMS(Multimedia Message Service) 서비스: 이동전화 멀티미디어 메시지서비스의 데이터 통신기능을 활용하여 컴퓨터 등 정보처리 능력을 가지고 있는 장치와 무선단말기 사이에 2,000Byte 이하의 장문메시지, 이미지, 오디오, 동영상의 멀티미디어메시지(이하 'MMS')를 전송해 주는 서비스 및 기타 부가하여 제공하는 서비스<br />
    ③ 부가서비스: SMS 및 MMS 외 '문자랑' 타 서비스와 연계한 통합서비스 또는 별도의 서비스 등을 제공할 수 있으며 이용조건 및 이용방법 등은 별도로 공지하는 바에 따릅니다.<br />
    <br />
    <img src="/images/line3.gif"><br />
    <br />
    <strong><img src="/images/icon.gif" alt="" align="absmiddle" /> 제6조 (이용계약의 성립)<br />
    </strong><br />
    1. 이용계약은 회원의 서비스 이용신청에 대하여 “회사”의 이용승낙과 회원의 약관 내용에 대한 동의로 성립됩니다.<br />
    2. 서비스의 대량이용 등 특별한 이용에 관한 계약은 별도 계약에 의하여 제공합니다.<br />
    <br />
    <img src="/images/line3.gif"><br />
    <br />
    <strong><img src="/images/icon.gif" alt="" align="absmiddle" /> 제7조 (이용신청의 승낙 및 유보)</strong><br />
    <br />
    1. “회사”는 이용신청고객에 대하여 업무 수행상 또는 기술상 지장이 없는 경우에는 접수순서에 따라 서비스 이용을 승낙합니다.<br />
    2. “회사”는 이용신청을 승낙한 때에는 다음 각호의 사항을 이용신청 고객에게 전화, 메일 등의 방법으로 통지합니다.<br />
    ① 서비스개통예정일<br />
    ② 요금 등에 관한 사항<br />
    ③ 회원의 권익보호 및 의무 등에 관한 사항<br />
    ④ 기타 “회사”가 필요하다고 인정하는 사항<br />
    3. 이용계약(회원가입) 신청 양식에 기재하는 모든 회원 정보는 실제 데이터인 것으로 간주하며 실명이나 실제 정보를 입력하지 않은 사용자는 법적인 보호를 받을 수 없으며, 서비스 중단 및 통보 없는 임의탈퇴 조치, 사안에 따라 형사고발 등을 할 수 있습니다.<br />
    4. “회사”는 아래 호에 해당하는 이용 신청에 대하여는 이를 승낙하지 아니할 수 있습니다.<br />
    ① 설비에 여유가 없는 경우<br />
    ② 기술상 지장이 있는 경우<br />
    ③ 기타 “회사”의 사정상 이용 승인이 곤란한 경우<br />
    ④ 사회의 안녕질서 또는 미풍양속의 현저한 저해, 법률 위반을 목적으로 신청한 것으로 판단될 경우<br />
    ⑤ 서비스 사용 중 약관에 정한 사항들을 위반하여 강제탈퇴 된 회원이 또다시 신청한 경우<br />
    ⑥ 이용신청 시 타인명의의 사용 및 허위내용의 기재 등 정확하지 않은 정보를 사용하여 신청한 경우<br />
    ⑦ 정보통신윤리위원회의 인터넷 서비스 불량회원으로 등록되어 있는 경우<br />
    <br />
    <img src="/images/line3.gif"><br />
    <br />
    <strong><img src="/images/icon.gif" alt="" align="absmiddle" /> 제8조 (개인정보의 수집 및 목적)</strong><br />
    <br />
    1. “회사”는 적법하고 공정한 수단에 의하여 이용계약의 성립 및 이행에 필요한 성명, 주민등록번호, 주소, 전화번호 등 최소한의 개인정보를 수집합니다.<br />
    2. “회사”는 회원이 특정항목에 해당하는 개인정보를 기입하지 않은 이유로 특정 서비스의 제공을 거부할 수 있습니다.<br />
    3. “회사”는 개인정보의 수집 시 관련법규에 따라 가입신청서 또는 이용약관에 그 수집범위 및 목적을 사전 고지하며, “회사”의 개인정보취급방침에 공개합니다.<br />
    <br />
    <img src="/images/line3.gif"><br />
    <br />
    <strong><img src="/images/icon.gif" alt="" align="absmiddle" /> 제9조 (이용 아이디 관리 등)</strong><br />
    <br />
    1. “회사”는 회원에 대하여 이 약관에 따라 이용 아이디를 부여합니다.<br />
    2. “회사”는 이용 아이디가 다음 각 호에 해당하는 경우에는 회원과 합의하여 변경할 수 있습니다. 단, 이용 아이디를 변경할 경우 기존 이용 아이디는 소멸됩니다.<br />
    ① 이용 아이디가 회원의 전화번호 또는 주민등록번호 등으로 등록되어 개인 정보 노출이 우려되는 경우 <br />
    ② 이용 아이디의 내용이 타인에게 혐오감을 주거나 미풍양속에 어긋나는 경우 <br />
    ③ 기타 “회사”가 인정하는 합리적인 사유가 있는 경우 <br />
    3. “회사”는 이용 아이디에 의하여 서비스 제공, 과금 등 제반 회원 관리 업무를 수행하며 회원은 이용 아이디를 공유, 양도 또는 변경할 수 없습니다. 단, 그 사유가 명백하고 “회사”가 인정하는 경우 이용 아이디를 변경 또는 공유할 수 있습니다.<br />
    4. 회원이 사용하는 이용 아이디 및 비밀번호에 의하여 발생하는 서비스 이용 상의 과실 또는 제3자에 의한 부정사용 등에 대한 모든 책임은 해당 회원에게 있습니다. 단, “회사”의 고의 또는 중대한 과실이 있는 경우에는 그러하지 아니합니다.<br />
   <br />
   <img src="/images/line3.gif"><br />
    <br />
    <strong><img src="/images/icon.gif" alt="" align="absmiddle" /> 제10조 (회사의 의무)</strong><br />
    <br />
    1. “회사”는 관련법령과 본 약관에 의거하여 지속적이고 안정적인 서비스를 제공하기 위해 항상 노력합니다.<br />
    2. “회사”는 서비스 제공과 관련하여 취득한 회원의 개인정보를 본인의 사전 승낙 없이 타인에게 누설, 공개 또는 배포할 수 없으며, 서비스관련 업무 이외의 상업적 목적으로 사용할 수 없습니다.<br />
    3. “회사”는 회원으로부터 제기되는 불만이 정당하다고 인정할 경우에는 최선을 다하여 처리함을 원칙으로 합니다. 다만, 즉시 처리가 곤란한 경우에는 그 사유와 처리 일정을 서비스 페이지 및 메일 등 유효한 방법으로 통보합니다. 이때 통보 확인의 책임은 회원에게 있습니다.<br />
    4. “회사”는 서비스 제공과 관련하여 취득한 회원의 개인정보를 본인의 동의 없이 타인에게 누설하거나 배포할 수 없으며 서비스 관련 업무 이외의 목적으로는 사용할 수 없습니다. 다만, 다음 각 호에 해당하는 경우에는 예외로 합니다.<br />
    ① 금융실명거래 및 비밀보장에 관한 법률, 신용정보의 이용 및 보호에 관한 법률, 전기통신 기본법, 전기통신 사업법, 소비자보호법, 형사소송법 등 관련 법령에 특별한 규정이 있는 경우 <br />
    ② 정보통신윤리위원회가 이 약관에 의하여 이용 제한을 받은 회원의 이용 아이디, 성명, 주민등록번호 및 이용제한 사유 등을 요청하는 경우 <br />
    ③ 유료서비스 제공에 따른 요금 정산을 위하여 필요한 경우 <br />
    ④ 통계 작성 또는 시장조사를 위하여 필요한 경우로서 특정 회원을 식별할 수 없는 형태로 제공하는 경우 <br />
    ⑤ 게시판 서비스 등 특정 서비스에 한하여 해당 서비스를 이용하는 회원 간 신뢰를 위하여 해당 회원의 동의 아래 신상정보를 공개할 수 있습니다.<br />
    5. “회사”는 회원이 서비스 제공목적에 맞는 서비스 이용여부를 확인하기 위하여 상시적으로 모니터링을 실시하며, 불법스팸을 전송한 사실을 확인한 경우, 한국정보보호진흥원 불법스팸대응센터에 관련 자료를 첨부하여 신고할 수 있습니다. <br />
   <br />
   <img src="/images/line3.gif"><br />
    <br />
    <strong><img src="/images/icon.gif" alt="" align="absmiddle" /> 제11조 (회원의 의무)</strong><br />
    <br />
    1. ID와 비밀번호에 관한 모든 관리의 책임은 회원에게 있습니다.<br />
    2. 회원은 주소 및 연락처 등 이용계약사항과 관련된 개인정보가 변경된 경우에는 이를 즉시 “회사”에 통보하여야 합니다. <br />
    3. 회원은 서비스를 이용함에 있어 공공질서 또는 미풍양속을 해치는 행위, 또는 다음 각 호에 해당하는 행위를 해서는 안 됩니다. <br />
    ① 다른 회원의 이용 아이디를 부정 사용하는 행위 <br />
    ② 해킹 행위 또는 컴퓨터 바이러스의 유포행위 <br />
    ③ 타인의 의사에 반하여 광고성 정보 등 유사한 내용을 지속적으로 전송하는 행위 <br />
    ④ 타인의 지적 재산권 등을 침해하는 행위 <br />
    ⑤ 범죄행위를 목적으로 하거나 범죄행위를 교사하는 행위<br />
    ⑥ 반국가적 범죄의 수행을 목적으로 하는 행위<br />
    ⑦ 선량한 풍속 또는 기타 사회질서를 해치는 행위 <br />
    ⑧ 서비스의 안전적인 운영이 지장을 주거나 줄 우려가 있는 일체의 행위 <br />
    4. 기타 관계법령에 위배되는 행위 <br />
    ① 회원은 관계법령, 이 약관에서 규정하는 사항, 서비스 이용 안내 및 서비스와 관련하여 공지사항을 게시하거나 별도로 공지한 준수 사항을 반드시 준수하여야 합니다.<br />
    ② 회원은 그 귀책사유로 인하여 “회사”나 다른 회원이 입은 손해를 배상할 책임이 있습니다.<br />
    5. 회원은 정보통신망법에 따라 SMS 또는 MMS 전송을 위한 이동통신사 가입자의 사전 수신동의를 직접 얻어야 하고, 스팸 또는 불법스팸 전송을 위해 서비스를 이용하여서는 아니 되며, “회사”를 통해 전달되는 수신거부 요청에 대해서는 즉각적으로 처리하고 24시간 이내에 처리결과를 “회사”에 회신하여야 하며 본 항의 위반으로 발생하는 모든 민/형사상의 책임은 회원이 직접 부담합니다.<br />
    6. 회원은 재화 및 용역의 거래관계를 통하여 수신자로부터 직접 연락처를 수집한 경우에는 수신자의 사전 동의 없이 “회사”가 취급하는 재화 및 용역에 대한 영리목적의 광고성 정보를 전송할 수 있습니다. 다만, 이 경우의 기존거래관계는 광고수신일자로부터 최근 6개월 이내에 거래관계가 있는 경우에만 인정합니다.<br />
    <br />
    <img src="/images/line3.gif"><br />
    <br />
    <strong><img src="/images/icon.gif" alt="" align="absmiddle" /> 제12조 (서비스의 내용)</strong><br />
    <br />
    1. “회사”가 제공하는 서비스의 내용은 다음과 같습니다.<br />
    ① 문자메시지 발송 서비스<br />
    ② 그림 컬러 문자메시지 발송 서비스 <br />
    ③ 대량 문자메시지 발송 서비스<br />
    ④ 기타 “회사”가 자체 개발하거나 다른 “회사”와의 협력 계약 등을 통해 회원에게 제공 하는 일체의 서비스 <br />
    2. “회사”는 필요한 경우 서비스의 내용을 추가 또는 변경할 수 있습니다. 이 경우 “회사”는 추가 또는 변경 내용을 제3조에서 정한 방법으로 회원에게 공지합니다. <br />
    3. “회사”는 서비스의 운용과 관련하여 서비스화면, 전자우편, 전화 응대 문구 등에 광고 등을 게재할 수 있습니다.<br />
    <br />
    <img src="/images/line3.gif"><br />
    <br />
    <strong><img src="/images/icon.gif" alt="" align="absmiddle" /> 제13조 (서비스 제공의 중지)</strong><br />
    <br />
    1. 다음 각 호에 해당하는 경우에 “회사”는 서비스 페이지에 그 사유 및 기간을 공지하고 서비스를 중지할 수 있습니다. <br />
    ① 서비스의 개선을 위한 정기점검 <br />
    ② 통신회선의 장애 등 “회사”가 서비스의 제공을 정지할 필요가 있다고 판단되는 경우 <br />
    ③ 천재지변, 국가비상사태, 정전, 서비스 설비의 장애 등으로 정상적인 서비스 이용에 지장이 있을 경우 <br />
    ④ 기타 “회사”와 회원이 협의한 경우<br />
    <br />
    <img src="/images/line3.gif"><br />
    <br />
    <strong><img src="/images/icon.gif" alt="" align="absmiddle" /> 제14조 (서비스의 이용 및 제한)</strong><br />
    <br />
    1. “회사”는 다음 각 호에 해당하는 경우에 한하여 회원의 서비스 이용을 정지시킬 수 있으며 이용정지 7일전까지 회원 또는 그 대리인에게 통지합니다. 단, “회사”가 긴급하게 이용을 정지할 필요가 있다고 인정하는 경우나, 회원의 귀책사유로 인하여 통지할 수 없는 경우에는 통지를 생략할 수 있습니다.<br />
    ① 유료서비스를 사용한 회원이 요금 등을 납입할 의무를 이행하지 아니한 경우 <br />
    ② 제11조의 규정에 의한 회원의 의무를 이행하지 아니한 경우 <br />
    ③ 회원의 이름 및 주민등록번호 등 개인정보가 정확하지 않은 경우 <br />
    ④ 다른 회원 또는 제3자의 지적재산권을 침해하거나 명예를 손상시킨 경우 <br />
    ⑤ 정보통신윤리위원회의 시정요구가 있거나 불법선거운동과 관련하여 선거관리위원회의 유권해석을 받은 경우 <br />
    ⑥ 공공질서 및 미풍양속에 저해되는 내용을 고의로 유포시킨 경우 <br />
    ⑦ 회원이 서비스를 별도의 이용 계약 없이 재판매 하거나 변형하여 제3자가 이용하도록 하는 경우<br />
    ⑧ 서비스를 이용하여 얻은 정보를 회원의 개인적인 이용 이외에 복사, 가공, 번역, 2차적 저작 등을 하여 복제, 공연, 방송, 전시, 배포, 출판 등에 사용하는 경우<br />
    ⑨ 기타 “회사”가 회원으로 부적당하다고 판단한 경우 <br />
    ⑩ 광고의 수신자가 스팸으로 신고하거나 회원이 발송하는 메시지가 불법스팸임이 판명될 경우<br />
    ⑪ “회사”의 수신거부 요청 처리에 불성실하여 수신거부 요청 건수가 감소되지 않거나 발송금지를 요청한 메시지 내용이 중복적으로 발송될 경우<br />
    ⑫ 방송통신위원회 또는 한국정보보호진흥원이 불법스팸 전송사실을 확인하여 이용정지를 요청하는 경우<br />
    2. 제1항의 규정에 의하여 이용정지의 통지를 받은 회원 또는 그 대리인은 그 이용정지의 통지에 대하여 이의신청을 할 수 있습니다.<br />
    3. “회사”는 제2항의 규정에 의하여 이의신청이 접수된 경우, 즉시 이를 확인하고 그 결과를 회원 또는 그 대리인에게 통지합니다.<br />
    4. “회사”는 이용정지 기간 중에 그 이용정지 사유가 해소된 것이 확인된 경우에는 이용정지조치를 즉시 해제합니다. <br />
    5. 제1항의 이용정지 경우 중 국익 또는 사회적 공익을 저해할 목적이나 범죄적 목적으로 서비스를 이용하고 있다고 판단되는 경우에 “회사”는 회원에게 사전통보 없이 서비스를 중단할 수 있으며 그에 따른 데이터도 복구를 전제로 하지 않고 삭제할 수 있습니다. <br />
    6. “회사”는 이용제한을 받은 회원의 이용 아이디, 성명, 주민등록번호 및 제한 사유 등을 정보통신윤리위원회의 불량회원 데이터베이스 등을 통해 다른 PC통신 및 인터넷사업자에게 열람시킬 수 있습니다. <br />
    7. “회사”의 귀책사유가 없는 서비스의 제한으로 인해 회원에게 손해가 발생한 경우, “회사”는 책임을 지지 않습니다.<br />
    <br />
    <img src="/images/line3.gif"><br />
    <br />
    <strong><img src="/images/icon.gif" alt="" align="absmiddle" /> 제15조 (각종 자료의 저장기간)</strong><br />
    <br />
    “회사”는 서비스 별로 회원이 게시판 자료나 회원의 필요에 의해 저장하고 있는 자료에 대하여 일정한 저장기간을 정할 수 있으며, 필요에 따라 그 기간을 변경할 수 있습니다.<br />
    <br />
    <img src="/images/line3.gif"><br />
    <br />
    <strong><img src="/images/icon.gif" alt="" align="absmiddle" /> 제16조 (게시물의 저작권 및 지적소유권의 보호)</strong><br />
    <br />
    1. 회원이 서비스 페이지에 게시하거나 등록한 게시물의 저작권은 저작권법에 의해 보호를 받습니다. 단, “회사”는 서비스 홈페이지의 게재 권을 가지며 “회사”의 서비스 내에 한하여 회원의 게시물을 활용할 수 있습니다.<br />
    2. 회원은 “회사”로부터 얻은 정보를 “회사”의 사전승낙 없이 가공, 판매하는 행위 등 게재된 자료를 상업적으로 이용할 수 없으며 이를 위반하여 발생하는 제반 문제에 대한 책임은 회원에게 있습니다. <br />
    3. “회사”는 회원이 게재한 자료를 홍보 등의 목적으로 “회사”의 서비스 내의 게재할 권리를 갖습니다. 또한 “회사”가 제공하는 서비스의 홍보용에 한하여 언론 등 타 매체에 일부, 혹은 전부를 제공할 수 있습니다.<br />
  <br />
  <img src="/images/line3.gif"><br />
  <br />
  <strong><img src="/images/icon.gif" alt="" align="absmiddle" /> 제17조 (요금 등의 종류)</strong><br />
  <br />
  “회사”가 제공하는 유료서비스 이용과 관련하여 회원이 납부하여야 할 요금은 '문자랑' 이용료 안내에 게재한 바에 따릅니다.<br />
  <br />
  <img src="/images/line3.gif"><br />
  <br />
  <strong><img src="/images/icon.gif" alt="" align="absmiddle" /> 제18조 (요금결제 방법 및 보안)</strong><br />
  <br />
  1. 요금 납부 시 요금 지급은 온라인 무통장 입금, 신용카드 결제, 계좌이체, 가상계좌 등으로 할 수 있습니다. <br />
  2. 요금 결제의 보안과 관련하여 발생한 문제에 대한 책임은 “회사”에게 있습니다. 단, 사용자의 귀책사유로 인한 경우에는 그러하지 아니합니다.<br />
  3. 요금 지불을 위한 포인트충전 및 결제에 관련된 모든 사항은 &quot;문자랑&quot;의 개별 방침에 따릅니다.<br />
  <br />
  <img src="/images/line3.gif"><br />
  <br />
  <strong><img src="/images/icon.gif" alt="" align="absmiddle" /> 제19조 (요금의 환불규정)</strong><br />
  <br />
  요금은 약관에 따라 환불할 수 있으며 서비스 계약을 해지할 수 있습니다. 단, 아래 경우에는 환불되지 않습니다. 또한 환불 시에는 반드시 고객센터(070-4070-5198)로 연락 주셔야 환불이 가능합니다.</p>
<p>1. 이용 고객의 귀책사유로 탈퇴가 된 경우<br />
  2. 폰피 서비스 URL SMS 수신에 따른 WAP 접속 시 발생되는 각 이동통신사 별 데이터요금에 대해서 “회사”는 일체의 책임을 지지 않습니다. <br />
  3. 그림컬러문자 서비스 MMS 발송 시 수신자의 단말이 MMS를 지원하지 않는 단말일 경우 발생 된 전송 요금에 대해 “회사”는 일체의 책임을 지지 않습니다.<br />
  4. 제 11조의 회원의 의무에 따라 회원 자격을 상실한 경우</p>
<p>환불절차는 다음과 같습니다.<br />
  위 사항에 의한 이유로 환불을 원하는 사용자는 사용금액 결제 후 15일 이내에 “회사”가 정한 절차를 통해 환불을 신청해야 하며, “회사”는 환불 신청이 정당함을 심사한 후, 정당한 이유가 있음으로 판명된 사용자에게 환불합니다. 단, 금액 사용 후 환불 요청을 하실 경우에는 사용금액만큼 선 금액 결제를 해주셔야만 환불처리가 가능합니다. 결제 후 15일이 지난 후에는 환불신청을 하여도 환불을 받으실 수 없습니다.</p>
<p>또한, 이용하고 남은 잔액에서 재정경제부에서 고시한 &lt;인터넷이용관련소비자피해보상&gt;상 규정한 위약금(총 이용료의 10%) 및 PG수수료, 송금비용을 공제한 이용료 잔액을 환불하여 드립니다. 단, 위약금 및 PG수수료, 송금비용의 합계는 최소 1,000원입니다. 따라서 1,000원 미만의 금액은 해지 시 자동 소멸되며 환불되지 않습니다. (결제대행사 10% , 위약금 10% : 총 수수료 20%)</p>
<p>전용계좌와 무통장입금 건은 은행수수료를 제외한 금액을 환불해 드립니다.<br />
  <br />
  <img src="/images/line3.gif"><br />
  <br />
  <strong><img src="/images/icon.gif" alt="" align="absmiddle" /> 제20조 (추가충전금액)</strong><br />
  <br />
  1. “회사”는 회원에게 추가충전금액을 부여할 수 있습니다.<br />
  2. 추가충전금액은 환불이 되지 않습니다.<br />
  3. “회사”는 서비스의 효율적 이용 및 운영을 위해 사전 공지 후 추가충전금액의 일부 또는 전부를 조정, 소멸할 수 있습니다.<br />
  <br />
  <img src="/images/line3.gif"><br />
  <br />
  <strong><img src="/images/icon.gif" alt="" align="absmiddle" /> 제21조 (요금 등의 이의신청)</strong><br />
  <br />
  1. 회원은 청구된 요금 등에 대하여 이의가 있는 경우 청구일로부터 3개월 이내에 이의 신청을 할 수 있습니다. <br />
  2. “회사”는 제1항의 이의 신청 접수 후 2주 이내에 해당 이의신청의 타당성 여부를 조사하여 그 결과를 회원 또는 그 대리인에게 통지합니다. <br />
  3. 부득이한 사유로 인하여 제2항에서 정한 기간 내에 이의신청결과를 통지할 수 없는 경우에는 그 사유와 재 지정된 처리기한을 명시하여 이를 회원 또는 그 대리인에게 통지합니다.<br />
  <br />
  <img src="/images/line3.gif"><br />
  <br />
  <strong><img src="/images/icon.gif" alt="" align="absmiddle" /> 제22조 (요금 등의 반환)</strong><br />
  <br />
  1. “회사”는 요금 등의 과납 또는 오납이 있을 때에는 그 과납 또는 오납된 요금을 반환하고, “회사”의 귀책사유로 발생한 경우에는 “회사”가 정한 규칙에 의거하여 과실에 대한 환불조치를 합니다. (단 1천원 미만 금액은 제외)<br />
  2. 회원은 “회사”의 귀책사유로 회원에게 24시간 이상 연속적으로 서비스를 제공하지 못하였거나 중단한 경우, 혹은 요금 충전을 시도하였으나 충전금액이 정상적으로 충전되지 못하여 환불을 요청한 경우 충전 후 2주 이내에 환불을 신청해야 하며 충전 후 2주가 지난 후에는 환불 신청을 하여도 환불을 받을 수 없습니다. <br />
  3. 제 2항의 경우 “회사”는 회원이 반환을 요청한 시점으로부터 익월 말일까지 회원이 지정한 은행계좌로 반환요청 요금을 반환합니다. 단, 요금반환은 반환요청 금액이 최소 1천원 이상인 경우에만 가능하며 요금 반환 시 소정의 수수료를 차감할 수 있습니다. <br />
  <br />
  <img src="/images/line3.gif"><br />
  <br />
  <strong><img src="/images/icon.gif" alt="" align="absmiddle" /> 제23조 (계약사항의 변경)</strong><br />
  <br />
  회원이 주소 등 이용계약과 관련된 사항을 변경할 경우에는 서비스 페이지를 통하여 변경하거나 전화, 전자우편 등을 통하여 변경을 요청할 수 있습니다. “회사”는 회원의 변경 요청이 있을 경우 즉시 이를 접수하여 처리합니다. <br />
  <br />
  <img src="/images/line3.gif"><br />
  <br />
  <strong><img src="/images/icon.gif" alt="" align="absmiddle" /> 제24조 (양도 등의 금지)</strong><br />
  <br />
  회원의 서비스를 제공받는 권리는 제 28조의 규정에 의하여 승계하는 경우를 제외하고는 이를 양도하거나 증여 등을 할 수 없으며 또한 질권의 목적으로도 사용할 수 없습니다.<br />
  <br />
  <img src="/images/line3.gif"><br />
  <br />
  <strong><img src="/images/icon.gif" alt="" align="absmiddle" /> 제25조 (회원의 지위승계)</strong><br />
  <br />
  상속, 합병, 분할, 영업양수 등으로 회원의 지위승계 사유가 발생한 때에는 그 사유가 발생한 날로부터 3개월 이내에 사업자등록증사본(법인에 한합니다)과 지위승계를 입증할 수 있는 관련서류를 첨부하여 “회사”에 신고하여야 합니다. <br />
  <br />
  <img src="/images/line3.gif"><br />
  <br />
  <strong><img src="/images/icon.gif" alt="" align="absmiddle" /> 제26조 (계약해지 및 이용제한)</strong><br />
  <br />
  1. 회원이 이용계약을 해지하고자 할 때에는 회원 본인이 온라인을 통해 “회사”에 해지 신청을 해야 합니다. 또한 회원 본인이 “회사”의 공식 유선전화로 유선상 해지 요청을 해야 합니다. <br />
  2. 회원이 “회사”의 핸드폰 문자메시지망(SMS망) 등의 유무선 통신망을 이용하여 스팸(SPAM)성 메시지를 전송하여 제3자에게 정신적 물질적 피해를 입히는 경우, 회원은 그 행위에 대한 모든 법적 책임을 감수합니다.<br />
  3. 사전 고지 없이 또는 불가항력을 제외한 “회사”의 귀책사유로 인하여 24시간 이상 서비스가 중지되거나 장애가 발생한 경우 또는 1개월 동안의 서비스 중지/장애발생 누적시간이 72시간을 초과한 경우, 회원은 서비스 이용에 대한 계약을 해지할 수 있습니다.<br />
  4. 주민등록법 위반 또는 중대한 불법행위를 한 것으로 판단되는 회원의 계정이용을 제한하거나 해당 회원과의 계약을 해지하는 경우, “회사”는 사이버머니를 환불하지 않을 수 있습니다. 이 경우 “회사”는 회원의 e-mail 및/또는 핸드폰을 통하여 이러한 사실 및 이의 제기 방법에 대하여 안내를 할 것이며, 개별적으로 고지된 소정의 절차에 따라 회원이 자신이 회원이거나 중대한 불법행위를 하지 않았다는 것을 소명하는 경우에는 그러하지 않습니다.<br />
  5. 환불 신청 시 환불 요청서 및 관련서류를 수령한 날부터 익월 말일까지 회원이 지정한 은행계좌로 환불요청 요금이 환불됩니다. 단, 회원의 사이버머니 결제방법에 따라 (핸드폰 결제, 무통장입금 결제, 신용카드 결제) 해당 결제 납부영수증(휴대폰요금납부영수증, 입금증, 신용카드요금납부영수증)과 환불요청서를 각각 “회사” 대표 팩스번호로 보내주셔야 환불이 가능하며 미납 상태에서는 환불이 불가능합니다.<br />
  6. 회원이 사이트상에서 직접 이용 계약 해지 시 사이버머니도 같이 자동 소멸됩니다.<br />
  7. “회사”는 회원이 다음 사항에 해당하는 행위를 하였을 경우, 사전 통지 없이 이용 계약을 해지하거나 또는 기간을 정하여 서비스 이용을 중지할 수 있습니다.</p>
<p>① 공공 질서 및 미풍 양속에 반하는 경우<br />
  ② 범죄적 행위에 관련되는 경우<br />
  ③ 국익 또는 사회적 공익을 저해할 목적으로 서비스 이용을 계획 또는 실행할 경우<br />
  ④ 타인의 ID 및 비밀번호를 도용한 경우<br />
  ⑤ 타인의 명예를 손상시키거나 불이익을 주는 경우<br />
  ⑥ 같은 사용자가 다른 ID로 이중 등록을 한 경우<br />
  ⑦ 서비스에 위해를 가하는 등 건전한 이용을 저해하는 경우<br />
  ⑧ 기타 관련 법령이나 “회사”가 정한 이용조건에 위배되는 경우<br />
  ⑨ 불법스팸(성인, 도박, 대출 등) 문자메시지를 전송하는 경우</p>
<p>8. 회원은 광고 수신동의를 얻은 자에게만 광고 메시지를 전송할 수 있으며, 비록 동의를 얻은 후라도 오후 9시부터 다음날 오전 8시까지는 광고 메시지 전송을 할 수 없습니다.<br />
  9. 불법스팸 메시지를 전송하여 정보보호진흥원 또는 이동통신사로부터 서비스 이용 신고가 접수되면 선 서비스 이용 중지 후 통보할 수 있으며 불법스팸 메시지로 인정되는 경우 잔여 사이버머니가 소멸됩니다.<br />
  <br />
  <img src="/images/line3.gif"><br />
  <br />
  <strong><img src="/images/icon.gif" alt="" align="absmiddle" /> 제27조 (이용제한의 해제)</strong><br />
  <br />
  서비스 이용제한을 통지 받은 회원이 “회사”에 이용제한 해제를 요청할 경우, “회사”는 이용제한 기간 중에 그 이용제한 사유가 해소된 것이 확인된 경우에 한하여 이용제한 조치를 즉시 해제할 수 있습니다.
  <br />
  <br />
  <img src="/images/line3.gif"><br />
  <br />
  <strong><img src="/images/icon.gif" alt="" align="absmiddle" /> 제28조 (손해배상의 범위 및 청구)</strong><br />
  <br />
  1. “회사”는 “회사”의 귀책사유가 없는 한, 서비스 이용과 관련하여 회원에게 발생한 어떠한 손해에 대하여도 책임을 지지 않습니다.<br />
  2. “회사”는 “회사”의 귀책사유로 회원이 유료 서비스를 이용하지 못하는 경우에 손해배상으로 지불되는 금액의 총액은 어떠한 경우에도 회원이 지불한 이용요금의 2배를 초과할 수 없습니다. 다만, 회원이 서비스 이용불가 사실을 “회사”에 접수한 이후 24시간 이내 서비스가 정상화 된 경우는 제외합니다.<br />
  3. “회사”가 제공하는 서비스 중 무료서비스의 경우에는 손해배상에 해당되지 않습니다. <br />
  4. “회사”는 그 손해가 천재지변 등 불가항력이거나 회원의 고의 또는 과실로 인하여 발생된 때에는 손해배상을 하지 않습니다. <br />
  5. 손해배상의 청구는 “회사”에 청구사유, 청구금액 및 산출근거를 기재하여 서면으로 신청하여야 하며 그 사유가 발생한 날로부터 3개월 이내 신청한 경우에만 유효합니다. <br />
  6. 회원이 서비스를 이용함에 있어 행한 불법행위나 본 약관 위반행위로 인하여 “회사”가 당해 회원 이외의 제 3자로부터 손해배상 청구 또는 소송을 비롯한 각종 이의제기를 받는 경우 당해 회원은 자신의 책임과 비용으로 “회사”를 면책시켜야 하며, “회사”가 면책되지 못한 경우 당해 회원은 그로 인하여 “회사”에 발생한 모든 손해를 배상하여야 합니다. <br />
  7. 손해배상 청구권은 청구사유가 발생한 날로부터 만 3개월 이후 소멸됩니다. 다만, 그 이전에 회원의 손해배상 청구가 접수된 경우는 제외합니다. <br />
  <br />
  <br />
  <img src="/images/line3.gif" /><br />
   <br />
  <strong><img src="/images/icon.gif" alt="" align="absmiddle" /> 제29조 (면책)</strong><br />
  <br />
  1. “회사”는 천재지변 또는 이에 준하는 불가항력으로 서비스를 제공할 수 없는 경우와 서비스의 효율적인 제공을 위한 시스템 개선, 장비 증설 등 계획된 서비스 중지 일정을 고객에게 사전 공지한 경우에는 책임을 면합니다. <br />
  2. “회사”는 회원이 서비스를 통해 얻은 정보 또는 자료 등으로 인해 발생한 손해와 서비스를 이용하거나 이용할 것으로부터 발생하거나 기대하는 손익 등에 대하여 책임을 면합니다. <br />
  3. “회사”는 회원의 귀책사유로 인하여 발생한 서비스 이용의 장애에 대하여는 책임을 면합니다. <br />
  4. “회사”는 회원이 게시 또는 전송한 자료의 내용에 대하여는 책임을 면합니다.<br />
  5. “회사”는 회원 상호간 또는 회원과 제3자 상호간에 서비스를 매개로 하여 물품거래 등을 한 경우에는 책임을 면합니다. <br />
  6. “회사”는 무료로 제공하는 서비스에 대하여 “회사”의 귀책사유로 회원에게 서비스를 제공하지 못하는 경우 책임을 면합니다.<br />
  7. 이 약관의 적용은 이용계약을 체결한 회원에 한하며, 제3자로부터의 어떠한 배상, 소송 등에 대하여 “회사”는 책임을 면합니다.<br />
  8. 정보통신망이용촉진 및 정보보호 등에 관한 법률에서 규정한 사항을 위반하여 발생한 손해에 대한 책임은 “회사”에서 책임을 지지 않습니다. 또한 “회사”가 물질적 정신적 손해를 입을 경우, 회원은 “회사”가 입은 손해에 대한 배상 책임 의무가 있습니다.<br />
  <br />
  <br />
  <img src="/images/line3.gif" /><br /> 
  <br />
  <strong><img src="/images/icon.gif" alt="" align="absmiddle" /> 제30조 (관할법원)</strong><br />
  <br />
  1. 서비스 이용과 관련하여 “회사”와 회원 사이에 분쟁이 발생한 경우, 쌍방간에 분쟁의 해결을 위해 성실히 협의한 후가 아니면 제소할 수 없습니다.<br />
  2. 서비스 이용으로 발생한 분쟁에 대해 소송이 제기될 경우 “회사”의 주사무소 소재지를 관할하는 법원을 전속 관할법원으로 합니다. <br />
  <br />
  <br />
  <strong>[부칙] </strong><br />
이 약관은 2011년 4월1일부터 시행합니다.</p>
</td>
    </tr>
    </table></td>
  </tr>
</table>

</div>