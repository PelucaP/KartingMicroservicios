package com.example.rack_service.Services;

import com.example.rack_service.Client.ReservaComprobanteClient;
import com.example.rack_service.DTO.ReservaRackRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RackService {
    private final ReservaComprobanteClient reservaComprobanteClient;

    @Autowired
    public RackService(ReservaComprobanteClient reservaComprobanteClient) {
        this.reservaComprobanteClient = reservaComprobanteClient;
    }

    public List<ReservaRackRequest> getReservasForDisplay() {
        // Add any business logic here if needed
        return reservaComprobanteClient.getAllReservasForRack();
    }


}
