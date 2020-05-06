$(document).ready(function () {
    $.getJSON('/products', function (data) {
        var tbl_body = document.createElement("tbody");
        $.each(data, function () {
            var tbl_row = tbl_body.insertRow();
            $.each(this, function (k, v) {
                var cell = tbl_row.insertCell();
                cell.appendChild(document.createTextNode(v.toString()));
            });
        });
        $("#table").append(tbl_body);   //DOM table doesn't have .appendChild
    });
});
