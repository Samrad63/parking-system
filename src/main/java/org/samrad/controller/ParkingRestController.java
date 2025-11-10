package org.samrad.controller;

import org.samrad.entity.ParkingRecord;
import org.samrad.service.ParkingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parking")
public class ParkingRestController {

    private final ParkingService service;

    public ParkingRestController(ParkingService service) {
        this.service = service;
    }


    @GetMapping
    public List<ParkingRecord> getAll() throws Exception {
        return service.getAllRecords();
    }


    @PostMapping("/entry")
    public ParkingRecord registerEntry(@RequestBody ParkingRecord record) throws Exception {
        service.registerEntry(record);
        return record;
    }


    @PostMapping("/exit/{id}")
    public ParkingRecord registerExit(@PathVariable long id) throws Exception {
        service.registerExit(id);
        return service.getRecordById(id);
    }


    @DeleteMapping("/{id}")
    public String delete(@PathVariable long id) throws Exception {
        service.deleteRecord(id);
        return "Deleted";
    }
}
