package com.ww.aggregator;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.mule.DefaultMuleEvent;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.routing.AggregationContext;
import org.mule.routing.AggregationStrategy;

import com.ww.bean.TweetDetails;

public class CustomAggregator implements AggregationStrategy {

	@Override
	public MuleEvent aggregate(AggregationContext context) throws MuleException {
		MuleEvent result = null;
		List<TweetDetails> tweetList = null;
		for (MuleEvent event : context.getEvents()) {
			result = DefaultMuleEvent.copy(event);
			if (tweetList == null) {
				System.out.println("First loop :"+tweetList);
				tweetList = (List<TweetDetails>) event.getMessage().getPayload();
			} else {
				System.out.println("Second loop :"+tweetList.size());
				tweetList.addAll((List<TweetDetails>) event.getMessage().getPayload());
			}
		}
		//result.getMessage().setPayload(tweetList);
		result.getMessage().setPayload(getFinalResult(tweetList));
		if (result != null) {
			return result;
		}
		throw new  RuntimeException("There are no Results");
	}

	public Map<String, Integer> getFinalResult(List tweetsList) {
		System.out.println("Start : getFinalResult");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		TweetDetails tweetDetail = null;
		Map<String, Integer> finalResult = new HashMap<String, Integer>();
		Iterator mainItr = tweetsList.iterator();
		System.out.println("TweetsList Soze : "+tweetsList.size());
		while(mainItr.hasNext()){
			List<TweetDetails> teetsList = (List<TweetDetails>) mainItr.next();
			int count = 0;
			System.out.println("Inside teetsList Size : "+tweetsList.size());
			Iterator tweetsItr = teetsList.iterator();
			while (tweetsItr.hasNext()) {
				tweetDetail = (TweetDetails) tweetsItr.next();
				count = 0;
				if (tweetDetail.getUserId() != null) {
					if (tweetDetail.getCreatedDate().compareTo(cal.getTime()) >= 0) {
						System.out.println("Created Date : "
								+ tweetDetail.getCreatedDate());
						if (finalResult.containsKey(tweetDetail.getUserName())) {
							count = finalResult.get(tweetDetail.getUserName()) + 1;
						} else {
							count = 1;
						}
					}
					finalResult.put(tweetDetail.getUserName(), count);
				} else {
					if (finalResult.containsKey(tweetDetail.getName())) {
						count = finalResult.get(tweetDetail.getName()) + 1;
					} else {
						count = 1;
					}
					finalResult.put(tweetDetail.getName(), count);
				}
				tweetDetail = null;
			}
		}
		System.out.println("End : getFinalResult");
		return finalResult;
	}

}
