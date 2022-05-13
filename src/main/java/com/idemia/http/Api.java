package com.idemia.http;

import com.idemia.http.dto.PlaceDto;
import com.idemia.http.dto.PutDto;
import com.idemia.http.dto.SeatingPolicyDto;
import com.idemia.model.Plane;
import com.idemia.parser.DtoParser;
import com.idemia.transformer.DtoRetrieveCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@Slf4j
public class Api {

    private final static DtoParser parser = new DtoParser();

    private Plane plane;

    @PutMapping("/plane")
    public ResponseEntity init(@RequestBody List<List<PlaceDto>> dtos) {
        plane = parser.parse(dtos);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/plane")
    public ResponseEntity get() {
        return ResponseEntity.ok(plane.execute(new DtoRetrieveCommand()));
    }

    @PutMapping("/plane/seating-policy")
    public ResponseEntity policy(@RequestBody SeatingPolicyDto dto) {
        plane.execute(new Plane.ChangePolicyCommand(dto.getType()));
        return ResponseEntity.status(CREATED).build();
    }

    @PostMapping("/plane/persons")
    public ResponseEntity add(@RequestBody PutDto dto) {
        plane.execute(new Plane.PutCommand(dto.getType().toPerson(), dto.getSeatTypePreference()));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/plane/persons/{row}/{column}")
    public ResponseEntity take(@PathVariable int row, @PathVariable int column) {
        plane.execute(new Plane.TakeCommand(row, column));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/plane/unoccupied")
    public ResponseEntity getUnoccupiedSize() {
        return ResponseEntity.ok(plane.execute(new Plane.UnoccupiedSignedCommand("SECURE KEY")));
    }
}
