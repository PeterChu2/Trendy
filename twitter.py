import tweepy
from util import Util

CONSUMER_KEY = "mvwhsiYQe3UTnnNv888GSXmIy"
CONSUMER_SECRET = "SfQ6j2bEOURZmzE5fd5oZYs02udxg2RpgAmj3BhF5pFjUyPl9W"
ACCESS_TOKEN = "2739941347-JFMFrA13BA5w3WQFmKLxm79oGt5IMuGY4BCfEBC"
ACCESS_SECRET = "BOWclPeXkxqy98DUFu1M1pg4RdgXOWUpfvsMS32Ezu66t"
TOP_K = 5

auth = tweepy.OAuthHandler(CONSUMER_KEY, CONSUMER_SECRET)

auth.set_access_token(ACCESS_TOKEN, ACCESS_SECRET)

api = tweepy.API(auth)

class Item:
    def __init__(self, text, imgs, tags):
        self.text = text
        self.imgs = imgs
        self.tags = tags

    def serialize(self):
        return {
            "text": self.text,
            "image_url": self.imgs,
            "hash_tags": self.tags
            }

def search_tweets(lat, lng, dist):
    gcode = str(lat) + "," + str(lng) + "," + str(dist) + "km"
    public_tweets = api.search(geocode=gcode, count=100)
    tags = {}
    tag_to_tweets = {}
    all_tweets = []

    for tweet in public_tweets:
        new_tweet = Util.remove_non_ascii(tweet.text)
        if len(new_tweet) > 0:
            hashtags = Util.extract_hashtags(new_tweet)
            image_urls = Util.extract_hashtags(new_tweet)
            text = Util.extract_text(new_tweet)

            tweet_object = Item(text, image_urls, hashtags)

            for tag in hashtags:
                if tag in tags:
                    tags[tag] += 1
                    tag_to_tweets[tag].append(tweet_object)
                else:
                    tags[tag] = 1
                    tag_to_tweets[tag] = [tweet_object]

    sorted_tags = Util.sort_dict_by_values(tags)
    
    for i in range(min(TOP_K, len(sorted_tags))):
        for tweet in tag_to_tweets[sorted_tags[i][0]]:
            all_tweets.append(tweet)

            

    return all_tweets

if __name__ == "__main__":
    lat = 43.665459 
    lng = -79.409498
    dist = 0.1
    
    search_tweets(lat, lng, dist)
