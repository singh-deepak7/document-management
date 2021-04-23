<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="resources/css/main.css">
<title>Search</title>
</head>
<body>

	<noscript>
		<h2>Sorry! Your browser doesn't support Javascript</h2>
	</noscript>
	<div class="upload-container">
		<div class="upload-header">
			<h2>Search...</h2>
		</div>
		<div class="upload-content">
			<div class="single-upload">
				<!-- Post to the search controller -->
				<form action="<c:url value="/search_user" />" method="post"
					style="margin: 10px;">
					<label for="search_input">Search:</label> <input name="search"
						id="search" />
					<button type="submit" class="primary submit-btn">Search</button>

					<c:if test="${searchresult != null}">

						<div>
							<c:forEach var="listValue" items="${searchresult}">
								<p class="p-t-10">Total Result : ${listValue.totalCount}</p>
								<c:forEach var="userListValue" items="${listValue.users}">
									<p class="p-t-10">Name: ${userListValue.name.replace("[", "").replace("]", "").replace("\"", "")} | City:  ${userListValue.city.replace("[", "").replace("]", "").replace("\"", "")}
										| Department: ${userListValue.dept.replace("[", "").replace("]", "").replace("\"", "")}
									</p>
								</c:forEach>
							</c:forEach>
						</div>

					</c:if>
				</form>
			</div>
		</div>
	</div>

</body>
</html>