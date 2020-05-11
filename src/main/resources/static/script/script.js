$(document).ready(function () {
    $.getJSON('/products', function (data) {
        var tbl_body = document.createElement("tbody");
        $.each(data, function (rowId, row) {
            var tbl_row = tbl_body.insertRow();
            $.each(this, function (k, v) {
                var cell = tbl_row.insertCell();
                cell.appendChild(document.createTextNode(v.toString()));
            });
            var cell = tbl_row.insertCell();
            cell.innerHTML = "<button class='btn' data-toggle='modal' data-target='#calculation-modal' data-product='" +
            row.name +
            "' data-bprice='" +
            row.base_price +
            "'>Prices</button>";
        });
        $("#table").append(tbl_body);   //DOM table doesn't have .appendChild
    });
});

$(document).on('show.bs.modal', "#calculation-modal", function (event) {
    var button = $(event.relatedTarget); // Button that triggered the modal
    var productName = button.data('product'); // Extract info from data-* attributes
    var productBasePrice = button.data('bprice');
    var modal = $(this);
    modal.find('#selectedProduct').text("Product: " + productName);
    modal.find('#inputBasePrice').val(productBasePrice);
})
