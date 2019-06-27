var html;
var _ticket;
var TT = TAOTAO = {
    checkLogin: function () {
        _ticket = $.cookie("TT_TOKEN");
        if (!_ticket) {
            return;
        }
        $.ajax({
            url: "http://localhost:8084/user/token/" + _ticket,
            dataType: "jsonp",
            type: "GET",
            success: function (data) {
                if (data.status == 200) {
                    var username = data.data.username;
                    html = username + "，欢迎来到淘淘！<a href=\"javascript:void(0);\" class=\"link-logout\" onclick='logout()'>[退出]</a>";
                    $("#loginbar").html(html);
                    
                }
            }
        });
    }
}

function logout(){
    $.ajax({
        url: "http://localhost:8084/user/logout/" + _ticket,
        dataType: "jsonp",
        type: "GET",
        success: function (data) {
                window.location.reload();
        }
    })
};

$(function () {
    // 查看是否已经登录，如果已经登录查询登录信息
    TT.checkLogin();

});