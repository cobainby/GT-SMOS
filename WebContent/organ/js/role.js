$(function(){
	//获得所有角色
	$("#roleDG").datagrid('loading');
	$.get("getRoles",function(data,status){
		var jsonData=JSON.parse(data);
		$("#roleDG").datagrid('loaded');
		$("#roleDG").datagrid("loadData",jsonData);
		$("#roleDG").datagrid("selectRow",0);
	});
	$("#addRoleBtn").bind("click",function(){
		//增加角色
		$("#addRoleDialog").dialog('open');
		$('#addRoleForm').form('clear');
	});
	$("#cancelSaveRoleBtn").bind("click",function(){
		//增加角色取消
		$("#addRoleDialog").dialog('close');
		$('#addRoleForm').form('clear');
	});
	//增加角色保存按钮点击
	$("#saveRoleBtn").bind('click',function(){
		//先验证表单，如果表单验证不通过则不提交
		var val=$("#addRoleForm").form('validate');
		if(!val){
			return;
		}
		var params={
			roleName:$("#roleName").val()
		};
		$.post("/smosplat/addRole",params,function(data,status){
			var dataObj=JSON.parse(data);
			if(dataObj.result==0){
				alert("增加成功！");
				$.get("getRoles",function(data,status){
					var jsonData=JSON.parse(data);
					$("#roleDG").datagrid('loaded');
					$("#roleDG").datagrid("loadData",jsonData);
					$("#roleDG").datagrid("selectRow",0);
				});
				$("#addRoleDialog").dialog('close');
			}else{
				alert(dataObj.msg);
			}
		});
	});
	//删除角色
	$("#deleteRoleBtn").bind("click",function(){
		var selectedData=$("#roleDG").datagrid('getSelected');
		if(selectedData==null){
			alert("没有选中记录！");
			return;
		}
		//超级管理员机构不能修改
		if(selectedData.roleName=="超级管理员"){
			alert("超级管理员机构不允许删除！");
			return;
		}
		//弹出提示框确认
		$.messager.confirm('确认', '您确定要删除吗?', function(r){
			if (r){
				$.post("/smosplat/deleteRole",{roleUuid:selectedData.roleUuid},function(data,status){
					if(JSON.parse(data).result==0){
						alert("删除成功！");
						//重新加载数据
						$.get("getRoles",function(data,status){
							var jsonData=JSON.parse(data);
							$("#roleDG").datagrid('loaded');
							$("#roleDG").datagrid("loadData",jsonData);
							$("#roleDG").datagrid("selectRow",0);
						});
					}else{
						alert(JSON.parse(data).msg);
					}
				});
			}
		});
	});
	//获取所有权限
	$("#roleItemDG").datagrid('loading');
	$.get("getRoleItems",function(data,status){
		var jsonData=JSON.parse(data);
		//根据序号排序
		jsonData.rows.sort(function(a,b){
			if(a.number<=b.number){
				return -1;
			}else{
				return 1;
			}
		});
		$("#roleItemDG").datagrid('loaded');
		$("#roleItemDG").datagrid("loadData",jsonData);
	});
	//增加权限
	$("#addRoleItemBtn").bind("click",function(){
		$("#addRoleItemDialog").dialog('open');
		$('#addRoleItemForm').form('clear');
	});
	//增加权限取消
	$("#cancelSaveRoleItemBtn").bind("click",function(){
		$("#addRoleItemDialog").dialog('close');
		$('#addRoleItemForm').form('clear');
	});
	//增加权限保存按钮点击
	$("#saveRoleItemBtn").bind('click',function(){
		//先验证表单，如果表单验证不通过则不提交
		var val=$("#addRoleItemForm").form('validate');
		if(!val){
			return;
		}
		var params={
				roleItemName:$("#roleItemName").val(),
				number:$("#number").val(),
				roleItemDesc:$("#roleItemDesc").val(),
		};
		$.post("/smosplat/addRoleItem",params,function(data,status){
			var dataObj=JSON.parse(data);
			if(dataObj.result==0){
				alert("增加成功！");
				$.get("getRoleItems",function(data,status){
					var jsonData=JSON.parse(data);
					jsonData.rows.sort(function(a,b){
						if(a.number<=b.number){
							return -1;
						}else{
							return 1;
						}
					});
					$("#roleItemDG").datagrid('loaded');
					$("#roleItemDG").datagrid("loadData",jsonData);
					//清除选中的角色
					$("#roleDG").datagrid("clearSelections");
				});
				$("#addRoleItemDialog").dialog('close');
			}else{
				alert(dataObj.msg);
			}
		});
	});
	//删除权限
	$("#deleteRoleItemBtn").bind("click",function(){
		var selectedData=$("#roleItemDG").datagrid('getSelected');
		if(selectedData==null){
			alert("没有选中记录！");
			return;
		}
		//弹出提示框确认
		$.messager.confirm('确认', '您确定要删除吗?', function(r){
			if (r){
				$.post("/smosplat/deleteRoleItem",{roleItemUuid:selectedData.roleItemUuid},function(data,status){
					if(JSON.parse(data).result==0){
						alert("删除成功！");
						//重新加载数据
						$.get("getRoleItems",function(data,status){
							var jsonData=JSON.parse(data);
							jsonData.rows.sort(function(a,b){
								if(a.number<=b.number){
									return -1;
								}else{
									return 1;
								}
							});
							$("#roleItemDG").datagrid('loaded');
							$("#roleItemDG").datagrid("loadData",jsonData);
							//清除选中的角色
							$("#roleDG").datagrid("clearSelections");
						});
					}else{
						alert(JSON.parse(data).msg);
					}
				});
			}
		});
	});
	//点击一个角色时获取该角色拥有的权限集合
	$("#roleDG").datagrid({
		onSelect:function(rowIndex, rowData){
			if(rowData.roleName=="超级管理员"){
				//超级管理员所有的权限都勾选上
				var roleItemData=$("#roleItemDG").datagrid('getData').rows;
				for (var i = 0; i < roleItemData.length; i++) {
					roleItemData[i]["useable"]=true;
					$("#roleItemDG").datagrid("refreshRow",i);
				}
				return;
			}
			var roleUuid=rowData.roleUuid;
			$.post("getRoleItemsByRole",{roleUuid:roleUuid},function(data,status){
				var roleItemForRole=JSON.parse(data).roleItems;
				var roleItemData=$("#roleItemDG").datagrid('getData').rows;
				for (var i = 0; i < roleItemData.length; i++) {
					var finded=false;
					for (var j = 0; j < roleItemForRole.length; j++) {
						if(roleItemForRole[j].roleItemUuid==roleItemData[i].roleItemUuid){
							finded=true;
							break;
						}
					}
					if(finded){
						//有权限，选中复选框
						roleItemData[i]["useable"]=true;
					}else{
						//没有权限，取消掉勾选
						roleItemData[i]["useable"]=false;
					}
					//更新一行
					$("#roleItemDG").datagrid("refreshRow",i);
				}
			});
		}
	});
	//保存角色对应的权限
	$("#saveRoleRoleItemBtn").bind("click",function(data,status){
		var selectedData=$("#roleDG").datagrid('getSelected');
		if(selectedData==null){
			alert("没有选中角色！");
			return;
		}
		if(selectedData.roleName=="超级管理员"){
			alert("超级管理员拥有所有权限！不允许设置！");
			return;
		}
		var roleUuid=selectedData.roleUuid;
		var roleItemUuidsArray=new Array();
		//得到所有权限的id
		var roleItemData=$("#roleItemDG").datagrid('getData').rows;
		for (var i = 0; i < roleItemData.length; i++) {
			if(roleItemData[i]["useable"]==true){
				roleItemUuidsArray.push(roleItemData[i].roleItemUuid);
			}
		}
		var roleItemUuids=roleItemUuidsArray.join(",");
		//更新角色对应的权限
		$.post("/smosplat/updateRoleItemForRole",{roleUuid:roleUuid,roleItemUuids:roleItemUuids},function(data,status){
			var dataObj=JSON.parse(data);
			if(dataObj.result==0){
				alert("保存成功！");
			}else{
				alert(dataObj.msg);
			}
		});
	});
});
function changeRoleItem(index,checkboxObj){
	var roleItemData=$("#roleItemDG").datagrid('getData').rows;
	if(checkboxObj.checked){
		roleItemData[index]["useable"]=true;
	}else{
		roleItemData[index]["useable"]=false;
	}
}
function formatUseable(value,row,index){
	if(value!=null&&value==true){
		return '<input type="checkbox" checked="checked" onclick="changeRoleItem('+index+',this)">';
	}else if(value==null||value==false){
		return '<input type="checkbox" onclick="changeRoleItem('+index+',this)">';
	}
}