$(document).ready(function () {
    var prevOpened = "";

    (function ($) {
        $.fn.serializeFormJSON = function () {

            var o = {};
            var a = this.serializeArray();
            $.each(a, function () {
                if (o[this.name]) {
                    if (!o[this.name].push) {
                        o[this.name] = [o[this.name]];
                    }
                    o[this.name].push(this.value || '');
                } else {
                    o[this.name] = this.value || '';
                }
            });
            return o;
        };
    })(jQuery);

    function dropdownButton(row) {
        return `<div class="btn-group">
                    <button type='button' class='btn btn-secondary' data-toggle='modal' data-target='#calculation-modal' data-product='${row.name}' data-bprice='${row.base_price}'>Prices</button>
                    <button type='button' class='btn btn-secondary dropdown-toggle dropdown-toggle-split' data-toggle='dropdown' aria-haspopup='true' aria-expanded='false'>
                        <span class='sr-only'>Toggle Dropdown</span>
                    </button>
                    <div class='dropdown-menu'>
                        <a class='dropdown-item' type='button' data-toggle='modal' data-target='#abroadCalculation' data-product='${row.name}' data-bprice='${row.base_price}'>Prices abroad</a>
                        <a class='dropdown-item' type='button' data-toggle='modal' data-target='#updateModal' data-quantity='${row.quantity}' data-description='${row.description}' data-product='${row.name}' data-bprice='${row.base_price}'>Update</a>
                        <a class='dropdown-item' href="#">Delete</a>
                    </div>
                </div>`;
    }

    const formatter = new Intl.NumberFormat('en-US', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2,
    });

    function productsTable(data) {
        var tbl_body = document.createElement("tbody");
        $.each(data, function (rowId, row) {
            var tbl_row = tbl_body.insertRow();
            $.each(this, function (k, v) {
                var cell = tbl_row.insertCell();
                cell.appendChild(document.createTextNode(v.toString()));
            });
            var cell = tbl_row.insertCell();
            cell.innerHTML = dropdownButton(row);
        });
        $("#table").append(tbl_body);   //DOM table doesn't have .appendChild
    }

    $.getJSON('/products', productsTable);


    $("#prices-form-send").click(function () {
        console.log("Form send clicked");
        /* get the action attribute from the <form action=""> element */
        const url = "/price/" +
            $("#selectedProduct").text() + "/" +
            $("#inputPreferredPrice").val() + "/" +
            $("#inputLogisticCost").val(),
            states = ($("#selectStates").val());

        $.each(states, function (i, item) {
            states[i] = parseInt(item);
        });

        /* Send the data using post with element id name and name2*/
        var posting = $.post(url, JSON.stringify(states));

        /* Alerts the results */
        posting.done(function (data) {
            console.log(data);
            $('#price-table-caption').html(data[0].productName + ' (' + data[0].category + ')');
            $('#price-table-body').empty();
            $.each(data, function (rowId, row) {
                var html_row = `<tr>
                                        <td>${row.stateName}</td>
                                        <td>$${formatter.format(row.basePrice)}</td>
                                        <td>$${formatter.format(row.finalPrice)}</td>
                                        <td>$${formatter.format(row.logisticCost)}</td>
                                        <td>${formatter.format(row.tax * 100)}%</td>
                                        <td>$${formatter.format(row.taxValue)}</td>
                                        <td>$${formatter.format(row.noTaxPrice)}</td>
                                        <td>$${formatter.format(row.profit)}</td>
                                   </tr>`;
                $('#price-table-body').append(html_row);
                $("#price-table").css('visibility', 'visible');
            });

        });
    });

    $("#updateFormSend").click(function () {
        const productName = $("#updateModalSelectedProduct").text()
        const url = "/product/" + productName;
        const obj = $("#updateProductForm").serializeFormJSON();

        console.log(JSON.stringify(obj));

        $.ajax({
            url: url,
            type: 'PUT',
            data: JSON.stringify(obj),
            contentType: "application/json",
            success: function () {
                location.reload();
            }
        });
    })

    $("#abroadPriceFormSend").click(function () {
        console.log("Form send clicked A");
        /* get the action attribute from the <form action=""> element */
        const url = "/priceabroad/" +
            $("#abroadSelectedProduct").text() + "/" +
            $("#abroadInputPreferredPrice").val() + "/" +
            $("#abroadInputLogisticCost").val(),
            countries = ($("#selectCountries").val());

        $.each(countries, function (i, item) {
            countries[i] = parseInt(item);
        });

        /* Send the data using post with element id name and name2*/
        var posting = $.post(url, JSON.stringify(countries));

        /* Alerts the results */
        posting.done(function (data) {
            console.log(data);
            $('#abroadPriceTableCaption').html(data[0].product + ' (' + data[0].category + ')');
            $('#abroadPriceTableBody').empty();
            $.each(data, function (rowId, row) {
                var html_row = `<tr>
                                        <td>${row.country}</td>
                                        <td>${row.currencyCode}${formatter.format(row.basePrice)}</td>
                                        <td>${row.currencyCode}${formatter.format(row.finalPrice)}</td>
                                        <td>${row.currencyCode}${formatter.format(row.logisticCost)}</td>
                                        <td>${row.currencyCode}${formatter.format(row.transportFee)}</td>
                                        <td>${formatter.format(row.importTariff * 100)}%</td>
                                        <td>${row.currencyCode}${formatter.format(row.importTariffValue)}</td>
                                        <td>${row.currencyCode}${formatter.format(row.noTaxPrice)}</td>
                                        <td>${row.currencyCode}${formatter.format(row.profit)}</td>
                                   </tr>`;
                $('#abroadPriceTableBody').append(html_row);
                $("#abroadPriceTable").css('visibility', 'visible');
            });

        });
    });

    $(document).on('show.bs.modal', "#calculation-modal", function (event) {
        var button = $(event.relatedTarget); // Button that triggered the modal
        var productName = button.data('product'); // Extract info from data-* attributes
        var productBasePrice = button.data('bprice');
        var modal = $(this);
        modal.find('#selectedProduct').text(productName);
        modal.find('#inputBasePrice').val(productBasePrice);
        if (prevOpened !== productName) {
            modal.find('#inputPreferredPrice').val("");
            modal.find('#inputLogisticCost').val("");
            prevOpened = productName;
        }
    })

    $(document).on('show.bs.modal', "#abroadCalculation", function (event) {
        var button = $(event.relatedTarget); // Button that triggered the modal
        var productName = button.data('product'); // Extract info from data-* attributes
        var productBasePrice = button.data('bprice');
        var modal = $(this);
        modal.find('#abroadSelectedProduct').text(productName);
        modal.find('#abroadInputBasePrice').val(productBasePrice);
        if (prevOpened !== productName) {
            modal.find('#abroadInputPreferredPrice').val("");
            modal.find('#abroadInputLogisticCost').val("");
            prevOpened = productName;
        }
    })

    $(document).on('show.bs.modal', "#updateModal", function (event) {
        var button = $(event.relatedTarget); // Button that triggered the modal
        var productName = button.data('product'); // Extract info from data-* attributes
        var productBasePrice = button.data('bprice');
        var quantity = button.data('quantity');
        var productDescription = button.data('description');
        var modal = $(this);
        modal.find('#updateModalSelectedProduct').text(productName);
        modal.find('#updInputBasePrice').val(productBasePrice);
        modal.find('#updInputDescription').val(productDescription);
        modal.find('#updInputQuantity').val(quantity);
    })
});
