$(function() {
    var estimatesInput = $("#estimates"),
        customEstimates = $("#customEstimates"),
        estimatesEditor = $("#estimatesEditor");

    estimatesEditor.on("show", function() {
        customEstimates.val(estimatesInput.val());
    });

    $("#predefinedEstimates").change(function() {
        customEstimates.val($(this).val());
    });

    $("#estimatesSubmit").click(function() {
        estimatesInput.val(customEstimates.val());
        estimatesEditor.modal("hide");
    });

});
