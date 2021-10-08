package com.voytek.BikeShopJDBC;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;


@RestController
public class Controller {

    @Autowired
    JdbcTemplate jdbcTemplate;


    @PostMapping("/bike")
    public List<Bikes> enterNewBike(@RequestBody Bikes bike) {
        var sql = "INSERT INTO bikes (name, description, price) VALUES(?,?,?)";
        jdbcTemplate.update(
                sql, ps -> {
                    ps.setString(1, bike.getName());
                    ps.setString(2, bike.getDescription());
                    ps.setInt(3, bike.getPrice());
                });
        var sqlRead = "SELECT id, name, description, price FROM bikes WHERE name=?";
        return jdbcTemplate.query(sqlRead, ps -> {
            ps.setString(1, bike.getName());
        }, BikesExtract::extract);
    }

    @GetMapping("/bike")
    public List<Bikes> getAllBikes() {
        return jdbcTemplate.query("SELECT id, name, description, price FROM bikes", BikesExtract::extract);
    }


    @GetMapping("/bike/page/{page}")
    public List<Bikes> getAllBikesPagedSorted(@PathVariable int page) {
        var numberOfPages = 3;

        var sql = "SELECT id, name, description, price FROM bikes ORDER BY name, id LIMIT ?,? ";
        return jdbcTemplate.query(sql, ps -> {
            ps.setInt(1, numberOfPages * page);
            ps.setInt(2, numberOfPages);
        }, BikesExtract::extract);
    }

    @GetMapping("/bike/{id}")
    public Bikes getBike(@PathVariable long id) {
        var sqlRead = "SELECT id, name, description, price FROM bikes WHERE id=?";
        try {
            Bikes dbBikes = jdbcTemplate.queryForObject(sqlRead, (rs, rowNum) ->
                    new Bikes(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getInt("price")
                    ), new Object[]{id});

            return dbBikes;

        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bike not found for id = " + id);
        }
    }

    @PutMapping("/bike")
    public String modifyBike(@RequestBody Bikes bike) {
        var sqlRead = "SELECT id, name, description, price FROM bikes WHERE id=?";
        try {
            Bikes dbBikes = jdbcTemplate.queryForObject(sqlRead, (rs, rowNum) ->
                    new Bikes(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getInt("price")
                    ), new Object[]{bike.getId()});

            var sql = "UPDATE bikes SET name=?, description=?, price=? WHERE id=?";

            jdbcTemplate.update(sql, ps -> {
                ps.setString(1, bike.getName());
                ps.setString(2, bike.getDescription());
                ps.setInt(3, bike.getPrice());
                ps.setLong(4, bike.getId());
            });

            Bikes newBikes = jdbcTemplate.queryForObject(sqlRead, (rs, rowNum) ->
                    new Bikes(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getInt("price")
                    ), new Object[]{bike.getId()});

            return dbBikes.toString() + "\n Changed to:\n" + newBikes.toString();

        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bike not found for id = " + bike.getId());
        }
    }

    @DeleteMapping("/bike/{id}")
    public String deleteBike(@PathVariable long id) {
        var sqlRead = "SELECT id, name, description, price FROM bikes WHERE id=?";
        try {
            Bikes dbBikes = jdbcTemplate.queryForObject(sqlRead, (rs, rowNum) ->
                    new Bikes(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getInt("price")
                    ), new Object[]{id});

            var sql = "DELETE FROM bikes WHERE id=?";

            jdbcTemplate.update(sql, id);

            return dbBikes.toString() + "\n Was deleted";

        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bike not found for id = " + id);
        }
    }

}
