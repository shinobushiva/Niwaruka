<%@page pageEncoding="UTF-8" isELIgnored="false" session="false"%>

<script src="/js/jquery.i18n.properties.js" type="text/javascript"></script>
<script type="text/javascript">
	jQuery.i18n.properties({
	  name: 'messages', 
	  path:'/i18n/', 
	  language:'<%=request.getLocale()%>',
	  callback:function(){}
	});
</script>