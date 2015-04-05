package com.lahacks.hacks;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by praveen on 4/4/15.
 */
public class UserScoreCache {
    public static LoadingCache<Long, Map<String, Integer>> userCategoryScoreCache;
    public static LoadingCache<Long, Set<String>> userLikeScoreCache;

    public static LoadingCache<Long,Map<String,Integer>> getScoreCache(){
        if(userCategoryScoreCache!=null){
            return userCategoryScoreCache;
        } else {
            LoadingCache<Long, Map<String,Integer>> graphs = CacheBuilder.newBuilder()
                    .maximumSize(1000)
                    .expireAfterWrite(1, TimeUnit.DAYS)
                    .build(
                            new CacheLoader<Long, Map<String,Integer>>() {
                                public Map<String,Integer> load(Long key){
                                    String token = users.getAccessToken(String.valueOf(key));
                                    return FbApiCall.getUserCategoryLikes(String.valueOf(key),token); //calls a function which calculates this.
                                }
                            });
            userCategoryScoreCache = graphs;
            return graphs;
        }
    }

    public static LoadingCache<Long, Set<String>> getUserLikeCache(){
        if(userLikeScoreCache!=null){
            return userLikeScoreCache;
        } else {
            LoadingCache<Long, Set<String>> graphs = CacheBuilder.newBuilder()
                    .maximumSize(1000)
                    .expireAfterWrite(1, TimeUnit.DAYS)
                    .build(
                            new CacheLoader<Long, Set<String>>() {
                                public Set<String> load(Long key){
                                    String token = users.getAccessToken(String.valueOf(key));
                                    return FbApiCall.getUserLikes(String.valueOf(key),token); //calls a function which calculates this
                                }
                            });
            userLikeScoreCache = graphs;
            return graphs;
        }
    }

    public boolean destroyCaches(){
        userCategoryScoreCache.invalidateAll();
        userLikeScoreCache.invalidateAll();
        userCategoryScoreCache=null;
        userLikeScoreCache=null;
        return true;
    }
}
