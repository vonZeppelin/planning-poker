$(function() {
    var estimatesInput = $("#estimates"),
        customEstimates = $("#customEstimates"),
        estimatesEditor = $("#estimatesEditor");

    estimatesEditor.on("show", function() {
        customEstimates.val(estimatesInput.val());
    });

    $("#standard").click(function() {
        customEstimates.val("0m 30m 1h 2h 3h 5h 8h 13h 20h 40h 100h");
    });

    $("#fib").click(function() {
        customEstimates.val("1h 2h 3h 5h 1d 13h 2d 21h 3d 34h 40h");
    });

    $("#estimatesSubmit").click(function() {
        estimatesInput.val(customEstimates.val());
        estimatesEditor.modal("hide");
    });

});
