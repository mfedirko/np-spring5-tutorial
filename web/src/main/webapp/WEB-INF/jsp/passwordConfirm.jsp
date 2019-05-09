<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="includes/header.jsp"%>

<div class="panel panel-primary">
  <div class="panel-heading">
    <h3 class="panel-title">Confirm Password</h3>
  </div>
  <div class="panel-body">
	<form:form modelAttribute="${(empty modelAttrNm) ? 'confirmPass' : modelAttrNm}" action="${contextPath}${actionUrl}">

	  <form:errors cssClass="error"/>

	  <div class="form-group">
	    <form:label path="password">Password</form:label>
	    <form:input type="password" path="password" class="form-control"  placeholder="Password" />
	    <form:errors path="password" cssClass="error" />
	  </div>

	  <button type="submit" class="btn btn-primary">Submit</button>
	</form:form>
  </div>
</div>

<%@include file="includes/footer.jsp"%>