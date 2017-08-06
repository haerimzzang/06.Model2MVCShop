
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<c:if test="${resultPage.beginUnitPage > resultPage.pageUnit}">
<%-- 	<a
		href="/listUser.do?currentPage=${ resultPage.beginUnitPage-1 }&searchCondition=${ search.searchCondition}&searchKeyword=${ search.searchKeyword}&menu=search">
		</a> --%>
		
		<a href="javascript:fncGetUserList('${ resultPage.beginUnitPage-1 }');">¢¸ pre</a>

</c:if>

<c:forEach var="i" begin="${resultPage.beginUnitPage}"
	end="${resultPage.endUnitPage}">
	<%-- <a href="/listUser.do?currentPage=${ i }&searchCondition=${ search.searchCondition}&menu=search">${ i }</a> --%>

	<a href="javascript:fncGetUserList('${ i }');">${ i }</a>
</c:forEach>
<c:if test="${resultPage.endUnitPage < resultPage.maxPage }">
	<%-- <a href="/listUser.do?currentPage=${ resultPage.endUnitPage+1}&searchCondition=${ search.searchCondition}&searchKeyword=${ search.searchKeyword}&menu=search">  next ¢º</a> --%>
	<a href="javascript:fncGetUserList('${resultPage.endUnitPage+1}')">
		next ¢º</a>
</c:if>

