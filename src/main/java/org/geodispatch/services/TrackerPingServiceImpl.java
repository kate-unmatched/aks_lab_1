package org.geodispatch.services;

import jakarta.ejb.Stateless;
import org.geodispatch.entity.TrackerPing;
import org.geodispatch.services.base.GenericCrudServiceImpl;

@Stateless
public class TrackerPingServiceImpl extends GenericCrudServiceImpl<TrackerPing> {
    public TrackerPingServiceImpl() { super(TrackerPing.class); }
}