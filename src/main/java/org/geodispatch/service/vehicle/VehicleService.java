package org.geodispatch.service.vehicle;

import org.geodispatch.entity.Vehicle;
import org.geodispatch.service.base.CrudService;

import java.util.List;

public interface VehicleService extends CrudService<Vehicle> {

    List<Vehicle> findActiveVehicles();

}
