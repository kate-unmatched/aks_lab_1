package org.geodispatch.services;

import org.geodispatch.entity.Vehicle;
import org.geodispatch.services.base.CrudService;

import java.util.List;

public interface VehicleService extends CrudService<Vehicle> {

    List<Vehicle> findActiveVehicles();

}
