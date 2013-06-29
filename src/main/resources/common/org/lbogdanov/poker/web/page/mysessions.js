var Poker = (function() {

    $(function() {
        var confirmDlg = $("#confirmDlg");
        $("#delete", confirmDlg).click(function() {
            $("#" + confirmDlg.data("item")).data("allow", true).click();
            confirmDlg.modal("hide");
        });
    });

    return {
        confirm: function(item) {
            if (!$("#" + item).data("allow")) {
                $("#confirmDlg").data("item", item).modal("show");
                return false;
            }
            return true;
        }
    };
})();
