package com.lahacks.hacks;

import com.google.common.cache.LoadingCache;

import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by praveen on 4/4/15.
 */
public class TopGroupsFinder {

	public static final Integer ItemMatchRank = 4;
	public static final Integer CategoryMatchRank = 1;

	public static List<Pair<Integer, Double>> calculateAllGroupScore(
			List<Integer> groups, long newUserId) throws ExecutionException {
		LoadingCache<Long, Map<String, Integer>> cache = UserScoreCache
				.getScoreCache();
		LoadingCache<Long, Set<String>> itemCache = UserScoreCache
				.getUserLikeCache();
		List<Pair<Integer, Double>> scoreList = new ArrayList<Pair<Integer, Double>>();
		Set<String> user1Items = itemCache.get(newUserId);
		Map<String, Integer> user1Categories = cache.get(newUserId);

		for (Integer id : groups) {
			double overallScore = 0;
			// call db to get users per group

			List<Long> userids = new ArrayList<Long>();
			for (Long user : userids) {
				Map<String, Integer> user2Categories = cache.get(user);
				Set<String> user2Items = itemCache.get(user);
				overallScore += getSimilarityBetweenUsers(user1Items,
						user2Items, user1Categories, user2Categories);
			}
			overallScore = overallScore / userids.size();
			scoreList.add(new Pair<Integer, Double>(id, overallScore));
		}
		Collections.sort(scoreList, new Comparator<Pair<Integer, Double>>() {
			@Override
			public int compare(Pair<Integer, Double> integerDoublePair,
					Pair<Integer, Double> integerDoublePair2) {
				if (integerDoublePair.getSecond() > integerDoublePair2
						.getSecond()) {
					return -1;
				} else if (integerDoublePair.getSecond() < integerDoublePair2
						.getSecond()) {
					return 1;
				} else
					return 0;
			}
		});
		return scoreList;
	}

	public static int getSimilarityBetweenUsers(Set<String> user1Items,
			Set<String> user2Items, Map<String, Integer> user1CategoryCount,
			Map<String, Integer> user2CategoryCount) {
		int score = 0;
		user1Items.retainAll(user2Items);
		score += user1Items.size() * ItemMatchRank;
		for (Map.Entry<String, Integer> entry : user1CategoryCount.entrySet()) {
			if (user2CategoryCount.containsKey(entry.getKey())) {
				score += Math.min(entry.getValue(),
						user2CategoryCount.get(entry.getKey())
								* CategoryMatchRank);
			}
		}
		return score;
	}

	/** unused code */
	// // item count in group contains every items count in group
	// // category count also does the same.
	// //user items is a string of items which need to match with item counts.
	// public static List<Integer> getTopGroups(Map<Integer,
	// Map<String,Integer>> categoryCount, Map<Integer, Map<String,Integer>>
	// itemCount,Map<Integer, Integer> groupMemberCount, Set<String> userItems,
	// Map<String,Integer> userCategoryCounts, int groupCount) {
	// Map<Integer, Double> groupMatchScore = new HashMap<Integer, Double>();
	// List<Integer> list = new ArrayList<Integer>();
	// int categoryScore = 0;
	// for(Map.Entry<Integer, Map<String,Integer>> entry :
	// categoryCount.entrySet()){
	// categoryScore =0;
	// for (Map.Entry<String, Integer> userCategoryCount :
	// userCategoryCounts.entrySet()) {
	// if (entry.getValue().containsKey(userCategoryCount.getKey())) {
	// categoryScore += Math.min(userCategoryCount.getValue(),
	// entry.getValue().get(userCategoryCount.getKey())) * CategoryMatchRank;
	// }
	// }
	// groupMatchScore.put(entry.getKey(),((double)categoryScore/groupMemberCount.get(entry.getKey())));
	// }
	//
	// Map<Integer, Double> groupItemMatchScore = new HashMap<Integer,
	// Double>();
	// int itemScore = 0;
	//
	// for(Map.Entry<Integer,Map<String,Integer>> entry: itemCount.entrySet()) {
	// itemScore = 0;
	// for (String item : userItems) {
	// if (itemCount.containsKey(item)) {
	// itemScore += entry.getValue().get(item) * ItemMatchRank;
	// }
	// }
	//
	// groupItemMatchScore.put(entry.getKey(),(double)itemScore/groupMemberCount.get(entry.getKey()));
	// }
	// double maxItemScore = -1;
	// int bestGroup =-1;
	// TreeMap<Double,Integer> tmpMap = new TreeMap<Double, Integer>();
	// for(Map.Entry<Integer,Double> entry : groupMatchScore.entrySet()){
	// tmpMap.put(groupItemMatchScore.get(entry.getKey()) + entry.getValue(),
	// entry.getKey());
	// }
	// int i =0;
	// for(Map.Entry<Double,Integer> entry: tmpMap.entrySet()){
	// list.add(entry.getValue());
	// i++;
	// if(i>10){
	// break;
	// }
	// }
	//
	// return list;
	// }

	public static List<Integer> getGroupIds(double lat, double lon,
			double radius, String query) {
		SearchRegion searchRegion = new SearchRegion(lat, lon, radius);
		SearchResult[] spatialResults = GroupSearch.spatialSearch(query,
				searchRegion);
		// System.out.println("Spatial Seacrh");
		// System.out.println("Received " + spatialResults.length + " results");
		List<Integer> groups = new ArrayList<Integer>();
		if (spatialResults != null) {
			for (SearchResult result : spatialResults) {
				groups.add(Integer.parseInt(result.getGroupID()));
				// System.out.println(result.getGroupID() + ": "
				// + result.getGroupName());
			}
		}
		return groups;
		// ResultSet
		// rs=st.executeQuery("SELECT GroupID FROM GroupDetails WHERE MBRCONTAINS( GEOMFROMTEXT(  'POLYGON(("+lx+" "+ly+"),("+lx+""+ry+"),("+rx+" "+ry+"),("+rx+" "+ly+"),("+lx+" "+ly+")))' ) , Geo_point ) ");
	}

}
