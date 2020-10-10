package ru.made.flitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.made.flitter.dto.AddUserForm;
import ru.made.flitter.dto.User;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("user")
public class UserRestController {

    @Autowired
    StateHolder holder;

    @PostMapping("/register")
    public ResponseEntity<User> add(@RequestBody AddUserForm form) {
        String userName = form.getUserName();
        if (holder.getNameToUserMap().containsKey(userName)) {
            return ResponseEntity.badRequest().body(null);
        }

        String userToken = Utils.generateUserToken();
        User newUser = new User(userName, userToken);

        holder.getTokenToUserMap().put(userToken, newUser);
        holder.getNameToUserMap().put(userName, newUser);

        return ResponseEntity.ok(newUser);
    }

    @GetMapping("/list")
    public ResponseEntity<List<String>> list() {
        List<String> userNames = holder
                .getNameToUserMap()
                .values()
                .stream()
                .map(User::getUserName)
                .collect(Collectors.toList());

        return ResponseEntity.ok(userNames);
    }
}
