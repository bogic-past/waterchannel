package com.softserveinc.if052_restful.resource;

import com.softserveinc.if052_core.domain.Indicator;
import com.softserveinc.if052_core.domain.Field;
import com.softserveinc.if052_core.domain.ValidationError;
import com.softserveinc.if052_core.domain.WaterMeter;
import com.softserveinc.if052_restful.controller.ErrorController;
import com.softserveinc.if052_restful.service.WaterMeterService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by Danylo Tiahun on 11.02.2015.
 */

@RestController
@RequestMapping("/rest/watermeters")
public class WaterMeterResource {

    @Autowired
    private WaterMeterService waterMeterService;

    private static Logger LOGGER = Logger.getLogger(WaterMeterResource.class.getName());
    
    @PostAuthorize("hasPermission(returnObject, 'getWaterMeter')")
    @RequestMapping(value = "{waterMeterId}", method = RequestMethod.GET, produces = "application/json")
    public WaterMeter getWaterMeter(
            @PathVariable("waterMeterId") int waterMeterId,
            HttpServletResponse response) {
        LOGGER.info("Searching for the meter with id " + waterMeterId + ".");
        WaterMeter waterMeter = waterMeterService.getWaterMeterById(waterMeterId);
        if (waterMeter == null) {
            LOGGER.info("Meter with requested id " + waterMeterId + " has not been found.");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        LOGGER.info("Meter with requested id " + waterMeterId + " has been successfully found.");
        return waterMeter;
    }

    @RequestMapping(value = "/firstMeter", method = RequestMethod.GET, produces = "application/json")
    public WaterMeter getFirstMeter(HttpServletResponse response) {
        LOGGER.info("Searching for first meter for the requested user.");
        WaterMeter waterMeter = waterMeterService.getFirstMeter();
        if (waterMeter == null) {
            LOGGER.info("First meter for the requested user has not been found.");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        LOGGER.info("First meter requested user has been successfully found.");
        return waterMeter;
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public WaterMeter insertWaterMeter(
            @Valid
            @RequestBody
            WaterMeter waterMeter,
            HttpServletResponse response) {
        LOGGER.info("Adding a new meter.");
        try {
            waterMeterService.insertWaterMeter(waterMeter);
            LOGGER.info("Meter has been successfully added with id " + waterMeter.getWaterMeterId() + ".");
            response.setStatus(HttpServletResponse.SC_CREATED);
            return waterMeter;
        } catch (DataIntegrityViolationException e) {
            LOGGER.warn("Meter with this name already exist.", e);
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return null;
    }

    @RequestMapping(value = "{waterMeterId}", method = RequestMethod.POST)
    public void insertWaterMeter(
            @PathVariable("waterMeterId") int waterMeterId,
            @RequestBody
            WaterMeter waterMeter,
            HttpServletResponse response) {
        LOGGER.info("Meter id cannot be provided by the request.");
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @RequestMapping(method = RequestMethod.PUT, produces = "application/json")
    public WaterMeter updateWaterMeter(
            @RequestBody
            WaterMeter waterMeter,
            HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        LOGGER.info("The whole collection of meters cannot be updated.");
        return waterMeter;
    }

    @PreAuthorize("hasPermission(#waterMeter.waterMeterId, 'udWaterMeter')")
    @RequestMapping(value = "{waterMeterId}", method = RequestMethod.PUT, produces = "application/json")
    public WaterMeter updateWaterMeter(
            @PathVariable("waterMeterId") int waterMeterId,
            @Valid
            @RequestBody
            WaterMeter waterMeter,
            HttpServletResponse response) {
        LOGGER.info("Updating a meter with id " + waterMeterId + ".");
        if (waterMeterService.getWaterMeterById(waterMeterId) == null) {
            LOGGER.info("Meter with requested id " + waterMeterId + " is not found.");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        try {
            waterMeterService.updateWaterMeter(waterMeter);
            LOGGER.info("Meter with id " + waterMeterId + " has been successfully updated.");
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            return waterMeter;
        } catch (DataIntegrityViolationException e) {
            LOGGER.warn("Meter with this name already exist.", e);
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return null;
    }

    @RequestMapping(method = RequestMethod.DELETE, produces = "application/json")
    public void deleteWaterMeter(HttpServletResponse response) {
        LOGGER.info("The whole collection of meters cannot be deleted.");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

    }

    @PreAuthorize("hasPermission(#waterMeterId, 'udWaterMeter')")
    @RequestMapping(value = "{waterMeterId}", method = RequestMethod.DELETE, produces = "application/json")
    public void deleteWaterMeter(
            @PathVariable("waterMeterId") int waterMeterId,
            HttpServletResponse response) {
        LOGGER.info("Deleting a meter with id " + waterMeterId + ".");
        if (waterMeterService.getWaterMeterById(waterMeterId) == null) {
            LOGGER.info("Meter with requested id " + waterMeterId + " is not found.");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } else
            try {
                waterMeterService.deleteWaterMeter(waterMeterId);
                LOGGER.info("Meter with id " + waterMeterId + " has been successfully deleted.");
            } catch (DataIntegrityViolationException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                LOGGER.warn("Meter with requester id " + waterMeterId
                        + " contains list of indicators so it cannot be deleted.", e);
            }
    }


}