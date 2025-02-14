$(document).ready(function() {
    $('.showGroups').click(function(event) {
        $('.table5').addClass('d-none');
        $('.table4').addClass('d-none');
        $('.table3').addClass('d-none');
        $('.table2').addClass('d-none');
        $('.table1').removeClass('d-none');
    });
    $('.showPlansSpring').click(function(event) {
        $('.table5').addClass('d-none');
        $('.table4').addClass('d-none');
        $('.table3').addClass('d-none');
        $('.table2').removeClass('d-none');
        $('.table1').addClass('d-none');
    });
    $('.showPlansAutumn').click(function(event) {
        $('.table5').addClass('d-none');
        $('.table4').addClass('d-none');
        $('.table3').removeClass('d-none');
        $('.table2').addClass('d-none');
        $('.table1').addClass('d-none');
    });
    $('.createStreamsAutumn').click(function(event) {
        $('.table5').addClass('d-none');
        $('.table4').removeClass('d-none');
        $('.table3').addClass('d-none');
        $('.table2').addClass('d-none');
        $('.table1').addClass('d-none');
    });
    $('.createStreamsSpring').click(function(event) {
        $('.table5').removeClass('d-none');
        $('.table4').addClass('d-none');
        $('.table3').addClass('d-none');
        $('.table2').addClass('d-none');
        $('.table1').addClass('d-none');
    });
});