var Poker = (function() {
    var initEditor = function() {
        var estimatesInput = $("#estimates"),
            estimatesEditor = $("#estimatesEditor"),
            customEstimates = $("#customEstimates");

        estimatesEditor.on("show", function() {
            customEstimates.val(estimatesInput.val());
        });

        $("a", estimatesEditor).click(function() {
            customEstimates.val($(this).text());
        });

        $("#estimatesSubmit").click(function() {
            estimatesInput.val(customEstimates.val());
            estimatesEditor.modal("hide");
        });
    };

    $(function() {
        initEditor();
    });

    return {
        initEditor: initEditor
    };
})();
