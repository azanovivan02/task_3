package ru.made.twitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.made.twitter.dto.AddTweetForm;
import ru.made.twitter.dto.Tweet;
import ru.made.twitter.dto.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("tweet")
public class TweetRestController {

	@Autowired
	StateHolder holder;

	@PostMapping("/add")
	public ResponseEntity<String> add(@RequestBody AddTweetForm form) {
		String userToken = form.getUserToken();
		if (!holder.getTokenToUserMap().containsKey(userToken)) {
			return ResponseEntity.badRequest().body("Unknown token");
		}

		User user = holder.getTokenToUserMap().get(userToken);
		String userName = user.getUserName();
		String content = form.getContent();
		Tweet newTweet = new Tweet(userName, content);

		List<Tweet> userTweets = holder.getNameToTweetsMap().computeIfAbsent(userName, e -> new ArrayList<>());
		userTweets.add(newTweet);

		return ResponseEntity.ok("Success");
	}

	@GetMapping("list")
	public ResponseEntity<List<Tweet>> listAllTweets() {
		List<Tweet> allTweets = holder.getNameToTweetsMap()
				.entrySet()
				.stream()
				.sorted(Comparator.comparing(Map.Entry::getKey))
				.map(Map.Entry::getValue)
				.flatMap(Collection::stream)
				.collect(Collectors.toList());

		System.out.println("Invoked list all tweets: "+allTweets.size());

		return ResponseEntity.ok(allTweets);
	}

	@GetMapping("list/{userName}")
	public ResponseEntity<List<Tweet>> listTweetsForUser(@PathVariable String userName) {
		List<Tweet> userTweets = holder.getNameToTweetsMap().getOrDefault(userName, Collections.emptyList());
		return ResponseEntity.ok(userTweets);
	}
}
