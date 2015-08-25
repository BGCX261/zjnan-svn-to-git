function jumpPage(pageNo){
		$("#pageNo").val(pageNo);
		$("#mainForm").submit();
}
	
function sort(orderBy,defaultOrder){
	if($("#orderBy").val()==orderBy){
			if($("#order").val()==""){
			$("#order").val(defaultOrder);}
			else if($("#order").val()=="desc"){
			$("#order").val("asc");}
			else if($("#order").val()=="asc"){
			$("#order").val("desc");}
	} else {
			$("#orderBy").val(orderBy);
			$("#order").val(defaultOrder);
	}

	$("#mainForm").submit();
}
	
 function edit(id, act, _this){
     form = $(_this).parents('form');
     form = form[0];
     idEle = document.createElement('input');
	     idEle.setAttribute("name", "id");
	     idEle.setAttribute("type", "hidden");
	     idEle.setAttribute("value", id);
     form.appendChild(idEle);
     
     with (form) {
         action = act;
         submit();
     }
}

function remove(_this){
     $.ajax({
        url: $(_this).attr("href"),
        type:'GET',
        timeout:1000*5,
        error:    function(){alert('Sorry! Server doesn\'t work well or request time out!'); },
        success: function(data){
            $(_this).parents('tr').hide();
            msg(msgWrap(data));
        }
     });
}