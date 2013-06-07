$(function() {
    var estimatesEditor = $("#estimatesEditor"),
        customEstimates = $("#customEstimates", estimatesEditor);

    estimatesEditor.on("show", function() {
        customEstimates.val($("#estimates").val());
    });

    $("a", estimatesEditor).click(function() {
        customEstimates.val($(this).text());
    });

    $("#estimatesSubmit").click(function() {
        $("#estimates").val(customEstimates.val());
        estimatesEditor.modal("hide");
    });
});
