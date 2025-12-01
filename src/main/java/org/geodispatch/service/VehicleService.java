package org.geodispatch.service;

import org.geodispatch.entity.Vehicle;

import java.util.List;

public interface VehicleService extends CrudService<Vehicle> {

    List<Vehicle> findActiveVehicles();

}
