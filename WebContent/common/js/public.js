//初始化左边菜单
function initLeftMenu() {
	//点击左边一级菜单
	$(".left-nav>ul>li").on("click", function(e) {
		//判断是否当前菜单
		if ($(this).hasClass("active")) {
			//关闭菜单
			$(this).removeClass("active");
		} else {
			//展开菜单
			$(".left-nav>ul>li").removeClass("active");
			$(this).addClass("active");
		}
	});
	//点击左边二级菜单
	$(".left-nav .submenu>li").on("click", function(e) {
		$(".left-nav .submenu>li").removeClass("active");
		$(this).addClass("active");
		e.stopImmediatePropagation();
		e.stopPropagation();
	});
}

function stopEvent(e) {
	e.stopImmediatePropagation();
	e.stopPropagation();
	e.preventDefault();
}

function initCheckbox() {
	//点击"全选"复选框
	$("#check-all").on("click", function(e) {
		$('input[type=checkbox][name=check-row]').prop('checked', $(this).prop('checked'));
		updateCheckedIds();
	});
	$("input[type=checkbox][name=check-row]").on("click", function(e) {
		updateCheckedIds();
	});
};
/**
 * 更新已被选中的行ID
 */
function updateCheckedIds() {
	var ids = [];
	var id;
	$('input[type=checkbox][name=check-row]:checked').each(function(index, element) {
		id = $(this).closest("tr").attr("data-id");
		ids.push(id);
	});
	$("#checkedIds").val(ids.join(","));
};
/**
 * 初始化下拉列表
 */
function initDropDownMenu() {
	var selector = "input[data-toggle=dropdown],button[data-toggle=dropdown],a[data-toggle=dropdown],div[data-toggle=dropdown],li[data-toggle=dropdown]";
	$(selector).on("click", function(e) {
		stopEvent(e);
		var dropDownMenu = $(this).siblings(".dropdown-menu").toggleClass("hide");
		var dataTarget = $(this).attr("data-target");
		var type = "value"; //设置值的方式
		if (!dataTarget) {
			dataTarget = this;
		}
		var tagName = $(dataTarget)[0].tagName.toLowerCase();
		if (tagName != "input") {
			type = "html";
		}
		//获取初始值
		var initValue;
		if (type == "value") {
			initValue = $(this).val();
		} else {
			initValue = $(this).html();
		}


		//设置选中值
		dropDownMenu.find("li").removeClass("active").find("a[data-value='" + initValue + "']").closest("li").addClass("active");
		if (dropDownMenu.attr("data-init") == undefined) {
			dropDownMenu.attr("data-init", "true");
			dropDownMenu.find("a").on("click", function(event) {
				stopEvent(event);
				dropDownMenu.toggleClass("hide").find("li").removeClass("active");
				//				$(this).closest("li").addClass("active");
				var value = $(this).attr("data-value");
				if (type == "value") {
					$(dataTarget).val(value);
				} else {
					$(dataTarget).html(value);
				}
			});
		}
	});
}