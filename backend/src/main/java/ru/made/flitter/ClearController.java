package ru.made.flitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClearController {

    @Autowired
    StateHolder holder;

    @DeleteMapping("/clear")
    public ResponseEntity<String> clear() {
        holder.getNameToUserMap().clear();
        holder.getTokenToUserMap().clear();
        holder.getNameToFlitsMap().clear();
        return ResponseEntity.ok("Success");
    }
}
