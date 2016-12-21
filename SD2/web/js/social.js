/**
 * Created by ritaalmeida on 17/12/16.
 */

function change_button_on_load(facebookid) {
    var form = document.getElementById("facebook_form");
    console.debug(facebookid);
    if (facebookid == null)
    {
        form.action = "login";
        form.innerHTML = '<input id="loginType" type="text" value="facebook" name="LoginType" hidden=""/>'+
            '<button type="submit" method="execute" class="btn btn-block btn-primary btn-facebook">'+
            '<i class="fa fa-facebook"></i> Connect with Facebook'+
            '</button>';
    }
    else
    {
        form.action = "remove";
        form.innerHTML = '<input id="loginType" type="text" value="facebook" name="LoginType" hidden=""/>'+
            '<button type="submit" method="execute" class="btn btn-block btn-primary btn-facebook">'+
            '<i class="fa fa-facebook"></i> Disconnect from Facebook'+
            '</button>';
    }
}