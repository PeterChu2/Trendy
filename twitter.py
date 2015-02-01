import tweepy
from util import Util
import urllib2
import unirest

CONSUMER_KEY = "mvwhsiYQe3UTnnNv888GSXmIy"
CONSUMER_SECRET = "SfQ6j2bEOURZmzE5fd5oZYs02udxg2RpgAmj3BhF5pFjUyPl9W"
ACCESS_TOKEN = "2739941347-JFMFrA13BA5w3WQFmKLxm79oGt5IMuGY4BCfEBC"
ACCESS_SECRET = "BOWclPeXkxqy98DUFu1M1pg4RdgXOWUpfvsMS32Ezu66t"
TOP_K = 1000
QUERY = "https://loudelement-free-natural-language-processing-service.p.mashape.com/nlp-text/?text="
HEADERS = {
    "X-Mashape-Key": "WOe6Znx5UpmshoVnevTHgeoYSUfDp1JaNjajsn6CJob9bRSHJd",
    "Accept": "application/json"
    }

auth = tweepy.OAuthHandler(CONSUMER_KEY, CONSUMER_SECRET)

auth.set_access_token(ACCESS_TOKEN, ACCESS_SECRET)

api = tweepy.API(auth)

class Item:
    def __init__(self, text, imgs, tags, sentiment="neutral"):
        self.text = text
        self.imgs = imgs
        self.tags = tags
        self.sentiment = sentiment

    def serialize(self):
        return {
            "text": self.text,
            "image_url": self.imgs,
            "hash_tags": self.tags,
            "sentiment": self.sentiment,
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
            image_urls = []
            image_urls = Util.extract_image_urls(tweet.entities["urls"])


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
        for tweet in tag_to_tweets[sorted_tags[len(sorted_tags) -1-i][0]]:
            response = unirest.get(QUERY + urllib2.quote(tweet.text, ""), 
                                   headers=HEADERS)
            sentiment = response.body["sentiment-text"]
            tweet.sentiment = sentiment
            all_tweets.append(tweet)

            

    return all_tweets

if __name__ == "__main__":
    dist = 0.1
    lat, lng = 43.659465, -79.397044
    all_tweets = search_tweets(lat, lng, dist)
    
    for tweet in all_tweets:
        print tweet.text
        print tweet.tags
        print
