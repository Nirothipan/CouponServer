package org.acardo.coupon;

import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

// The class to do all coupon related operations like add modify delete get or retrieve valid tokens.
@Path("coupon")
public class CouponManagement {

    private static HashMap<String, Coupon> coupons = new HashMap<>();
    private SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    private Logger logger = Logger.getLogger(CouponManagement.class.getName());

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("add")
    // adds or modify coupon and return status based on it whether addition or modification.
    public String addOrModifyCoupon(String request) {

        logger.info("Resource add called.");
        try {
            JSONObject requestBody = new JSONObject(request);

            String id = (String) requestBody.get("id");
            String threeDigits = id.substring(0, 3);

            // check whether EAN / GTIN ID has 13 digits and starts with 981 or 982.
            if (id.length() != 13 || !(threeDigits.equals("981") || threeDigits.equals("982"))) {
                return "{\"Status\":\" Invalid Request \"}";
            }

            String actionName = (String) requestBody.get("ActionName");
            String startDate = (String) requestBody.get("StartDate");
            String endDate = (String) requestBody.get("EndDate");
            Double discount = (Double) requestBody.get("Discount");
            boolean status = (boolean) requestBody.get("Status");
            boolean couponExisted = (coupons.get(id) != null);

            Coupon coupon = new Coupon(id, actionName, startDate, endDate, discount, status);
            coupons.put(id, coupon);

            if (couponExisted) {
                return "{\"Status\":\" Coupon Modified \"}";
            } else {
                return "{\"Status\":\" Coupon Added \"}";
            }

        } catch (JSONException | ParseException | StringIndexOutOfBoundsException ex) {
            return "{\"Status\":\" Invalid Request \"}";
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("delete")
    // deletes a coupon if it exists.
    public String deleteCoupon(String request) {

        logger.info("Resource delete called.");
        try {
            JSONObject requestBody = new JSONObject(request);
            String id = (String) requestBody.get("id");

            if (coupons.get(id) != null) {
                coupons.remove(id);
                return "{\"Status\":\" Coupon Deleted \"}";
            } else {
                String threeDigits = id.substring(0, 3);
                if (id.length() != 13 || !(threeDigits.equals("981") || threeDigits.equals("982"))) {
                    return "{\"Status\":\" Invalid Request \"}";
                }
                return "{\"Status\":\" Coupon Not Found \"}";
            }

        } catch (JSONException | StringIndexOutOfBoundsException ex) {
            return "{\"Status\":\" Invalid Request \"}";
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("valid")
    // obtain valid coupons
    // criteria : the status need to be ACTIVE  and current date should be between start and end date.
    public String validCoupon() {

        logger.info("Resource valid called.");
        Date currentDate = new Date();
        String responses = "[";
        for (Map.Entry<String, Coupon> entry : coupons.entrySet()) {

            if (entry.getValue().status && currentDate.after(entry.getValue().startDate) && currentDate.before(entry.getValue().endDate)) {
                String response = "{\"id\":\"" + entry.getValue().eanID +
                        "\",\"ActionName\":\"" + entry.getValue().actionName +
                        "\",\"StartDate\":\"" + df.format(entry.getValue().startDate) +
                        "\",\"EndDate\":\"" + df.format(entry.getValue().endDate) +
                        "\",\"Discount\":\"" + entry.getValue().discountValue +
                        "\",\"Status\":" + entry.getValue().status +
                        "}";
                responses += response + ",";
            }
        }

        if (responses.equals("[")) {
            return "{\"Status\":\" No Valid Coupons Found \"}";
        } else {
            return responses.substring(0, responses.length() - 1) + "]";
        }

    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("get")
    // retrieve coupon with the ID
    public String getCoupon(String request) {

        logger.info("Resource get called.");
        try {
            JSONObject requestBody = new JSONObject(request);
            String id = (String) requestBody.get("id");
            String threeDigits = id.substring(0, 3);

            if (id.length() != 13 || !(threeDigits.equals("981") || threeDigits.equals("982"))) {
                return "{\"Status\":\" Invalid Request \"}";
            }
            Coupon coupon = coupons.get(id);

            if (coupon == null) {
                return "{\"Status\":\" Coupon Not Found \"}";
            }

            String response = "{\"id\":\"" + coupon.eanID +
                    "\",\"ActionName\":\"" + coupon.actionName +
                    "\",\"StartDate\":\"" + df.format(coupon.startDate) +
                    "\",\"EndDate\":\"" + df.format(coupon.endDate) +
                    "\",\"Discount\":\"" + coupon.discountValue +
                    "\",\"Status\":" + coupon.status +
                    "}";

            return response;

        } catch (JSONException | StringIndexOutOfBoundsException ex) {
            return "{\"Status\":\" Invalid Request \"}";
        }
    }

}
