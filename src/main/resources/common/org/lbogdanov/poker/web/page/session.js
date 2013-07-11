var Poker = (function() {

    var msgTpl = "[%s]: %s",
        appendMsg = function(msg) {
            var chatLog = $("#chatLog");
            if (typeof msg === "string") { // otherwise msg is an Element
                msg = $("<pre>").text(msg);
            }
            $(".mCSB_container", chatLog).append(msg);
            chatLog.mCustomScrollbar("update");
            chatLog.mCustomScrollbar("scrollTo", "last");
        },
        showEstimate = function(slider, value) {
            slider.siblings("span").text(value);
            slider.siblings("#est").val(value);
        },
        showSliderValue = function(event, data) {
            showEstimate($(this), estimates[data.value]);
        },
        removeItem = function(id) {
            $("#item" + id).remove();
            $("#items").mCustomScrollbar("update");
        },
        editItem = function(item) {
            var element = $("#item" + item.id);
            element.find("span[id ^= title]").text(item.title);
            element.find("span[id ^= description]").text(item.description == null? "": item.description);
        },
        estimates = ${estimates};

    $(function() {
        // send chat messages on Ctrl / Meta + Enter, ignore single line break in a message input
        $("#chatMsg").keydown(function(evt) {
            if (evt.which === 10 || evt.which === 13) {
                if ($(this).val().length === 0) {
                    return false;
                }
                if (evt.ctrlKey || evt.metaKey) {
                    $("#chatSend").click();
                }
            }
        });
        // turn Bootstrap tooltips on
        $(".tip").tooltip();
        // turn custom scrollbars on
        $("#chatLog, #items").mCustomScrollbar({
            theme : "dark",
            scrollButtons : {
                enable : true
            }
        });
        var slider = $(".estimate");
        slider.bind("slider:changed", showSliderValue);
        showEstimate(slider, estimates[slider.val()]);
    });

    return {
        msgSent: function(xhr) {
            var msg,
                chatMsg = $("#chatMsg");
            if (xhr.status === 200) {
                msg = $.i18n.printf(msgTpl, [$.i18n._("chat.me"), chatMsg.val()]);
                chatMsg.val("");
            } else {
                msg = $.i18n._("chat.sendError", [xhr.statusText]);
                msg = $("<div>").addClass("error").text(msg);
            }
            appendMsg(msg);
        },
        toggleForm: function(form, disabled) {
            $($.i18n.printf("#%s :input", [form])).prop("disabled", disabled);
        },
        dispatch: function(msg) {
            switch (msg.type) {
                case "chatMsg":
                    appendMsg($.i18n.printf(msgTpl, [msg.author, msg.message]));
                    break;
                case "itemAdd":
                    break;
                case "itemRemove":
                    removeItem(msg.message.id);
                    break;
                case "itemEdit":
                    editItem(msg.message);
                    break;
            }
        },
        appendItem: function(itemMsg) {
            var item = $('<span/>', {id: itemMsg.markupId});
            $('#items .mCSB_container').append(item);
        },
        slider: function(itemMsg) {
            var items = $("#items"),
                slider = $("#item" + itemMsg.message.id).find(".estimate");
            slider.simpleSlider({
                snap: 'true',
                range: [0, (estimates.length - 1)],
                step: '1'
            });
            slider.bind("slider:changed", showSliderValue);
            showEstimate(slider, estimates[0]);
            items.mCustomScrollbar("update");
            items.mCustomScrollbar("scrollTo", "last");
        }
    };
})();
