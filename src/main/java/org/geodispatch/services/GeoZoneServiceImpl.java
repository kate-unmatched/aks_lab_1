package org.geodispatch.services;

import jakarta.ejb.Stateless;
import org.geodispatch.entity.GeoZone;
import org.geodispatch.services.base.GenericCrudServiceImpl;

@Stateless
public class GeoZoneServiceImpl extends GenericCrudServiceImpl<GeoZone> {
    public GeoZoneServiceImpl() { super(GeoZone.class); }
}