package org.geodispatch.api;

import jakarta.ejb.EJB;
import jakarta.ws.rs.core.Response;
import org.geodispatch.dtos.JobOrderCreateRequestDTO;
import org.geodispatch.dtos.JobOrderUpdateRequestDTO;
import org.geodispatch.entity.JobOrder;
import org.geodispatch.mappers.JobOrderMapper;
import org.geodispatch.service.JobOrderService;

import java.util.stream.Collectors;

public class JobOrderApiImpl implements JobOrderApi {

    @EJB
    private JobOrderService jobOrderService;

    @EJB
    private JobOrderMapper mapper;

    @Override
    public Response create(JobOrderCreateRequestDTO req) {
        var entity = mapper.fromCreateDto(req);
        var saved = jobOrderService.create(entity);
        return Response.ok(mapper.toDto(saved)).build();
    }

    @Override
    public Response findById(Long id) {
        var entity = jobOrderService.findById(id);
        return Response.ok(mapper.toDto(entity)).build();
    }

    @Override
    public Response update(Long id, JobOrderUpdateRequestDTO req) {
        var existing = jobOrderService.findById(id);
        existing.setStatus(JobOrder.Status.valueOf(req.getStatus()));

        var updated = jobOrderService.update(existing);
        return Response.ok(mapper.toDto(updated)).build();
    }

    @Override
    public Response findAll() {
        var list = jobOrderService.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        return Response.ok(list).build();
    }
}
