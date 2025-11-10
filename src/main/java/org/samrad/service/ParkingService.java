package org.samrad.service;

import org.samrad.entity.ParkingRecord;
import org.samrad.repository.ParkingRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
@Service
public class ParkingService {
    private final ParkingRepository repository=ParkingRepository.getInstance();
    private final BigDecimal hourlyRate = new BigDecimal("5000");

    public void registerEntry(ParkingRecord record) throws Exception {

        List<ParkingRecord> allRecords = repository.findAll();
        for (ParkingRecord r : allRecords) {
            if (r.getExitTime() == null) {
                if (r.getLicensePlate().equals(record.getLicensePlate())) {
                    throw new RuntimeException("این شماره پلاک قبلا ثبت شده است!");
                }
                if (r.getSpotId() == record.getSpotId()) {
                    throw new RuntimeException("این جای پارک قبلا اشغال شده است!");
                }
            }
        }

        record.setEntryTime(LocalDateTime.now());
        record.setExitTime(null);
        record.setParkingFee(null);
        repository.insert(record);
    }


    public void registerExit(long id) throws Exception{
        ParkingRecord record = repository.findById(id);
        if(record==null){
            throw new RuntimeException("Record not found");
        }
        record.setExitTime(LocalDateTime.now());

        long minutes = Duration.between(record.getEntryTime(), record.getExitTime()).toMinutes();
        long hours = minutes / 60;
        if(minutes % 60 >=30){
            hours++;
        }
        if(hours == 0) hours = 1;

        BigDecimal fee = hourlyRate.multiply(BigDecimal.valueOf(hours));
        record.setParkingFee(fee);
        repository.update(record);
    }

    public List<ParkingRecord> getAllRecords() throws Exception{
        return repository.findAll();
    }
    public ParkingRecord getRecordById(long id) throws Exception{
        return repository.findById(id);
    }

    public void deleteRecord(long id) throws Exception{
        repository.delete(id);
    }
}
