import tweepy
from util import Util

CONSUMER_KEY = "mvwhsiYQe3UTnnNv888GSXmIy"
CONSUMER_SECRET = "SfQ6j2bEOURZmzE5fd5oZYs02udxg2RpgAmj3BhF5pFjUyPl9W"
ACCESS_TOKEN = "2739941347-JFMFrA13BA5w3WQFmKLxm79oGt5IMuGY4BCfEBC"
ACCESS_SECRET = "BOWclPeXkxqy98DUFu1M1pg4RdgXOWUpfvsMS32Ezu66t"
TOP_K = 5

if __name__ == "__main__":
    auth = tweepy.OAuthHandler(CONSUMER_KEY, CONSUMER_SECRET)

    auth.set_access_token(ACCESS_TOKEN, ACCESS_SECRET)

    api = tweepy.API(auth)
    
    public_tweets = api.search(geocode="43.665459,-79.409498,0.1mi", count=100)
    
    tags = {}
    tag_to_tweets = {}

    for tweet in public_tweets:
        new_tweet = Util.remove_non_ascii(tweet.text)
        if len(new_tweet) > 0:
            hashtags = Util.extract_hashtags(new_tweet)
            
            for tag in hashtags:
                if tag in tags:
                    tags[tag] += 1
                    tag_to_tweets[tag].append(new_tweet)
                else:
                    tags[tag] = 1
                    tag_to_tweets[tag] = [new_tweet]

    sorted_tags = Util.sort_dict_by_values(tags)
    
    for i in range(min(TOP_K, len(sorted_tags))):
        print sorted_tags[i][0], sorted_tags[i][1]
        for tweet in tag_to_tweets[sorted_tags[i][0]]:
            print tweet

        print

