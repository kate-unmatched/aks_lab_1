package org.geodispatch.services;

import jakarta.ejb.Stateless;
import org.geodispatch.entity.JobOrder;
import org.geodispatch.services.base.GenericCrudServiceImpl;

@Stateless
public class JobOrderServiceImpl extends GenericCrudServiceImpl<JobOrder> {
    public JobOrderServiceImpl() { super(JobOrder.class); }
}