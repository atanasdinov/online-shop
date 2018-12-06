$(document).ready(function () {

    $("#checkout").on("click", function (event) {
        event.preventDefault();
        requestProductQuantity();
    });

})

function requestProductQuantity() {

    var quantity = [];

    $('.quantity').each(function (i) {
        var currentQuantity = $(this).val();

        if(currentQuantity == "")
            currentQuantity = 0;

        quantity.push(currentQuantity);
    });

    $(".quantity").val("");

    $.ajax({
        type: "POST",
        url: "/sales/purchase",
        data: $('#submitForm').serialize() + '&requestedQuantity=' + quantity,
        success: function (data) {
            checkStatus(data);
        },
        error: function (error, data) {
            console.log("error");
        }
    });

}


function checkStatus(data) {
    if (data == 307)
        $("#error").text("Empty cart!")
    else if (data == 406)
        $("#error").text("Invalid quantity!")
    else if (data == 204)
        $("#error").text("Selected quantity is not available!")
    else
        location.href = "/success";
}