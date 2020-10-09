package ru.made.flitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.made.flitter.dto.AddFlitForm;
import ru.made.flitter.dto.Flit;
import ru.made.flitter.dto.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("flit")
public class FlitRestController {

	@Autowired
	StateHolder holder;

	@PostMapping("/add")
	public ResponseEntity<Boolean> add(@RequestBody AddFlitForm form) {
		String userToken = form.getUserToken();
		if (!holder.getTokenToUserMap().containsKey(userToken)) {
			return ResponseEntity.badRequest().body(false);
		}

		User user = holder.getTokenToUserMap().get(userToken);
		String userName = user.getUserName();
		String content = form.getContent();
		Flit newFlit = new Flit(userName, content);

		List<Flit> userFlits = holder.getNameToFlitsMap().computeIfAbsent(userName, e -> new ArrayList<>());
		userFlits.add(newFlit);

		return ResponseEntity.ok(true);
	}

	@GetMapping("list")
	public ResponseEntity<List<Flit>> listAllFlits() {
		List<Flit> allFlits = holder.getNameToFlitsMap()
				.entrySet()
				.stream()
				.sorted(Comparator.comparing(Map.Entry::getKey))
				.map(Map.Entry::getValue)
				.flatMap(Collection::stream)
				.collect(Collectors.toList());

		System.out.println("Invoked list all flits: "+allFlits.size());

		return ResponseEntity.ok(allFlits);
	}

	@GetMapping("list/{userName}")
	public ResponseEntity<List<Flit>> listFlitsForUser(@PathVariable String userName) {
		List<Flit> userFlits = holder.getNameToFlitsMap().getOrDefault(userName, Collections.emptyList());
		return ResponseEntity.ok(userFlits);
	}
}
