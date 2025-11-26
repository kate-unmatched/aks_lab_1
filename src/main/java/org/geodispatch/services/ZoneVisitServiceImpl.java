package org.geodispatch.services;

import jakarta.ejb.Stateless;
import org.geodispatch.entity.ZoneVisit;
import org.geodispatch.services.base.GenericCrudServiceImpl;

@Stateless
public class ZoneVisitServiceImpl extends GenericCrudServiceImpl<ZoneVisit> {
    public ZoneVisitServiceImpl() { super(ZoneVisit.class); }
}