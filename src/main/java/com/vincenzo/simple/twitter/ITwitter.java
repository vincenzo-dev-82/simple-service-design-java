package com.vincenzo.simple.twitter;

import java.util.List;

/**
 * Twitter 인터페이스: 트윗 작성, 팔로우/언팔로우, 타임라인 조회 기능 제공
 */
interface ITwitter {
    void postTweet(int userId, int tweetId);
    List<Integer> getNewsFeed(int userId);
    void follow(int followerId, int followeeId);
    void unfollow(int followerId, int followeeId);
}
