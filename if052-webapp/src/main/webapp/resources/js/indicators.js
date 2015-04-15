/**
 * Created by Max on 3/23/2015.
 */
$(document).ready(function() {
    $('#paginated').dataTable({
        "lengthMenu":[ [5, 10, 15, 25, -1], [5, 10, 15, 25, "Всі"]],
        "info":     false,
        "columns": [
            null,
            null,
            null,
            null,
            { "orderable": false },
            { "orderable": false },
            { "orderable": false }
        ],
        "order": [[ 0, 'desc' ], [ 1, 'desc' ]],
        "language": {
            "url": "//cdn.datatables.net/plug-ins/f2c75b7247b/i18n/Ukranian.json"
        }
    });
});
$(function() {
    $.datepicker.setDefaults($.datepicker.regional[ "uk" ]);
    $("#datepicker").datepicker({dateFormat:"dd-mm-yy"}).datepicker("setDate", new Date());
});