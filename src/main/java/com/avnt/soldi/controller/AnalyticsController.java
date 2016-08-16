package com.avnt.soldi.controller;

import com.avnt.soldi.model.request.AnalyticsPreferences;
import com.avnt.soldi.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Class AnalyticsController is controller, that handle analytical requests.
 * Use @Autowired for connect to necessary services
 * Use RequestMapping for handle request from the client-side
 *
 * @version 1.1
 * @author Dmitry
 * @since 21.01.2016
 */

@RestController
public class AnalyticsController {

    @Autowired AnalyticsService analyticsService;

    @RequestMapping(value = "/api/analytics", method = RequestMethod.POST,
            produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public byte[] getAnalytics(@RequestBody AnalyticsPreferences analyticsPreferences) {
        return analyticsService.getExcelFile(analyticsPreferences);
    }

}
