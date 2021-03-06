import tweepy
from util import Util
import urllib2
import unirest
from common import Item


class Twitter:

    CONSUMER_KEY = "mvwhsiYQe3UTnnNv888GSXmIy"
    CONSUMER_SECRET = "SfQ6j2bEOURZmzE5fd5oZYs02udxg2RpgAmj3BhF5pFjUyPl9W"
    ACCESS_TOKEN = "2739941347-JFMFrA13BA5w3WQFmKLxm79oGt5IMuGY4BCfEBC"
    ACCESS_SECRET = "BOWclPeXkxqy98DUFu1M1pg4RdgXOWUpfvsMS32Ezu66t"
    TOP_K = 50


    def __init__(self):
        auth = tweepy.OAuthHandler(self.CONSUMER_KEY, self.CONSUMER_SECRET)
        auth.set_access_token(self.ACCESS_TOKEN, self.ACCESS_SECRET)
        self.api = tweepy.API(auth)

    def get_posts(self, request):
        # request.form['dist'] is the distance in km.
        all_posts = self.search_tweets(
            request.form['lat'], request.form['long'], request.form['value'])
        return all_posts

    def search_tweets(self, lat, lng, dist):
        gcode = str(lat) + "," + str(lng) + "," + str(dist) + "km"
        public_tweets = self.api.search(geocode=gcode, count=100, include_entities=True)
        tags = {}
        tag_to_tweets = {}
        all_tweets = []

        for tweet in public_tweets:
            new_tweet = Util.remove_non_ascii(tweet.text)
            if len(new_tweet) > 0:
                hashtags = Util.extract_hashtags(new_tweet)
                image_urls = []
                if 'media' in tweet.entities:
                  image_urls = Util.extract_image_urls(tweet.entities["media"])
                  all_tweets = []

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

        for i in range(min(self.TOP_K, len(sorted_tags))):
            for tweet in tag_to_tweets[sorted_tags[i][0]]:
                all_tweets.append(tweet)

        # Remove all duplicate items
        return list(set(all_tweets))

    if __name__ == "__main__":
        lat = 43.665459
        lng = -79.409498
        dist = 0.1
        search_tweets(lat, lng, dist)
