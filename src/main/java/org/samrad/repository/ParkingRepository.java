package org.samrad.repository;

import org.samrad.entity.ParkingRecord;
import org.samrad.util.JDBC;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParkingRepository {
    private ParkingRepository() {
    }

    ;

    private static final ParkingRepository PARKING_REPOSITORY = new ParkingRepository();

    public static ParkingRepository getInstance() {
        return PARKING_REPOSITORY;
    }

    public void insert(ParkingRecord record) throws Exception {
        String sql = "INSERT INTO parking_record (license_plate, vehicle_type, spot_id, entry_time, exit_time, fee) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = JDBC.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            ps.setString(1, record.getLicensePlate());
            ps.setString(2, record.getVehicleType());
            ps.setInt(3, record.getSpotId());
            ps.setTimestamp(4, Timestamp.valueOf(record.getEntryTime()));
            if (record.getExitTime() != null) {
                ps.setTimestamp(5, Timestamp.valueOf(record.getExitTime()));
            } else {
                ps.setNull(5, Types.TIMESTAMP);
            }
            if (record.getParkingFee() != null) {
                ps.setBigDecimal(6, record.getParkingFee());
            } else {
                ps.setNull(6, Types.NUMERIC);
            }
            ps.executeUpdate();
            try(ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    record.setId(rs.getInt(1));
                }
            }
        }
    }

    public void update(ParkingRecord record) throws Exception {
        String sql = "UPDATE parking_record SET license_plate=?, vehicle_type=?, spot_id=?, entry_time=?, exit_time=?, fee=? WHERE id=?";
        try (Connection conn = JDBC.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, record.getLicensePlate());
            ps.setString(2, record.getVehicleType());
            ps.setInt(3, record.getSpotId());
            ps.setTimestamp(4, Timestamp.valueOf(record.getEntryTime()));
            if (record.getExitTime() != null) {
                ps.setTimestamp(5, Timestamp.valueOf(record.getExitTime()));
            } else {
                ps.setNull(5, Types.TIMESTAMP);
            }
            if (record.getParkingFee() != null) {
                ps.setBigDecimal(6, record.getParkingFee());
            } else {
                ps.setNull(6, Types.NUMERIC);
            }
            ps.setLong(7, record.getId());
            ps.executeUpdate();
        }
    }

    public void delete(long id) throws Exception {
        String sql = "DELETE FROM parking_record WHERE id=?";
        try (Connection conn = JDBC.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }

    public ParkingRecord findById(long id) throws SQLException {
        String sql = "SELECT * FROM parking_record WHERE id = ?";
        try (Connection conn = JDBC.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToParkingRecord(rs);
                }
            }
        }
        return null;
    }

    public List<ParkingRecord> findAll() throws SQLException {
        List<ParkingRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM parking_record";
        try (Connection conn = JDBC.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToParkingRecord(rs));
            }
        }
        return list;
    }

    private ParkingRecord mapResultSetToParkingRecord(ResultSet rs) throws SQLException {
        ParkingRecord record = new ParkingRecord();
        record.setId(rs.getLong("id"));
        record.setLicensePlate(rs.getString("license_plate"));
        record.setVehicleType(rs.getString("vehicle_type"));
        record.setSpotId(rs.getInt("spot_id"));
        Timestamp entry = rs.getTimestamp("entry_time");
        if (entry != null) record.setEntryTime(entry.toLocalDateTime());
        Timestamp exit = rs.getTimestamp("exit_time");
        if (exit != null) record.setExitTime(exit.toLocalDateTime());
        BigDecimal fee = rs.getBigDecimal("fee");
        record.setParkingFee(fee);
        return record;
    }

}
