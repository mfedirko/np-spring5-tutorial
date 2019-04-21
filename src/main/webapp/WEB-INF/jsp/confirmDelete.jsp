<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="includes/header.jsp"%>

<div class="panel panel-primary">
  <div class="panel-heading">
    <h3 class="panel-title">Delete User</h3>
  </div>
  <div class="panel-body">

	<form:form modelAttribute="deleteForm" action="${contextPath}/delete-account?step=confirm" >

	  <form:errors cssClass="error"/>


	  <div class="form-group">
	    <form:label path="confirmDelete">Please confirm user deletion</form:label>
	    <form:checkbox path="confirmDelete"  class="form-control"  />
	    <form:errors path="confirmDelete" cssClass="error" />
	  </div>
	  <button type="submit" class="btn btn-primary">Submit</button>
	</form:form>

  </div>
</div>

<%@include file="includes/footer.jsp"%>