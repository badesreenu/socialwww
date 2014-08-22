package com.ww.transformer;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractTransformer;

import com.ww.bean.TweetDetails;

public class TweetsTransformer extends AbstractTransformer {

	@Override
	protected Object doTransform(Object src, String enc)
			throws TransformerException {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		TweetDetails tweetDetail = null;
		Map<String, Integer> result = new HashMap<String, Integer>();
		List tweetList = (List) src;
		Iterator mainItr = tweetList.iterator();
		System.out.println("TweetsList Size : " + tweetList.size());
		while (mainItr.hasNext()) {
			List<TweetDetails> tweetsList = (List<TweetDetails>) mainItr.next();
			int count = 0;
			Iterator<TweetDetails> tweetsItr = tweetsList.iterator();
			while (tweetsItr.hasNext()) {
				tweetDetail = (TweetDetails) tweetsItr.next();
				count = 0;
				if (tweetDetail.getUserId() != null) {
					if (tweetDetail.getCreatedDate().compareTo(cal.getTime()) >= 0) {
						if (result.containsKey(tweetDetail.getUserName())) {
							count = result.get(tweetDetail.getUserName()) + 1;
						} else {
							count = 1;
						}
					}
					result.put(tweetDetail.getUserName(), count);
				} else {
					if (result.containsKey(tweetDetail.getName())) {
						count = result.get(tweetDetail.getName()) + 1;
					} else {
						count = 1;
					}
					result.put(tweetDetail.getName(), count);
				}
				tweetDetail = null;
			}
		}
		// TODO Auto-generated method stub
		return result;
	}

}
