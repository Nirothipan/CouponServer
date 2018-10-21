package org.acardo.coupon;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// The item coupon.
class Coupon {

    String eanID; //  EAN / GTIN ID
    String actionName;
    Date startDate;
    Date endDate;
    double discountValue;
    boolean status; // active ( status true ) inactive ( status false )

    Coupon(String ID, String actionName, String startDate, String endDate, double discountValue, boolean status)
            throws ParseException {

        this.eanID = ID;
        this.actionName = actionName;
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        this.startDate = df.parse(startDate);
        this.endDate = df.parse(endDate);
        this.discountValue = discountValue;
        this.status = status;
    }

}
