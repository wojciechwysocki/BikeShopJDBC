package com.voytek.BikeShopJDBC;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BikesExtract {


    static List<Bikes> extract(ResultSet rs) throws SQLException {
        List<Bikes> list = new ArrayList<>();
        while (rs.next()) {
            Bikes b = new Bikes();
            b.setId(rs.getLong("id"));
            b.setName(rs.getString("name"));
            b.setDescription(rs.getString("description"));
            b.setPrice(rs.getInt("price"));
            list.add(b);
        }
        return list;
    }

}
