$(function() {
    var editor = $("#editor"),
        input = $("input", editor);

    editor.on("show", function() {
        input.val($("#estimates").val());
    });

    $("a", editor).click(function() {
        input.val($(this).text());
    });

    $("#apply", editor).click(function() {
        $("#estimates").val(input.val());
        editor.modal("hide");
    });
});
