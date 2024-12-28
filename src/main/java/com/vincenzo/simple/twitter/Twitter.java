package com.vincenzo.simple.twitter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Twitter 클래스: ITwitter 인터페이스 구현체
 */
public class Twitter implements ITwitter {
    private static int timestamp = 0; // 트윗 고유 타임스탬프
    private static final int MAX_FEED_SIZE = 10; // 타임라인 최대 트윗 수

    // 트윗 정보를 저장하는 클래스
    private static class Tweet {
        int tweetId;
        int time;

        public Tweet(int tweetId, int time) {
            this.tweetId = tweetId;
            this.time = time;
        }
    }

    private ConcurrentHashMap<Integer, List<Tweet>> userTweets; // 사용자별 트윗 저장
    private ConcurrentHashMap<Integer, Set<Integer>> following; // 사용자별 팔로우 관계

    public Twitter() {
        userTweets = new ConcurrentHashMap<>();
        following = new ConcurrentHashMap<>();
    }

    /**
     * 트윗 작성: 사용자의 트윗 목록에 새 트윗 추가
     */
    @Override
    public void postTweet(int userId, int tweetId) {
        userTweets.putIfAbsent(userId, Collections.synchronizedList(new ArrayList<>()));
        userTweets.get(userId).add(new Tweet(tweetId, timestamp++));
    }

    /**
     * 타임라인 조회: 본인 및 팔로우한 사용자의 최신 트윗 가져오기
     */
    @Override
    public List<Integer> getNewsFeed(int userId) {
        PriorityQueue<Tweet> pq = new PriorityQueue<>((a, b) -> b.time - a.time); // 최신순 정렬

        // 본인의 트윗 추가
        if (userTweets.containsKey(userId)) {
            pq.addAll(userTweets.get(userId));
        }

        // 팔로우한 사용자의 트윗 추가
        if (following.containsKey(userId)) {
            for (int followeeId : following.get(userId)) {
                if (userTweets.containsKey(followeeId)) {
                    pq.addAll(userTweets.get(followeeId));
                }
            }
        }

        // 상위 10개 트윗을 리스트로 반환
        List<Integer> feed = new ArrayList<>();
        while (!pq.isEmpty() && feed.size() < MAX_FEED_SIZE) {
            feed.add(pq.poll().tweetId);
        }

        return feed;
    }

    /**
     * 팔로우: 사용자가 특정 사용자를 팔로우
     */
    @Override
    public void follow(int followerId, int followeeId) {
        if (followerId != followeeId) { // 자기 자신을 팔로우하지 않음
            following.putIfAbsent(followerId, ConcurrentHashMap.newKeySet());
            following.get(followerId).add(followeeId);
        }
    }

    /**
     * 언팔로우: 사용자가 특정 사용자를 언팔로우
     */
    @Override
    public void unfollow(int followerId, int followeeId) {
        if (following.containsKey(followerId)) {
            following.get(followerId).remove(followeeId);
        }
    }

    // 테스트 코드
    public static void main(String[] args) {
        ITwitter twitter = new Twitter();

        // 트윗 작성
        twitter.postTweet(1, 101); // User 1 트윗 101 작성
        twitter.postTweet(1, 102); // User 1 트윗 102 작성

        // 팔로우 및 트윗 추가
        twitter.follow(1, 2);      // User 1이 User 2를 팔로우
        twitter.postTweet(2, 201); // User 2 트윗 201 작성

        // 타임라인 조회
        System.out.println("User 1's feed: " + twitter.getNewsFeed(1)); // [201, 102, 101]

        // 언팔로우 후 타임라인 조회
        twitter.unfollow(1, 2);    // User 1이 User 2를 언팔로우
        System.out.println("User 1's feed after unfollow: " + twitter.getNewsFeed(1)); // [102, 101]
    }
}