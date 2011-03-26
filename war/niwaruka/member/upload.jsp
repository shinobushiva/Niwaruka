<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="f" uri="http://www.slim3.org/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>twitter Index</title>
<link rel="stylesheet" type="text/css" href="/css/global.css" />
</head>
<body>

<form action="${f:url('upload')}" method="post"
	enctype="multipart/form-data"><input type="file" name="formFile" /><br />
<input type="submit" value="Upload" /></form>
<table>
	<c:if test="${fn:length(dataList) > 0}">
		<thead>
			<tr>
				<th>FileName</th>
				<th>Bytes</th>
			</tr>
		</thead>
	</c:if>
	<tbody>
		<c:forEach var="e" items="${dataList}">
			<tr>
				<td>${f:h(e.fileName)}</td>
				<td>${f:h(e.length)}</td>
				<c:set var="downloadUrl"
					value="download?key=${f:h(e.key)}&version=${e.version}" />
				<c:set var="showUrl"
					value="show?key=${f:h(e.key)}&version=${e.version}" />
				<c:set var="deleteUrl"
					value="delete?key=${f:h(e.key)}&version=${e.version}" />
				<td><a href="${f:url(downloadUrl)}">Download</a></td>
				<td><img src="${f:url(downloadUrl)}">Show</img></td>
				<td><a href="${f:url(deleteUrl)}"
					onclick="return confirm('delete OK?')">Delete</a></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
</body>
</html>